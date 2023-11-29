package com.baixiu.middleware.mq.core.impl.rocketmq.config;

/**
 * producer config 配置
 * @author baixiu
 * @date 创建时间 2023/11/29 4:14 PM
 */
public class RocketProducerConfig {

    private String name;

    public RocketProducerConfig(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
