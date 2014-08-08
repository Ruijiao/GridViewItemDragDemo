package com.aboo.gridviewitemdragdemo.app;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class ChannelItem implements Serializable {
    // 即用户选中的类型
    public static final int CHANNEL_TYPE_USER = 1;
    // 即用户没有选中的类型
    public static final int CHANNEL_TYPE_STUDY = 0;
    // 工作类型
    public static final int CHANNEL_TYPE_WORK = 2;
    // 娱乐类型
    public static final int CHANNEL_TYPE_ENTERTAINMENT = 3;
    // 运动类型
    public static final int CHANNEL_TYPE_SPORT = 4;

    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应ID
     */
    private Integer id;
    /**
     * 栏目对应NAME
     */
    private String name;
    /**
     * 栏目在整体中的排序顺序  rank
     */
    private Integer orderId;
    /**
     * 栏目是否选中
     */
    private Integer type;

    private Integer originalType;

    public ChannelItem() {
    }

    public ChannelItem(int id, String name, int orderId, int type) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.type = type;
        this.originalType = type;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOrderId() {
        return this.orderId;
    }

    public Integer getType() {
        return this.type;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setOrderId(int paramInt) {
        this.orderId = paramInt;
    }

    public void setType(Integer paramInteger) {
        this.type = paramInteger;
    }

    public Integer getOriginalType() {
        return originalType;
    }

    public void setOriginalType(Integer originalType) {
        this.originalType = originalType;
    }

    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", type=" + this.type + ", originalType=" + this.originalType + "]";
    }
}