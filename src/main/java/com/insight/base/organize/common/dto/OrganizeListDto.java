package com.insight.base.organize.common.dto;

import com.insight.util.Json;

import java.io.Serializable;

/**
 * @author 宣炳刚
 * @date 2019/12/4
 * @remark 角色列表DTO
 */
public class OrganizeListDto implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 组织机构ID
     */
    private String id;

    /**
     * 父级ID
     */
    private String parentId;

    /**
     * 节点类型:0.机构;1.部门;2.职位
     */
    private Integer type;

    /**
     * 序号
     */
    private Integer index;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String alias;

    /**
     * 全称
     */
    private String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return Json.toJson(this);
    }
}
