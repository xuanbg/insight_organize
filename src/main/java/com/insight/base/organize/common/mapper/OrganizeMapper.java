package com.insight.base.organize.common.mapper;

import com.insight.base.organize.common.dto.MemberUserDto;
import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019/12/4
 * @remark 组织机构DTO
 */
@Mapper
public interface OrganizeMapper {
    /**
     * 获取组织机构列表
     *
     * @param tenantId 租户ID
     * @param key      查询关键词
     * @return 组织机构列表
     */
    @Select("<script>select id, parent_id, type, `index`, code, name, alias, full_name from ibo_organize where " +
            "<if test = 'tenantId != null'>tenant_id = #{tenantId} </if>" +
            "<if test = 'tenantId == null'>tenant_id is null </if>" +
            "<if test = 'key != null'>and (code = #{key} or name like concat('%',#{key},'%')) </if>" +
            "order by type, `index`</script>")
    List<OrganizeListDto> getOrganizes(@Param("tenantId") String tenantId, @Param("key") String key);

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return 组织机构详情
     */
    @Select("select * from ibo_organize where id = #{id};")
    Organize getOrganize(String id);

    /**
     * 获取下属组织机构数量
     *
     * @param id 组织机构ID
     * @return 下属组织机构数量
     */
    @Select("select count(*) from ibo_organize where parent_id = #{id};")
    int getOrganizeCount(String id);

    /**
     * 更新组织机构
     *
     * @param organize 组织机构DTO
     */
    @Update("update ibo_organize set parent_id = #{parentId}, type = #{type}, `index` = #{index}, code = #{code}, name = #{name}, " +
            "alias = #{alias}, full_name = #{fullName}, remark = #{remark} where id = #{id};")
    void updateOrganize(Organize organize);

    /**
     * 删除组织机构
     *
     * @param id 组织机构ID
     */
    @Delete("delete o, m from ibo_organize o left join ibo_organize_member m on m.post_id = o.id where o.id = #{id};")
    void deleteRole(String id);

    /**
     * 查询组织机构成员用户
     *
     * @param id  组织机构ID
     * @param key 查询关键词
     * @return 组织机构成员用户集合
     */
    @Select("<script>select u.id, u.code, u.name, u.account, u.mobile, u.is_invalid from ibo_organize_member m join ibu_user u on u.id = m.user_id " +
            "<if test = 'key != null'>and (u.code = #{key} or u.account = #{key} or u.name like concat('%',#{key},'%')) </if>" +
            "where m.post_id = #{id} order by u.created_time</script>")
    List<MemberUserDto> getMemberUsers(@Param("id") String id, @Param("key") String key);

    /**
     * 添加组织机构成员
     *
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Insert("<script>insert ibo_organize_member (id, post_id, user_id) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(replace(uuid(), '-', ''), #{id}, #{item})</foreach>;</script>")
    void
    addMembers(@Param("id") String id, @Param("list") List<String> members);

    /**
     * 移除组织机构成员
     *
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Delete("<script>delete from ibo_organize_member where post_id = #{id} and user_id in (" +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">#{item}</foreach>);</script>")
    void removeMember(@Param("id") String id, @Param("list") List<String> members);
}
