package com.easytrip.entity.enums;


public enum PhoneCodeStatusEnum {

    DISABLE(0, "未使用"),
    ENABLE(1, "已使用");


    private Integer status;
    private String desc;

    PhoneCodeStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static PhoneCodeStatusEnum getByStatus(Integer status) {
        for (PhoneCodeStatusEnum item : PhoneCodeStatusEnum.values()) {
            if (item.getStatus().equals(status)) {
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
