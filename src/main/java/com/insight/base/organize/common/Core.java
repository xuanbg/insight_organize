package com.insight.base.organize.common;

import com.insight.base.organize.common.dto.Organize;
import com.insight.base.organize.common.mapper.CoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 宣炳刚
 * @date 2019/11/30
 * @remark 组织机构核心类
 */
@Component
public class Core {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CoreMapper mapper;

    /**
     * 构造方法
     *
     * @param mapper TenantMapper
     */
    public Core(CoreMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 新增组织机构
     *
     * @param dto 组织机构DTO
     */
    public void addOrganize(Organize dto) {
        dto.setInvalid(false);
        dto.setCreatedTime(LocalDateTime.now());

        mapper.addOrganize(dto);
    }
}
