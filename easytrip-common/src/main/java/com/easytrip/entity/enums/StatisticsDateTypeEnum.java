package com.easytrip.entity.enums;

public enum StatisticsDateTypeEnum {
    REGISTER_USER(1, "注册用户"),LOGIN_USER(2,"登录用户"),PHONE_CODE_SEND(3,"短信发送");
    private Integer type;
    private String description;

    StatisticsDateTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static StatisticsDateTypeEnum getByType(Integer type) {
        for (StatisticsDateTypeEnum item : StatisticsDateTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
