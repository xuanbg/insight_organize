package com.insight.base.organize.common;

import com.insight.util.pojo.Organize;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
     * @param dto 队列消息
     */
    @RabbitHandler
    @RabbitListener(queues = "insight.organize")
    public void receiveOrganize(Organize dto, Channel channel, Message message) {
        try {
            core.addOrganize(dto);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ex) {
            logger.error("发生异常: {}", ex.getMessage());
        }
    }
}