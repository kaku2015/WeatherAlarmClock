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
import com.kaku.weac.common.WeacConstants;

/**
 * 生活指数详情Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/10/10
 */
public class LifeIndexDetailFragment extends Fragment {

    /**
     * 生活指数详情
     */
    private String mDetail;
    /**
     * 生活指数标题
     */
    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setFinishOnTouchOutside(false);
        mTitle = getActivity().getIntent().getStringExtra(WeacConstants.TITLE);
        mDetail = getActivity().getIntent().getStringExtra(WeacConstants.DETAIL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_life_index_detail, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        // 生活指数标题TextView
        TextView indexTitleTv = (TextView) view.findViewById(R.id.index_title);
        indexTitleTv.setText(mTitle);

        // 生活指数详情TextView
        TextView indexDetailTv = (TextView) view.findViewById(R.id.index_detail);
        if (mDetail == null)
            indexDetailTv.setText("无");
        else
            indexDetailTv.setText(mDetail);

        // 知道了Button
        Button rogerBtn = (Button) view.findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoomout);
            }
        });


        return view;
    }
}
