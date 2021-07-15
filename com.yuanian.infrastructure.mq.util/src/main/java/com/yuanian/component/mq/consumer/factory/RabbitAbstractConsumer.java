package com.yuanian.component.mq.consumer.factory;

import com.rabbitmq.client.Channel;
import com.yuanian.component.mq.consumer.AbstractRabbitMqConsumerListener;
import com.yuanian.component.mq.consumer.impl.RabbitMqConsumerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.Arrays;

/**
 * 动态消费者
 * @Description
 * 自身
 * @Author tianyang
 * @Date 2021/7/5 15:00
 */
public class RabbitAbstractConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAbstractConsumer.class);

    private final SimpleMessageListenerContainer container;

    /**
     * 有参构造方法
     * @Description
     * @param fac
     * @Author tianyang
     * @Date 2021/7/5 15:09
     * @Return
     */
    public RabbitAbstractConsumer(MQContainerFactory fac) throws Exception {
        SimpleMessageListenerContainer container = fac.getObject();

//        AbstractRabbitMqConsumerListener rabbitMqConsumerListener = new AbstractRabbitMqConsumerListener() {
//            @Override
//            public boolean process(Message message, Channel channel){
//                return true;
//            }
//        };

        RabbitMqConsumerListener rabbitMqConsumerListener = new RabbitMqConsumerListener();
        // 指定要创建的并发使用者数。
        rabbitMqConsumerListener.registerMessageHandler(fac.getEcsMqConsumer());
        rabbitMqConsumerListener.setDefaultSubjectPath(fac.getDefaultSubjectPath());
        rabbitMqConsumerListener.setContainer(container);
        container.setMessageListener(rabbitMqConsumerListener);
        logger.info("注册监听实例频道:{},处理器:{}",Arrays.toString(container.getQueueNames()),fac.getEcsMqConsumer().getClass().getName());
        this.container = container;
    }

    /**
     * 启动消费者监听
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 15:10
     * @Return void
     */
    public void start() {
        container.start();
    }

    /**
     * 消费者停止监听
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 15:10
     * @Return void
     */
    public void stop() {
        container.stop();
    }

    /**
     * 消费者重启
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 15:10
     * @Return void
     */
    public void shutdown() {
        container.shutdown();
    }


    /**
     * 用户扩展处理消息
     */
//    public void distributionConsumerMsg(Message message, Channel channel) {
//        byte[] body = message.getBody();
//        System.out.println("DynamicConsumer处理数据："+new String(body));

//    }

}
