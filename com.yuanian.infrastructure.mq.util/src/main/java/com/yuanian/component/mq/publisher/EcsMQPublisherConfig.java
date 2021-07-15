package com.yuanian.component.mq.publisher;

import com.yuanian.component.mq.publisher.impl.RabbitMqPublisherImpl;
import com.yuanian.infrastructure.mq.IMessagePublisher;
import com.yuanian.infrastructure.mq.config.MQComplexConfig;
import com.yuanian.infrastructure.mq.factory.IPublisherFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static com.yuanian.component.mq.constant.PublisherConstant.PRE_GROUP_NAME;

/**
 * @author liujy
 * @date 2020/6/4 15:02
 **/
@Configuration
public class EcsMQPublisherConfig {
    @Value("${spring.application.name}")
    private String serviceName;
    @Value("${yn.mq.prefix:}")
    private String prefix;
    @Value("${yn.mq.retryTimesWhenSendFailed:2}")
    private Integer retryTimesWhenSendFailed;
    @Value("${yn.mq.sendMessageTimeout:0}")
    private Integer sendMessageTimeout;
    @Value("${yn.mq.enable:false}")
    private Boolean enable;
    @Value("${yn.mq.aclEnable:false}")
    private Boolean aclEnable;
    @Value("${yn.mq.accessKey:}")
    private String accessKey;
    @Value("${yn.mq.secretKey:}")
    private String secretKey;

    @Bean
    IMessagePublisher defaultPublisher() {
        if (!enable){
            return null;
        }

//        String producerGroup = null;
//        MQComplexConfig mqComplexConfig = new MQComplexConfig();

        // TODO: 2021/6/17 创建默认的publisher
//        IMessagePublisher publisher = factory.createPublisher(producerGroup,mqComplexConfig);
        IMessagePublisher publisher = new RabbitMqPublisherImpl();

//        publisher.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
//        if(sendMessageTimeout > 0){
//            publisher.setSendMessageTimeout(sendMessageTimeout);
//        }
        return publisher;
    }

}
