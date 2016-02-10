/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 录音详细Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDetailFragment extends BaseFragment {

    /**
     * Log tag ：RecordDetailFragment
     */
    private static final String LOG_TAG = "RecordDetailFragment";

    /**
     * 文件名称
     */
    private String mFileName;

    /**
     * 文件大小
     */
    private String mFileSize;

    /**
     * 保存路径
     */
    private String mSavePath;

    /**
     * 修改时间
     */
    private String mModifyTime;

    /**
     * 播放时长
     */
    private String mPlayDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
        mFileName = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_NAME);
        mSavePath = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_URL);
        // 文件操作
        File file = new File(mSavePath);
        mFileSize = MyUtil.formatFileSize(file.length(), "0.00");
        long time = file.lastModified();
        mModifyTime = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault()).format(time);
        try {
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(mSavePath);
            player.prepare();
            int ms = player.getDuration();
            mPlayDuration = MyUtil.formatFileDuration(ms);
        } catch (IllegalArgumentException | SecurityException
                | IllegalStateException | IOException e) {
            mPlayDuration = getString(R.string.unknown);
            LogUtil.e(LOG_TAG, "error: " + e.toString());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_detail, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        // 文件名称TextView
        TextView fileNameTv = (TextView) view.findViewById(R.id.fime_name);
        // 文件格式TextView
        TextView fileFormatTv = (TextView) view.findViewById(R.id.fime_format);
        // 文件大小TextView
        TextView fileSizeTv = (TextView) view.findViewById(R.id.fime_size);
        // 修改时间TextView
        TextView modifyTimeTv = (TextView) view.findViewById(R.id.modify_time);
        // 播放时长TextView
        TextView playDurationTv = (TextView) view.findViewById(R.id.play_duration);
        // 保存路径TextView
        TextView savePathTv = (TextView) view.findViewById(R.id.save_path);
        // 知道了Button
        Button rogerBtn = (Button) view.findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });

        fileNameTv.setText(mFileName);
        fileFormatTv.setText(getString(R.string.amr));
        fileSizeTv.setText(mFileSize);
        modifyTimeTv.setText(mModifyTime);
        playDurationTv.setText(mPlayDuration);
        savePathTv.setText(mSavePath);

        return view;
    }
}
