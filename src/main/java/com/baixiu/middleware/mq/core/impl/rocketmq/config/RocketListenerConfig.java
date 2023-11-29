package com.baixiu.middleware.mq.core.impl.rocketmq.config;

/**
 * @author baixiu
 * @date 创建时间 2023/11/29 4:17 PM
 */
public class RocketListenerConfig extends RocketConsumerConfig{

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


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getListenerBeanName() {
        return listenerBeanName;
    }

    public void setListenerBeanName(String listenerBeanName) {
        this.listenerBeanName = listenerBeanName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
