package com.insight.base.organize.manage;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.insight.base.organize.common.Core;
import com.insight.base.organize.common.dto.MemberUserDto;
import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import com.insight.base.organize.common.mapper.OrganizeMapper;
import com.insight.utils.ReplyHelper;
import com.insight.utils.Util;
import com.insight.utils.pojo.Log;
import com.insight.utils.pojo.LoginInfo;
import com.insight.utils.pojo.OperateType;
import com.insight.utils.pojo.Reply;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 组织机构管理服务
 */
@Service
public class OrganizeServiceImpl implements OrganizeService {
    private final OrganizeMapper mapper;
    private final Core core;

    /**
     * 构造方法
     *
     * @param mapper RoleMapper
     * @param core   Core
     */
    public OrganizeServiceImpl(OrganizeMapper mapper, Core core) {
        this.mapper = mapper;
        this.core = core;
    }

    /**
     * 查询组织机构列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    @Override
    public Reply getOrganizes(String tenantId, String keyword, int page, int size) {
        PageHelper.startPage(page, size);
        List<OrganizeListDto> organizes = mapper.getOrganizes(tenantId, keyword);
        PageInfo<OrganizeListDto> pageInfo = new PageInfo<>(organizes);

        return ReplyHelper.success(organizes, pageInfo.getTotal());
    }

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return Reply
     */
    @Override
    public Reply getOrganize(String id) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        return ReplyHelper.success(organize);
    }

    /**
     * 新增组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    @Override
    public Reply newOrganize(LoginInfo info, Organize dto) {
        String id = Util.uuid();
        dto.setId(id);
        dto.setTenantId(info.getTenantId());
        dto.setCreator(info.getUserName());
        dto.setCreatorId(info.getUserId());

        core.addOrganize(dto);
        core.writeLog(info, OperateType.INSERT, "组织机构管理", id, dto);

        return ReplyHelper.created(id);
    }

    /**
     * 编辑组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    @Override
    public Reply editOrganize(LoginInfo info, Organize dto) {
        String id = dto.getId();
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        mapper.updateOrganize(dto);
        core.writeLog(info, OperateType.UPDATE, "组织机构管理", id, dto);

        return ReplyHelper.success();
    }

    /**
     * 删除组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     * @return Reply
     */
    @Override
    public Reply deleteOrganize(LoginInfo info, String id) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未删除数据");
        }

        int count = mapper.getOrganizeCount(id);
        if (count > 0) {
            return ReplyHelper.fail("存在下属节点,请先删除下属节点");
        }

        mapper.deleteRole(id);
        core.writeLog(info, OperateType.DELETE, "组织机构管理", id, organize);

        return ReplyHelper.success();
    }

    /**
     * 查询组织机构成员用户
     *
     * @param id      组织机构ID
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @Override
    public Reply getMemberUsers(String id, String keyword, int page, int size) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        PageHelper.startPage(page, size);
        List<MemberUserDto> users = mapper.getMemberUsers(id, keyword);
        PageInfo<MemberUserDto> pageInfo = new PageInfo<>(users);

        return ReplyHelper.success(users, pageInfo.getTotal());
    }

    /**
     * 添加组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     * @return Reply
     */
    @Override
    public Reply addMembers(LoginInfo info, String id, List<String> members) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未更新数据");
        }

        mapper.addMembers(id, members);
        core.writeLog(info, OperateType.INSERT, "组织机构管理", id, members);

        return ReplyHelper.success();
    }

    /**
     * 移除组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     * @return Reply
     */
    @Override
    public Reply removeMember(LoginInfo info, String id, List<String> members) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            return ReplyHelper.fail("ID不存在,未删除数据");
        }

        mapper.removeMember(id, members);
        core.writeLog(info, OperateType.DELETE, "组织机构管理", id, members);

        return ReplyHelper.success();
    }

    /**
     * 获取日志列表
     *
     * @param tenantId 租户ID
     * @param keyword  查询关键词
     * @param page     分页页码
     * @param size     每页记录数
     * @return Reply
     */
    @Override
    public Reply getOrganizeLogs(String tenantId, String keyword, int page, int size) {
        PageHelper.startPage(page, size);
        List<Log> logs = core.getLogs(tenantId, "组织机构管理", keyword);
        PageInfo<Log> pageInfo = new PageInfo<>(logs);

        return ReplyHelper.success(logs, pageInfo.getTotal());
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @Override
    public Reply getOrganizeLog(String id) {
        Log log = core.getLog(id);
        if (log == null) {
            return ReplyHelper.fail("ID不存在,未读取数据");
        }

        return ReplyHelper.success(log);
    }
}
