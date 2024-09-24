package com.easytrip.entity.enums;


public enum CodeStatusEnum {

    DISABLE(0, "未使用"),
    ENABLE(1, "已使用");


    private Integer status;
    private String desc;

    CodeStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static CodeStatusEnum getByStatus(Integer status) {
        for (CodeStatusEnum item : CodeStatusEnum.values()) {
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
