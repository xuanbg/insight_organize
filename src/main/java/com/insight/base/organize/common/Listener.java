package com.insight.base.organize.common;

import com.insight.base.organize.common.config.QueueConfig;
import com.insight.base.organize.common.dto.Organize;
import com.insight.utils.Json;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 宣炳刚
 * @date 2019-09-03
 * @remark 组织机构队列监听类
 */
@Component
public class Listener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Core core;

    /**
     * 构造方法
     *
     * @param core Core
     */
    public Listener(Core core) {
        this.core = core;
    }

    /**
     * 从队列订阅新增组织机构消息
     *
     * @param channel Channel
     * @param message Message
     * @throws IOException IOException
     */
    @RabbitHandler
    @RabbitListener(queues = "insight.organize")
    public void receiveOrganize(Channel channel, Message message) throws IOException {
        try {
            String body = new String(message.getBody());
            core.addOrganize(Json.toBean(body, Organize.class));
        } catch (Exception ex) {
            logger.error("发生异常: {}", ex.getMessage());
            channel.basicPublish(QueueConfig.DELAY_EXCHANGE_NAME, QueueConfig.DELAY_QUEUE_NAME, null, message.getBody());
        }
    }
}