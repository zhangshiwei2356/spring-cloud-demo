package com.cloud.business.service.impl;

import com.cloud.business.dto.AdminOrderSaveDTO;
import com.cloud.business.dto.SubmitOrderDTO;
import com.cloud.business.dto.VehiclePurchaseDTO;
import com.cloud.business.entity.VehicleEntity;
import com.cloud.business.feign.SystemUserFeignClient;
import com.cloud.business.feign.dto.UserRemoteVO;
import com.cloud.business.mapper.VehicleMapper;
import com.cloud.business.service.AdminOrderService;
import com.cloud.business.service.OrderService;
import com.cloud.business.service.VehiclePurchaseService;
import com.cloud.business.service.VehicleService;
import com.cloud.business.vo.AdminOrderVO;
import com.cloud.business.vo.OrderVO;
import com.cloud.business.vo.VehiclePurchaseResultVO;
import com.cloud.business.vo.VehicleVO;
import com.cloud.common.context.UserContext;
import com.cloud.common.domain.Result;
import com.cloud.common.exception.GlobalException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VehiclePurchaseServiceImpl implements VehiclePurchaseService {

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final OrderService orderService;
    private final AdminOrderService adminOrderService;
    private final SystemUserFeignClient systemUserFeignClient;

    public VehiclePurchaseServiceImpl(VehicleService vehicleService,
                                      VehicleMapper vehicleMapper,
                                      OrderService orderService,
                                      AdminOrderService adminOrderService,
                                      SystemUserFeignClient systemUserFeignClient) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.orderService = orderService;
        this.adminOrderService = adminOrderService;
        this.systemUserFeignClient = systemUserFeignClient;
    }

    @Override
    public VehiclePurchaseResultVO purchase(Long vehicleId, VehiclePurchaseDTO dto) {
        Long uid = UserContext.getUid();
        if (uid == null) {
            throw new GlobalException("请先登录后再购买");
        }

        VehicleVO vehicle = vehicleService.getById(vehicleId);
        if (!"1".equals(vehicle.getStatus())) {
            throw new GlobalException("该车辆已下架");
        }
        int qty = dto.getQuantity() == null ? 1 : dto.getQuantity();
        if (vehicle.getStock() == null || vehicle.getStock() < qty) {
            throw new GlobalException("库存不足");
        }

        SubmitOrderDTO submit = new SubmitOrderDTO();
        submit.setBuyerUserId(uid);
        submit.setProductCode(vehicle.getCode());
        submit.setQuantity(qty);
        OrderVO realOrder = orderService.submitOrder(submit);

        String buyerName = resolveBuyerName(uid);

        String productName = vehicle.getName();
        if (StringUtils.hasText(dto.getRemark())) {
            productName = productName + "（" + dto.getRemark().trim() + "）";
        }

        AdminOrderSaveDTO adminDto = new AdminOrderSaveDTO();
        adminDto.setOrderNo(realOrder.getOrderNo());
        adminDto.setBuyerName(buyerName);
        adminDto.setProductCode(vehicle.getCode());
        adminDto.setProductName(productName);
        adminDto.setQuantity(qty);
        adminDto.setAmount(realOrder.getTotalAmount().toPlainString());
        adminDto.setStatus("PAID");
        adminDto.setCreateTime(LocalDateTime.now().format(DT));
        AdminOrderVO adminOrder = adminOrderService.create(adminDto);

        VehicleEntity entity = vehicleMapper.selectById(vehicleId);
        if (entity != null && entity.getStock() != null) {
            entity.setStock(Math.max(0, entity.getStock() - qty));
            vehicleMapper.updateById(vehicleId, entity);
        }

        VehiclePurchaseResultVO result = new VehiclePurchaseResultVO();
        result.setAdminOrderId(adminOrder.getId());
        result.setOrderNo(adminOrder.getOrderNo());
        result.setRealOrder(realOrder);
        result.setAdminOrder(adminOrder);
        result.setVehicle(vehicleService.getById(vehicleId));
        return result;
    }

    private String resolveBuyerName(Long uid) {
        try {
            Result<UserRemoteVO> userResp = systemUserFeignClient.getUser(uid);
            if (userResp != null && userResp.getData() != null
                    && StringUtils.hasText(userResp.getData().getUserName())) {
                return userResp.getData().getUserName();
            }
        } catch (Exception ignored) {
            // 演示环境降级为 UID 展示
        }
        return "用户" + uid;
    }
}
