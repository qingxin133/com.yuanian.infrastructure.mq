package com.yuanian.component.mq.publisher;


import com.alibaba.fastjson.JSON;
import com.epoch.infrastructure.util.service.CurrentUserUtils;
import com.epoch.infrastructure.util.service.loginsubject.ISubject;
import com.yuanian.component.mq.constant.ConsumerConstant;
import com.yuanian.component.mq.constant.PublisherConstant;
import com.yuanian.component.mq.model.EcsMessageDTO;
import com.yuanian.component.mq.util.EcsMQObjectUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author liujy
 * @date 2020/6/4 10:25
 **/
public class EcsMQMessageUtil {

    /**
     *
     * @param ecsMessageDTO
     * @return
     * @throws IllegalAccessException
     */
    public static Message getMessage(EcsMessageDTO ecsMessageDTO) throws IllegalAccessException {
        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        Map<String, Object> stringObjectMap = EcsMQObjectUtils.objectToMap(ecsMessageDTO);
        stringObjectMap.forEach((key, value) -> {
            if(!Objects.equals(value,null) && !Objects.equals("",value)){
                messageHeaderAccessor.setHeader(key, value);
            }
        });
        Message message = initMessage(messageHeaderAccessor, ecsMessageDTO.getPayload());
        return message;
    }
    public static Message getMessage(Map<String, Object> headers, Object payload) {
        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        headers.forEach((key, value) -> {
            messageHeaderAccessor.setHeader(key, value);
        });
        Message message = initMessage(messageHeaderAccessor, payload);
        return message;
    }

    /**
     * 必须在此处调用初始化message方法，否则消息中无法获取用户信息
     * @param messageHeaderAccessor
     * @param payload
     * @return
     */
    private static Message initMessage(MessageHeaderAccessor messageHeaderAccessor,Object payload) {
        MessageBuilder<Object> objectMessageBuilder = MessageBuilder.withPayload(payload).setHeaders(messageHeaderAccessor);
        //生产队列的服务器ip
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (localHost != null){
            objectMessageBuilder.setHeader(ConsumerConstant.HOST_NAME, localHost.getHostAddress());
        }
        //全局消息id
        if(null == messageHeaderAccessor.getHeader(ConsumerConstant.GLOBAL_MSG_ID)) {
            objectMessageBuilder.setHeader(ConsumerConstant.GLOBAL_MSG_ID, UUID.randomUUID().toString().replace("-", ""));
        }
        //消息产生时间
        objectMessageBuilder.setHeader(PublisherConstant.MQ_BORN_TIME, System.currentTimeMillis());
        //保存用户信息
        saveCurrentUser(objectMessageBuilder);
        return objectMessageBuilder.build();
    }

    private static void saveCurrentUser(MessageBuilder objectMessageBuilder){
        ISubject subject = CurrentUserUtils.getSubject();
        if(subject != null){
            String userMessage = JSON.toJSONString(subject);
            objectMessageBuilder.setHeader(PublisherConstant.MQ_USER_MESSAGE, userMessage);
        }

    }
}
