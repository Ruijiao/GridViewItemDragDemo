package com.aboo.gridviewitemdragdemo.app;

import android.database.SQLException;
import com.aboo.gridviewitemdragdemo.app.dao.ChannelDao;
import com.aboo.gridviewitemdragdemo.app.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelManage {
    public static ChannelManage channelManage;

    public static List<ChannelItem> defaultUserChannels = new ArrayList<ChannelItem>();
    public static List<ChannelItem> defaultStudyChannels = new ArrayList<ChannelItem>();
    public static List<ChannelItem> defaultWorkChannels = new ArrayList<ChannelItem>();
    public static List<ChannelItem> defaultEntertainmentChannels = new ArrayList<ChannelItem>();
    public static List<ChannelItem> defaultSportChannels = new ArrayList<ChannelItem>();

    private ChannelDao channelDao;
    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    static int index = 3;

    static {
        initDefaultUserChannels();
        initDefaultStudyChannels();
        initDefaultWorkChannels();
        initDefaultEntertainmentChannels();
        initDefaultSportChannels();
    }

    private static void initDefaultUserChannels() {
        //TODO 添加用户默认选中的频道
        defaultUserChannels.add(new ChannelItem(1, "做题", 1, ChannelItem.CHANNEL_TYPE_USER));
        defaultUserChannels.add(new ChannelItem(2, "背书", 2, ChannelItem.CHANNEL_TYPE_USER));
    }

    private static void initDefaultStudyChannels() {
        //TODO 添加用户可选频道 - 学习
        String[] items = {"听力", "口语", "背单词", "上自习", "逃课", "编程", "手绘", "学软件"
                , "模型", "制图", "测绘", "分析案例", "分析数据", "设计", "比赛", "写作", "写报告"};
        for (int i = 0; i < items.length; i++) {
            defaultStudyChannels.add(new ChannelItem(index, items[i], i + 1, ChannelItem.CHANNEL_TYPE_STUDY));
            index++;
        }
    }

    private static void initDefaultWorkChannels() {
        //TODO 添加用户可选频道 - 工作
        String[] items = {"班级", "学生会", "社团", "实习", "兼职", "志愿者", "勤工俭学"};
        for (int i = 0; i < items.length; i++) {
            defaultWorkChannels.add(new ChannelItem(index, items[i], i + 1, ChannelItem.CHANNEL_TYPE_WORK));
            index++;
        }
    }

    private static void initDefaultEntertainmentChannels() {
        //TODO 添加用户可选频道 - 娱乐
        String[] items = {"游戏", "电影", "电视剧", "综艺", "聚会", "吃货", "旅游", "唱K", "听音乐", "淘宝", "shopping", "约会"};
        for (int i = 0; i < items.length; i++) {
            defaultEntertainmentChannels.add(new ChannelItem(index, items[i], i + 1, ChannelItem.CHANNEL_TYPE_ENTERTAINMENT));
            index++;
        }
    }

    private static void initDefaultSportChannels() {
        //TODO 添加用户可选频道 - 运动
        String[] items = {"篮球", "足球", "羽毛球", "乒乓球", "跑步", "游泳", "骑行", "瑜伽"};
        for (int i = 0; i < items.length; i++) {
            defaultSportChannels.add(new ChannelItem(index, items[i], i + 1, ChannelItem.CHANNEL_TYPE_SPORT));
            index++;
        }
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
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ChannelItem> getUserChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.TYPE + "= ?", new String[]{"" + ChannelItem.CHANNEL_TYPE_USER});
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
                navigate.setType(Integer.valueOf(maplist.get(i).get(SQLHelper.TYPE)));
                navigate.setOriginalType(Integer.valueOf(maplist.get(i).get(SQLHelper.ORIGINAL_TYPE)));
                list.add(navigate);
            }
            return list;
        }
        initDefaultChannel();
        return defaultUserChannels;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ChannelItem> getStudyChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.TYPE + "= ?", new String[]{"" + ChannelItem.CHANNEL_TYPE_STUDY});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setType(Integer.valueOf(maplist.get(i).get(SQLHelper.TYPE)));
                navigate.setOriginalType(Integer.valueOf(maplist.get(i).get(SQLHelper.ORIGINAL_TYPE)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultStudyChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 获取工作的频道
     *
     * @return
     */
    public List<ChannelItem> getWorkChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.TYPE + "= ?", new String[]{"" + ChannelItem.CHANNEL_TYPE_WORK});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setType(Integer.valueOf(maplist.get(i).get(SQLHelper.TYPE)));
                navigate.setOriginalType(Integer.valueOf(maplist.get(i).get(SQLHelper.ORIGINAL_TYPE)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultWorkChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 获取娱乐的频道
     *
     * @return
     */
    public List<ChannelItem> getEntertainmentChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.TYPE + "= ?", new String[]{"" + ChannelItem.CHANNEL_TYPE_ENTERTAINMENT});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setType(Integer.valueOf(maplist.get(i).get(SQLHelper.TYPE)));
                navigate.setOriginalType(Integer.valueOf(maplist.get(i).get(SQLHelper.ORIGINAL_TYPE)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultEntertainmentChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 获取运动的频道
     *
     * @return
     */
    public List<ChannelItem> getSportChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.TYPE + "= ?", new String[]{"" + ChannelItem.CHANNEL_TYPE_SPORT});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDER_ID)));
                navigate.setType(Integer.valueOf(maplist.get(i).get(SQLHelper.TYPE)));
                navigate.setOriginalType(Integer.valueOf(maplist.get(i).get(SQLHelper.ORIGINAL_TYPE)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultSportChannels;
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
            channelItem.setOriginalType(channelItem.getOriginalType());
            channelItem.setType(ChannelItem.CHANNEL_TYPE_USER);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存学习频道到数据库
     *
     * @param otherList
     */
    public void saveStudyChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setType(ChannelItem.CHANNEL_TYPE_STUDY);
            channelItem.setOriginalType(channelItem.getType());
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存工作频道到数据库
     *
     * @param otherList
     */
    public void saveWorkChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setType(ChannelItem.CHANNEL_TYPE_WORK);
            channelItem.setOriginalType(channelItem.getType());
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存娱乐频道到数据库
     *
     * @param otherList
     */
    public void saveEntertainmentChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setType(ChannelItem.CHANNEL_TYPE_ENTERTAINMENT);
            channelItem.setOriginalType(channelItem.getType());
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存运动频道到数据库
     *
     * @param otherList
     */
    public void saveSportChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setType(ChannelItem.CHANNEL_TYPE_SPORT);
            channelItem.setOriginalType(channelItem.getType());
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel() {
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveStudyChannel(defaultStudyChannels);
        saveWorkChannel(defaultWorkChannels);
        saveEntertainmentChannel(defaultEntertainmentChannels);
        saveSportChannel(defaultSportChannels);
    }
}
