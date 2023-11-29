package com.baixiu.middleware.mq.core;

import com.baixiu.middleware.mq.model.CommonMessage;

/**
 * abstract message listener
 * 用以具体的技术实现来进行复写
 * 如：rocket mq xxxMessageListener. 复写onMessage 来具体实现细节
 *
 * @author baixiu
 * @date 创建时间 2023/11/29 3:23 PM
 */
public interface  MessageListener {

    void onMessage(CommonMessage msg);

}
