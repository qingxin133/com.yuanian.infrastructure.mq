package com.yuanian.component.mq.consumer.impl;

import com.rabbitmq.client.Channel;
import com.yuanian.component.mq.consumer.AbstractRabbitMqConsumerListener;
import com.yuanian.component.mq.util.EcsMQObjectUtils;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;

/**
 * rabbitmq监听的实现类
 * @Description
 * @Author tianyang
 * @Date 2021/7/15 14:34
 */
@Log4j2
public class RabbitMqConsumerListener extends AbstractRabbitMqConsumerListener {

    /**
     * 在收到消息，处理消费之后，应答消息之前的增强方法
     * @Description
     * @param message
     * @param channel
     * @Author tianyang
     * @Date 2021/7/15 14:36
     * @Return boolean
     */
    @Override
    public boolean process(Message message, Channel channel){
        System.out.println("------------RabbitMqConsumerListener process----------------");
        return true;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("------------RabbitMqConsumerListener onMessage------------");
    }

    @Override
    public void containerAckMode(AcknowledgeMode mode) {
        System.out.println("------------RabbitMqConsumerListener containerAckMode------------{}"+mode.toString());
    }

}
