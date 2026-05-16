package com.cloud.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "登录结果")
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "访问令牌")
    private String accessToken;
    @Schema(description = "过期秒数")
    private long expireInSeconds;

    public LoginVO() {
    }

    public LoginVO(String accessToken, long expireInSeconds) {
        this.accessToken = accessToken;
        this.expireInSeconds = expireInSeconds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpireInSeconds() {
        return expireInSeconds;
    }

    public void setExpireInSeconds(long expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }
}
