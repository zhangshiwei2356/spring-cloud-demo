package com.cloud.business.entity;

import com.cloud.business.common.Identifiable;

import java.io.Serializable;

/** 映射表占位：{@code biz_product} */
public class ProductEntity implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String category;
    private String price;
    private Integer stock;
    private String status;

    public ProductEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
