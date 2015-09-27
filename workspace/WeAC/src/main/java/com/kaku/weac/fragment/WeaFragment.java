package com.kaku.weac.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.bean.WeatherInfo;
import com.kaku.weac.util.HttpCallbackListener;
import com.kaku.weac.util.WeatherUtil;
import com.kaku.weac.view.LineChartView;

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

    TextView mCityNameTv;
    TextView mAlarmTv;
    TextView mUpdateTimeTv;

    ImageView mTemperature1Iv;
    ImageView mTemperature2Iv;
    TextView mWeatherTypeTv;

    TextView mAqiTv;
    TextView mHumidityTv;
    TextView mWindPowerTv;

    TextView mWeatherTypeIvToday;
    TextView mTempHighTvToday;
    TextView mTemplowTvToday;
    TextView mWeatherTypeTvToday;

    TextView mWeatherTypeIvTomorrow;
    TextView mTempHighTvTomorrow;
    TextView mTemplowTvTomorrow;
    TextView mWeatherTypeTvTomorrow;

    TextView mWeatherTypeIvDayAfterTomorrow;
    TextView mTempHighTvDayAfterTomorrow;
    TextView mTemplowTvDayAfterTomorrow;
    TextView mWeatherTypeTvDayAfterTomorrow;

    TextView mDaysForecastTvWeek1;
    TextView mDaysForecastTvWeek2;
    TextView mDaysForecastTvWeek3;
    TextView mDaysForecastTvWeek4;
    TextView mDaysForecastTvWeek5;
    TextView mDaysForecastTvWeek6;

    TextView mDaysForecastTvDay1;
    TextView mDaysForecastTvDay2;
    TextView mDaysForecastTvDay3;
    TextView mDaysForecastTvDay4;
    TextView mDaysForecastTvDay5;
    TextView mDaysForecastTvDay6;

    TextView mDaysForecastWeaTypeDayIv1;
    TextView mDaysForecastWeaTypeDayIv2;
    TextView mDaysForecastWeaTypeDayIv3;
    TextView mDaysForecastWeaTypeDayIv4;
    TextView mDaysForecastWeaTypeDayIv5;
    TextView mDaysForecastWeaTypeDayIv6;

    TextView mDaysForecastWeaTypeDayTv1;
    TextView mDaysForecastWeaTypeDayTv2;
    TextView mDaysForecastWeaTypeDayTv3;
    TextView mDaysForecastWeaTypeDayTv4;
    TextView mDaysForecastWeaTypeDayTv5;
    TextView mDaysForecastWeaTypeDayTv6;

    LineChartView mCharDay;
    LineChartView mCharNight;

    TextView mDaysForecastWeaTypeNightIv1;
    TextView mDaysForecastWeaTypeNightIv2;
    TextView mDaysForecastWeaTypeNightIv3;
    TextView mDaysForecastWeaTypeNightIv4;
    TextView mDaysForecastWeaTypeNightIv5;
    TextView mDaysForecastWeaTypeNightIv6;

    TextView mDaysForecastWeaTypeNightTv1;
    TextView mDaysForecastWeaTypeNightTv2;
    TextView mDaysForecastWeaTypeNightTv3;
    TextView mDaysForecastWeaTypeNightTv4;
    TextView mDaysForecastWeaTypeNightTv5;
    TextView mDaysForecastWeaTypeNightTv6;

    TextView mDaysForecastWindDirectionTv1;
    TextView mDaysForecastWindDirectionTv2;
    TextView mDaysForecastWindDirectionTv3;
    TextView mDaysForecastWindDirectionTv4;
    TextView mDaysForecastWindDirectionTv5;
    TextView mDaysForecastWindDirectionTv6;

    TextView mDaysForecastWindPowerTv1;
    TextView mDaysForecastWindPowerTv2;
    TextView mDaysForecastWindPowerTv3;
    TextView mDaysForecastWindPowerTv4;
    TextView mDaysForecastWindPowerTv5;
    TextView mDaysForecastWindPowerTv6;

    TextView mLifeIndexUmbrellaTv;
    TextView mLifeIndexulTravioletRaysTv;
    TextView mLifeIndexDressTv;
    TextView mLifeIndexcoldTv;
    TextView mLifeIndexMorningExerciseTv;
    TextView mLifeIndexSportTv;
    TextView mLifeIndexCarWashTv;
    TextView mLifeIndexFishTv;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fm_wea, container, false);

        init(view);

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
                            public void onFinish(final WeatherInfo weatherInfo) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


//                                        tv.setText(weatherInfo);

                                        /*try {
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
                                            bw.write(weatherInfo);
                                            bw.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
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

    private void init(View view) {
    }

}
