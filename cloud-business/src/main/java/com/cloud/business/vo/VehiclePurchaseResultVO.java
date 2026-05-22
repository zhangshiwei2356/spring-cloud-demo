package com.cloud.business.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "车辆购买下单结果")
public class VehiclePurchaseResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "管理中心订单 ID，用于订单详情页")
    private Long adminOrderId;
    private String orderNo;
    private OrderVO realOrder;
    private AdminOrderVO adminOrder;
    private VehicleVO vehicle;

    public Long getAdminOrderId() {
        return adminOrderId;
    }

    public void setAdminOrderId(Long adminOrderId) {
        this.adminOrderId = adminOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public OrderVO getRealOrder() {
        return realOrder;
    }

    public void setRealOrder(OrderVO realOrder) {
        this.realOrder = realOrder;
    }

    public AdminOrderVO getAdminOrder() {
        return adminOrder;
    }

    public void setAdminOrder(AdminOrderVO adminOrder) {
        this.adminOrder = adminOrder;
    }

    public VehicleVO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleVO vehicle) {
        this.vehicle = vehicle;
    }
}
