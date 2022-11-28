package com.insight.base.organize.manage;

import com.github.pagehelper.PageHelper;
import com.insight.base.organize.common.Core;
import com.insight.base.organize.common.client.LogClient;
import com.insight.base.organize.common.client.LogServiceClient;
import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import com.insight.base.organize.common.mapper.OrganizeMapper;
import com.insight.utils.ReplyHelper;
import com.insight.utils.SnowflakeCreator;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.BusinessException;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.message.OperateType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 组织机构管理服务
 */
@Service
public class OrganizeServiceImpl implements OrganizeService {
    private static final String BUSINESS = "组织机构管理";
    private final SnowflakeCreator creator;
    private final OrganizeMapper mapper;
    private final LogServiceClient client;
    private final Core core;

    /**
     * 构造方法
     *
     * @param creator 雪花算法ID生成器
     * @param mapper  RoleMapper
     * @param client  LogServiceClient
     * @param core    Core
     */
    public OrganizeServiceImpl(SnowflakeCreator creator, OrganizeMapper mapper, LogServiceClient client, Core core) {
        this.creator = creator;
        this.mapper = mapper;
        this.client = client;
        this.core = core;
    }

    /**
     * 查询组织机构列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public List<OrganizeListDto> getOrganizes(Search search) {
        Long id = search.getId();
        if (id != null) {
            return mapper.getSubOrganizes(id);
        } else {
            Long tenantId = search.getTenantId();
            if (tenantId == null) {
                throw new BusinessException("租户ID不能为空");
            }

            return mapper.getOrganizes(tenantId);
        }
    }

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return Reply
     */
    @Override
    public Organize getOrganize(Long id) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        return organize;
    }

    /**
     * 新增组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    @Override
    public Long newOrganize(LoginInfo info, Organize dto) {
        Long id = creator.nextId(6);
        dto.setId(id);
        dto.setTenantId(info.getTenantId());
        dto.setCreator(info.getUserName());
        dto.setCreatorId(info.getUserId());

        core.addOrganize(dto);
        LogClient.writeLog(info, BUSINESS, OperateType.INSERT, id, dto);

        return id;
    }

    /**
     * 编辑组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     */
    @Override
    public void editOrganize(LoginInfo info, Organize dto) {
        Long id = dto.getId();
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未更新数据");
        }

        mapper.updateOrganize(dto);
        LogClient.writeLog(info, BUSINESS, OperateType.UPDATE, id, dto);
    }

    /**
     * 删除组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     */
    @Override
    public void deleteOrganize(LoginInfo info, Long id) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未删除数据");
        }

        int count = mapper.getOrganizeCount(id);
        if (count > 0) {
            throw new BusinessException("存在下属节点,请先删除下属节点");
        }

        mapper.deleteRole(id);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, organize);
    }

    /**
     * 查询组织机构成员用户
     *
     * @param id     组织机构ID
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public Reply getMemberUsers(Long id, Search search) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未读取数据");
        }

        search.setId(id);
        var page = PageHelper.startPage(search.getPageNum(), search.getPageSize())
                .setOrderBy(search.getOrderBy()).doSelectPage(() -> mapper.getMemberUsers(search));

        var total = page.getTotal();
        return total > 0 ? ReplyHelper.success(page.getResult(), total) : ReplyHelper.resultIsEmpty();
    }

    /**
     * 添加组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Override
    public void addMembers(LoginInfo info, Long id, List<Long> members) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未更新数据");
        }

        mapper.addMembers(id, members);
        LogClient.writeLog(info, BUSINESS, OperateType.INSERT, id, members);
    }

    /**
     * 移除组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Override
    public void removeMember(LoginInfo info, Long id, List<Long> members) {
        Organize organize = mapper.getOrganize(id);
        if (organize == null) {
            throw new BusinessException("ID不存在,未删除数据");
        }

        mapper.removeMember(id, members);
        LogClient.writeLog(info, BUSINESS, OperateType.DELETE, id, members);
    }

    /**
     * 获取日志列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @Override
    public Reply getOrganizeLogs(Search search) {
        return client.getLogs(BUSINESS, search.getKeyword(), search.getPageNum(), search.getPageSize());
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @Override
    public Reply getOrganizeLog(Long id) {
        return client.getLog(id);
    }
}
