package com.baixiu.middleware.mq.core.inner;

import com.baixiu.middleware.mq.exception.MessageException;
import com.baixiu.middleware.mq.model.CommonMessage;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

/**
 * 抽象消息生产类
 * @author baixiu
 * @date 创建时间 2023/11/29 4:40 PM
 */
public abstract class AbstractMessageProducer implements CustomMessageProducer {

    public AbstractMessageProducer(){

    }

    @Override
    public void send(String topic, CommonMessage message) {
        if(StringUtils.isNotBlank (topic)){
            if(Objects.nonNull(message) && StringUtils.isNotBlank(message.getText())){
                this.doSend(topic,message);
            }else{
                throw new MessageException("baixiuMQ-1001","send message is null.");
            }
        } else {
            throw new MessageException ("baixiuMQ-1002","send topic is null");
        }
    }

    protected abstract void doSend(String topic, CommonMessage message);
}
