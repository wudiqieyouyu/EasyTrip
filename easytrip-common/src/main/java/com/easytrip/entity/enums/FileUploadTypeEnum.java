package com.easytrip.entity.enums;

public enum FileUploadTypeEnum {
    AVATAR(0,  "头像"), IMAGES(1, "照片");

    private Integer type;
    private String description;

    FileUploadTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }



    public static FileUploadTypeEnum getType(Integer type) {
        for (FileUploadTypeEnum at : FileUploadTypeEnum.values()) {
            if (at.type.equals(type)) {
                return at;
            }
        }
        return null;
    }
}
