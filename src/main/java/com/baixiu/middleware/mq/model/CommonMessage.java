package com.baixiu.middleware.mq.model;

/**
 * 通用msg bean。用以抹平不同平台的message bean.
 * 仅设置通用字段
 * @author baixiu
 * @date 创建时间 2023/11/29 2:43 PM
 */
public class CommonMessage {

    /**
     * topic name
     */
    private String topic;

    /**
     * 业务Id
     */
    private String businessId;

    /**
     * 消息体
     */
    private String text;

    /**
     * msg byte stream
     */
    private byte[] body;

    /**
     * 延迟队列等级
     */
    private Integer delayTimeLevel;

    public CommonMessage(String topic, String businessId, String text, Integer delayTimeLevel) {
        this.topic = topic;
        this.businessId = businessId;
        this.text = text;
        this.delayTimeLevel = delayTimeLevel;
    }

    public CommonMessage() {
    }



    public static CommonMessage createMessage(String topic, String businessId, String text, byte[] body) {
        CommonMessage commonMessage = new CommonMessage();
        commonMessage.setBusinessId(businessId);
        commonMessage.setText(text);
        commonMessage.setTopic(topic);
        commonMessage.setBody(body);
        return commonMessage;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Integer getDelayTimeLevel() {
        return delayTimeLevel;
    }

    public void setDelayTimeLevel(Integer delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel;
    }
}
