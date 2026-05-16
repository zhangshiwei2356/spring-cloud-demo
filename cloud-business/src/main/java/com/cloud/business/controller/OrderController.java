package com.cloud.business.controller;

import com.cloud.business.dto.SubmitOrderDTO;
import com.cloud.business.service.OrderService;
import com.cloud.business.vo.OrderVO;
import com.cloud.common.base.BaseController;
import com.cloud.common.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单")
public class OrderController extends BaseController<SubmitOrderDTO, OrderVO> {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    @Operation(summary = "提交订单")
    public Result<OrderVO> submit(@Valid @RequestBody SubmitOrderDTO dto) {
        return ok(orderService.submitOrder(dto));
    }
}
