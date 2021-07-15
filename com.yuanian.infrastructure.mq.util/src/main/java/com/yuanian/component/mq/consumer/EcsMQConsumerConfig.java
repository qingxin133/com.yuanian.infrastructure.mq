package com.yuanian.component.mq.consumer;

import com.yuanian.component.mq.config.RabbitMqProperties;
import com.yuanian.component.mq.consumer.factory.ConsumerGenerate;
import com.yuanian.component.mq.consumer.factory.RabbitAbstractConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;

/**
 * @author liujy
 * @date 2020/6/4 11:12
 **/
@Configuration
public class EcsMQConsumerConfig implements Lifecycle, DisposableBean , SmartInitializingSingleton {

    private static final Logger logger = LoggerFactory.getLogger(EcsMQConsumerConfig.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${yn.mq.reconsumeTimes:16}")
    private Integer reconsumeTimes;
    @Value("${yn.mq.dlqEnable:true}")
    private Boolean dlqEnable;
    @Value("${yn.mq.enable:false}")
    private Boolean enable;
    private Boolean running = true;
    @Value("${yn.mq.aclEnable:false}")
    private Boolean aclEnable;
    @Value("${yn.mq.accessKey:}")
    private String accessKey;
    @Value("${yn.mq.secretKey:}")
    private String secretKey;
    @Value("${yn.mq.concurrentNum:}")
    private Integer concurrentNum;

    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    private RabbitMqProperties rabbitMqProperties;

    private final HashSet<RabbitAbstractConsumer> subscriberSet = new HashSet();

    @Override
    public void start() {
        logger.info("MQ has been open");
    }

    /**
     * mq监听启动配置类初始化方法
     * @Description
     * @param
     * @Author tianyang
     * @Date 2021/7/5 13:49
     * @Return void
     */
    @Override
    public void afterSingletonsInstantiated() {
        if(!enable){
            return;
        }
        Map<String, AbstractMQConsumer> consumers = applicationContext.getBeansOfType(AbstractMQConsumer.class);
        if (CollectionUtils.isEmpty(consumers)) {
            logger.info("init consumer 0");
            return;
        }
        consumers.forEach((key, consumer) -> {
        // 创建mq的consumer
            try {
                consumer.init();
                String topic = consumer.topic;
                Assert.hasText(topic, "消费者TOPIC不能为空！");
                if (!StringUtils.isEmpty(this.rabbitMqProperties.getPrefix())) {
                    topic = this.rabbitMqProperties.getPrefix().toLowerCase() + "." + topic;
                }
                //创建消费者
                RabbitAbstractConsumer dynamicConsumer = ConsumerGenerate.genConsumer(connectionFactory, rabbitAdmin, rabbitMqProperties.getExchange(), topic, topic
                        , false, true, rabbitMqProperties.getAck(),consumer,rabbitMqProperties.getDefaultSubjectPath());
                //启动消费者
                dynamicConsumer.start();
                if (dynamicConsumer != null) {
                    this.subscriberSet.add(dynamicConsumer);
                }
            } catch (Exception e) {
                logger.error("mq监听启动配置类初始化方法异常",e);
            }

        });
    }

    @Override
    public void stop() {
        if (isRunning()) {
            // 停止消费
            this.subscriberSet.forEach((subscriber) -> {
                subscriber.stop();
            });
            this.running = false;
            logger.info("mq监听停止消费");
        }
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void destroy() throws Exception {
        this.running = false;
    }


}
