package com.cloud.business.entity;

import com.cloud.business.common.Identifiable;

import java.io.Serializable;

/** 映射表占位：{@code biz_admin_user}（管理中心用户，与 cloud-system 用户独立） */
public class AdminUserEntity implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String loginName;
    private String userName;
    private String deptName;
    private String role;
    private String phone;
    private String status;

    public AdminUserEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
