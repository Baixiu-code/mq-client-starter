package com.baixiu.middleware.mq.core.inner;

import com.baixiu.middleware.mq.model.CommonMessage;

/**
 * abstract message listener
 * 用以具体的技术实现来进行复写
 * 如：具体的业务类 复写onMessage 来具体实现细节消费细节。
 * 中间件将通过不同的配置实现不同消费回调。
 *
 * @author baixiu
 * @date 创建时间 2023/11/29 3:23 PM
 */
public interface CustomMessageListener {

    /**
     * MQ message listener 各大中间件将直接实现onMessage 方法实现消息监听
     * @param msg
     */
    void onMessage(CommonMessage msg);

}
