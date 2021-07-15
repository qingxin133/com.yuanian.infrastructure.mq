package com.yuanian.component.mq.consumer.factory;


import com.yuanian.component.mq.consumer.AbstractMQConsumer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * 消费者生成
 * @Description
 * @Author tianyang
 * @Date 2021/7/5 15:29
 */
public class ConsumerGenerate {

    /**
     * 创建消费者
     *
     * @param connectionFactory
     * @param rabbitAdmin 主要用于在Java代码中对理队和队列进行管理，用于创建、绑定、删除队列与交换机，发送消息等。
     * @param exchangeName 交换机名称
     * @param queueName 队列名称
     * @param routingKey 路由密钥
     * @param autoDelete 自动删除
     * @param durable 耐用的
     * @param autoAck 自动确认
     * @return
     * @throws Exception
     */
    public static RabbitAbstractConsumer genConsumer(ConnectionFactory connectionFactory, RabbitAdmin rabbitAdmin,
                                                     String exchangeName, String queueName, String routingKey, boolean autoDelete, boolean durable,
                                                     boolean autoAck, AbstractMQConsumer ecsMqConsumer, String defaultSubjectPath) throws Exception {
        MQContainerFactory fac =
                MQContainerFactory.builder().directExchange(exchangeName)
                        .queue(queueName)
                        .autoDeleted(autoDelete)
                        .autoAck(autoAck)
                        .durable(durable)
                        .routingKey(routingKey)
                        .rabbitAdmin(rabbitAdmin)
                        .ecsMqConsumer(ecsMqConsumer)
                        .defaultSubjectPath(defaultSubjectPath)
                        .connectionFactory(connectionFactory).build();
        return new RabbitAbstractConsumer(fac);
    }

}
