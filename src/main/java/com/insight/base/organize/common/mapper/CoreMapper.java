package com.insight.base.organize.common.mapper;

import com.insight.base.organize.common.dto.Organize;
import com.insight.util.common.JsonTypeHandler;
import com.insight.util.pojo.Log;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    /**
     * 记录操作日志
     *
     * @param log 日志DTO
     */
    @Insert("insert ibl_operate_log(id, tenant_id, type, business, business_id, content, creator, creator_id, created_time) values " +
            "(#{id}, #{tenantId}, #{type}, #{business}, #{businessId}, #{content, typeHandler = com.insight.util.common.JsonTypeHandler}, " +
            "#{creator}, #{creatorId}, #{createdTime});")
    void addLog(Log log);

    /**
     * 获取操作日志列表
     *
     * @param tenantId 租户ID
     * @param business 业务类型
     * @param key      查询关键词
     * @return 操作日志列表
     */
    @Select("<script>select id, type, business, business_id, creator, creator_id, created_time " +
            "from ibl_operate_log where business = #{business} " +
            "<if test = 'tenantId != null'>and tenant_id = #{tenantId} </if>" +
            "<if test = 'tenantId == null'>and tenant_id is null </if>" +
            "<if test = 'key!=null'>and (type = #{key} or business = #{key} or business_id = #{key} or " +
            "creator = #{key} or creator_id = #{key}) </if>" +
            "order by created_time</script>")
    List<Log> getLogs(@Param("tenantId") String tenantId, @Param("business") String business, @Param("key") String key);

    /**
     * 获取操作日志列表
     *
     * @param id 日志ID
     * @return 操作日志列表
     */
    @Results({@Result(property = "content", column = "content", javaType = Object.class, typeHandler = JsonTypeHandler.class)})
    @Select("select * from ibl_operate_log where id = #{id};")
    Log getLog(String id);
}
