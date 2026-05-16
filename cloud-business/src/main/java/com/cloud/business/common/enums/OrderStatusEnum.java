package com.cloud.business.common.enums;

/** 订单状态。 */
public enum OrderStatusEnum {

    CREATED(10, "已创建");

    private final int code;
    private final String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
