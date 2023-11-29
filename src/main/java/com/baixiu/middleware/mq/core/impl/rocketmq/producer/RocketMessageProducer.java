package com.baixiu.middleware.mq.core.impl.rocketmq.producer;

import com.baixiu.middleware.mq.core.inner.AbstractMessageProducer;
import com.baixiu.middleware.mq.model.CommonMessage;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * rocket mq 具体消息生产者实现
 * 提供最终的消息发送
 * @author baixiu
 * @date 创建时间 2023/11/29 4:38 PM
 */
public class RocketMessageProducer extends AbstractMessageProducer {

    private DefaultMQProducer producer;

    @Override
    protected void doSend(String topic, CommonMessage message) {
        Message rocketMessage = new Message();
        rocketMessage.setTopic(topic);
        rocketMessage.setKeys(message.getBusinessId());
        rocketMessage.setBody(message.getText().getBytes());
        try {
            this.producer.send(rocketMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }
}
