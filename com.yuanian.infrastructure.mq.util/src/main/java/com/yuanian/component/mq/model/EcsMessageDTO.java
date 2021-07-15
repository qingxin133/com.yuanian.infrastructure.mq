package com.yuanian.component.mq.model;


/**
 * 消息传递的DTO
 * @author liujy
 * @date 2020/6/4 10:27
 **/
public class EcsMessageDTO<T> {
    /**
     * 发送消息的topic
     */
    private String topic;
    /**
     * 业务id，可以是和消息有关的主键id，方便在补偿中心中检索
     */
    private String businessId;
    /**
     * 操作人账号（loginName）
     */
    private String operatorName;
    /**
     * 操作人Id
     */
    private String operatorId;
    /**
     * 业务模块
     */
    private String produceModelName;
    /**
     * 回调接口
     */
    private String businessCallbackUrl;
    /**
     * 回调队列
     */
    private String businessCallbackTopic;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否需要回调
     */
    private Boolean needCallback;
    /**
     * 操作类型
     */
    private String operatorType;
    /**
     * 消息全局id
     */
    private String globalMsgId;
    /**
     * 消息描述
     */
    private String messageDescribe;

    private T payload;

    public String getGlobalMsgId() {
        return globalMsgId;
    }

    public void setGlobalMsgId(String globalMsgId) {
        this.globalMsgId = globalMsgId;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getProduceModelName() {
        return produceModelName;
    }

    public void setProduceModelName(String produceModelName) {
        this.produceModelName = produceModelName;
    }

    public String getBusinessCallbackUrl() {
        return businessCallbackUrl;
    }

    public void setBusinessCallbackUrl(String businessCallbackUrl) {
        this.businessCallbackUrl = businessCallbackUrl;
    }

    public String getBusinessCallbackTopic() {
        return businessCallbackTopic;
    }

    public void setBusinessCallbackTopic(String businessCallbackTopic) {
        this.businessCallbackTopic = businessCallbackTopic;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getNeedCallback() {
        return needCallback;
    }

    public void setNeedCallback(Boolean needCallback) {
        this.needCallback = needCallback;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getMessageDescribe() {
        return messageDescribe;
    }

    public void setMessageDescribe(String messageDescribe) {
        this.messageDescribe = messageDescribe;
    }
}
