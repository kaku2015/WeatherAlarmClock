/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kaku.weac.Listener.OnItemClickListener;
import com.kaku.weac.R;
import com.kaku.weac.activities.AlarmClockEditActivity;
import com.kaku.weac.activities.AlarmClockNewActivity;
import com.kaku.weac.adapter.AlarmClockAdapter;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.bean.Event.AlarmClockDeleteEvent;
import com.kaku.weac.bean.Event.AlarmClockUpdateEvent;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.AlarmClockOperate;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.OttoAppConfig;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInLeftAnimator;

/**
 * 闹钟fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class AlarmClockFragment extends BaseFragment implements OnClickListener {

    /**
     * 新建闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_NEW = 1;

    /**
     * 修改闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_EDIT = 2;

    /**
     * 闹钟列表
     */
    private RecyclerView mRecyclerView;

    /**
     * 保存闹钟信息的list
     */
    private List<AlarmClock> mAlarmClockList;

    /**
     * 保存闹钟信息的adapter
     */
    private AlarmClockAdapter mAdapter;

    /**
     * 操作栏编辑按钮
     */
    private ImageView mEditAction;

    /**
     * 操作栏编辑完成按钮
     */
    private ImageView mAcceptAction;

    /**
     * List内容为空时的视图
     */
    private LinearLayout mEmptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoAppConfig.getInstance().register(this);
        mAlarmClockList = new ArrayList<>();
        mAdapter = new AlarmClockAdapter(getActivity(), mAlarmClockList);
        // 注册Loader
//        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_alarm_clock, container, false);

        mEmptyView = (LinearLayout) view
                .findViewById(R.id.alarm_clock_empty);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new ScaleInLeftAnimator(new OvershootInterpolator(1f)));
        mRecyclerView.getItemAnimator().setAddDuration(300);
        mRecyclerView.getItemAnimator().setRemoveDuration(300);
        mRecyclerView.getItemAnimator().setMoveDuration(300);
        mRecyclerView.getItemAnimator().setChangeDuration(300);
        mRecyclerView.setAdapter(mAdapter);

        // 监听闹铃item点击事件Listener
       OnItemClickListener onItemClickListener =  new OnItemClickListenerImpl();
        mAdapter.setOnItemClickListener(onItemClickListener);

        // 操作栏新建按钮
        ImageView newAction = (ImageView) view.findViewById(R.id.action_new);
        newAction.setOnClickListener(this);

        // 编辑闹钟
        mEditAction = (ImageView) view.findViewById(R.id.action_edit);
        mEditAction.setOnClickListener(this);

        // 完成按钮
        mAcceptAction = (ImageView) view.findViewById(R.id.action_accept);
        mAcceptAction.setOnClickListener(this);

        updateList();
        return view;
    }

    class OnItemClickListenerImpl implements OnItemClickListener {

        @Override
        public void onItemClick(View view, int position) {
            // 不响应重复点击
            if (MyUtil.isFastDoubleClick()) {
                return;
            }
            AlarmClock alarmClock = mAlarmClockList.get(position);
            Intent intent = new Intent(getActivity(),
                    AlarmClockEditActivity.class);
            intent.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
            // 开启编辑闹钟界面
            startActivityForResult(intent, REQUEST_ALARM_CLOCK_EDIT);
            // 启动移动进入效果动画
            getActivity().overridePendingTransition(R.anim.move_in_bottom,
                    0);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            // 显示删除，完成按钮，隐藏修改按钮
            displayDeleteAccept();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_new:
                // 不响应重复点击
                if (MyUtil.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        AlarmClockNewActivity.class);
                // 开启新建闹钟界面
                startActivityForResult(intent, REQUEST_ALARM_CLOCK_NEW);
                // 启动渐变放大效果动画
                getActivity().overridePendingTransition(R.anim.zoomin, 0);
                break;
            case R.id.action_edit:
                // 当列表内容为空时禁止响应编辑事件
                if (mAlarmClockList.size() == 0) {
                    return;
                }
                // 显示删除，完成按钮，隐藏修改按钮
                displayDeleteAccept();
                break;
            case R.id.action_accept:
                // 隐藏删除，完成按钮,显示修改按钮
                hideDeleteAccept();
                break;
        }

    }

    /**
     * 显示删除，完成按钮，隐藏修改按钮
     */
    private void displayDeleteAccept() {
        mAdapter.setIsCanClick(false);
        mAdapter.displayDeleteButton(true);
        mAdapter.notifyDataSetChanged();
        mEditAction.setVisibility(View.GONE);
        mAcceptAction.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏删除，完成按钮,显示修改按钮
     */
    private void hideDeleteAccept() {
        mAdapter.setIsCanClick(true);
        mAdapter.displayDeleteButton(false);
        mAdapter.notifyDataSetChanged();
        mAcceptAction.setVisibility(View.GONE);
        mEditAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        AlarmClock ac = data
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        switch (requestCode) {
            // 新建闹钟
            case REQUEST_ALARM_CLOCK_NEW:
                // 插入新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).insert(ac);
                AlarmClockOperate.getInstance().saveAlarmClock(ac);
                addList(ac);
                break;
            // 修改闹钟
            case REQUEST_ALARM_CLOCK_EDIT:
                // 更新闹钟数据
//                TabAlarmClockOperate.getInstance(getActivity()).update(ac);
                AlarmClockOperate.getInstance().updateAlarmClock(ac);
                updateList();
                break;

        }
    }

    @Subscribe
    public void onAlarmClockUpdate(AlarmClockUpdateEvent event) {
        updateList();
    }

    @Subscribe
    public void OnAlarmClockDelete(AlarmClockDeleteEvent event) {
        deleteList(event);
    }

    private void addList(AlarmClock ac) {
        mAlarmClockList.clear();

        int id = ac.getId();
        int count = 0;
        int position = 0;
        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            alarmClock.setAlarmClockCode(alarmClock.getId());
            mAlarmClockList.add(alarmClock);

            if (id == alarmClock.getId()) {
                position = count;
                if (alarmClock.isOnOff()) {
                    MyUtil.startAlarmClock(getActivity(), alarmClock);
                }
            }
            count++;
        }

        checkIsEmpty(list);

        mAdapter.notifyItemInserted(position);
    }

    private void deleteList(AlarmClockDeleteEvent event) {
        mAlarmClockList.clear();

        int position = event.getPosition();
        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            alarmClock.setAlarmClockCode(alarmClock.getId());
            mAlarmClockList.add(alarmClock);
        }
        // 列表为空时不显示删除，完成按钮
        if (mAlarmClockList.size() == 0) {
            mAcceptAction.setVisibility(View.GONE);
            mEditAction.setVisibility(View.VISIBLE);
            mAdapter.displayDeleteButton(false);
        }

        checkIsEmpty(list);

        mAdapter.notifyItemRemoved(position);
//        mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
    }

    private void updateList() {
        mAlarmClockList.clear();

        List<AlarmClock> list = AlarmClockOperate.getInstance().loadAlarmClocks();
        for (AlarmClock alarmClock : list) {
            alarmClock.setAlarmClockCode(alarmClock.getId());
            mAlarmClockList.add(alarmClock);

            // 当闹钟为开时刷新开启闹钟
            if (alarmClock.isOnOff()) {
                MyUtil.startAlarmClock(getActivity(), alarmClock);
            }
        }

        checkIsEmpty(list);

        mAdapter.notifyDataSetChanged();
    }

    private void checkIsEmpty(List<AlarmClock> list) {
        if (list.size() != 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

/*    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), WeacDBMetaData.CONTENT_URI,
                null, null, null, WeacDBMetaData.SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            return;
        }
        mAlarmClockList.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(WeacDBMetaData.AC_ID));
            int hour = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_HOUR));
            int minute = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_MINUTE));
            String weeks = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_WEEKS));
            String repeat = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_REPEAT));
            String tag = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_TAG));
            String ringName = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_NAME));
            String ringUrl = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_URL));
            int ringPager = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_PAGER));
            int volume = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_VOLUME));
            boolean vibrate = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_VIBRATE)) == 1;
            boolean nap = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP)) == 1;
            int napInterval = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP_INTERVAL));
            int napTimes = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP_TIMES));
            boolean weaPrompt = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_WEA_PROMPT)) == 1;
            boolean onOff = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_ON_OFF)) == 1;
            AlarmClock alarmClock = new AlarmClock(id, hour, minute, repeat,
                    weeks, tag, ringName, ringUrl, ringPager, volume, vibrate,
                    nap, napInterval, napTimes, weaPrompt, onOff);
            mAlarmClockList.add(alarmClock);
            // 当闹钟为开时刷新开启闹钟
            if (onOff) {
                MyUtil.startAlarmClock(getActivity(), alarmClock);
            }

        }
        // 列表为空时不显示删除，完成按钮
        if (mAlarmClockList.size() == 0) {
            mAcceptAction.setVisibility(View.GONE);
            mEditAction.setVisibility(View.VISIBLE);
            mAdapter.displayDeleteButton(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }*/

    @Override
    public void onPause() {
        super.onPause();
        // 隐藏删除，完成按钮,显示修改按钮
        hideDeleteAccept();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoAppConfig.getInstance().unregister(this);
    }
}
