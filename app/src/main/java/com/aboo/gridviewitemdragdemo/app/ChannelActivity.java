package com.aboo.gridviewitemdragdemo.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.aboo.gridviewitemdragdemo.app.db.SQLHelper;

import java.util.ArrayList;

/**
 * 频道管理
 */
public class ChannelActivity extends Activity implements OnItemClickListener {

    // 点击类型
    private static final int CLICK_TYPE_USER = 0;
    private static final int CLICK_TYPE_STUDY = 1;
    private static final int CLICK_TYPE_WORK = 2;
    private static final int CLICK_TYPE_ENTERTAINMENT = 3;
    private static final int CLICK_TYPE_SPORT = 4;
    // 记录当前点击类型
    private int mCurrentClickType = CLICK_TYPE_USER;

    //用户栏目的GRIDVIEW
    private DragGrid userGridView;
    // 其它栏目的GRIDVIEW
    private OtherGridView studyGridView;
    // 工作栏目的GRIDVIEW
    private OtherGridView workGridView;
    // 娱乐栏目的GRIDVIEW
    private OtherGridView entertainmentGridView;
    // 运动栏目的GRIDVIEW
    private OtherGridView sportGridView;

    // 用户栏目对应的适配器，可以拖动
    private DragAdapter userAdapter;
    // 其他栏目对应的适配器，不可以拖动
    private OtherAdapter studyAdapter;
    private OtherAdapter workAdapter;
    private OtherAdapter entertainmentAdapter;
    private OtherAdapter sportAdapter;

    private ArrayList<ChannelItem> studyChannelList = new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> workChannelList = new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> entertainmentChannelList = new ArrayList<ChannelItem>();
    private ArrayList<ChannelItem> sportChannelList = new ArrayList<ChannelItem>();

    // 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
    private boolean isMove = false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        mContext = this;

        initView();
        initData();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        studyGridView = (OtherGridView) findViewById(R.id.otherGridView);
        workGridView = (OtherGridView) findViewById(R.id.workGridView);
        entertainmentGridView = (OtherGridView) findViewById(R.id.entertainmentGridView);
        sportGridView = (OtherGridView) findViewById(R.id.sportGridView);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initChannelData();

        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        studyAdapter = new OtherAdapter(this, studyChannelList);
        studyGridView.setAdapter(studyAdapter);
        workAdapter = new OtherAdapter(this, workChannelList);
        workGridView.setAdapter(workAdapter);
        entertainmentAdapter = new OtherAdapter(this, entertainmentChannelList);
        entertainmentGridView.setAdapter(entertainmentAdapter);
        sportAdapter = new OtherAdapter(this, sportChannelList);
        sportGridView.setAdapter(sportAdapter);

        //设置GridView的ITEM的点击监听
        studyGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
        workGridView.setOnItemClickListener(this);
        entertainmentGridView.setOnItemClickListener(this);
        sportGridView.setOnItemClickListener(this);
    }

    private void initChannelData() {
        SQLHelper sqlHelper = new SQLHelper(mContext);
        ChannelManage channelManage = ChannelManage.getManage(sqlHelper);
        userChannelList = (ArrayList<ChannelItem>) channelManage.getUserChannel();
        studyChannelList = (ArrayList<ChannelItem>) channelManage.getStudyChannel();
        workChannelList = (ArrayList<ChannelItem>) channelManage.getWorkChannel();
        entertainmentChannelList = (ArrayList<ChannelItem>) channelManage.getEntertainmentChannel();
        sportChannelList = (ArrayList<ChannelItem>) channelManage.getSportChannel();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }

        switch (parent.getId()) {
            case R.id.userGridView:
                mCurrentClickType = CLICK_TYPE_USER;
                deleteUserSelectedEvent(((DragAdapter) parent.getAdapter()).getItem(position), view, position);
                break;
            case R.id.otherGridView:
                mCurrentClickType = CLICK_TYPE_STUDY;
                addOtherEvent(((OtherAdapter) parent.getAdapter()).getItem(position), view, position);
                break;

            case R.id.workGridView:
                mCurrentClickType = CLICK_TYPE_WORK;
                addOtherEvent(((OtherAdapter) parent.getAdapter()).getItem(position), view, position);
                break;

            case R.id.entertainmentGridView:
                mCurrentClickType = CLICK_TYPE_ENTERTAINMENT;
                addOtherEvent(((OtherAdapter) parent.getAdapter()).getItem(position), view, position);
                break;

            case R.id.sportGridView:
                mCurrentClickType = CLICK_TYPE_SPORT;
                addOtherEvent(((OtherAdapter) parent.getAdapter()).getItem(position), view, position);
                break;

            default:
                break;
        }
    }

    /**
     * 添加其他事项，包括学习、工作、娱乐、运动类型
     *
     * @param item
     * @param view
     * @param position
     */
    private void addOtherEvent(ChannelItem item, View view, final int position) {

        final OtherGridView otherGridView;
        final OtherAdapter otherAdapter;
        switch (mCurrentClickType) {
            case CLICK_TYPE_STUDY:
                otherGridView = studyGridView;
                otherAdapter = studyAdapter;
                break;

            case CLICK_TYPE_WORK:
                otherGridView = workGridView;
                otherAdapter = workAdapter;
                break;

            case CLICK_TYPE_ENTERTAINMENT:
                otherGridView = entertainmentGridView;
                otherAdapter = entertainmentAdapter;
                break;

            case CLICK_TYPE_SPORT:
                otherGridView = sportGridView;
                otherAdapter = sportAdapter;
                break;

            default:
                otherGridView = null;
                otherAdapter = null;
                break;
        }

        final ImageView moveImageView3 = getView(view);
        if (moveImageView3 != null) {
            TextView newTextView = (TextView) view.findViewById(R.id.text_item);
            final int[] startLocation = new int[2];
            newTextView.getLocationInWindow(startLocation);

            final ChannelItem channel = item;
            userAdapter.setVisible(false);
            userAdapter.addItem(channel);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        int[] endLocation = new int[2];
                        //获取终点的坐标
                        userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                        MoveAnim(moveImageView3, startLocation, endLocation, channel, otherGridView);

                        assert otherAdapter != null;
                        otherAdapter.setRemove(position);
                    } catch (Exception localException) {
                        //
                    }
                }
            }, 50L);
        }
    }

    /**
     * 删除用户已选择的事项
     *
     * @param item
     * @param view
     * @param position
     */
    private void deleteUserSelectedEvent(ChannelItem item, View view, final int position) {
        // TODO position为 0，1 的不可以进行任何操作
        if (position != 0 && position != 1) {
            final ImageView moveImageView = getView(view);
            if (moveImageView != null) {
                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                final int[] startLocation = new int[2];
                newTextView.getLocationInWindow(startLocation);
                final ChannelItem channel = item;//获取点击的频道内容

                addOneItemInAdapter(channel);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            int[] endLocation = getEndLocation(channel);

                            MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                            userAdapter.setRemove(position);
                        } catch (Exception localException) {
                        }
                    }
                }, 50L);
            }
        }
    }

    /**
     * 获取动画移动的终点坐标
     *
     * @param channel 点击的Item
     * @return 终点坐标
     */
    private int[] getEndLocation(ChannelItem channel) {
        int[] endLocation = new int[2];

        switch (channel.getOriginalType()) {

            case ChannelItem.CHANNEL_TYPE_STUDY:
                studyGridView.getChildAt(studyGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                break;

            case ChannelItem.CHANNEL_TYPE_WORK:
                workGridView.getChildAt(workGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                break;

            case ChannelItem.CHANNEL_TYPE_ENTERTAINMENT:
                entertainmentGridView.getChildAt(entertainmentGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                break;

            case ChannelItem.CHANNEL_TYPE_SPORT:
                sportGridView.getChildAt(sportGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                break;

            default:
                break;
        }

        return endLocation;
    }

    /**
     * 往目标适配器最后添加一项，
     *
     * @param channel 点击的Item
     */
    private void addOneItemInAdapter(ChannelItem channel) {
        switch (channel.getOriginalType()) {
            case ChannelItem.CHANNEL_TYPE_STUDY:
                studyAdapter.setVisible(false);
                studyAdapter.addItem(channel);
                break;

            case ChannelItem.CHANNEL_TYPE_WORK:
                workAdapter.setVisible(false);
                workAdapter.addItem(channel);
                break;

            case ChannelItem.CHANNEL_TYPE_ENTERTAINMENT:
                entertainmentAdapter.setVisible(false);
                entertainmentAdapter.addItem(channel);
                break;

            case ChannelItem.CHANNEL_TYPE_SPORT:
                sportAdapter.setVisible(false);
                sportAdapter.addItem(channel);
                break;

            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);

                // TODO 判断当前点击的频道类型
                switch (mCurrentClickType) {
                    case CLICK_TYPE_USER:
                        changeOtherAdapter(moveChannel);

                        userAdapter.remove();
                        break;

                    case CLICK_TYPE_STUDY:
                        userAdapter.setVisible(true);
                        userAdapter.notifyDataSetChanged();
                        studyAdapter.remove();
                        break;

                    case CLICK_TYPE_WORK:
                        userAdapter.setVisible(true);
                        userAdapter.notifyDataSetChanged();
                        workAdapter.remove();
                        break;

                    case CLICK_TYPE_ENTERTAINMENT:
                        userAdapter.setVisible(true);
                        userAdapter.notifyDataSetChanged();
                        entertainmentAdapter.remove();
                        break;

                    case CLICK_TYPE_SPORT:
                        userAdapter.setVisible(true);
                        userAdapter.notifyDataSetChanged();
                        sportAdapter.remove();
                        break;

                    default:
                        break;
                }

                isMove = false;
            }
        });
    }

    /**
     * 点击删除用户事项动画结束后，修改相应的数据适配器
     *
     * @param moveChannel 移动还原的Item
     */
    private void changeOtherAdapter(ChannelItem moveChannel) {
        switch (moveChannel.getOriginalType()) {

            case ChannelItem.CHANNEL_TYPE_STUDY:
                studyAdapter.setVisible(true);
                studyAdapter.notifyDataSetChanged();
                break;

            case ChannelItem.CHANNEL_TYPE_WORK:
                workAdapter.setVisible(true);
                workAdapter.notifyDataSetChanged();
                break;

            case ChannelItem.CHANNEL_TYPE_ENTERTAINMENT:
                entertainmentAdapter.setVisible(true);
                entertainmentAdapter.notifyDataSetChanged();
                break;

            case ChannelItem.CHANNEL_TYPE_SPORT:
                sportAdapter.setVisible(true);
                sportAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveChannel() {
        SQLHelper sqlHelper = new SQLHelper(mContext);
        ChannelManage channelManage = ChannelManage.getManage(sqlHelper);
        channelManage.deleteAllChannel();
        channelManage.saveUserChannel(userAdapter.getChannelList());
        channelManage.saveStudyChannel(studyAdapter.getChannelList());
        channelManage.saveWorkChannel(workAdapter.getChannelList());
        channelManage.saveEntertainmentChannel(entertainmentAdapter.getChannelList());
        channelManage.saveSportChannel(sportAdapter.getChannelList());
    }

    @Override
    public void finish() {
        super.finish();

        saveChannel();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
