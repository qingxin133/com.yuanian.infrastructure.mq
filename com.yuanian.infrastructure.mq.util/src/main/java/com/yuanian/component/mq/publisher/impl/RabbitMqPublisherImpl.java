package com.yuanian.component.mq.publisher.impl;

import com.yuanian.component.mq.config.RabbitMqProperties;
import com.yuanian.component.mq.util.EcsMQObjectUtils;
import com.yuanian.infrastructure.mq.IMessagePublisher;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

public class RabbitMqPublisherImpl implements IMessagePublisher {

    private final String charset = "UTF-8";
    private MessageConverter converter;
    @Autowired
    private org.springframework.amqp.rabbit.core.RabbitTemplate rabbitTemplate;
//    @Autowired
//    private MqConnectionConfig.AmqpProducer amqpProducer;

    @Resource
    private RabbitMqProperties rabbitMqProperties;

    @Override
    public void publish(String message) {

    }

    @Override
    public void publish(Message<?> message) {

    }

    @Override
    public void publish(String topic, String message) {

    }

    @Override
    public void publish(String topic, Message<?> message) {

    }

    @Override
    public void setSuccessHandler(MessageHandler successHandler) {

    }

    @Override
    public void setErrorHandler(MessageHandler errorHandler) {

    }

    @Override
    public boolean syncPublish(String message) {
        return false;
    }

    @Override
    public boolean syncPublish(Message<?> message) {
        return false;
    }

    @Override
    public boolean syncPublish(String topic, String message) {
        return false;
    }

    /**
     * 产品调用的发送
     * @Description
     * @param topic
     * @param message
     * @Author tianyang
     * @Date 2021/6/23 19:17
     * @Return boolean
     */
    @Override
    public boolean syncPublish(String topic, Message<?> message) {
        try {
            String topicNew = convertToRabbitMessage(this.converter, this.charset, topic, message);
            MessageProperties sendMp = new MessageProperties();
            setSendMsgHeaders(sendMp.getHeaders(),message.getHeaders());
            org.springframework.amqp.core.Message messageTo = new org.springframework.amqp.core.Message(EcsMQObjectUtils.objectToBytes(message.getPayload()),sendMp);
            rabbitTemplate.send(rabbitMqProperties.getExchange(),topicNew, messageTo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 组装发送消息HEADERS里面的值
     * @Description
     * @param sendHeaders
     * @param messageHeaders
     * @Author tianyang
     * @Date 2021/7/7 18:41
     * @Return void
     */
    public void setSendMsgHeaders(Map<String, Object> sendHeaders, MessageHeaders messageHeaders){
        for(Map.Entry<String, Object> entrySet :messageHeaders.entrySet()){
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            sendHeaders.put(key,value);
        }
    }

    /**
     * queue转换成固定格式方法
     * @Description
     * @param messageConverter
     * @param charset
     * @param destination
     * @param message
     * @Author tianyang
     * @Date 2021/7/6 13:59
     * @Return java.lang.String
     */
    public static String convertToRabbitMessage(MessageConverter messageConverter, String charset, String destination, org.springframework.messaging.Message<?> message) {
        Object payloadObj = message.getPayload();
        String topic = "";
        try {
            if (null == payloadObj) {
                throw new RuntimeException("the message cannot be empty");
            }
            if (StringUtils.isEmpty(destination)) {
                throw new RuntimeException("the topic cannot be empty");
            }
        } catch (Exception var7) {
            throw new RuntimeException("convert to MQ message failed.", var7);
        }
        if (destination != null && destination.length() >= 1) {
            String[] tempArr = destination.split(":", 2);
            topic = tempArr[0];
        }
        return topic;
    }

    @Override
    public boolean syncPublish(Collection<Message> messages) {
        return false;
    }

    @Override
    public boolean syncPublish(String topic, Collection<Message> messages) {
        return false;
    }

    @Override
    public void setSendMessageTimeout(int timeout) {

    }

    @Override
    public void setDefaultTopic(String defaultTopic) {

    }

    @Override
    public String getDefaultTopic() {
        return null;
    }

    @Override
    public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {

    }

    @Override
    public int getRetryTimesWhenSendFailed() {
        return 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
