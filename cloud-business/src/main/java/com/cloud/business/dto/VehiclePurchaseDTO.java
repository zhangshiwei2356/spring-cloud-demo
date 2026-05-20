package com.cloud.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema(description = "车辆购买下单入参")
public class VehiclePurchaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    @Schema(description = "购买数量", example = "1")
    private Integer quantity = 1;

    @Schema(description = "备注（可选）")
    private String remark;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
