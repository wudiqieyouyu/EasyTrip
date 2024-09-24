package com.easytrip.entity.enums;

public enum MessageTypeEnum {

    SYSTEM(0,"sys", "系统消息"), COMMENT(1,"reply", "评论"), ARTICLE_LIKE(2, "likePost","文章点赞"), COMMENT_LIKE(3,"likeComment", "评论点赞"), DOWNLOAD(4,"downloadAttachment","附件下载"), FEEDBACK(5, "feedback","问题反馈");
    private Integer type;
    private String code;
    private String desc;

    MessageTypeEnum(Integer type, String code,String desc) {
        this.type = type;
        this.code = code;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static MessageTypeEnum getMessageypeByType(Integer type) {
        if (null == type) {
            return null;
        }
        for (MessageTypeEnum item : MessageTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }


    public static MessageTypeEnum getMessageypeByCode(String code) {
        if (null == code) {
            return null;
        }
        for (MessageTypeEnum item : MessageTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
