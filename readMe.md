# 一.rocketmq 环境配置
rocketmq 下载地址:
```https://rocketmq.apache.org/download/```

本文兼容rocketmq 版本4.9.7 
# 二.rocketmq 安装

## 2.1 解压rocketmq bin zip 包

```
upzip rocketmq-all-4.9.7-bin-release.zip
```

## 2.2 启动 namesrv 注册中心
```
sh bin/mqnamesrv 
```
## 2.3 启动 broker 消息中心
```nohup sh bin/mqbroker -n 127.0.0.1:9876 -c ../conf/broker.conf autoCreateTopicEnable=true &```


```nohup sh mqbroker -n 127.0.0.1:9876 -c ../conf/broker.conf autoCreateTopicEnable=true ```

验证是否启动broker成功 
```telnet 127.0.0.1 9876```



# 三.mq-client-starter middleware 接入

接入demo :
https://github.com/Baixiu-code/mq-client-starter-test

完整可运行
## 3.1 配置
    ```
        common-mq.rocketmq.transport.address=127.0.0.1:9876
        common-mq.rocketmq.transport.app=mq-client-test
        #common-mq.rocketmq.transport.user=baixiu
        #common-mq.rocketmq.transport.password=baixiuTest
        common-mq.rocketmq.producer.name=messageProducer
        common-mq.rocketmq.listeners[0].topicName=test
        common-mq.rocketmq.listeners[0].listenerBeanName=testConsumerMsg
        #common-mq.rocketmq.listeners[1].topicName=${mq.demo.topic2}
        #common-mq.rocketmq.listeners[1].listenerBeanName=topic2Listenerbrew update
    ```
## 3.2 junit 接入
    
send msg 

```
package com.baixiu.middleware.test;

import com.baixiu.middleware.mq.core.impl.rocketmq.config.RocketBootConfig;
import com.baixiu.middleware.mq.core.inner.CustomMessageProducer;
import com.baixiu.middleware.mq.model.CommonMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author baixiu
 * @date 创建时间 2023/11/29 7:15 PM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ComponentScan(basePackages={"com.baixiu.middleware.test","com.baixiu.middleware.mq"})
@TestPropertySource("classpath:rocketmq.properties")
public class TestProducerMsg {

    @Autowired
    private CustomMessageProducer customMessageProducer;

    public void sendMsgTest(){
        try {
            CommonMessage commonMessage=new CommonMessage ();
            commonMessage.setTopic("test");
            commonMessage.setText ("hello baixiu mq.");
            customMessageProducer.send ("test",commonMessage);
            System.out.println ("test topic send succeed");
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }

    @Test
    public void testSendMsg(){
        sendMsgTest();
    }

}
```


consumer msg
```
package com.baixiu.middleware.test;

import com.baixiu.middleware.mq.core.inner.CustomMessageListener;
import com.baixiu.middleware.mq.model.CommonMessage;
import org.springframework.stereotype.Component;

/**
 * @author baixiu
 * @date 创建时间 2023/12/5 2:43 PM
 */
@Component
public class TestConsumerMsg implements CustomMessageListener {

    @Override
    public void onMessage(CommonMessage commonMessage) {
        System.out.println("接收到消息："+commonMessage);
    }

}
```
## 3.3 发送消息测试
![生产发送消息测试](https://github.com/Baixiu-code/mq-client-starter-test/blob/master/msg%20producer.png)
## 3.4 消费消息测试

![消费消息图片](https://github.com/Baixiu-code/mq-client-starter-test/blob/master/msg%20consumer.png)

# 四.遇到的问题
## 4.1 connect to [127.0.0.1:9876] failed

需要同时启动namesrv

```sh mqnamesrv ```

   ##  4.2 connect to 0.0.1.1:10911 failed.
        ```brokerIP1 = 127.0.0.1```
       ``` namesrvAddr = 127.0.0.1:9876```
        关闭broker 
        ```sh bin/mqshutdown broker```
        关闭namesrv 
        ```sh bin/mqshutdown namesrv```
