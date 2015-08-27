package com.kaku.weac.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kaku.weac.R;
import com.kaku.weac.bean.RingSelectItem;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.AudioPlayer;
import com.kaku.weac.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 铃声选择fragment
 *
 * @author 咖枯
 * @version 1.0 2015/05/18
 */
public class RingSelectFragment extends Fragment implements OnClickListener {

    /**
     * 铃声选择界面
     */
    private ViewGroup mViewGroup;

    /**
     * 铃声种类
     */
    private ViewPager mViewPager;

    /**
     * 铃声种类集合
     */
    private List<Fragment> mFragmentList;

    /**
     * 展示系统铃声的Fragment
     */
    private SystemRingFragment mSystemRingFragment;

    /**
     * 展示录音的Fragment
     */
    private RecorderFragment mRecordFragment;

    /**
     * 展示本地铃声的Fragment
     */
    private LocalMusicFragment mLocalMusicFragment;

    /**
     * 返回按钮
     */
    private ImageView mActionCancel;

    /**
     * 保存按钮
     */
    private TextView mActionSave;

    /**
     * 铃声名
     */
    public static String sRingName;

    /**
     * 铃声地址
     */
    public static String sRingUrl;

    /**
     * 铃声界面
     */
    public static int sRingPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取得新建闹钟Activity传递过来的铃声信息参数
        Intent intent = getActivity().getIntent();
        sRingName = intent.getStringExtra(WeacConstants.RING_NAME);
        sRingUrl = intent.getStringExtra(WeacConstants.RING_URL);
        sRingPager = intent.getIntExtra(WeacConstants.RING_PAGER, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fm_ring_select, container, false);
        mViewGroup = (ViewGroup) view.findViewById(R.id.ring_select_llyt);
        // 设置页面背景
        mViewGroup.setBackgroundResource(MyUtil.getWallPaper(getActivity()));

        // 设置显示铃声列表的Fragment
        initFragment();
        // 设置ViewPager
        initViewPager(view);

        // 返回按钮
        mActionCancel = (ImageView) view.findViewById(R.id.ring_select_cancel);
        mActionCancel.setOnClickListener(this);

        // 保存按钮
        mActionSave = (TextView) view.findViewById(R.id.ring_select_save);
        mActionSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 当没有播放录音停止播放音时
        if (!AudioPlayer.sIsRecordStopMusic) {
            // 停止播放
            AudioPlayer.getInstance(getActivity()).stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 当点击返回按钮
            case R.id.ring_select_cancel:
                getActivity().finish();
                break;
            // 当点击保存按钮
            case R.id.ring_select_save:
                // 取得选中的铃声信息
                String ringName = RingSelectItem.getInstance().getName();
                String ringUrl = RingSelectItem.getInstance().getUrl();
                int ringPager = RingSelectItem.getInstance().getRingPager();

                // 保存选中的铃声信息
                SharedPreferences share = getActivity().getSharedPreferences(
                        WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                edit.putString(WeacConstants.RING_NAME, ringName);
                edit.putString(WeacConstants.RING_URL, ringUrl);
                edit.putInt(WeacConstants.RING_PAGER, ringPager);
                edit.apply();

                // 传递选中的铃声信息
                Intent i = new Intent();
                i.putExtra(WeacConstants.RING_NAME, ringName);
                i.putExtra(WeacConstants.RING_URL, ringUrl);
                i.putExtra(WeacConstants.RING_PAGER, ringPager);
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
                break;

        }

    }

    /**
     * 设置显示铃声列表的Fragment
     */
    private void initFragment() {
        mSystemRingFragment = new SystemRingFragment();
        mRecordFragment = new RecorderFragment();
        mLocalMusicFragment = new LocalMusicFragment();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(mSystemRingFragment);
        mFragmentList.add(mLocalMusicFragment);
        mFragmentList.add(mRecordFragment);
    }

    /**
     * 设置ViewPager并与开源组件【PagerSlidingTabStrip】相关联
     *
     * @param view view
     */
    private void initViewPager(View view) {
        mViewPager = (ViewPager) view
                .findViewById(R.id.fragment_ring_select_sort);
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getActivity()
                .getSupportFragmentManager()));

        // 铃声界面位置
        int currentIndex;
        // 当由编辑闹钟界面跳转时
        if (sRingPager != -1) {
            // 设置闹钟界面为保存的铃声界面位置
            mViewPager.setCurrentItem(sRingPager);
            currentIndex = sRingPager;
        } else {
            // 取得最近一次选择的闹钟界面位置信息
            SharedPreferences shares = getActivity().getSharedPreferences(
                    WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
            int position = shares.getInt(WeacConstants.RING_PAGER, 0);
            mViewPager.setCurrentItem(position);
            currentIndex = position;
        }

        PagerSlidingTabStrip strip = (PagerSlidingTabStrip) view
                .findViewById(R.id.tabstrip);
        strip.setViewPager(mViewPager);
        // 设置当前铃声界面位置，来初始化选中文字颜色
        strip.setCurrentIndex(currentIndex);
        // 普通字体
        strip.setTypeface(Typeface.DEFAULT, 0);
    }

    /**
     * 铃声种类ViewPager适配器
     */
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        /**
         * 铃声选择列表标题
         */
        private final String[] titles = {getString(R.string.system_ring),
                getString(R.string.local_music), getString(R.string.record)};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // 开源控件PagerSlidingTabStrip需要通过它获取标签标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}
