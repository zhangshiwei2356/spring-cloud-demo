package com.cloud.business.feign.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductRemoteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productCode;
    private BigDecimal unitPrice;

    public ProductRemoteVO() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
