package com.cloud.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema(description = "提交订单入参")
public class SubmitOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "购买人用户ID，须与网关解析的登录UID一致")
    private Long buyerUserId;

    @NotBlank
    @Schema(description = "商品编码", example = "SKU-DEMO")
    private String productCode;

    @NotNull
    @Min(value = 1, message = "数量至少为1")
    @Schema(description = "数量")
    private Integer quantity;

    public SubmitOrderDTO() {
    }

    public Long getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(Long buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
