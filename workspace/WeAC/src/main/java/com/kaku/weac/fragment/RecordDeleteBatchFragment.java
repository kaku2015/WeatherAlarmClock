/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
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

import com.kaku.weac.Listener.RecordCheckChangedListener;
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
     * 标题
     */
    private TextView mTitleTv;

    /**
     * 全选按钮
     */
    private TextView mSelectAllBtn;

    /**
     * 取消全选按钮
     */
    private TextView mSelectNoneBtn;

    /**
     * 删除按钮
     */
    private Button mDeleteBtn;

    /**
     * 保存录音信息的List
     */
    private List<RecordDeleteItem> mRecordList;

    /**
     * 保存选中删除的录音路径
     */
    private List<String> mDeleteList;

    /**
     * 保存录音信息的Adapter
     */
    private RecordDeleteAdapter mAdapter;

    /**
     * 标题内容格式
     */
    private String mTitleFormat;

    /**
     * 删除按钮可用文字灰白色
     */
    private int mColorWhite;

    /**
     * 删除按钮不可用文字0.6透明白色
     */
    private int mColorWhiteTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter() {
        mDeleteList = new ArrayList<>();
        mRecordList = new ArrayList<>();
        // 设置录音List
        setRingList();
        mAdapter = new RecordDeleteAdapter(getActivity(), mRecordList);
        mAdapter.setRecordCheckChangedListener(new RecordCheckChangedListener() {
            @Override
            public void onChecked(RecordDeleteItem recordDeleteItem) {
                onCheck(recordDeleteItem);
            }

            @Override
            public void unChecked(RecordDeleteItem recordDeleteItem) {
                unCheck(recordDeleteItem);
            }
        });
        mTitleFormat = getString(R.string.selected_xx_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_delete_batch,
                container, false);
        // 录音批量删除画面
        ViewGroup viewGroup = (ViewGroup) view
                .findViewById(R.id.record_delete_batch_llyt);
        // 设置页面背景
        MyUtil.setBackground(viewGroup, getActivity());

        mColorWhite = getResources().getColor(R.color.gray_background_color);
        mColorWhiteTrans = getResources().getColor(R.color.white_trans60);

        // 录音ListView
        ListView recordLv = (ListView) view.findViewById(R.id.record_delete_batch_lv);
        recordLv.setAdapter(mAdapter);
        recordLv.setOnItemClickListener(new OnItemClickListener() {

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

        mTitleTv = (TextView) view.findViewById(R.id.title_tv);
        mTitleTv.setText(String.format(mTitleFormat, 0));

        // 取消按钮
        ImageView cancelBtn = (ImageView) view.findViewById(R.id.cancel_btn);
        mSelectAllBtn = (TextView) view.findViewById(R.id.select_all_btn);
        mSelectNoneBtn = (TextView) view.findViewById(R.id.select_none_btn);
        mDeleteBtn = (Button) view.findViewById(R.id.delete_btn);

        cancelBtn.setOnClickListener(this);
        mSelectAllBtn.setOnClickListener(this);
        mSelectNoneBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        mDeleteBtn.setClickable(false);

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
                mSelectAllBtn.setVisibility(View.GONE);
                mSelectNoneBtn.setVisibility(View.VISIBLE);

                // 设置删除按钮可用
                mDeleteBtn.setClickable(true);
                mDeleteBtn.setBackgroundResource(R.drawable.bg_btn_sure);
                mDeleteBtn.setTextColor(mColorWhite);

                // 清除录音删除列表
                mDeleteList.clear();

                // 设置录音列表
                for (int i = 0; i < mRecordList.size(); i++) {
                    // 设置为选中
                    mRecordList.get(i).setSelected(true);
                    // 添加录音路径
                    mDeleteList.add(mRecordList.get(i).getRingUrl());
                }
                // 设置标题为全部选中
                mTitleTv.setText(String.format(mTitleFormat, mDeleteList.size()));
                mAdapter.notifyDataSetChanged();
                break;
            // 取消全选按钮
            case R.id.select_none_btn:
                // 隐藏取消全选显示全选按钮
                mSelectNoneBtn.setVisibility(View.GONE);
                mSelectAllBtn.setVisibility(View.VISIBLE);

                // 设置删除按钮不可用
                mDeleteBtn.setClickable(false);
                mDeleteBtn
                        .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
                mDeleteBtn.setTextColor(mColorWhiteTrans);

                // 清除录音删除列表
                mDeleteList.clear();

                // 设置录音列表
                for (int i = 0; i < mRecordList.size(); i++) {
                    // 设置为未选中
                    mRecordList.get(i).setSelected(false);
                }
                // 设置标题为选中0件
                mTitleTv.setText(String.format(mTitleFormat, 0));
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
            int length = mDeleteList.size();
            for (int i = 0; i < length; i++) {
                File file = new File(mDeleteList.get(i));
                // 删除文件
                result = file.delete();
            }
            if (result) {
                ToastUtil.showShortToast(getActivity(),
                        getString(R.string.delete_success));
            }

            // 清空删除列表
            mDeleteList.clear();
            // 刷新录音列表
            refreshList();

            // 当录音列表为空
            if (mRecordList.size() == 0) {
                // 返回到铃声选择界面
                getActivity().finish();
            } else {
                // 设置标题为选中0件
                mTitleTv.setText(String.format(mTitleFormat, 0));

                // 设置删除按钮不可用
                mDeleteBtn.setClickable(false);
                mDeleteBtn
                        .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
                mDeleteBtn.setTextColor(mColorWhiteTrans);
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
            mRecordList.add(item);
        }

        // 排序铃声列表
        Collections.sort(mRecordList, new RecordItemDeleteComparator());
    }

    /**
     * 更新录音列表显示
     */
    private void refreshList() {
        mRecordList.clear();
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
    private void onCheck(final RecordDeleteItem recordItem) {
        recordItem.setSelected(true);
        // 添加到录音删除列表
        mDeleteList.add(recordItem.getRingUrl());
        // 录音删除列表已添加的项目个数
        int size = mDeleteList.size();
        // 设置标题显示选中的个数
        mTitleTv.setText(String.format(mTitleFormat, size));

        // 当录音删除个数为1时
        if (size == 1) {
            // 设置删除按钮可用
            mDeleteBtn.setClickable(true);
            mDeleteBtn.setBackgroundResource(R.drawable.bg_btn_sure);

            mDeleteBtn.setTextColor(mColorWhite);

        }
        // 当录音删除个数为录音列表最大个数时
        if (size == mRecordList.size()) {
            // 隐藏全选按钮，显示取消全选按钮
            mSelectAllBtn.setVisibility(View.GONE);
            mSelectNoneBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 选中
     *
     * @param recordItem 录音项目实例
     */
    private void unCheck(final RecordDeleteItem recordItem) {
        recordItem.setSelected(false);
        // 移除录音删除列表
        mDeleteList.remove(recordItem.getRingUrl());
        // 录音删除列表已添加的项目个数
        int size = mDeleteList.size();

        // 设置标题显示选中的个数
        mTitleTv.setText(String.format(mTitleFormat, size));

        // 当录音删除个数为0时
        if (size == 0) {
            // 设置删除按钮不可用
            mDeleteBtn.setClickable(false);
            mDeleteBtn
                    .setBackgroundResource(R.drawable.shape_circle_btn_sure_invalidate);
            mDeleteBtn.setTextColor(mColorWhiteTrans);

        }

        // 当没有显示全选按钮时
        if ((mSelectAllBtn.getVisibility() == View.GONE)) {
            // 显示全选按钮，隐藏取消全选按钮
            mSelectAllBtn.setVisibility(View.VISIBLE);
            mSelectNoneBtn.setVisibility(View.GONE);

        }
    }
}
