package com.yuanian.component.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.epoch.infrastructure.util.service.CurrentUserUtils;
import com.epoch.infrastructure.util.service.loginsubject.ISubject;
import com.rabbitmq.client.Channel;
import com.yuanian.component.mq.constant.ConsumerConstant;
import com.yuanian.component.mq.constant.PublisherConstant;
import com.yuanian.component.mq.publisher.EcsMQMessageUtil;
import com.yuanian.component.mq.util.EcsMQObjectUtils;
import com.yuanian.infrastructure.mq.ConsumeStatus;
import com.yuanian.infrastructure.mq.IMessageHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * 主数据同步mq自收转发给处理器
 * @Description
 * @Author tianyang
 * @Date 2021/7/7 11:38
 */
public abstract class AbstractRabbitMqConsumerListener implements IDynamicConsumerListener {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitMqConsumerListener.class);

    private volatile boolean end = false;
    private SimpleMessageListenerContainer container;
    private boolean autoAck;

//    private AbstractMQConsumer ecsMqConsumer;
    private IMessageHandler messageHandler;

    private String defaultSubjectPath;

    private ConsumeStatus consumeStatus = ConsumeStatus.RECONSUME_LATER;

    public void registerMessageHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public String getDefaultSubjectPath() {
        return defaultSubjectPath;
    }

    public void setDefaultSubjectPath(String defaultSubjectPath) {
        this.defaultSubjectPath = defaultSubjectPath;
    }

    @Override
    public void setContainer(SimpleMessageListenerContainer container) {
        this.container = container;
        autoAck = container.getAcknowledgeMode().isAutoAck();
    }
    @Override
    public void shutdown() {
        end = true;
    }


    /**
     * 增强方法
     * @Description
     * @param message
     * @param channel
     * @Author tianyang
     * @Date 2021/7/7 11:50
     * @Return boolean
     */
    public abstract boolean process(Message message, Channel channel) throws UnsupportedEncodingException;

    private static final SerializerFeature[] FEATURES;
    private static final SerializerFeature[] FEATURES_NoBoolean;

    static {
        FEATURES = new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat};
        FEATURES_NoBoolean = new SerializerFeature[]{SerializerFeature.IgnoreErrorGetter, SerializerFeature.WriteBigDecimalAsPlain, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat};
    }

    protected static String toJSON_NoBoolean(Object obj) {
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object object, String name, Object value) {
                return value != null && (!(value instanceof String) || !StringUtils.isEmpty((String)value));
            }
        };
        return JSON.toJSONString(obj, filter, FEATURES_NoBoolean);
    }

    /**
     * 去除只读字段
     * @Description
     * @param headers
     * @Author tianyang
     * @Date 2021/7/15 11:16
     * @Return void
     */
    protected void removeReadOnlyColumn(Map<String, Object> headers) {
        headers.remove(ConsumerConstant.ID);
        headers.remove(ConsumerConstant.TIMESTAMP);
    }

    /**
     * 接收到rabbitmq的消息，将消息转成ecs产品当中handle类的handleMessage方法
     * @Description
     * @param message
     * @param channel
     * @Author tianyang
     * @Date 2021/7/15 14:40
     * @Return void
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        boolean hasSubjectClass = false;
        try {
            MessageProperties pro = message.getMessageProperties();
            Object payload = EcsMQObjectUtils.bytesToObject(message.getBody());
            logger.info("Mq Consumer exchange: {} ", pro.getReceivedExchange());
            logger.info("Mq Consumer Queue: {} ",pro.getConsumerQueue());
            logger.info("ECS Consumer Handler Class: {}",messageHandler.getClass().getName());
            logger.info("Mq MESSAGE CONTEXT: {}", payload);

            Map<String, Object> headers = pro.getHeaders();
            removeReadOnlyColumn(headers);
            org.springframework.messaging.Message ecsMessage = EcsMQMessageUtil.getMessage(headers, payload);

            try {
                if(null!=headers.get(PublisherConstant.MQ_USER_MESSAGE)){
                    String userMessage = String.valueOf(headers.get(PublisherConstant.MQ_USER_MESSAGE));

                    if(!CollectionUtils.isEmpty(headers) && org.springframework.util.StringUtils.hasText(userMessage)){
                        Class clazz = checkClassExist(defaultSubjectPath);
                        if(clazz != null){
                            ISubject iSubject = JSON.parseObject(userMessage, (Type) clazz);
                            CurrentUserUtils.putSubject(iSubject);
                            hasSubjectClass = true;
                        }
                    }
                }

                ConsumeStatus consumeStatus = messageHandler.handleMessage(ecsMessage);
                setConsumeStatus(consumeStatus);
            }catch (Exception e){
                logger.error("RABBITMQ主数据同步，接收消息失败：",e);
                throw e;
            }finally {
                if(hasSubjectClass){
                    CurrentUserUtils.remove();
                }
            }
            //确认收到消息
            autoAck(message, channel, process(message, channel));
        } catch (Exception e) {
            autoAck(message, channel, true);
            logger.error("平台底层MQ接收消息失败:",e);
            throw e;
        } finally {
            if (end) {
                container.stop();
            }
        }
    }

    /**
     * 反序列化用户信息，放在线程变量中
     * @param defaultSubjectPath
     */
    protected Class checkClassExist(String defaultSubjectPath){
        try {
            Class<ISubject> clazz = (Class<ISubject>) Class.forName(defaultSubjectPath);
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    /**
     * 应答处理
     * @Description
     * @param message
     * @param channel
     * @param success
     * @Author tianyang
     * @Date 2021/7/5 18:09
     * @Return void
     */
    protected void autoAck(Message message, Channel channel, boolean success) throws IOException {
        logger.info("是否自动应签方式：{},handle执行结果：{}",autoAck,success);
        if (autoAck) {
            return;
        }
        if (success) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }


    public ConsumeStatus getConsumeStatus() {
        return consumeStatus;
    }

    public void setConsumeStatus(ConsumeStatus consumeStatus) {
        this.consumeStatus = consumeStatus;
    }
}
