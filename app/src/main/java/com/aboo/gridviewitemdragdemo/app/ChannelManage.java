package com.aboo.gridviewitemdragdemo.app;

import android.database.SQLException;
import com.aboo.gridviewitemdragdemo.app.dao.ChannelDao;
import com.aboo.gridviewitemdragdemo.app.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelManage {
    public static ChannelManage channelManage;
    /**
     * 默认的用户选择频道列表
     */
    public static List<ChannelItem> defaultUserChannels = new ArrayList<ChannelItem>();
    /**
     * 默认的其他频道列表
     */
    public static List<ChannelItem> defaultOtherChannels = new ArrayList<ChannelItem>();
    private ChannelDao channelDao;
    /** 判断数据库中是否存在用户数据 */
    private boolean userExist = false;

    static {
        //TODO 添加用户默认选中的频道
        defaultUserChannels.add(new ChannelItem(1, "推荐", 1, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(2, "热点", 2, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(3, "Java", 3, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(4, "C", 4, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(5, "C++", 5, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(6, "C#", 6, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(7, "Python", 7, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(8, "Swift", 8, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(9, "Go", 9, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(10, "JavaScript", 10, ChannelItem.CHANNEL_TYPE_USER));

        //TODO 添加用户可选频道
        defaultOtherChannels.add(new ChannelItem(8, "PHP", 1, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(9, "Visual Basic", 2, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(10, "Ruby", 3, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(11, "Perl", 4, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(12, "SQL", 5, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(13, "Node.js", 6, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(14, "HTML5", 7, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(15, "CSS3", 8, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(16, "Groovy", 9, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(17, "Objective-C", 10, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(18, "GitHub", 11, ChannelItem.CHANNEL_TYPE_OTHER));
        defaultOtherChannels.add(new ChannelItem(19, "Lisp", 12, ChannelItem.CHANNEL_TYPE_OTHER));
    }

    private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
        if (channelDao == null)
            channelDao = new ChannelDao(paramDBHelper.getContext());
    }

    /**
     * 初始化频道管理类
     *
     * @param dbHelper 数据库帮助对象
     * @throws android.database.SQLException
     */
    public static ChannelManage getManage(SQLHelper dbHelper) throws SQLException {
        if (channelManage == null)
            channelManage = new ChannelManage(dbHelper);
        return channelManage;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        channelDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ChannelItem> getUserChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?",new String[] { "1" });
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<ChannelItem> list = new ArrayList<ChannelItem>();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        initDefaultChannel();
        return defaultUserChannels;
    }

    /**
     * 获取其他的频道
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ChannelItem> getOtherChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?" ,new String[] { "0" });
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()){
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate= new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        if(userExist){
            return list;
        }
        cacheList = defaultOtherChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     *
     * @param userList
     */
    public void saveUserChannel(List<ChannelItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ChannelItem channelItem = userList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(1);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存其他频道到数据库
     *
     * @param otherList
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(0);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel(){
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveOtherChannel(defaultOtherChannels);
    }
}
