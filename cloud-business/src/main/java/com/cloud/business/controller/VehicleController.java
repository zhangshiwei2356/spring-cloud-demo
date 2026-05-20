package com.cloud.business.controller;

import com.cloud.business.dto.VehiclePurchaseDTO;
import com.cloud.business.service.VehiclePurchaseService;
import com.cloud.business.service.VehicleService;
import com.cloud.business.vo.VehiclePurchaseResultVO;
import com.cloud.business.vo.VehicleVO;
import com.cloud.common.base.BaseController;
import com.cloud.common.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "车辆购买")
public class VehicleController extends BaseController<Object, VehicleVO> {

    private final VehicleService vehicleService;
    private final VehiclePurchaseService vehiclePurchaseService;

    public VehicleController(VehicleService vehicleService,
                             VehiclePurchaseService vehiclePurchaseService) {
        this.vehicleService = vehicleService;
        this.vehiclePurchaseService = vehiclePurchaseService;
    }

    @GetMapping
    @Operation(summary = "在售车辆列表（支持关键字查询）")
    public Result<List<VehicleVO>> list(@RequestParam(value = "keyword", required = false) String keyword) {
        return ok(vehicleService.listOnSale(keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "车辆详情")
    public Result<VehicleVO> get(@PathVariable("id") Long id) {
        return ok(vehicleService.getById(id));
    }

    @PostMapping("/{id}/purchase")
    @Operation(summary = "购买车辆并创建订单")
    public Result<VehiclePurchaseResultVO> purchase(@PathVariable("id") Long id,
                                                    @Valid @RequestBody VehiclePurchaseDTO dto) {
        return ok(vehiclePurchaseService.purchase(id, dto));
    }
}
