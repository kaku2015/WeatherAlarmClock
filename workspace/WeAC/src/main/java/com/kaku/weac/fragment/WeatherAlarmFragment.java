/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.common.WeacConstants;

/**
 * 天气预警Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/11/29
 */
public class WeatherAlarmFragment extends BaseFragment {

    /**
     * 天气预警详情
     */
    private String mDetail;

    /**
     * 天气预警标题
     */
    private String mTitle;

    /**
     * 天气预警发布时间
     */
    private String mTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().setFinishOnTouchOutside(false);
        mTitle = getActivity().getIntent().getStringExtra(WeacConstants.TITLE);
        mDetail = getActivity().getIntent().getStringExtra(WeacConstants.DETAIL);
        mTime = getActivity().getIntent().getStringExtra(WeacConstants.TIME);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_weather_alarm, container,
                false);
        // 设置Dialog全屏显示
        getActivity().getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        // 天气预警标题TextView
        TextView titleTv = (TextView) view.findViewById(R.id.title);
        titleTv.setText(mTitle);

        // 天气预警详情TextView
        TextView detailTv = (TextView) view.findViewById(R.id.detail);
        if (mDetail == null) {
            detailTv.setText(getString(R.string.no_data));
        } else {
            detailTv.setText(mDetail);
        }

        // 天气预警发布时间TextView
        TextView timeTv = (TextView) view.findViewById(R.id.time);
        timeTv.setText(mTime);

        // 知道了Button
        Button rogerBtn = (Button) view.findViewById(R.id.roger_btn);
        rogerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoomout);
            }
        });

        return view;
    }
}
