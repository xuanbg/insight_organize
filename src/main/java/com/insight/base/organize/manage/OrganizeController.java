package com.insight.base.organize.manage;

import com.insight.base.organize.common.dto.Organize;
import com.insight.util.Json;
import com.insight.util.ReplyHelper;
import com.insight.util.pojo.LoginInfo;
import com.insight.util.pojo.Reply;
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
     * @param info    用户关键信息
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @GetMapping("/v1.0/organizes")
    public Reply getOrganizes(@RequestHeader("loginInfo") String info, @RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getOrganizes(loginInfo.getTenantId(), keyword, page, size);
    }

    /**
     * 获取组织机构详情
     *
     * @param id 组织机构ID
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/{id}")
    public Reply getOrganize(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ReplyHelper.invalidParam();
        }

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
    public Reply newOrganize(@RequestHeader("loginInfo") String info, @Valid @RequestBody Organize dto) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.newOrganize(loginInfo, dto);
    }

    /**
     * 编辑组织机构
     *
     * @param info 用户关键信息
     * @param dto  组织机构DTO
     * @return Reply
     */
    @PutMapping("/v1.0/organizes")
    public Reply editOrganize(@RequestHeader("loginInfo") String info, @Valid @RequestBody Organize dto) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.editOrganize(loginInfo, dto);
    }

    /**
     * 删除组织机构
     *
     * @param info 用户关键信息
     * @param id   组织机构ID
     * @return Reply
     */
    @DeleteMapping("/v1.0/organizes")
    public Reply deleteOrganize(@RequestHeader("loginInfo") String info, @RequestBody String id) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.deleteOrganize(loginInfo, id);
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
    @GetMapping("/v1.0/organizes/{id}/users")
    public Reply getMemberUsers(@PathVariable String id, @RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        return service.getMemberUsers(id, keyword, page, size);
    }

    /**
     * 添加组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员集合
     * @return Reply
     */
    @PostMapping("/v1.0/organizes/{id}/members")
    public Reply addOrganizeMembers(@RequestHeader("loginInfo") String info, @PathVariable String id, @RequestBody List<String> members) {
        if (members == null || members.isEmpty()) {
            return ReplyHelper.invalidParam("请选择需要添加的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.addMembers(loginInfo, id, members);
    }

    /**
     * 移除组织机构成员
     *
     * @param info    用户关键信息
     * @param id      组织机构ID
     * @param members 组织机构成员DTO
     * @return Reply
     */
    @DeleteMapping("/v1.0/organizes/{id}/members")
    public Reply removeOrganizeMembers(@RequestHeader("loginInfo") String info, @PathVariable String id, @RequestBody List<String> members) {
        if (members == null || members.isEmpty()) {
            return ReplyHelper.invalidParam("请选择需要移除的成员");
        }

        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);
        return service.removeMember(loginInfo, id, members);
    }

    /**
     * 获取日志列表
     *
     * @param info    用户关键信息
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs")
    public Reply getOrganizeLogs(@RequestHeader("loginInfo") String info, @RequestParam(required = false) String keyword,
                                 @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getOrganizeLogs(loginInfo.getTenantId(), keyword, page, size);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs/{id}")
    public Reply getOrganizeLog(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ReplyHelper.invalidParam();
        }

        return service.getOrganizeLog(id);
    }
}
