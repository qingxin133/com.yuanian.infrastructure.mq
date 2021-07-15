package com.yuanian.infrastructure.mq;

/**
 * 用于标识消息消费的结果
 */
public enum ConsumeStatus {
    CONSUME_SUCCESS,
    RECONSUME_LATER;

    ConsumeStatus() {}
}
