package com.kaku.weac.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.LineChartView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 天气fragment
 *
 * @author 咖枯
 * @version 1.0 2015/9
 */
public class WeaFragment extends Fragment {
    /**
     * Log tag ：WeaFragment
     */
    private static final String LOG_TAG = "WeaFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_wea, container, false);

        LineChartView chartDay = (LineChartView) view.findViewById(R.id.line_char_day);
        chartDay.setTemp(new int[]{32, 31, 31, 30, 25, 26});
        chartDay.setTextSpace(10);
        int colorDay = getResources().getColor(R.color.yellow_hot);
        chartDay.setLineColor(colorDay);
        chartDay.setPointColor(colorDay);

        LineChartView chartNight = (LineChartView) view.findViewById(R.id.line_chart_night);
        chartNight.setTemp(new int[]{20, 25, 24, 25, 20, 20});
        chartNight.setTextSpace(-10);
        int colorNight = getResources().getColor(R.color.blue_ice);
        chartNight.setLineColor(colorNight);
        chartNight.setPointColor(colorNight);


        Button readButton = (Button) view.findViewById(R.id.read_wea);
        final TextView tv = (TextView) view.findViewById(R.id.wea_prompt_tv);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherUtil.sendHttpRequest("http://wthrcdn.etouch.cn/WeatherApi?citykey=101030100",
                        new HttpCallbackListener() {
                            @Override
                            public void onFinish(final String response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText(response);

                                        try {
                                            // 外部存储根路径
                                            String fileName = Environment.getExternalStorageDirectory()
                                                    .getAbsolutePath();
                                            // 录音文件路径
                                            fileName += "/WeaAlarmClock/wea2.txt";
                                            File file = new File(fileName);
                                            if (!file.exists()) {
                                                file.createNewFile();
                                            }

                                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                            BufferedWriter bw = new BufferedWriter(fw);
                                            bw.write(response);
                                            bw.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(final Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv.setText("读取失败：" + e.toString());
                                    }
                                });
                            }
                        });
            }
        });

        return view;
    }

}
