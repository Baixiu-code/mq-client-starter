package com.baixiu.middleware.mq.core.inner;

import com.baixiu.middleware.mq.model.CommonMessage;

/**
 * abstracter msg producer
 * @author baixiu
 * @date 创建时间 2023/11/29 3:33 PM
 */
public interface CustomMessageProducer {


    /**
     * send msg
     * @param topic topic Name
     * @param message topic msg
     */
    void send(String topic, CommonMessage message);
}
