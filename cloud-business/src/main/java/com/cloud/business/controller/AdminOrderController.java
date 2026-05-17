package com.cloud.business.controller;

import com.cloud.business.dto.AdminOrderSaveDTO;
import com.cloud.business.service.AdminOrderService;
import com.cloud.business.vo.AdminOrderVO;
import com.cloud.common.base.BaseController;
import com.cloud.common.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/orders")
@Tag(name = "管理中心订单", description = "与真实下单 API /api/orders 独立")
public class AdminOrderController extends BaseController<AdminOrderSaveDTO, AdminOrderVO> {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    @Operation(summary = "订单列表")
    public Result<List<AdminOrderVO>> list() {
        return ok(adminOrderService.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "订单详情")
    public Result<AdminOrderVO> get(@PathVariable("id") Long id) {
        return ok(adminOrderService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增订单")
    public Result<AdminOrderVO> create(@Valid @RequestBody AdminOrderSaveDTO dto) {
        return ok(adminOrderService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改订单")
    public Result<AdminOrderVO> update(@PathVariable("id") Long id, @Valid @RequestBody AdminOrderSaveDTO dto) {
        return ok(adminOrderService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单")
    public Result<Void> delete(@PathVariable("id") Long id) {
        adminOrderService.remove(id);
        return ok();
    }

    @PostMapping("/reset")
    @Operation(summary = "重置为种子数据")
    public Result<List<AdminOrderVO>> reset() {
        return ok(adminOrderService.reset());
    }
}
