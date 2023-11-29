package com.baixiu.middleware.mq.core.impl.rocketmq.config;

import java.util.List;

/**
 * consumer config
 * @author baixiu
 * @date 创建时间 2023/11/29 4:16 PM
 */
public class RocketConsumerConfig {

    private List<RocketListenerConfig> listeners;

    public RocketConsumerConfig() {

    }

    public List<RocketListenerConfig> getListeners() {
        return listeners;
    }

    public void setListeners(List<RocketListenerConfig> listeners) {
        this.listeners = listeners;
    }
}
