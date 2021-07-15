package com.yuanian.component.mq.consumer;

import com.yuanian.infrastructure.mq.ConsumeStatus;
import com.yuanian.infrastructure.mq.IMessageHandler;
import org.springframework.messaging.Message;

/**
 * @author liujy
 * @date 2020/6/4 14:04
 **/
public  abstract class AbstractMQConsumer implements IMessageHandler{

    protected String topic;

    @Override
    public abstract ConsumeStatus handleMessage(Message message) ;

   protected void necessary(String topic){
       this.topic = topic;
   }

    public abstract void init();
}
