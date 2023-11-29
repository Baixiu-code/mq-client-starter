package com.baixiu.middleware.mq.core.impl.rocketmq.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenfanglin1
 * @date 创建时间 2023/11/29 4:19 PM
 */
@ConfigurationProperties(prefix = "saas-mq.rocketmq")
public class RocketCommonConfig {

    private RocketClientConfiguration transport;

    private RocketProducerConfig producer;

    private List<RocketListenerConfig> listeners;


    public RocketCommonConfig(){

    }

    public RocketClientConfiguration getTransport() {
        return transport;
    }

    public void setTransport(RocketClientConfiguration transport) {
        this.transport = transport;
    }

    public RocketProducerConfig getProducer() {
        return producer;
    }

    public void setProducer(RocketProducerConfig producer) {
        this.producer = producer;
    }

    public List<RocketListenerConfig> getListeners() {
        return listeners;
    }

    public void setListeners(List<RocketListenerConfig> listeners) {
        this.listeners = listeners;
    }
}
