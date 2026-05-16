package com.cloud.business.vo;

import com.cloud.business.feign.dto.ProductRemoteVO;
import com.cloud.business.feign.dto.UserRemoteVO;

import java.io.Serializable;

/**
 * Handler 编排后的远程快照（用户 + 商品）。
 */
public class OrderIntegrationResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserRemoteVO user;
    private ProductRemoteVO product;

    public OrderIntegrationResult() {
    }

    public UserRemoteVO getUser() {
        return user;
    }

    public void setUser(UserRemoteVO user) {
        this.user = user;
    }

    public ProductRemoteVO getProduct() {
        return product;
    }

    public void setProduct(ProductRemoteVO product) {
        this.product = product;
    }
}
