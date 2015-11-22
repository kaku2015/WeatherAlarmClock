package com.kaku.weac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaku.weac.Listener.DBObserverListener;
import com.kaku.weac.R;
import com.kaku.weac.bean.CityManage;
import com.kaku.weac.db.WeatherDBOperate;

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
     * 进度条显示位置
     */
    private int mPosition = -1;

    /**
     * 显示进度条
     *
     * @param position 位置
     */
    public void displayProgressBar(int position) {
        mPosition = position;
    }

    /**
     * db数据观察者
     */
    private DBObserverListener mDBObserverListener;

    public void setDBObserverListener(DBObserverListener DBObserverListener) {
        mDBObserverListener = DBObserverListener;
    }

    /**
     * 删除城市按钮状态
     */
    private boolean mIsVisible;

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

    /**
     * 更新删除城市按钮状态
     *
     * @param isVisible 删除按钮是否可见
     */
    public void setCityDeleteButton(boolean isVisible) {
        mIsVisible = isVisible;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CityManage cityManage = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.gv_city_manage, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.background = (ViewGroup) convertView.findViewById(R.id.background);
            viewHolder.cityWeather = (ViewGroup) convertView
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
            viewHolder.addCityIv = (ImageView) convertView
                    .findViewById(R.id.add_city);
            convertView.setTag(viewHolder);
            viewHolder.deleteCityBtn = (ImageView) convertView.findViewById(R.id.city_delete_btn);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 当显示删除按钮并且不是添加城市按钮
        if (mIsVisible && (position != mList.size() - 1)) {
            viewHolder.deleteCityBtn.setVisibility(View.VISIBLE);
            viewHolder.deleteCityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeatherDBOperate.getInstance().deleteCityManage(cityManage);
                    mList.remove(cityManage);
                    notifyDataSetChanged();
                }
            });
            // 不加这个编辑状态会错乱
            viewHolder.background.setVisibility(View.VISIBLE);
            // 当显示删除按钮并且是添加城市按钮
        } else if (mIsVisible && (position == mList.size() - 1) && mList.size() != 1) {
            // 隐藏添加城市按钮
            viewHolder.background.setVisibility(View.GONE);
            // 当不显示删除按钮并且不是添加城市按钮
        } else if (!mIsVisible && (position != mList.size() - 1)) {
            // 隐藏删除按钮
            viewHolder.deleteCityBtn.setVisibility(View.GONE);
            // 当不显示删除按钮并且是添加城市按钮
        } else if (!mIsVisible && (position == mList.size() - 1)) {
            // 显示添加城市按钮
            viewHolder.background.setVisibility(View.VISIBLE);

        }

        // 当为最后一项（添加城市按钮）
        if (position == mList.size() - 1) {
            viewHolder.addCityIv.setVisibility(View.VISIBLE);
            viewHolder.cityWeather.setVisibility(View.INVISIBLE);
            viewHolder.deleteCityBtn.setVisibility(View.GONE);

            viewHolder.progressBar.setVisibility(View.GONE);
        } else {
            viewHolder.addCityIv.setVisibility(View.GONE);
            viewHolder.cityWeather.setVisibility(View.VISIBLE);
            viewHolder.cityName.setText(cityManage.getCityName());
            viewHolder.weatherTypeIv.setImageResource(cityManage.getImageId());
            viewHolder.tempHigh.setText(cityManage.getTempHigh());
            viewHolder.tempLow.setText(cityManage.getTempLow());
            viewHolder.weatherTypeTv.setText(cityManage.getWeatherType());
        }

        // 当列表为空（仅有添加按钮）
        if (mIsVisible && (mList.size() == 1)) {
            mIsVisible = false;
            mDBObserverListener.onDBDataChanged();
        }

        // 当显示进度条并且不是添加按钮
        if ((position == mPosition) && !(position == mList.size() - 1)) {
            viewHolder.cityWeather.setVisibility(View.INVISIBLE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            // 当不显示进度条并且不是添加按钮
        } else if ((position != mPosition) && !(position == mList.size() - 1)) {
            viewHolder.cityWeather.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.GONE);
        }
        return convertView;
    }

    /**
     * 保存控件实例
     */
    private final class ViewHolder {
        // 城市天气控件
        ViewGroup cityWeather;
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
        // 删除城市按钮
        ImageView deleteCityBtn;
        // 控件布局
        ViewGroup background;
        // 进度条
        ProgressBar progressBar;
    }
}
