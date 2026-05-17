package com.cloud.business.controller;

import com.cloud.business.config.BusinessDataProperties;
import com.cloud.common.base.BaseController;
import com.cloud.common.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/business")
@Tag(name = "业务元数据")
public class BusinessMetaController extends BaseController<Object, Object> {

    private final BusinessDataProperties businessDataProperties;

    public BusinessMetaController(BusinessDataProperties businessDataProperties) {
        this.businessDataProperties = businessDataProperties;
    }

    @GetMapping("/storage-path")
    @Operation(summary = "JSON 数据存储根目录")
    public Result<String> storagePath() {
        return ok(Path.of(businessDataProperties.getDataDir()).toAbsolutePath().normalize().toString());
    }
}
