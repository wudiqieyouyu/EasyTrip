package com.easytrip.entity.enums;

public enum BoardTypeEnum {

    ARTICLE(0, "文章分类"),
    QUESTION(1, "问答分类"),
    ARTICLE_QUESTION(2, "文章分类和问答分类"),
    LAYOUT(3, "导航栏分类");

    private Integer type;
    private String desc;

    BoardTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static BoardTypeEnum getByType(Integer type) {
        for (BoardTypeEnum item : BoardTypeEnum.values()) {
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
}
