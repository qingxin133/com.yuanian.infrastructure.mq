package com.yuanian.component.mq.publisher;

import com.yuanian.component.mq.constant.ConsumerConstant;
import com.yuanian.infrastructure.mq.IMessagePublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Locale;


/**
 * @author liujy
 * @date 2020/6/4 9:59
 **/
@Component
public class EcsMQSendMessageUtil {

    private static String prefix;

    @Value("${yn.mq.prefix:}")
    public void setPrefix(String prefix) {
        EcsMQSendMessageUtil.prefix = prefix;
    }

    /**
     * 异步消息
     * @param publisher：消息发送者，可以直接通过 @Autowired等方式注入
     * @param message：EcsMQMessageUtil返回的Message对象
     */
    public static void sendMessage(IMessagePublisher publisher, Message message) {
        String topic = getTopic(message);
        publisher.publish(topic, message);
    }

    /**
     * 异步消息，提供发送失败处理类
     * @param publisher：消息发送者，可以直接通过 @Autowired等方式注入
     * @param message：EcsMQMessageUtil返回的Message对象
     * @param errorHandler：org.springframework.messaging.MessageHandler 发送失败回调
     */
    public static void sendMessageWithErrorHandler(IMessagePublisher publisher, Message message, MessageHandler errorHandler) {
        String topic = getTopic(message);
        publisher.setErrorHandler(errorHandler);
        publisher.publish(topic, message);
    }
    /**
     * 异步消息，提供发送失败处理类
     * @param publisher：消息发送者，可以直接通过 @Autowired等方式注入
     * @param message：EcsMQMessageUtil返回的Message对象
     * @param successHandler：org.springframework.messaging.MessageHandler 发送成功回调
     */
    public static void sendMessageWithSuccessHandler(IMessagePublisher publisher, Message message, MessageHandler successHandler) {
        String topic = getTopic(message);
        publisher.setErrorHandler(successHandler);
        publisher.publish(topic, message);
    }
    /**
     * 异步消息，提供发送失败处理类
     * @param publisher：消息发送者，可以直接通过 @Autowired等方式注入
     * @param message：EcsMQMessageUtil返回的Message对象
     * @param messageTimeout：超时时间（单位毫秒），默认值为3000
     */
    public static void sendMessage(IMessagePublisher publisher, Message message, int messageTimeout) {
        String topic = getTopic(message);
        publisher.setSendMessageTimeout(messageTimeout);
        publisher.publish(topic, message);
    }

    public static void sendMessageWithErrorHandler(IMessagePublisher publisher, Message message, MessageHandler errorHandler, int messageTimeout) {
        String topic = getTopic(message);
        publisher.setErrorHandler(errorHandler);
        publisher.setSendMessageTimeout(messageTimeout);
        publisher.publish(topic, message);
    }

    public static void sendMessageWithSuccessHandler(IMessagePublisher publisher, Message message, MessageHandler successHandler, int messageTimeout) {
        String topic = getTopic(message);
        publisher.setSendMessageTimeout(messageTimeout);
        publisher.setErrorHandler(successHandler);
        publisher.publish(topic, message);
    }

    /**
     * todo: 使用rabbitmq重写发送消息
     * 同步消息
     * @param publisher
     * @param message
     */
    public static Boolean syncSendMessage(IMessagePublisher publisher, Message message) {
        String topic = getTopic(message);
       return publisher.syncPublish(topic, message);
    }

    /**
     * 同步批量消息，必须同一Topic，不支持延时消息，建议一个批量消息最好不要超过4MB大小
     * @param publisher
     * @param messages
     */
    public static Boolean syncSendMessage(IMessagePublisher publisher, Collection<Message> messages) {
        Assert.notEmpty(messages,"消息数量至少为1");
        String topic = getTopic(messages.iterator().next());
        return publisher.syncPublish(topic, messages);
    }

    private static String getTopic(Message message) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "topic信息不存在！");
        String topic = (String) headers.get(ConsumerConstant.TOPIC_NAME);
        Assert.hasText(topic, "topic信息不存在！");
        if (!StringUtils.isEmpty(prefix)) {
            topic = prefix.toLowerCase() + "." + topic;
        }
        return topic;
    }



}
