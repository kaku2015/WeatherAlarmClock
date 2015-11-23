package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kaku.weac.R;
import com.kaku.weac.activities.AlarmClockEditActivity;
import com.kaku.weac.activities.AlarmClockNewActivity;
import com.kaku.weac.adapter.AlarmClockAdapter;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.db.TabAlarmClockOperate;
import com.kaku.weac.db.WeacDBMetaData;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 闹钟fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class AlarmClockFragment extends Fragment implements OnClickListener,
        LoaderCallbacks<Cursor> {

    /**
     * 新建闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_NEW = 1;

    /**
     * 修改闹钟的requestCode
     */
    private static final int REQUEST_ALARM_CLOCK_EDIT = 2;

    /**
     * 闹钟列表
     */
    private ListView mListView;

    /**
     * 保存闹钟信息的list
     */
    private List<AlarmClock> mAlarmClockList;

    /**
     * 保存闹钟信息的adapter
     */
    private AlarmClockAdapter mAdapter;

    /**
     * 操作栏编辑按钮
     */
    private ImageView mEditAction;

    /**
     * 操作栏编辑完成按钮
     */
    private ImageView mAcceptAction;

    /**
     * 新建编辑闹钟按钮点击时间
     */
    private long mLastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmClockList = new ArrayList<>();
        mAdapter = new AlarmClockAdapter(getActivity(), mAlarmClockList);
        // 注册Loader
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_alarm_clock, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view);
        // 当录音List内容为空时显示的内容控件
        LinearLayout emptyView = (LinearLayout) view
                .findViewById(R.id.alarm_clock_empty);
        // 设置录音List内容为空时的视图
        mListView.setEmptyView(emptyView);
        // 注册上下文菜单
        registerForContextMenu(mListView);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 不响应重复点击
                if (isFastDoubleClick()) {
                    return;
                }
                AlarmClock alarmClock = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),
                        AlarmClockEditActivity.class);
                intent.putExtra(WeacConstants.ALARM_CLOCK, alarmClock);
                // 开启编辑闹钟界面
                startActivityForResult(intent, REQUEST_ALARM_CLOCK_EDIT);
                // 启动移动进入效果动画
                getActivity().overridePendingTransition(R.anim.move_in_bottom,
                        0);

            }
        });
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // 显示删除，完成按钮，隐藏修改按钮
                displayDeleteAccept();
                return true;
            }
        });

        // 新建闹钟
        // 操作栏新建按钮
        ImageView newAction = (ImageView) view.findViewById(R.id.action_new);
        newAction.setOnClickListener(this);

        // 编辑闹钟
        mEditAction = (ImageView) view.findViewById(R.id.action_edit);
        mEditAction.setOnClickListener(this);

        // 完成按钮
        mAcceptAction = (ImageView) view.findViewById(R.id.action_accept);
        mAcceptAction.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_new:
                // 不响应重复点击
                if (isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        AlarmClockNewActivity.class);
                // 开启新建闹钟界面
                startActivityForResult(intent, REQUEST_ALARM_CLOCK_NEW);
                // 启动渐变放大效果动画
                getActivity().overridePendingTransition(R.anim.zoomin, 0);
                break;
            case R.id.action_edit:
                // 当列表内容为空时禁止响应编辑事件
                if (mListView.getChildCount() == 0) {
                    return;
                }
                // 显示删除，完成按钮，隐藏修改按钮
                displayDeleteAccept();
                break;
            case R.id.action_accept:
                // 隐藏删除，完成按钮,显示修改按钮
                hideDeleteAccept();
                break;
        }

    }

    /**
     * 显示删除，完成按钮，隐藏修改按钮
     */
    private void displayDeleteAccept() {
        mAdapter.displayDeleteButton(true);
        mAdapter.notifyDataSetChanged();
        mEditAction.setVisibility(View.GONE);
        mAcceptAction.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏删除，完成按钮,显示修改按钮
     */
    private void hideDeleteAccept() {
        mAdapter.displayDeleteButton(false);
        mAdapter.notifyDataSetChanged();
        mAcceptAction.setVisibility(View.GONE);
        mEditAction.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        AlarmClock ac = data
                .getParcelableExtra(WeacConstants.ALARM_CLOCK);
        switch (requestCode) {
            // 新建闹钟
            case REQUEST_ALARM_CLOCK_NEW:
                // 插入新闹钟数据
                TabAlarmClockOperate.getInstance(getActivity()).insert(ac);
                break;
            // 修改闹钟
            case REQUEST_ALARM_CLOCK_EDIT:
                // 更新闹钟数据
                TabAlarmClockOperate.getInstance(getActivity()).update(ac);
                break;

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(getActivity(), WeacDBMetaData.CONTENT_URI,
                null, null, null, WeacDBMetaData.SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            return;
        }
        mAlarmClockList.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(WeacDBMetaData.AC_ID));
            int hour = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_HOUR));
            int minute = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_MINUTE));
            String weeks = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_WEEKS));
            String repeat = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_REPEAT));
            String tag = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_TAG));
            String ringName = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_NAME));
            String ringUrl = cursor.getString(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_URL));
            int ringPager = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_RING_PAGER));
            int volume = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_VOLUME));
            boolean vibrate = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_VIBRATE)) == 1;
            boolean nap = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP)) == 1;
            int napInterval = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP_INTERVAL));
            int napTimes = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_NAP_TIMES));
            boolean weaPrompt = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_WEA_PROMPT)) == 1;
            boolean onOff = cursor.getInt(cursor
                    .getColumnIndex(WeacDBMetaData.AC_ON_OFF)) == 1;
            AlarmClock alarmClock = new AlarmClock(id, hour, minute, repeat,
                    weeks, tag, ringName, ringUrl, ringPager, volume, vibrate,
                    nap, napInterval, napTimes, weaPrompt, onOff);
            mAlarmClockList.add(alarmClock);
            // 当闹钟为开时刷新开启闹钟
            if (onOff) {
                MyUtil.startAlarmClock(getActivity(), alarmClock);
            }

        }
        // 列表为空时不显示删除，完成按钮
        if (mAlarmClockList.size() == 0) {
            mAcceptAction.setVisibility(View.GONE);
            mEditAction.setVisibility(View.VISIBLE);
            mAdapter.displayDeleteButton(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

    @Override
    public void onPause() {
        super.onPause();
        // 隐藏删除，完成按钮,显示修改按钮
        hideDeleteAccept();
    }

    /**
     * 是否连续点击新建或者编辑闹钟按钮多次
     *
     * @return 点击多次与否
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        // 初次点击响应事件
        if (mLastClickTime == 0) {
            mLastClickTime = time;
            return false;
        }
        long timeD = time - mLastClickTime;
        // 间隔一秒以内重复点击不多次响应
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }
}
