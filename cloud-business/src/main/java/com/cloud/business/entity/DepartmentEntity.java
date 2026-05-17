package com.cloud.business.entity;

import com.cloud.business.common.Identifiable;

import java.io.Serializable;

/** 映射表占位：{@code biz_department} */
public class DepartmentEntity implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String companyName;
    private String name;
    private String leader;
    private String phone;
    private String status;

    public DepartmentEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
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
}
