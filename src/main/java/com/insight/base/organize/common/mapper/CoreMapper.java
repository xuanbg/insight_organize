package com.insight.base.organize.common.mapper;

import com.insight.base.organize.common.dto.Organize;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 宣炳刚
 * @date 2019/12/5
 * @remark 组织机构核心DAL
 */
@Mapper
public interface CoreMapper {

    /**
     * 新增组织机构
     *
     * @param organize 组织机构DTO
     */
    @Insert("insert ibo_organize(id, tenant_id, parent_id, type, `index`, code, `name`, alias, full_name, remark, is_invalid, creator, creator_id, created_time) values " +
            "(#{id}, #{tenantId}, #{parentId}, #{type}, #{index}, #{code}, #{name}, #{alias}, #{fullName}, #{remark}, #{isInvalid}, #{creator}, #{creatorId}, #{createdTime});")
    void addOrganize(Organize organize);
}
