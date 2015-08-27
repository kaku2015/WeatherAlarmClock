package com.kaku.weac.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.kaku.weac.R;

/**
 * 录音删除Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/08
 */
public class RecordDeleteFragment extends Fragment implements OnClickListener {

    /**
     * 取消按钮
     */
    private Button mCancelBtn;

    /**
     * 确定按钮
     */
    private Button mSureBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_delete, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        mCancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        mSureBtn = (Button) view.findViewById(R.id.sure_btn);
        mCancelBtn.setOnClickListener(this);
        mSureBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_btn:
                getActivity().setResult(Activity.RESULT_OK,
                        getActivity().getIntent());
                getActivity().finish();

                break;
            case R.id.cancel_btn:
                getActivity().finish();
                break;
        }

    }
}
