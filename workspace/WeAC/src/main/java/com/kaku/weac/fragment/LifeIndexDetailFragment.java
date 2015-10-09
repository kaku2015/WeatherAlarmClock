package com.kaku.weac.fragment;

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

/**
 * 生活指数详情Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/10/10
 */
public class LifeIndexDetailFragment extends Fragment {

    /**
     * Log tag ：LifeIndexDetailFragment
     */
    private static final String LOG_TAG = "LifeIndexDetailFragment";

    /**
     * 知道了Button
     */
    private Button mRogerBtn;

    /**
     * 生活指数详情
     */
    private String mDetail;
    /**
     * 生活指数标题
     */
    private String mTitle;

    /**
     * 生活指数标题TextView
     */
    private TextView mIndexTitleTv;

    /**
     * 生活指数详情TextView
     */
    private TextView mIndexDetailTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
        mTitle = getActivity().getIntent().getStringExtra("life_index_title");
        mDetail = getActivity().getIntent().getStringExtra("life_index_detail");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_record_detail, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        mIndexTitleTv = (TextView) view.findViewById(R.id.index_title);
        mIndexTitleTv.setText(mTitle);

        mIndexDetailTv = (TextView) view.findViewById(R.id.index_detail);
        mIndexDetailTv.setText(mDetail);

        mRogerBtn = (Button) view.findViewById(R.id.roger_btn);
        mRogerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();

            }
        });


        return view;
    }
}
