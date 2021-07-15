package com.yuanian.component.mq.model;

import com.alibaba.fastjson.JSON;
import com.epoch.infrastructure.util.service.CurrentUserUtils;
import com.epoch.infrastructure.util.service.loginsubject.ISubject;
import com.yuanian.component.mq.constant.PublisherConstant;
import com.yuanian.component.mq.consumer.AbstractMQConsumer;
import com.yuanian.infrastructure.mq.ConsumeStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;

/**
 * 消费者代理 负责在consumer消费前后做处理
 * @author liujy
 * @date 2021/1/21 11:27
 **/
public class MQConsumerProxy extends AbstractMQConsumer {

    protected String topic;

    private final AbstractMQConsumer abstractMQConsumer;

    private final String defaultSubjectPath;

    public MQConsumerProxy(String topic, AbstractMQConsumer abstractMQConsumer,String defaultSubjectPath) {
        this.topic = topic;
        this.abstractMQConsumer = abstractMQConsumer;
        this.defaultSubjectPath = defaultSubjectPath;
    }

    @Override
    public ConsumeStatus handleMessage(Message message) {
        MessageHeaders headers = message.getHeaders();
        String userMessage = headers.get(PublisherConstant.MQ_USER_MESSAGE, String.class);
        boolean hasSubjectClass = false;
        if(!CollectionUtils.isEmpty(headers) && StringUtils.hasText(userMessage)){
            Class clazz = checkClassExist(defaultSubjectPath);
            if(clazz != null){
                ISubject  iSubject = JSON.parseObject(userMessage, (Type) clazz);
                CurrentUserUtils.putSubject(iSubject);
                hasSubjectClass = true;
            }
        }
        ConsumeStatus consumeStatus = ConsumeStatus.RECONSUME_LATER;

        try {
             consumeStatus = abstractMQConsumer.handleMessage(message);
        }catch (Exception e){
            throw e;
        }finally {
            if(hasSubjectClass){
                CurrentUserUtils.remove();
            }
        }

        return consumeStatus;
    }

    /**
     * 反序列化用户信息，放在线程变量中
     * @param defaultSubjectPath
     */
    private Class checkClassExist(String defaultSubjectPath){
        try {
            Class<ISubject> clazz = (Class<ISubject>) Class.forName(defaultSubjectPath);
            return clazz;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public void init() {
    }
}
