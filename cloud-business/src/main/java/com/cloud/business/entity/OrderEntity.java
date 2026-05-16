package com.cloud.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 映射表占位：{@code biz_order}。接入 MySQL 时可按需补 {@code TableName}/{@code TableId} 等注解。 */
public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 演示由 {@link com.cloud.business.persistence.DemoMemoryOrderPersistence} 分配；入库时多为自增主键 */
    private Long orderId;

    private String orderNo;

    private Long buyerUserId;

    private String productCode;

    private Integer quantity;

    private BigDecimal totalAmount;

    /** 状态：10=已创建 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public OrderEntity() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
