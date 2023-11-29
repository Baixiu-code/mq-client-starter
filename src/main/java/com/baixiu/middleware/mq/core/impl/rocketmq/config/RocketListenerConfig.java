package com.baixiu.middleware.mq.core.impl.rocketmq.config;

/**
 * @author chenfanglin1
 * @date 创建时间 2023/11/29 4:17 PM
 */
public class RocketListenerConfig {

    /**
     * topic name
     */
    private String topicName;

    /**
     * 注册的listener bean 的name
     */
    private String listenerBeanName;

    /**
     * rocket mq tag 能力支持
     */
    private String tag;

}
