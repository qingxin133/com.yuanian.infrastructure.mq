package com.yuanian.infrastructure.mq;

import org.springframework.messaging.Message;

/**
 * 消息处理器，用于字符串类型的消息的订阅
 */
public interface IMessageHandler {

    /**
     * 消息处理回调
     * @param message 消息
     */
    // 应用可以手动抛出异常来使得框架获知消费失败，从而进行消息的丢弃或DLQ处理
    ConsumeStatus handleMessage(Message message);

}
