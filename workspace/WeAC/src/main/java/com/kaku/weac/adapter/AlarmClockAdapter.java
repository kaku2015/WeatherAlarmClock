/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.adapter;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kaku.weac.R;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.db.TabAlarmClockOperate;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.MyUtil;

import java.util.List;

/**
 * 保存闹钟信息的adapter
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class AlarmClockAdapter extends ArrayAdapter<AlarmClock> {

    private final Context mContext;

    /**
     * 是否显示删除按钮
     */
    private boolean mIsDisplayDeleteBtn = false;

    /**
     * 白色
     */
    private final int mWhite = getContext().getResources().getColor(
            android.R.color.white);

    /**
     * 淡灰色
     */
    private final int mGray = getContext().getResources().getColor(R.color.gray_tab);

    /**
     * 保存闹钟信息的adapter
     *
     * @param context Activity上下文
     * @param objects 闹钟信息List
     */
    public AlarmClockAdapter(Context context, List<AlarmClock> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AlarmClock alarmClock = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lv_alarm_clock, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.repeat = (TextView) convertView
                    .findViewById(R.id.tv_repeat);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tv_tag);
            viewHolder.toggleBtn = (ToggleButton) convertView
                    .findViewById(R.id.toggle_btn);
            viewHolder.deleteBtn = (ImageView) convertView
                    .findViewById(R.id.alarm_list_delete_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 当闹钟为开启状态时
        if (alarmClock.isOnOff()) {
            // 设置字体颜色为白色
            viewHolder.time.setTextColor(mWhite);
            viewHolder.repeat.setTextColor(mWhite);
            viewHolder.tag.setTextColor(mWhite);
        } else {
            // 设置字体颜色为淡灰色
            viewHolder.time.setTextColor(mGray);
            viewHolder.repeat.setTextColor(mGray);
            viewHolder.tag.setTextColor(mGray);

        }

        // 显示删除按钮
        if (mIsDisplayDeleteBtn) {
            viewHolder.deleteBtn.setVisibility(View.VISIBLE);
            viewHolder.deleteBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 删除闹钟数据
                    TabAlarmClockOperate.getInstance(mContext).delete(alarmClock
                            .getAlarmClockCode());

                    // 关闭闹钟
                    MyUtil.cancelAlarmClock(mContext,
                            alarmClock.getAlarmClockCode());
                    // 关闭小睡
                    MyUtil.cancelAlarmClock(mContext,
                            -alarmClock.getAlarmClockCode());

                    NotificationManager notificationManager = (NotificationManager) mContext
                            .getSystemService(Activity.NOTIFICATION_SERVICE);
                    // 取消下拉列表通知消息
                    notificationManager.cancel(alarmClock.getAlarmClockCode());

                }
            });
        } else {
            viewHolder.deleteBtn.setVisibility(View.GONE);
        }

        // 取得格式化后的时间
        String time = MyUtil.formatTime(alarmClock.getHour(),
                alarmClock.getMinute());
        // 设定闹钟时间的显示
        viewHolder.time.setText(time);
        // 设定重复的显示
        viewHolder.repeat.setText(alarmClock.getRepeat());
        // 设定标签的显示
        viewHolder.tag.setText(alarmClock.getTag());
        viewHolder.toggleBtn
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // ToggleButton默认为false
                        if (isChecked) {
                            // 闹钟状态为开的话不更新数据
                            if (!alarmClock.isOnOff()) {
                                updateTab(1);
                            }
                        } else {
                            // 闹钟状态为关的话不处理
                            if (!alarmClock.isOnOff()) {
                                return;
                            }
                            updateTab(0);
                            // 取消闹钟
                            MyUtil.cancelAlarmClock(mContext,
                                    alarmClock.getAlarmClockCode());
                            // 取消小睡
                            MyUtil.cancelAlarmClock(mContext,
                                    -alarmClock.getAlarmClockCode());

                            NotificationManager notificationManager = (NotificationManager) getContext()
                                    .getSystemService(
                                            Activity.NOTIFICATION_SERVICE);
                            // 取消下拉列表通知消息
                            notificationManager.cancel(alarmClock
                                    .getAlarmClockCode());

                            // 停止播放
                            AudioPlayer.getInstance(mContext).stop();
                        }

                    }

                    /**
                     * 更新闹钟列表
                     *
                     * @param onOff
                     *            开关
                     */
                    private void updateTab(int onOff) {
                        // 更新闹钟数据
                        TabAlarmClockOperate.getInstance(getContext()).update(onOff,
                                alarmClock.getAlarmClockCode());
                    }
                });
        // 设定闹钟开关
        viewHolder.toggleBtn.setChecked(alarmClock.isOnOff());

        return convertView;
    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 时间
        TextView time;
        // 重复
        TextView repeat;
        // 标签
        TextView tag;
        // 开关
        ToggleButton toggleBtn;
        // 删除
        ImageView deleteBtn;
    }

    /**
     * 更新删除闹钟按钮状态
     *
     * @param isDisplayDeleteBtn 是否显示删除按钮
     */
    public void displayDeleteButton(boolean isDisplayDeleteBtn) {
        mIsDisplayDeleteBtn = isDisplayDeleteBtn;
    }
}
