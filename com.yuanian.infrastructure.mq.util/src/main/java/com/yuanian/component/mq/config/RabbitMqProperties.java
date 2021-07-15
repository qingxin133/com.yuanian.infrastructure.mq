package com.yuanian.component.mq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件读取配置类
 * @Description
 * @Author tianyang
 * @Date 2021/7/5 14:57
 * @Update 20210707 18:44 基于项目上已经有很多地方在使用rabbitmq，不再增加mq的连接池，使用spring里面的配置，取消了在CONFIG里面去连接MQ服务器
 */
@Configuration
public class RabbitMqProperties {

    /**
     * 主机地址
     */
    @Value("${mq.host}")
    private String host;
    /**
     * 端口
     */
    @Value("${mq.port}")
    private int port;
    /**
     * 用户名
     */
    @Value("${mq.username}")
    private String userName;
    /**
     * 密码
     */
    @Value("${mq.password}")
    private String password;
    /**
     * 虚拟主路径
     */
    @Value("${mq.vhost}")
    private String vhost;
    /**
     * 交换机
     */
    @Value("${mq.exchange:}")
    private String exchange;

    /**
     * 是否自动应答
     */
    @Value("${mq.autoAck:false}")
    private Boolean ack;
    /**
     * mq 队列名称前缀
     */
    @Value("${yn.mq.prefix}")
    private String prefix;
    /**
     * 用户信息实现类CLASS地址
     */
    @Value("${yn.mq.defaultSubjectPath:com.epoch.infrastructure.security.service.authc.DefaultSubject}")
    private String defaultSubjectPath;

    public String getDefaultSubjectPath() {
        return defaultSubjectPath;
    }

    public void setDefaultSubjectPath(String defaultSubjectPath) {
        this.defaultSubjectPath = defaultSubjectPath;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public Boolean getAck() {
        return ack;
    }

    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
