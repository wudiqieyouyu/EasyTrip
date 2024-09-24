package com.easytrip.entity.enums;


public enum RecordOpTypeEnum {

    ARTICLE_LIKE(0, "文章点赞"),
    COMMENT_LIKE(1, "评论点赞");


    private Integer type;
    private String desc;

    RecordOpTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static RecordOpTypeEnum getByStatus(Integer type) {
        for (RecordOpTypeEnum item : RecordOpTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
