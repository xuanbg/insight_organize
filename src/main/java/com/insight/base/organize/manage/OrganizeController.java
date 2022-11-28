package com.insight.base.organize.manage;

import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import com.insight.utils.Json;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.BusinessException;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 宣炳刚
 * @date 2019-09-01
 * @remark 组织机构管理服务控制器
 */
@CrossOrigin
@RestController
@RequestMapping("/base/organize")
public class OrganizeController {
    private final OrganizeService service;

    /**
     * 构造方法
     *
     * @param service 自动注入的Service
     */
    public OrganizeController(OrganizeService service) {
        this.service = service;
    }

    /**
     * 查询组织机构列表
     *
     * @param info   用户关键信息
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/organizes")
    public List<OrganizeListDto> getOrganizes(@RequestHeader("loginInfo") String info, Search search) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        search.setTenantId(loginInfo.getTenantId());

        return service.getOrganizes(search);
    }

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/{id}")
    public Organize getOrganize(@PathVariable Long id) {
        return service.getOrganize(id);
    }

    /**
     * 新增组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    @PostMapping("/v1.0/organizes")
    public Long newOrganize(@RequestHeader("loginInfo") String info, @Valid @RequestBody Organize dto) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.newOrganize(loginInfo, dto);
    }

    /**
     * 编辑组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     * @param dto  组织机构DTO
     */
    @PutMapping("/v1.0/organizes/{id}")
    public void editOrganize(@RequestHeader("loginInfo") String info, @PathVariable Long id, @Valid @RequestBody Organize dto) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        dto.setId(id);

        service.editOrganize(loginInfo, dto);
    }

    /**
     * 删除组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     */
    @DeleteMapping("/v1.0/organizes/{id}")
    public void deleteOrganize(@RequestHeader("loginInfo") String info, @PathVariable Long id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        service.deleteOrganize(loginInfo, id);
    }

    /**
     * 查询组织机构成员用户
     *
     * @param id     组织机构ID
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/{id}/users")
    public Reply getMemberUsers(@PathVariable Long id, Search search) {
        return service.getMemberUsers(id, search);
    }

    /**
     * 添加组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员集合
     */
    @PostMapping("/v1.0/organizes/{id}/members")
    public void addOrganizeMembers(@RequestHeader("loginInfo") String info, @PathVariable Long id, @RequestBody List<Long> members) {
        if (members == null || members.isEmpty()) {
            throw new BusinessException("请选择需要添加的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        service.addMembers(loginInfo, id, members);
    }

    /**
     * 移除组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员DTO
     */
    @DeleteMapping("/v1.0/organizes/{id}/members")
    public void removeOrganizeMembers(@RequestHeader("loginInfo") String info, @PathVariable Long id, @RequestBody List<Long> members) {
        if (members == null || members.isEmpty()) {
            throw new BusinessException("请选择需要移除的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        service.removeMember(loginInfo, id, members);
    }

    /**
     * 获取日志列表
     *
     * @param search 查询实体类
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs")
    public Reply getOrganizeLogs(Search search) {

        return service.getOrganizeLogs(search);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs/{id}")
    public Reply getOrganizeLog(@PathVariable Long id) {
        return service.getOrganizeLog(id);
    }
}
