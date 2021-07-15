package com.yuanian.infrastructure.mq.factory;

import com.yuanian.infrastructure.mq.IMessagePublisher;
import com.yuanian.infrastructure.mq.config.MQComplexConfig;

/**
 * 消息发布者工厂，会根据配置指定的消息中间件类型进行创建。
 */
public interface IPublisherFactory {
    /**
     * 创建发布者实例，并指定默认主题
     * @param defaultTopic
     * @return
     */
    IMessagePublisher createPublisher(String defaultTopic, String group, MQComplexConfig mqComplexConfig);

    /**
     * 创建发布者实例
     * @param group
     * @return
     */
    IMessagePublisher createPublisher(String group,MQComplexConfig mqComplexConfig);
}
