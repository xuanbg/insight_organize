package com.insight.base.organize.manage;

import com.insight.base.organize.common.dto.Organize;
import com.insight.util.pojo.LoginInfo;
import com.insight.util.pojo.Reply;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 组织机构管理服务接口
 */
public interface OrganizeService {

    /**
     * 查询组织机构列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    Reply getOrganizes(String tenantId, String keyword, int page, int size);

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return Reply
     */
    Reply getOrganize(String id);

    /**
     * 新增组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    Reply newOrganize(LoginInfo info, Organize dto);

    /**
     * 编辑组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    Reply editOrganize(LoginInfo info, Organize dto);

    /**
     * 删除组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     * @return Reply
     */
    Reply deleteOrganize(LoginInfo info, String id);

    /**
     * 查询组织机构成员用户
     *
     * @param id      组织机构ID
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    Reply getMemberUsers(String id, String keyword, int page, int size);

    /**
     * 添加组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     * @return Reply
     */
    Reply addMembers(LoginInfo info, String id, List<String> members);

    /**
     * 移除组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     * @return Reply
     */
    Reply removeMember(LoginInfo info, String id, List<String> members);

    /**
     * 获取日志列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    Reply getOrganizeLogs(String tenantId, String keyword, int page, int size);

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    Reply getOrganizeLog(String id);
}
