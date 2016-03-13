/*
 * © 2016 咖枯. All Rights Reserved.
 */
package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kaku.weac.R;
import com.kaku.weac.adapter.RingSelectAdapter;
import com.kaku.weac.bean.RingSelectItem;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 显示本地音乐列表的Fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class LocalMusicFragment extends BaseListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * 保存铃声信息的Adapter
     */
    RingSelectAdapter mLocalMusicAdapter;

    /**
     * loader Id
     */
    private static final int LOADER_ID = 1;

    /**
     * 铃声选择位置
     */
    private int mPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_ring_local_music, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 管理cursor
        LoaderManager loaderManager = getLoaderManager();
        // 注册Loader
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Map<String, String> map = mLocalMusicAdapter.getItem(position);
        // 取得铃声名
        String ringName = map.get(WeacConstants.RING_NAME);
        // 取得播放地址
        String ringUrl = map.get(WeacConstants.RING_URL);
        // 更新当前铃声选中的位置
        mLocalMusicAdapter.updateSelection(ringName);
        // 更新适配器刷新铃声列表显示
        mLocalMusicAdapter.notifyDataSetChanged();
        // 设置最后一次选中的铃声选择界面位置为本地音乐界面
        RingSelectItem.getInstance().setRingPager(1);

        // 播放音频文件
        AudioPlayer.getInstance(getActivity()).play(ringUrl, false, false);

        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.fragment_ring_select_sort);
        PagerAdapter f = pager.getAdapter();
        SystemRingFragment systemRingFragment = (SystemRingFragment) f.instantiateItem(pager, 0);
        RecorderFragment recorderFragment = (RecorderFragment) f.instantiateItem(pager, 2);
        // 取消系统铃声选中标记
        if (systemRingFragment.mSystemRingAdapter != null) {
            systemRingFragment.mSystemRingAdapter.updateSelection("");
            systemRingFragment.mSystemRingAdapter.notifyDataSetChanged();
        }
        // 取消录音选中标记
        if (recorderFragment.mRecorderAdapter != null) {
            recorderFragment.mRecorderAdapter.updateSelection("");
            recorderFragment.mRecorderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // 查询外部存储音频文件
        return new CursorLoader(getActivity(),
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA}, null, null,
                MediaStore.Audio.Media.DISPLAY_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_ID:
                // 当为编辑闹钟状态时，铃声名为闹钟单例铃声名
                String ringName1;
                if (RingSelectFragment.sRingName != null) {
                    ringName1 = RingSelectFragment.sRingName;
                } else {
                    SharedPreferences share = getActivity().getSharedPreferences(
                            WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                    // 当为新建闹钟状态时，铃声名为最近一次选择保存的铃声名
                    ringName1 = share.getString(WeacConstants.RING_NAME, "");
                }
                // 保存铃声信息的List
                List<Map<String, String>> list = new ArrayList<>();
                // 过滤重复音频文件的Set
                HashSet<String> set = new HashSet<>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                        .moveToNext()) {
                    // 音频文件名
                    String ringName = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    // 取得音频文件的地址
                    String ringUrl = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    if (ringName != null) {
                        // 当过滤集合里不存在此音频文件，并且文件扩展名不为[.amr]，并且不是默认铃声
                        if (!set.contains(ringName)
                                && !ringUrl.contains("/WeaAlarmClock/audio/record")
                                && !ringName.equals("record_start.mp3")
                                && !ringName.equals("record_stop.mp3")
                                && !ringName
                                .equals("ring_weac_alarm_clock_default.mp3")) {
                            // 添加音频文件到列表过滤同名文件
                            set.add(ringName);
                            // 去掉音频文件的扩展名
                            ringName = MyUtil.removeEx(ringName);
                            Map<String, String> map = new HashMap<>();
                            map.put(WeacConstants.RING_NAME, ringName);
                            map.put(WeacConstants.RING_URL, ringUrl);
                            list.add(map);
                            // 当列表中存在与保存的铃声名一致时，设置该列表的显示位置
                            if (ringName.equals(ringName1)) {
                                mPosition = list.size() - 1;
                                RingSelectItem.getInstance().setRingPager(1);
                            }
                        }
                    }
                }
                mLocalMusicAdapter = new RingSelectAdapter(getActivity(), list, ringName1);
                setListAdapter(mLocalMusicAdapter);
                setSelection(mPosition);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {

    }

}
