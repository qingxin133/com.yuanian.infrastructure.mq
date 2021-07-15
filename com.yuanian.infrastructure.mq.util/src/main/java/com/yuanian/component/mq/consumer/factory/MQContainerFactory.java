package com.yuanian.component.mq.consumer.factory;

import com.rabbitmq.client.BuiltinExchangeType;
import com.yuanian.component.mq.consumer.AbstractMQConsumer;
import lombok.Builder;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

/**
 * MQ监听工厂类
 * @Description
 * @Author tianyang
 * @Date 2021/7/5 14:02
 */
@Data
@Builder
public class MQContainerFactory implements FactoryBean<SimpleMessageListenerContainer> {

    private BuiltinExchangeType exchangeType;

    private String directExchange;

    private String topicExchange;

    private String fanoutExchange;

    private String queue;

    private String routingKey;

    private Boolean autoDeleted;

    private Boolean durable;

    private Boolean autoAck;

    private AbstractMQConsumer ecsMqConsumer;

    private ConnectionFactory connectionFactory;

    private RabbitAdmin rabbitAdmin;
    //消费者数量
    private Integer concurrentNum;

    // 消费方
    private RabbitAbstractConsumer consumer;

    private String defaultSubjectPath;

    /**
     * 创建交换机
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:02
     * @Return org.springframework.amqp.core.Exchange
     */
   private Exchange buildExchange() {
       if (directExchange != null) {
           exchangeType = BuiltinExchangeType.DIRECT;
           return new DirectExchange(directExchange);
       } else if (topicExchange != null) {
           exchangeType = BuiltinExchangeType.TOPIC;
           return new TopicExchange(topicExchange);
       } else if (fanoutExchange != null) {
           exchangeType = BuiltinExchangeType.FANOUT;
           return new FanoutExchange(fanoutExchange);
       } else {
           if (StringUtils.isEmpty(routingKey)) {
               throw new IllegalArgumentException("defaultExchange's routingKey should not be null!");
           }
           exchangeType = BuiltinExchangeType.HEADERS;
           return new DirectExchange("");
       }
   }

    /**
     * 创建队列
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:03
     * @Return org.springframework.amqp.core.Queue
     */
    private Queue buildQueue() {
        if (StringUtils.isEmpty(queue)) {
            throw new IllegalArgumentException("queue name should not be null!");
        }
        return new Queue(queue, durable != null && durable, false, autoDeleted == null || autoDeleted);
    }


    /**
     * 交换机和队列绑定
     * @Description
     * @param queue
     * @param exchange
     * @Author tianyang
     * @Date 2021/7/5 14:03
     * @Return org.springframework.amqp.core.Binding
     */
    private Binding bind(Queue queue, Exchange exchange) {
       if(exchange instanceof DirectExchange){
           return  BindingBuilder.bind(queue).to((DirectExchange) exchange).with(routingKey);
       }
       else if(exchange instanceof TopicExchange){
           return  BindingBuilder.bind(queue).to((TopicExchange) exchange).with(routingKey);
       }
       else if(exchange instanceof FanoutExchange){
           return  BindingBuilder.bind(queue).to((FanoutExchange) exchange);
       }
       else {
           throw new RuntimeException("绑定的类型不存在");
       }
        //return  BindingBuilder.bind(queue).to(exchange).with(routingKey);
         //exchange.binding(queue, exchange, routingKey);
    }

    /**
     * 验证当前工厂中实例是否为空
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:04
     * @Return void
     */
    private void checkRabbitNotNull() {
        if (rabbitAdmin == null || connectionFactory == null) {
            throw new IllegalArgumentException("rabbitAdmin and connectionFactory should not be null!");
        }
    }

    /**
     * 重写创建监听方法
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:05
     * @Return org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
     */
    @Override
    public SimpleMessageListenerContainer getObject() throws Exception {
        checkRabbitNotNull();
        Queue queue = buildQueue();
        Exchange exchange = buildExchange();
        Binding binding = bind(queue, exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(binding);
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setRabbitAdmin(rabbitAdmin);
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setPrefetchCount(20);
        container.setConcurrentConsumers(concurrentNum == null ? 1 : concurrentNum);
        container.setAcknowledgeMode(autoAck ? AcknowledgeMode.AUTO : AcknowledgeMode.MANUAL);
        if (consumer != null) {
            container.setMessageListener(consumer);
        }
        return container;
    }

    /**
     * 重写对象类型获取
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:05
     * @Return java.lang.Class<?>
     */
    @Override
    public Class<?> getObjectType() {
        return SimpleMessageListenerContainer.class;
    }

    /**
     * 是否单例模式
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 14:05
     * @Return boolean
     */
    @Override
    public boolean isSingleton() {
        return false;
    }

}
