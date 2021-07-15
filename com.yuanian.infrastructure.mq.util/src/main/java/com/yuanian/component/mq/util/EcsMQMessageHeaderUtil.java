package com.yuanian.component.mq.util;


import com.yuanian.component.mq.constant.ConsumerConstant;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author liujy
 * @date 2020/6/4 20:21
 **/
public class EcsMQMessageHeaderUtil {

    /**
     * 获取响应队列名
     *
     * @param message
     * @return
     */
    public static String getCallbackTopicName(Message message) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "响应topic信息不存在！");
        String callbackTopicName = (String) headers.get(ConsumerConstant.TOPIC_CALLBACK_NAME);
        Assert.hasText(callbackTopicName, "响应topic信息不存在！");
        return callbackTopicName;
    }

    /**
     * 判断是否需要响应队列
     *
     * @param message
     * @return
     */
    public static Boolean needCallback(Message message) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "headers信息不存在！");
        String needCallback = (String) headers.get(ConsumerConstant.NEED_CALLBACK);
        Assert.hasText(needCallback, "header信息缺失，无法判断是否需要响应！");
        return Objects.equals(needCallback, "true");
    }

    /**
     * 获取任一head内容
     *
     * @param message
     * @return
     */
    public static Object getHeaderValue(Message message, String headerKey) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "headers信息不存在！");
        Object object = headers.get(headerKey);
        return object;
    }

    /**
     * 获取全局mq msg id
     *
     * @param message
     * @return
     */
    public static String getGlobalMsgId(Message message) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "headers信息不存在！");
        String globalMsgId = (String)headers.get(ConsumerConstant.GLOBAL_MSG_ID);
        Assert.notNull(globalMsgId, "header信息缺失，无法获得头信息！");
        return globalMsgId;
    }

    /**
     * 获取OperatorType
     *
     * @param message
     * @return
     */
    public static String getOperatorType(Message message) {
        MessageHeaders headers = message.getHeaders();
        Assert.notEmpty(headers, "headers信息不存在！");
        String operatorType = (String)headers.get(ConsumerConstant.OPERATOR_TYPE);
        Assert.hasText(operatorType, "头信息中确实操作类型");
        return operatorType;
    }
}
