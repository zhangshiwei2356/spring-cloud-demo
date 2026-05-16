package com.cloud.auth.demo;

/**
 * 演示用户档案（内存数据，与 cloud-system 演示用户一致）。
 */
public final class DemoUserProfile {

    private final Long userId;
    private final String userName;
    private final String role;
    private final String avatarUrl;

    public DemoUserProfile(Long userId, String userName, String role, String avatarUrl) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public static DemoUserProfile of(Long userId) {
        return switch (userId.intValue()) {
            case 1 -> new DemoUserProfile(1L, "演示用户一号", "超级管理员", "/img/avatar-1.svg");
            case 2 -> new DemoUserProfile(2L, "演示用户二号", "运营专员", "/img/avatar-2.svg");
            case 10086 -> new DemoUserProfile(10086L, "演示用户10086", "普通用户", "/img/avatar-3.svg");
            default -> null;
        };
    }
}
