package com.kaku.weac.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class RecordDetailFragment extends Fragment {

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

    /**
     * 文件操作
     */
    private File mFile;

    /**
     * 文件名称TextView
     */
    private TextView mFileNameTv;

    /**
     * 文件格式TextView
     */
    private TextView mFileFormatTv;

    /**
     * 文件大小TextView
     */
    private TextView mFileSizeTv;

    /**
     * 修改时间TextView
     */
    private TextView mModifyTimeTv;

    /**
     * 播放时长TextView
     */
    private TextView mPlayDurationTv;

    /**
     * 保存路径TextView
     */
    private TextView mSavePathTv;

    /**
     * 知道了Button
     */
    private Button mRogerBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
        mFileName = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_NAME);
        mSavePath = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_URL);
        mFile = new File(mSavePath);
        mFileSize = MyUtil.FormatFileSize(mFile.length());
        long time = mFile.lastModified();
        mModifyTime = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault()).format(time);
        try {
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(mSavePath);
            player.prepare();
            int ms = player.getDuration();
            mPlayDuration = MyUtil.FormatFileDuration(ms);
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
        mFileNameTv = (TextView) view.findViewById(R.id.fime_name);
        mFileFormatTv = (TextView) view.findViewById(R.id.fime_format);
        mFileSizeTv = (TextView) view.findViewById(R.id.fime_size);
        mModifyTimeTv = (TextView) view.findViewById(R.id.modify_time);
        mPlayDurationTv = (TextView) view.findViewById(R.id.play_duration);
        mSavePathTv = (TextView) view.findViewById(R.id.save_path);
        mRogerBtn = (Button) view.findViewById(R.id.roger_btn);
        mRogerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });

        mFileNameTv.setText(mFileName);
        mFileFormatTv.setText("amr");
        mFileSizeTv.setText(mFileSize);
        mModifyTimeTv.setText(mModifyTime);
        mPlayDurationTv.setText(mPlayDuration);
        mSavePathTv.setText(mSavePath);

        return view;
    }
}
