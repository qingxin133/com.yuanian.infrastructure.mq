package com.yuanian.component.mq.consumer;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * 自定义消费者的接口
 * @Description
 * @Author tianyang
 * @Date 2021/7/5 14:06
 */
public interface IDynamicConsumerListener extends ChannelAwareMessageListener {
 
    void setContainer(SimpleMessageListenerContainer container);
 
    default void shutdown() {}
 
}
