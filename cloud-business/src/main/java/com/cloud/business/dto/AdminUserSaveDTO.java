package com.cloud.business.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AdminUserSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "登录名不能为空")
    private String loginName;

    @NotBlank(message = "姓名不能为空")
    private String userName;

    private String deptName;
    private String role;
    private String phone;
    private String status;

    public AdminUserSaveDTO() {
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
