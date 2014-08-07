package com.aboo.gridviewitemdragdemo.app;

import java.io.Serializable;

/**
 * ITEM的对应可序化队列属性
 */
public class ChannelItem implements Serializable {
    // 即用户选中的类型
    public static final int CHANNEL_TYPE_USER = 1;
    // 即用户没有选中的类型
    public static final int CHANNEL_TYPE_OTHER = 0;

    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应ID
     */
    public Integer id;
    /**
     * 栏目对应NAME
     */
    public String name;
    /**
     * 栏目在整体中的排序顺序  rank
     */
    public Integer orderId;
    /**
     * 栏目是否选中
     */
    public Integer selected;

    public ChannelItem() {
    }

    public ChannelItem(int id, String name, int orderId, int selected) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
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

    public Integer getSelected() {
        return this.selected;
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

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", selected=" + this.selected + "]";
    }
}