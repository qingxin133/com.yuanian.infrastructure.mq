package com.yuanian.test;

import com.epoch.infrastructure.util.service.JsonUtils;
import com.google.common.collect.Maps;
import com.yuanian.component.mq.model.DimObjectVO;
import com.yuanian.component.mq.publisher.EcsMQMessageUtil;
import com.yuanian.component.mq.consumer.factory.ConsumerGenerate;
import com.yuanian.component.mq.consumer.factory.RabbitAbstractConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestConsumer {

    public static void main(String[] args) {
        new TestConsumer().doTest();
    }

    private static final Logger logger = LoggerFactory.getLogger(TestConsumer.class);

    private volatile static CachingConnectionFactory connectionFactory;

    @Value("${yn.mq.concurrentNum:}")
    private Integer concurrentNum;

    private RabbitTemplate rabbitTemplate;
    private RabbitAdmin rabbitAdmin;


    private static final String queue = "v1.fip.masterdata.direct.queue.dev";

    private static final String exchange = "v1.fip.masterdata.direct.exchange.dev";

    public ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            synchronized (CachingConnectionFactory.class) {
                if(connectionFactory == null){
                    connectionFactory = new CachingConnectionFactory();
                    //connectionFactory.setAddresses(address);
                    connectionFactory.setPort(5672);
                    connectionFactory.setHost("10.102.230.104");
                    connectionFactory.setUsername("cls-uat");
                    connectionFactory.setPassword("H^aW$1wL#Y1cB$0z");
                    connectionFactory.setPublisherConfirms(true);
                    connectionFactory.setVirtualHost("/");
                    logger.info("连接部署测试MQ:"  );
                }
            }
        }
        return connectionFactory;
    }

    public void doTest(){
        getConnectionFactory();
        initEcsConsumer();
//        initComsumer();
        sendMsg();
    }


    public void initEcsConsumer(){
//        AbstractMQConsumer rabbitrMqConsumer = new RabbitMqConsumerImpl();
//        try {
//            DynamicConsumer consumer = null;
//            try {
//                //创建消费者
//                consumer = ConsumerGenerate.genConsumer(connectionFactory, rabbitAdmin,exchange, queue, queue
//                                , false, true, true,rabbitrMqConsumer);
//            } catch (Exception e) {
//                throw  new RuntimeException("系统异常"+e);
//            }
//            //启动消费者
//            consumer.start();
////                allQueueContainerMap.put(topicName, consumer);
//            //启动消费者
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void initConsumer(){
        try {
            RabbitAbstractConsumer consumer = null;
            try {
                //创建消费者
                consumer = ConsumerGenerate
                        .genConsumer(connectionFactory, rabbitAdmin,exchange, queue, queue
                                , false, true, true,null,null);
            } catch (Exception e) {
                throw  new RuntimeException("系统异常"+e);
            }
            //启动消费者
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void syncPublish(String topic, Message<?> message){
//        RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory);
//        amqpTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//      System.out.println(JSON.toJSONString(message));
//        amqpTemplate.convertAndSend(exchange, topic, message);
//        amqpProducer.publishMsg(exchange,topic,message);
//        System.out.println("发送消息成功");
//    }

     public void sendMsg(){
         Message<DimObjectVO> message = getSendMessage();
         RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory);
         amqpTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
         amqpTemplate.convertAndSend(exchange, queue, message);
         System.out.println("发送消息成功");
//       syncPublish(queue,message);
     }

    public static Message getSendMessage() {
        List<DimObjectVO> objectVOList = new ArrayList<>();
        DimObjectVO dimObjectVO = new DimObjectVO();
        dimObjectVO.setAccountId("aaaa");
        dimObjectVO.setAccountNo("abcd");
        dimObjectVO.setBuildExtend(false);
        dimObjectVO.setOrgId("1121321");
        dimObjectVO.setParentName("1124322");
        objectVOList.add(dimObjectVO);

        final Map<String, Object> headers = Maps.newHashMap();
        headers.put("topic", queue);
        headers.put("TAGS", "TAGS1");

        headers.put("KEYS", "KEYS1");
        headers.put("businessId", "businessId1");

        headers.put("produceModelName", "ECS_MESSAGE1");
        headers.put("operatorName", "operatorName1");
        headers.put("operatorType", "operatorType1");
        headers.put("tenantCode", "tenantCode1");


        headers.put("callBackUrl", "" + "/mq/api/rest/callback11");
        headers.put("needCallback", true);

        String payload = JsonUtils.toJSON_NoBoolean(objectVOList);
        Message message = EcsMQMessageUtil.getMessage(headers, payload);
        return message;

    }
}
