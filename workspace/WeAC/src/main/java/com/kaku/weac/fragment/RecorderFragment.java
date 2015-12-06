/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.activities.RecordDeleteActivity;
import com.kaku.weac.activities.RecordDeleteBatchActivity;
import com.kaku.weac.activities.RecordDetailActivity;
import com.kaku.weac.activities.RecordOperateActivity;
import com.kaku.weac.activities.RecordRenameActivity;
import com.kaku.weac.adapter.RingSelectAdapter;
import com.kaku.weac.bean.RingSelectItem;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.AudioRecorder;
import com.kaku.weac.util.LogUtil;
import com.kaku.weac.util.MyUtil;
import com.kaku.weac.util.RingItemComparator;
import com.kaku.weac.util.ToastUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 录音Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class RecorderFragment extends BaseFragment implements OnClickListener {

    /**
     * Log tag ：RecorderFragment
     */
    private static final String LOG_TAG = "RecorderFragment";

    /**
     * 录音操作的requestCode
     */
    private static final int REQUEST_RECORD_OPERATE = 1;

    /**
     * 重命名requestCode
     */
    private static final int REQUEST_RENAME = 2;

    /**
     * 删除requestCode
     */
    private static final int REQUEST_DELETE = 3;

    /**
     * 保存录音信息的List
     */
    private List<Map<String, String>> mList;

    /**
     * 保存录音信息的Adapter
     */
    RingSelectAdapter mRecorderAdapter;

    /**
     * 录音按钮
     */
    private ImageButton mRecordBtn;

    /**
     * 停止录音按钮
     */
    private ImageButton mStopBtn;

    /**
     * 录音按钮描述信息
     */
    private TextView mRecordButtonInfo;

    /**
     * 铃声名标记位置
     */
    private String mRingName;

    /**
     * 麦克风状态组件
     */
    private ViewGroup mRecordMic;

    /**
     * 麦克风状态图
     */
    private ImageView mRecordMicStatusImage;

    /**
     * 录音时长
     */
    private TextView mRecordTime;

    /**
     * 是否正在录音
     */
    private boolean mIsRecording = false;

    /**
     * 铃声选择位置
     */
    private int mPosition = 0;

    /**
     * 录音时间线程标记
     */
    private static final int UPDATE_TIME = 1;

    /**
     * 麦克风状态Handler
     */
    private MicStatusHandler mMicStatusHandler;

    /**
     * 录音时间Handler
     */
    private RecordTimeHandler mRecordTimeHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        // 当为编辑闹钟状态时，铃声名为闹钟单例铃声名
        if (RingSelectFragment.sRingName != null) {
            mRingName = RingSelectFragment.sRingName;
        } else {
            // 当为新建闹钟状态时，铃声名为最近一次选择保存的铃声名
            SharedPreferences share = getActivity().getSharedPreferences(
                    WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
            mRingName = share.getString(WeacConstants.RING_NAME, "");
        }
        // 设置录音List
        setRingList();
        mRecorderAdapter = new RingSelectAdapter(getActivity(), mList, mRingName);
        mMicStatusHandler = new MicStatusHandler(this);
        mRecordTimeHandler = new RecordTimeHandler(this);
    }

    @Override
    public void onStart() {
        LogUtil.i(LOG_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.i(LOG_TAG, "onResume()");
        super.onResume();
        // 当重新显示录音界面时刷新列表显示
        refreshList();
    }

    @Override
    public void onPause() {
        LogUtil.i(LOG_TAG, "onPause()");
        // 停止录音
        recordStop();
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.i(LOG_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtil.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
        if (mMicStatusHandler != null) {
            mMicStatusHandler.removeCallbacksAndMessages(null);
        }
        if (mRecordTimeHandler != null) {
            mRecordTimeHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.i(LOG_TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fm_ring_recorder, container,
                false);

        // 录音ListView
        ListView recordListView = (ListView) view.findViewById(R.id.ring_record_lv);
        // 当录音List内容为空时显示的TextView
        TextView emptyView = (TextView) view
                .findViewById(R.id.ring_record_empty);
        // 设置录音List内容为空时的视图
        recordListView.setEmptyView(emptyView);
        recordListView.setAdapter(mRecorderAdapter);
        // 设置列表初始化显示的位置
        recordListView.setSelection(mPosition);
        recordListView.setOnItemClickListener(new OnItemClickListenerImpl());
        recordListView
                .setOnItemLongClickListener(new onItemLongClickListenerImpl());
        // 录音按钮组件
        mRecordBtn = (ImageButton) view.findViewById(R.id.ring_record_record);
        mStopBtn = (ImageButton) view.findViewById(R.id.ring_record_stop);
        mRecordButtonInfo = (TextView) view
                .findViewById(R.id.ring_record_button_info);
        mRecordBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);

        // 麦克风状态图组件
        mRecordMic = (ViewGroup) view.findViewById(R.id.ring_record_mic_llyt);
        mRecordMicStatusImage = (ImageView) view
                .findViewById(R.id.ring_record_mic_status);
        mRecordTime = (TextView) view.findViewById(R.id.ring_record_time);
        return view;
    }

    private class OnItemClickListenerImpl implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Map<String, String> map = mRecorderAdapter.getItem(position);
            // 取得铃声名
            String ringName = map.get(WeacConstants.RING_NAME);
            // 取得播放地址
            String ringUrl = map.get(WeacConstants.RING_URL);
            // 更新当前录音选中的位置
            mRecorderAdapter.updateSelection(ringName);
            // 更新适配器刷新录音列表显示
            mRecorderAdapter.notifyDataSetChanged();
            // 播放音频文件
            AudioPlayer.getInstance(getActivity()).play(ringUrl, false, false);
            // 设置最后一次选中的铃声选择界面位置为录音界面
            RingSelectItem.getInstance().setRingPager(2);


            ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_ring_select_sort);
            PagerAdapter f = pager.getAdapter();
            LocalMusicFragment localMusicFragment = (LocalMusicFragment) f.instantiateItem(pager, 1);
            SystemRingFragment systemRingFragment = (SystemRingFragment) f.instantiateItem(pager, 0);
            // 清空本地音乐界面选中标记
            if (localMusicFragment.mLocalMusicAdapter != null) {
                localMusicFragment.mLocalMusicAdapter.updateSelection("");
                localMusicFragment.mLocalMusicAdapter.notifyDataSetChanged();
            }
            // 清空系统铃声界面选中标记
            if (systemRingFragment.mSystemRingAdapter != null) {
                systemRingFragment.mSystemRingAdapter.updateSelection("");
                systemRingFragment.mSystemRingAdapter.notifyDataSetChanged();
            }
        }

    }

    private class onItemLongClickListenerImpl implements
            OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            Intent intent = new Intent(getActivity(),
                    RecordOperateActivity.class);
            intent.putExtra(WeacConstants.POSITION, position);
            startActivityForResult(intent, REQUEST_RECORD_OPERATE);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 当点击录音按钮
            case R.id.ring_record_record:
                // XXX：当没有外部存储时保存在内部
                // 当存在外部存储介质时
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    // 录音文件夹路径
                    String fileName = getRecordDirectory();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            return;
                        }
                    }
                    // 当前系统时间
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()).format(new Date());
                    // 录音文件名
                    fileName += String.format(getActivity().getResources()
                            .getString(R.string.record_file_name), time);

                    // 播放录音开始提示音
                    AudioPlayer.getInstance(getActivity()).playRaw(
                            R.raw.record_start, false, false);
                    // 延迟200毫秒防止提示音被录入
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 开始录音
                    AudioRecorder.getInstance(getActivity()).record(fileName);

                    // 当录音失败
                    if (!AudioRecorder.getInstance(getActivity()).mIsStarted) {
                        // 删除录音文件
                        File f = new File(fileName);
                        boolean result = false;
                        if (f.exists()) {
                            result = f.delete();
                        }
                        if (!result) {
                            ToastUtil.showShortToast(getActivity(), getString(R.string.error_delete_fail));
                        }
                        break;
                    }

                    // 隐藏录音按钮
                    mRecordBtn.setVisibility(View.GONE);
                    // 显示停止录音按钮
                    mStopBtn.setVisibility(View.VISIBLE);
                    // 录音按钮描述信息
                    mRecordButtonInfo.setText(getString(R.string.click_stop));
                    // 显示麦克风状态组件
                    mRecordMic.setVisibility(View.VISIBLE);
                    // 后台更新麦克风状态
                    mIsRecording = true;

                    // 开启线程，后台更新麦克风状态
                    new Thread(new UpdateMicStatus()).start();
                    mRecordTime.setText(getResources()
                            .getString(R.string.zero_zero));
                    new Thread(new updateRecorderTime()).start();

                    // Handler handler = new Handler();
                    // Runnable stopRecordRun = new Runnable() {
                    // @Override
                    // public void run() {
                    // // 停止录音
                    // recordStop();
                    // ToastUtil.showLongToast(getActivity(), "录音最大时长为10分钟");
                    // }
                    //
                    // };
                    // // 当录音达到10分钟自动执行结束录音操作
                    // handler.postDelayed(stopRecordRun,
                    // WeacConstants.MAX_RECORD_LENGTH);
                } else {
                    ToastUtil.showShortToast(getActivity(),
                            getString(R.string.error_sd_card));
                }
                break;
            case R.id.ring_record_stop:
                // 停止录音
                recordStop();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 录音操作
            case REQUEST_RECORD_OPERATE:
                int position = data.getIntExtra(WeacConstants.POSITION, 0);
                Map<String, String> map = mRecorderAdapter.getItem(position);
                // 文件名
                String ringName = map.get(WeacConstants.RING_NAME);
                // 文件路径
                String ringUrl = map.get(WeacConstants.RING_URL);
                // 录音操作类型
                int type = data.getIntExtra(WeacConstants.TYPE, -1);
                switch (type) {
                    // 重命名
                    case 1:
                        Intent intent1 = new Intent(getActivity(),
                                RecordRenameActivity.class);
                        intent1.putExtra(WeacConstants.RING_NAME, ringName);
                        intent1.putExtra(WeacConstants.RING_URL, ringUrl);
                        startActivityForResult(intent1, REQUEST_RENAME);
                        break;
                    // 删除
                    case 2:
                        Intent intent2 = new Intent(getActivity(),
                                RecordDeleteActivity.class);
                        intent2.putExtra(WeacConstants.RING_URL, ringUrl);
                        startActivityForResult(intent2, REQUEST_DELETE);
                        break;
                    // 批量删除
                    case 3:
                        Intent intent3 = new Intent(getActivity(),
                                RecordDeleteBatchActivity.class);
                        intent3.putExtra(WeacConstants.RING_NAME, ringName);
                        intent3.putExtra(WeacConstants.RING_URL, ringUrl);
                        startActivity(intent3);
                        break;
                    // 详情
                    case 4:
                        Intent intent4 = new Intent(getActivity(),
                                RecordDetailActivity.class);
                        intent4.putExtra(WeacConstants.RING_NAME, ringName);
                        intent4.putExtra(WeacConstants.RING_URL, ringUrl);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
                break;
            // 重命名
            case REQUEST_RENAME:
                String oldUrl = data.getStringExtra(WeacConstants.RING_URL);
                String newUrl = data.getStringExtra(WeacConstants.NEW_URL);
                File oldFile = new File(oldUrl);
                // 重命名文件
                boolean result = oldFile.renameTo(new File(newUrl));
                if (result) {
                    ToastUtil.showShortToast(getActivity(),
                            getString(R.string.rename_success));
                }
                // 刷新列表显示
                refreshList();
                break;
            case REQUEST_DELETE:
                String ringUrl2 = data.getStringExtra(WeacConstants.RING_URL);
                File file = new File(ringUrl2);
                // 删除文件
                boolean result2 = file.delete();
                if (result2) {
                    ToastUtil.showShortToast(getActivity(),
                            getString(R.string.delete_success));
                }
                // 刷新列表显示
                refreshList();
                break;
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
            Map<String, String> map = new HashMap<>();
            map.put(WeacConstants.RING_NAME, ringName);
            map.put(WeacConstants.RING_URL, ringUrl);
            mList.add(map);
        }

        // 排序铃声列表
        Collections.sort(mList, new RingItemComparator());

        // 当列表中存在与保存的铃声名一致时，设置该列表的显示位置
        for (int i = 0; i < mList.size(); i++) {
            Map<String, String> map = mList.get(i);
            String ringName = map.get(WeacConstants.RING_NAME);
            if (ringName.equals(mRingName)) {
                mPosition = i;
            }
        }
    }

    /**
     * 更新录音列表显示
     */
    private void refreshList() {
        mList.clear();
        setRingList();
        mRecorderAdapter.notifyDataSetChanged();

    }

    /**
     * 停止录音
     */
    private void recordStop() {
        // 当正在录音
        if (mIsRecording) {
            // 停止录音
            AudioRecorder.getInstance(getActivity()).stop();
            // 播放停止提示音
            AudioPlayer.getInstance(getActivity()).playRaw(R.raw.record_stop,
                    false, false);
            // 隐藏停止录音按钮
            mStopBtn.setVisibility(View.GONE);
            // 显示录音按钮
            mRecordBtn.setVisibility(View.VISIBLE);
            // 录音按钮描述信息
            mRecordButtonInfo.setText(getString(R.string.click_record));
            // 更新列表
            refreshList();
            // 隐藏麦克风状态组件
            mRecordMic.setVisibility(View.GONE);
            // 停止后台更新麦克风状态
            mIsRecording = false;

            // 快速点击不可用
            mRecordBtn.setClickable(false);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                        mRecordBtn.setClickable(true);
                    } catch (Exception e) {
                        LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
                    }

                }
            }).start();
        }
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
     * 更新麦克风状态图
     */
    private class UpdateMicStatus implements Runnable {
        /**
         * 振幅参考值
         */
        private static final int BASE = 3;

        /**
         * 分贝间隔取样时间
         */
        private static final int SPACE = 100;

        /**
         * 分贝识别标记
         */
        private int micStatus;

        /**
         * 分贝
         */
        private int db;

        @Override
        public void run() {
            while (mIsRecording) {
                try {
                    if (AudioRecorder.getInstance(getActivity()).mRecorder == null) {
                        return;
                    }
                    // 振幅与参考值的比率
                    double ratio = (double) AudioRecorder
                            .getInstance(getActivity()).mRecorder
                            .getMaxAmplitude()
                            / BASE;
                    if (ratio > 1) {
                        // 计算分贝公式：dB=20lg(A/B)（振幅）
                        db = (int) (20 * Math.log10(ratio));
                    } else {
                        db = 0;
                    }
                    Thread.sleep(SPACE);
                    // LogUtil.d(LOG_TAG, "分贝值：" + db);

                    if (db < 27) {
                        micStatus = 1;
                    } else if (db < 32) {
                        micStatus = 2;
                    } else if (db < 38) {
                        micStatus = 3;
                    } else if (db < 45) {
                        micStatus = 4;
                    } else if (db < 55) {
                        micStatus = 5;
                    } else if (db < 65) {
                        micStatus = 6;
                    } else if (db < 75) {
                        micStatus = 7;
                    } else if (db < 80) {
                        micStatus = 8;
                    } else {
                        micStatus = 9;
                    }
                    Message msg = mMicStatusHandler.obtainMessage(micStatus);
                    mMicStatusHandler.sendMessage(msg);
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
                }

            }
        }
    }

    /**
     * 更新录音时长
     */
    private class updateRecorderTime implements Runnable {

        // 录音时长
        private int time = 1;

        @Override
        public void run() {
            while (mIsRecording) {
                try {
                    Thread.sleep(1000);
                    CharSequence recordTime = new SimpleDateFormat("mm:ss",
                            Locale.getDefault()).format(time * 1000);
                    Message msg = mRecordTimeHandler.obtainMessage(UPDATE_TIME,
                            recordTime);
                    mRecordTimeHandler.sendMessage(msg);
                    time++;
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
                }
            }
        }
    }

    /**
     * 麦克风状态Handler
     */
    static class MicStatusHandler extends Handler {
        WeakReference<RecorderFragment> mWeakReference;

        public MicStatusHandler(RecorderFragment recorderFragment) {
            mWeakReference = new WeakReference<>(recorderFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecorderFragment recorderFragment = mWeakReference.get();
            // 设置麦克风状态图
            switch (msg.what) {
                case 1:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status1);
                    break;
                case 2:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status2);
                    break;
                case 3:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status3);
                    break;
                case 4:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status4);
                    break;
                case 5:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status5);
                    break;
                case 6:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status6);
                    break;
                case 7:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status7);
                    break;
                case 8:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status8);
                    break;
                case 9:
                    recorderFragment.mRecordMicStatusImage
                            .setImageResource(R.drawable.ic_ring_record_mic_status9);
                    break;
            }

        }
    }

    /**
     * 录音时间Handler
     */
    static class RecordTimeHandler extends Handler {
        WeakReference<RecorderFragment> mWeakReference;

        public RecordTimeHandler(RecorderFragment recorderFragment) {
            mWeakReference = new WeakReference<>(recorderFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RecorderFragment recorderFragment = mWeakReference.get();
            switch (msg.what) {
                case UPDATE_TIME:
                    recorderFragment.mRecordTime.setText(msg.obj.toString());
                    break;
            }
        }

    }
}