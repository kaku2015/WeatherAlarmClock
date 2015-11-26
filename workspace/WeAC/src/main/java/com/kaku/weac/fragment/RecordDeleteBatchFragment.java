/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.activities.RecordDeleteActivity;
import com.kaku.weac.adapter.RecordDeleteAdapter;
import com.kaku.weac.bean.RecordDeleteItem;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.RecordItemDeleteComparator;
import com.kaku.weac.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 录音批量删除Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDeleteBatchFragment extends BaseFragment implements
        OnClickListener {

    /**
     * 删除requestCode
     */
    private static final int REQUEST_DELETE = 1;

    /**
     * 取消按钮
     */
    private ImageView mCancelBtn;

    /**
     * 标题
     */
    private static TextView sTitleTv;

    /**
     * 全选按钮
     */
    private static TextView sSelectAllBtn;

    /**
     * 取消全选按钮
     */
    private static TextView sSelectNoneBtn;

    /**
     * 删除按钮
     */
    private static Button sDeleteBtn;

    /**
     * 录音批量删除画面
     */
    private ViewGroup mViewGroup;

    /**
     * 保存录音信息的List
     */
    private static List<RecordDeleteItem> sRecordList;

    /**
     * 保存选中删除的录音路径
     */
    private static List<String> sDeleteList;

    /**
     * 保存录音信息的Adapter
     */
    private RecordDeleteAdapter mAdapter;

    /**
     * 录音ListView
     */
    private ListView mRecordLv;

    /**
     * 标题内容格式
     */
    private static String sTitleFormat;

    /**
     * 删除按钮可用文字灰白色
     */
    private static final int sColorWhite = Color.parseColor("#F4F4F4");

    /**
     * 删除按钮不可用文字0.6透明白色
     */
    private static final int sColorWhiteTrans = Color.parseColor("#9affffff");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sDeleteList = new ArrayList<>();
        sRecordList = new ArrayList<>();
        // 设置录音List
        setRingList();
        mAdapter = new RecordDeleteAdapter(getActivity(), sRecordList);
        sTitleFormat = getString(R.string.selected_xx_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_delete_batch,
                container, false);
        mViewGroup = (ViewGroup) view
                .findViewById(R.id.record_delete_batch_llyt);
        // 设置页面背景
        MyUtil.setBackground(mViewGroup, getActivity());

        mRecordLv = (ListView) view.findViewById(R.id.record_delete_batch_lv);
        mRecordLv.setAdapter(mAdapter);
        mRecordLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 取得录音项目实例
                RecordDeleteItem recordItem = mAdapter.getItem(position);
                // 当前为选中状态时
                if (recordItem.isSelected()) {
                    // 解除选中
                    unCheck(recordItem);
                    mAdapter.notifyDataSetChanged();
                } else {
                    // 选中
                    onCheck(recordItem);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

        sTitleTv = (TextView) view.findViewById(R.id.title_tv);
        sTitleTv.setText(String.format(sTitleFormat, 0));

        mCancelBtn = (ImageView) view.findViewById(R.id.cancel_btn);
        sSelectAllBtn = (TextView) view.findViewById(R.id.select_all_btn);
        sSelectNoneBtn = (TextView) view.findViewById(R.id.select_none_btn);
        sDeleteBtn = (Button) view.findViewById(R.id.delete_btn);

        mCancelBtn.setOnClickListener(this);
        sSelectAllBtn.setOnClickListener(this);
        sSelectNoneBtn.setOnClickListener(this);
        sDeleteBtn.setOnClickListener(this);
        sDeleteBtn.setClickable(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 取消按钮
            case R.id.cancel_btn:
                getActivity().finish();
                break;
            // 全选按钮
            case R.id.select_all_btn:
                // 隐藏全选显示取消全选按钮
                sSelectAllBtn.setVisibility(View.GONE);
                sSelectNoneBtn.setVisibility(View.VISIBLE);

                // 设置删除按钮可用
                sDeleteBtn.setClickable(true);
                sDeleteBtn.setBackgroundResource(R.drawable.bg_btn_sure);
                sDeleteBtn.setTextColor(sColorWhite);

                // 清除录音删除列表
                sDeleteList.clear();

                // 设置录音列表
                for (int i = 0; i < sRecordList.size(); i++) {
                    // 设置为选中
                    sRecordList.get(i).setSelected(true);
                    // 添加录音路径
                    sDeleteList.add(sRecordList.get(i).getRingUrl());
                }
                // 设置标题为全部选中
                sTitleTv.setText(String.format(sTitleFormat, sDeleteList.size()));
                mAdapter.notifyDataSetChanged();
                break;
            // 取消全选按钮
            case R.id.select_none_btn:
                // 隐藏取消全选显示全选按钮
                sSelectNoneBtn.setVisibility(View.GONE);
                sSelectAllBtn.setVisibility(View.VISIBLE);

                // 设置删除按钮不可用
                sDeleteBtn.setClickable(false);
                sDeleteBtn
                        .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
                sDeleteBtn.setTextColor(sColorWhiteTrans);

                // 清除录音删除列表
                sDeleteList.clear();

                // 设置录音列表
                for (int i = 0; i < sRecordList.size(); i++) {
                    // 设置为未选中
                    sRecordList.get(i).setSelected(false);
                }
                // 设置标题为选中0件
                sTitleTv.setText(String.format(sTitleFormat, 0));
                mAdapter.notifyDataSetChanged();
                break;
            // 删除按钮
            case R.id.delete_btn:
                // 弹出删除确认对话框
                Intent intent = new Intent(getActivity(),
                        RecordDeleteActivity.class);
                startActivityForResult(intent, REQUEST_DELETE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DELETE) {
            boolean result = false;
            // 遍历删除列表
            int length = sDeleteList.size();
            for (int i = 0; i < length; i++) {
                File file = new File(sDeleteList.get(i));
                // 删除文件
                result = file.delete();
            }
            if (result) {
                ToastUtil.showShortToast(getActivity(),
                        getString(R.string.delete_success));
            }

            // 清空删除列表
            sDeleteList.clear();
            // 刷新录音列表
            refreshList();

            // 当录音列表为空
            if (sRecordList.size() == 0) {
                // 返回到铃声选择界面
                getActivity().finish();
            } else {
                // 设置标题为选中0件
                sTitleTv.setText(String.format(sTitleFormat, 0));

                // 设置删除按钮不可用
                sDeleteBtn.setClickable(false);
                sDeleteBtn
                        .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
                sDeleteBtn.setTextColor(sColorWhiteTrans);
            }

        }

    }

    /**
     * 设置录音列表List
     */
    private void setRingList() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return;
        }
        // 录音文件路径
        String fileName = getRecordDirectory();
        File file = new File(fileName);
        // 当录音列表不存在时
        if (!file.exists()) {
            return;
        }
        // 列出录音文件夹所有文件
        File[] files = file.listFiles();
        for (File file1 : files) {
            // 音频文件名
            String ringName = file1.getName();
            // 去掉音频文件的扩展名
            ringName = MyUtil.removeEx(ringName);
            // 取得音频文件的地址
            String ringUrl = file1.getAbsolutePath();
            // 录音删除信息实例
            RecordDeleteItem item = new RecordDeleteItem(ringUrl, ringName,
                    false);
            sRecordList.add(item);
        }

        // 排序铃声列表
        Collections.sort(sRecordList, new RecordItemDeleteComparator());
    }

    /**
     * 更新录音列表显示
     */
    private void refreshList() {
        sRecordList.clear();
        // 设置录音列表
        setRingList();
        mAdapter.notifyDataSetChanged();

    }

    /**
     * 取得录音文件路径
     *
     * @return 录音文件路径
     */
    private String getRecordDirectory() {
        // 外部存储根路径
        String fileName = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        // 录音文件路径
        fileName += "/WeaAlarmClock/audio/record";
        return fileName;
    }

    /**
     * 解除选中
     *
     * @param recordItem 录音项目实例
     */
    public static void onCheck(final RecordDeleteItem recordItem) {
        recordItem.setSelected(true);
        // 添加到录音删除列表
        sDeleteList.add(recordItem.getRingUrl());
        // 录音删除列表已添加的项目个数
        int size = sDeleteList.size();
        // 设置标题显示选中的个数
        sTitleTv.setText(String.format(sTitleFormat, size));

        // 当录音删除个数为1时
        if (size == 1) {
            // 设置删除按钮可用
            sDeleteBtn.setClickable(true);
            sDeleteBtn.setBackgroundResource(R.drawable.bg_btn_sure);

            sDeleteBtn.setTextColor(sColorWhite);

        }
        // 当录音删除个数为录音列表最大个数时
        if (size == sRecordList.size()) {
            // 隐藏全选按钮，显示取消全选按钮
            sSelectAllBtn.setVisibility(View.GONE);
            sSelectNoneBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 选中
     *
     * @param recordItem 录音项目实例
     */
    public static void unCheck(final RecordDeleteItem recordItem) {
        recordItem.setSelected(false);
        // 移除录音删除列表
        sDeleteList.remove(recordItem.getRingUrl());
        // 录音删除列表已添加的项目个数
        int size = sDeleteList.size();

        // 设置标题显示选中的个数
        sTitleTv.setText(String.format(sTitleFormat, size));

        // 当录音删除个数为0时
        if (size == 0) {
            // 设置删除按钮不可用
            sDeleteBtn.setClickable(false);
            sDeleteBtn
                    .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
            sDeleteBtn.setTextColor(sColorWhiteTrans);

        }

        // 当没有显示全选按钮时
        if ((sSelectAllBtn.getVisibility() == View.GONE)) {
            // 显示全选按钮，隐藏取消全选按钮
            sSelectAllBtn.setVisibility(View.VISIBLE);
            sSelectNoneBtn.setVisibility(View.GONE);

        }
    }
}
