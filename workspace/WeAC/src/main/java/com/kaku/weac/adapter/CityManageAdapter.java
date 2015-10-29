package com.kaku.weac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.bean.CityManage;

import java.util.List;

/**
 * 城市管理适配器类
 *
 * @author 咖枯
 * @version 1.0 2015/10/22
 */
public class CityManageAdapter extends ArrayAdapter<CityManage> {

    private final Context mContext;
    private List<CityManage> mList;

    /**
     * 城市管理适配器构造方法
     *
     * @param context context
     * @param list    城市管理列表
     */
    public CityManageAdapter(Context context, List<CityManage> list) {
        super(context, 0, list);
        mContext = context;
        mList = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CityManage cityManage = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_city_manage, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.cityWeather = (LinearLayout) convertView
                    .findViewById(R.id.city_weather);
            viewHolder.cityName = (TextView) convertView
                    .findViewById(R.id.city_name);
            viewHolder.weatherTypeIv = (ImageView) convertView
                    .findViewById(R.id.weather_type_iv);
            viewHolder.tempHigh = (TextView) convertView
                    .findViewById(R.id.temp_high);
            viewHolder.tempLow = (TextView) convertView
                    .findViewById(R.id.temp_low);
            viewHolder.weatherTypeTv = (TextView) convertView
                    .findViewById(R.id.weather_type_tv);
            convertView.setTag(viewHolder);
            viewHolder.addCityIv = (ImageView) convertView
                    .findViewById(R.id.add_city);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == mList.size() - 1) {
            viewHolder.addCityIv.setVisibility(View.VISIBLE);
            viewHolder.cityWeather.setVisibility(View.GONE);
        } else {
            viewHolder.addCityIv.setVisibility(View.GONE);
            viewHolder.cityWeather.setVisibility(View.VISIBLE);

            viewHolder.cityName.setText(cityManage.getCityName());
            viewHolder.weatherTypeIv.setImageResource(cityManage.getImageId());
            viewHolder.tempHigh.setText(cityManage.getTempHigh());
            viewHolder.tempLow.setText(cityManage.getTempLow());
            viewHolder.weatherTypeTv.setText(cityManage.getWeatherType());
        }
        return convertView;
    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 城市天气控件
        LinearLayout cityWeather;
        // 城市名
        TextView cityName;
        // 天气类型图片
        ImageView weatherTypeIv;
        // 高温
        TextView tempHigh;
        // 低温
        TextView tempLow;
        // 天气类型文字
        TextView weatherTypeTv;
        // 添加城市按钮
        ImageView addCityIv;
    }
}
