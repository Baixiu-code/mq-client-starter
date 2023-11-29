package com.baixiu.middleware.mq.core.impl.rocketmq.config;

import com.baixiu.middleware.mq.core.impl.rocketmq.producer.RocketMessageProducer;
import com.baixiu.middleware.mq.core.inner.CustomMessageListener;
import com.baixiu.middleware.mq.core.inner.CustomMessageProducer;
import com.baixiu.middleware.mq.exception.MessageException;
import com.baixiu.middleware.mq.model.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baixiu
 * @date 创建时间 2023/11/29 4:35 PM
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({RocketCommonConfig.class})
public class RocketBootConfig implements ApplicationContextAware {

    @Autowired
    private RocketCommonConfig rocketConfig;

    ApplicationContext applicationContext;

    private Map<String, CustomMessageListener> TOPIC_TO_LISTENERS;

    public RocketBootConfig() {

    }

    /**
     * 在配置了common-mq.配置地址后进行rocket producer 初始化
     * mq producer
     * @return
     */
    @Bean(name = {"defaultRocketMQProducer"},
            initMethod = "start",
            destroyMethod = "shutdown")
    @ConditionalOnProperty({"common-mq.rocketmq.transport.address"})
    public DefaultMQProducer getRocketProducer() {
        RocketClientConfiguration transport = this.rocketConfig.getTransport();
        String user = this.rocketConfig.getTransport().getUser();
        String password = this.rocketConfig.getTransport().getPassword();
        // 创建消息生产者
        DefaultMQProducer defaultMQProducer;
        //生产支持用户名和密码模式
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
            defaultMQProducer = new DefaultMQProducer(transport.getApp(), getAclRpcHook()
                    , true, "RMQ_SYS_TRACE_TOPIC");
        } else {
            defaultMQProducer = new DefaultMQProducer(transport.getApp(), true
                    , "RMQ_SYS_TRACE_TOPIC");
        }
        // 设置生产者NameServer地址，用于寻找Broker
        defaultMQProducer.setNamesrvAddr(transport.getAddress());
        return defaultMQProducer;

    }

    /**
     * 在完成默认rocketMQ producer 后进行 自定义rocket message producer 设置。
     * 设置通过set方法进行 bean 注入
     * @return
     */
    @Bean(name = {"defaultRocketCommonMessageProducer"})
    @ConditionalOnBean(name = {"defaultRocketMQProducer"})
    public CustomMessageProducer customMessageProducer() {
        RocketMessageProducer rocketMessageProducer = new RocketMessageProducer();
        rocketMessageProducer.setProducer(this.getRocketProducer());
        return rocketMessageProducer;
    }

    @Bean(name = {"rocketListenersInit"})
    @ConditionalOnBean(
            value = {CustomMessageListener.class}
    )
    @ConditionalOnProperty({"common-mq.rocketmq.transport.address"})
    public String listeners() {
        List<RocketListenerConfig> confs = this.rocketConfig.getListeners();
        if (CollectionUtils.isEmpty(confs)) {
            return "false";
        } else {
            this.registerMessageConsumer(confs);
            return "true";
        }
    }

    /**
     * 注册的消费者
     * @param listenerConfigs
     */
    private void registerMessageConsumer(List<RocketListenerConfig> listenerConfigs) {
        TOPIC_TO_LISTENERS = new HashMap<> (listenerConfigs.size(),0.75f);
        for (RocketListenerConfig listenerConfig : listenerConfigs) {
            CustomMessageListener listener = this.applicationContext.getBean(listenerConfig.getListenerBeanName ()
                    , CustomMessageListener.class);
            TOPIC_TO_LISTENERS.put(listenerConfig.getTopicName(), listener);
        }
        String user = this.rocketConfig.getTransport().getUser();
        String password = this.rocketConfig.getTransport().getPassword();
        DefaultMQPushConsumer consumer;
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {
            AclClientRPCHook rpcHook = new AclClientRPCHook(new SessionCredentials (user, password));
            consumer = new DefaultMQPushConsumer(rocketConfig.getTransport().getApp(), rpcHook
                    , new AllocateMessageQueueAveragely (), true, "RMQ_SYS_TRACE_TOPIC");
        } else {
            consumer = new DefaultMQPushConsumer(rocketConfig.getTransport().getApp()
                    , true, "RMQ_SYS_TRACE_TOPIC");
        }
        try {
            consumer.setNamesrvAddr(rocketConfig.getTransport().getAddress());
            for (RocketListenerConfig listenerConfig : listenerConfigs) {
                consumer.subscribe(listenerConfig.getTopicName (), "*");
            }
            consumer.registerMessageListener(new DefaultMessageListenerConcurrently());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.start();
            System.out.printf("Baixiu.Rocketmq Consumer Start succeed.%n");
        } catch (Exception e) {
            log.error("Baixiu.Rocketmq Start container failed. {}", consumer, e);
            throw new MessageException(e);
        }
    }

    private int delayLevelWhenNextConsume = 0;

    public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

        @SuppressWarnings("unchecked")
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : msgs) {
                log.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    handleMessage(messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    log.warn("consume message failed. messageId:{}, topic:{}, reconsumeTimes:{}"
                            , messageExt.getMsgId(), messageExt.getTopic(), messageExt.getReconsumeTimes(), e);
                    context.setDelayLevelWhenNextConsume(delayLevelWhenNextConsume);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    private void handleMessage(
            MessageExt messageExt) {
        CustomMessageListener commonMessageListener = TOPIC_TO_LISTENERS.get(messageExt.getTopic());
        if (commonMessageListener != null) {
            String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
            CommonMessage commonMessage = CommonMessage.createMessage(messageExt.getTopic(), messageExt.getKeys(),
                    body,messageExt.getBody());
            commonMessageListener.onMessage(commonMessage);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @return
     */
    public RPCHook getAclRpcHook() {
        return new AclClientRPCHook(new SessionCredentials(this.rocketConfig.getTransport().getUser()
                , this.rocketConfig.getTransport().getPassword()));
    }

}
