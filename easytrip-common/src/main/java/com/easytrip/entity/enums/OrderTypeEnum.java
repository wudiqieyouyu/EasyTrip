package com.easytrip.entity.enums;


public enum OrderTypeEnum {

    Hot (0,"top_type desc,comment_count desc,good_count desc,read_count desc","热榜"),
    SEND(1,"post_time asc","发布"),
    NEW(2,"post_time desc","最新");

    private Integer type;
    private String orderSql;
    private String desc;

    OrderTypeEnum(Integer type,String orderSql,String desc) {
        this.type = type;
        this.desc = desc;
        this.orderSql=orderSql;
    }

    public static OrderTypeEnum getByType(Integer type) {
        for (OrderTypeEnum item : OrderTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public void setOrderSql(String orderSql) {
        this.orderSql = orderSql;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
