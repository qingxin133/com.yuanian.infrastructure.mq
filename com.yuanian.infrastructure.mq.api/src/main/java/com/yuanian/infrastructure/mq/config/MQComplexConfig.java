package com.yuanian.infrastructure.mq.config;

/**
 *
 * @author WangQiang
 * @version 1.0
 * @date 2021/4/22 13:43
 */
public class MQComplexConfig {

    private Boolean aclEnable;
    private String accessKey;
    private String secretKey;

    public Boolean getAclEnable() {
        return aclEnable;
    }

    public void setAclEnable(Boolean aclEnable) {
        this.aclEnable = aclEnable;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
