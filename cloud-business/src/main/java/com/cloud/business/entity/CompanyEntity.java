package com.cloud.business.entity;

import com.cloud.business.common.Identifiable;

import java.io.Serializable;

/** 映射表占位：{@code biz_company} */
public class CompanyEntity implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private String contact;
    private String phone;
    private String status;
    private String createTime;

    public CompanyEntity() {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
