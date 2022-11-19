package com.insight.base.organize.common.mapper;

import com.insight.base.organize.common.dto.MemberUserDto;
import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import com.insight.utils.pojo.base.Search;
import com.insight.utils.pojo.base.TreeVo;
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
     * @return 组织机构列表
     */
    @Select("select id, parent_id, type, `index`, code, name, alias, full_name from ibo_organize where " +
            "tenant_id = #{tenantId} order by type, `index`;")
    List<OrganizeListDto> getOrganizes(Long tenantId);

    /**
     * 查询指定ID的机构的下级机构ID
     *
     * @param id 组织机构ID
     * @return 下级机构ID集合
     */
    @Select("with recursive orgs as (select o.id, o.parent_id, o.type, o.code, o.name from ibo_organize o where o.id = #{id} " +
            "union select p.id, p.parent_id, p.type, p.code, p.name from ibo_organize p join orgs s on s.id = p.parent_id) " +
            "select * from orgs;")
    List<TreeVo> getSubOrganizes(long id);

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return 组织机构详情
     */
    @Select("select * from ibo_organize where id = #{id};")
    Organize getOrganize(Long id);

    /**
     * 获取下属组织机构数量
     *
     * @param id 组织机构ID
     * @return 下属组织机构数量
     */
    @Select("select count(*) from ibo_organize where parent_id = #{id};")
    int getOrganizeCount(Long id);

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
    void deleteRole(Long id);

    /**
     * 查询组织机构成员用户
     *
     * @param search 查询关键词
     * @return 组织机构成员用户集合
     */
    @Select("<script>select u.id, u.code, u.name, u.account, u.mobile, u.is_invalid from ibo_organize_member m join ibu_user u on u.id = m.user_id " +
            "<if test = 'keyword != null'>and (u.code = #{keyword} or u.account = #{keyword} or u.name like concat('%',#{keyword},'%')) </if>" +
            "where m.post_id = #{id} order by u.created_time</script>")
    List<MemberUserDto> getMemberUsers(Search search);

    /**
     * 添加组织机构成员
     *
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Insert("<script>insert ibo_organize_member (post_id, user_id) values " +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">" +
            "(#{id}, #{item})</foreach>;</script>")
    void
    addMembers(@Param("id") Long id, @Param("list") List<Long> members);

    /**
     * 移除组织机构成员
     *
     * @param id      组织机构ID
     * @param members 组织机构成员ID集合
     */
    @Delete("<script>delete from ibo_organize_member where post_id = #{id} and user_id in (" +
            "<foreach collection = \"list\" item = \"item\" index = \"index\" separator = \",\">#{item}</foreach>);</script>")
    void removeMember(@Param("id") Long id, @Param("list") List<Long> members);
}
