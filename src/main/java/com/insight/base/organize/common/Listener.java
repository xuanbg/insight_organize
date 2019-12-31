package com.insight.base.organize.common;

import com.insight.util.pojo.Organize;
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
    public void receiveOrganize(Organize dto) {
        core.addOrganize(dto);
    }
}