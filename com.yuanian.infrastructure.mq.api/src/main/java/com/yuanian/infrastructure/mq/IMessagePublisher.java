package com.yuanian.infrastructure.mq;

import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.Collection;

/**
 * 消息发布者
 */
public interface IMessagePublisher extends SmartLifecycle {

    /**
     * 异步发布消息到默认主题
     * @param message
     */
    void publish(String message);

    /**
     * 异步发布消息到默认主题，目前只支持范型为String的org.springframework.messaging.Message
     * @param message
     */
    void publish(Message<?> message);

    /**
     * 异步发布消息到指定的主题
     * @param topic
     * @param message
     */
    void publish(String topic, String message);

    /**
     * 异步发布消息到指定的主题，目前只支持范型为String的org.springframework.messaging.Message
     * @param topic
     * @param message
     */
    void publish(String topic, Message<?> message);

    /**
     * 设置发送成功的处理回调
     * @param successHandler
     */
    void setSuccessHandler(MessageHandler successHandler);

    /**
     * 设置发送失败的处理回调
     * @param errorHandler
     */
    void setErrorHandler(MessageHandler errorHandler);

    /**
     * 同步发布消息到默认主题
     * @param message
     * @return
     */
    boolean syncPublish(String message);

    /**
     * 同步发布消息到默认主题
     * @param message
     * @return
     */
    boolean syncPublish(Message<?> message);

    /**
     * 同步发布消息到指定的主题
     * @param message
     * @return
     */
    boolean syncPublish(String topic, String message);

    /**
     * 同步发布消息到指定的主题
     * @param message
     * @return
     */
    boolean syncPublish(String topic, Message<?> message);

    /**
     * 批量同步发布Message消息到默认主题
     * @param messages
     * @return
     */
    boolean syncPublish(Collection<Message> messages);

    /**
     * 批量同步发布Message消息到指定的主题
     * @param messages
     * @return
     */
    boolean syncPublish(String topic, Collection<Message> messages);

    /**
     * 设置发送消息的超时时间
     * @param timeout
     */
    void setSendMessageTimeout(int timeout);

    /**
     * 设置默认主题
     * @param defaultTopic
     */
    void setDefaultTopic(String defaultTopic);

    /**
     * 获取默认主题
     * @return
     */
    String getDefaultTopic();

    /**
     * 设置发送失败的重试次数，默认2次
     */
    void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed);

    /**
     * 获取发送重试次数
     * @return
     */
    int getRetryTimesWhenSendFailed();

}
