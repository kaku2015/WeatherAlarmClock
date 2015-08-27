package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.kaku.weac.R;
import com.kaku.weac.activities.ThemeActivity;
import com.kaku.weac.util.MyUtil;

/**
 * 更多fragment
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class MoreFragment extends Fragment {
    /**
     * 变更主题按钮
     */
    private Button mButton;

    /**
     * 主题壁纸的requestCode
     */
    private static final int REQUEST_THEME_WALLPAPER = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_more, container, false);
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThemeActivity.class);
                // 启动主题界面
                startActivityForResult(intent, REQUEST_THEME_WALLPAPER);

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_THEME_WALLPAPER) {
            ViewGroup vg = (ViewGroup) getActivity().findViewById(
                    R.id.llyt_activity_main);
            // 更新壁纸
            vg.setBackgroundResource(MyUtil.getWallPaper(getActivity()));
        }
    }

}
