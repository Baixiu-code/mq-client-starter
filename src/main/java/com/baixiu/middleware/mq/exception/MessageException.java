package com.baixiu.middleware.mq.exception;

/**
 * custom message exception to define errorCode
 * @author baixiu
 * @date 创建时间 2023/11/29 2:58 PM
 */
public class MessageException extends RuntimeException{

    private String errorCode;

    public MessageException(String errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }

    public MessageException(String errorCode,String message,Throwable throwable){
        super(message,throwable);
        this.errorCode=errorCode;
    }

    public MessageException(Exception e) {
        super(e);
    }
}
