package com.cloud.auth.service.impl;

import com.cloud.auth.demo.DemoUserProfile;
import com.cloud.auth.dto.LoginRequest;
import com.cloud.auth.service.AuthService;
import com.cloud.auth.vo.LoginVO;
import com.cloud.common.base.BaseService;
import com.cloud.common.domain.PageResult;
import com.cloud.common.exception.GlobalException;
import com.cloud.common.properties.JwtProperties;
import com.cloud.common.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证实现：演示环境固定密码，签发 JWT（含 uid claim）。
 */
@Service
public class AuthServiceImpl extends BaseService<LoginRequest, LoginVO> implements AuthService {

    private static final String DEMO_PASSWORD = "123456";

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public LoginVO login(LoginRequest request) {
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new GlobalException("用户ID非法");
        }
        if (!DEMO_PASSWORD.equals(request.getPassword())) {
            throw new GlobalException("用户名或密码错误");
        }
        DemoUserProfile profile = DemoUserProfile.of(request.getUserId());
        if (profile == null) {
            throw new GlobalException("演示环境仅支持用户 1、2、10086");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", request.getUserId());
        claims.put("role", profile.getRole());
        String token = jwtUtil.createAccessToken(String.valueOf(request.getUserId()), claims);
        LoginVO vo = new LoginVO(token, jwtProperties.getAccessTokenTtlSeconds());
        vo.setUserId(profile.getUserId());
        vo.setUserName(profile.getUserName());
        vo.setRole(profile.getRole());
        vo.setAvatarUrl(profile.getAvatarUrl());
        return vo;
    }

    @Override
    public List<LoginVO> list(LoginRequest dto) {
        return Collections.emptyList();
    }

    @Override
    public PageResult<LoginVO> page(LoginRequest dto) {
        return new PageResult<>(1, 10, 0, Collections.emptyList());
    }

    @Override
    public Boolean save(LoginRequest dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean update(LoginRequest dto) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean delete(Long id) {
        return Boolean.FALSE;
    }
}
