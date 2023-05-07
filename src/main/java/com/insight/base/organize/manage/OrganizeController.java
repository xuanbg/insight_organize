package com.insight.base.organize.manage;

import com.insight.base.organize.common.client.LogServiceClient;
import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.dto.OrganizeListDto;
import com.insight.utils.Json;
import com.insight.utils.pojo.auth.LoginInfo;
import com.insight.utils.pojo.base.BusinessException;
import com.insight.utils.pojo.base.Reply;
import com.insight.utils.pojo.base.Search;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    private final LogServiceClient client;
    private final OrganizeService service;

    /**
     * 构造方法
     *
     * @param client  Feign客户端
     * @param service 自动注入的Service
     */
    public OrganizeController(LogServiceClient client, OrganizeService service) {
        this.client = client;
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
     * 查询日志
     *
     * @param loginInfo 用户登录信息
     * @param search    查询条件
     * @return 日志集合
     */
    @GetMapping("/v1.0/organizes/{id}/logs")
    public Reply getAirportLogs(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id, Search search) {
        var info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        return client.getLogs(id, "Organize", search.getKeyword());
    }

    /**
     * 获取日志
     *
     * @param loginInfo 用户登录信息
     * @param id        日志ID
     * @return 日志VO
     */
    @GetMapping("/v1.0/organizes/logs/{id}")
    public Reply getAirportLog(@RequestHeader("loginInfo") String loginInfo, @PathVariable Long id) {
        var info = Json.toBeanFromBase64(loginInfo, LoginInfo.class);
        return client.getLog(id);
    }
}
