package com.insight.base.organize.manage;

import com.insight.util.Json;
import com.insight.util.ReplyHelper;
import com.insight.util.pojo.LoginInfo;
import com.insight.util.pojo.Reply;
import org.springframework.web.bind.annotation.*;

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
     * 获取日志列表
     *
     * @param info    用户关键信息
     * @param keyword 查询关键词
     * @param page    分页页码
     * @param size    每页记录数
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs")
    public Reply getRoleLogs(@RequestHeader("loginInfo") String info, @RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        LoginInfo loginInfo = Json.toBeanFromBase64(info, LoginInfo.class);

        return service.getRoleLogs(loginInfo.getTenantId(), keyword, page, size);
    }

    /**
     * 获取日志详情
     *
     * @param id 日志ID
     * @return Reply
     */
    @GetMapping("/v1.0/organizes/logs/{id}")
    public Reply getRoleLog(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ReplyHelper.invalidParam();
        }

        return service.getRoleLog(id);
    }
}
