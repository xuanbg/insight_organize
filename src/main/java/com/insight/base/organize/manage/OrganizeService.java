package com.insight.base.organize.manage;

import com.insight.util.pojo.Reply;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 组织机构管理服务接口
 */
public interface OrganizeService {

    /**
     * 获取日志列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    Reply getRoleLogs(String tenantId, String keyword, int page, int size);

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    Reply getRoleLog(String id);
}
