/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.LogUtil;

import java.io.File;

/**
 * 录音重命名Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordRenameFragment extends BaseFragment implements OnClickListener {

    /**
     * Log tag ：RecordRenameFragment
     */
    private static final String LOG_TAG = "RecordRenameFragment";

    /**
     * 文件名称
     */
    private String mFileName;

    /**
     * 录音URL
     */
    private String mRingUrl;

    /**
     * 重命名EditText
     */
    private EditText mRenameEt;

    /**
     * 显示错误信息的TextView
     */
    private TextView mErrorTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
        mFileName = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_NAME);
        mRingUrl = getActivity().getIntent().getStringExtra(
                WeacConstants.RING_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_rename, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        mRenameEt = (EditText) view.findViewById(R.id.record_rename_edit_text);
        mRenameEt.setText(mFileName);
        // 取消按钮
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        // 确定按钮
        Button sureBtn = (Button) view.findViewById(R.id.sure_btn);
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        mErrorTv = (TextView) view.findViewById(R.id.record_rename_error);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 延时弹出软键盘
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    // 软键盘服务
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 弹出软键盘
                    inputManager.showSoftInput(mRenameEt, 0);
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, "run方法出现错误：" + e.toString());
                }

            }
        }, 200);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                // 取得输入的内容
                String rename = mRenameEt.getText().toString();
                // 当输入内容不为空时
                if (!rename.equals("")) {
                    // 取得不带扩展名的地址
                    String newUrl = mRingUrl
                            .substring(0, mRingUrl.lastIndexOf("/"));
                    // 新命名的文件地址
                    newUrl += "/" + rename + ".amr";
                    File newFile = new File(newUrl);
                    // 当重命名文件不存在时
                    if (!newFile.exists()) {
                        getActivity().getIntent().putExtra(WeacConstants.NEW_URL,
                                newUrl);
                        getActivity().setResult(Activity.RESULT_OK,
                                getActivity().getIntent());
                        getActivity().finish();

                    } else {
                        mErrorTv.setVisibility(View.VISIBLE);
                        mErrorTv.setText(getString(R.string.file_name_exist));
                    }

                } else {
                    // 当输入内容为空时
                    mErrorTv.setVisibility(View.VISIBLE);
                    mErrorTv.setText(getString(R.string.input_empty));
                }
                break;
            case R.id.cancel_btn:
                getActivity().finish();
                break;
        }

    }
}
