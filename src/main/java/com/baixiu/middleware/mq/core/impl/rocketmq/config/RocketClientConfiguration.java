package com.baixiu.middleware.mq.core.impl.rocketmq.config;

/**
 * rocket 客户端的配置参数类
 * @author baixiu
 * @date 创建时间 2023/11/29 3:36 PM
 */
public class RocketClientConfiguration {

    /**
     * 服务地址
     */
    private String address;

    /**
     * 消费者组 consumerGroup
     */
    private String app;

    /**
     * app key
     */
    private String user;

    /**
     * secretKey
     */
    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
