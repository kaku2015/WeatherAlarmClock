package com.kaku.weac.fragment;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ToggleButton;

import com.kaku.weac.R;
import com.kaku.weac.activities.NapEditActivity;
import com.kaku.weac.activities.RingSelectActivity;
import com.kaku.weac.bean.AlarmClock;
import com.kaku.weac.common.WeacConstants;
import com.kaku.weac.util.MyUtil;

/**
 * 新建闹钟fragment
 * 
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class AlarmClockNewFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener {

	/**
	 * 铃声选择按钮的requestCode
	 */
	private static final int REQUEST_RING_SELECT = 1;

	/**
	 * 小睡按钮的requestCode
	 */
	private static final int REQUEST_NAP_EDIT = 2;

	/**
	 * 闹钟时间选择器
	 */
	private TimePicker mTimePicker;

	/**
	 * 新建闹钟界面
	 */
	private ViewGroup mViewGroup;

	/**
	 * 操作栏取消按钮
	 */
	private ImageView mCancelAction;

	/**
	 * 操作栏确定按钮
	 */
	private ImageView mAcceptAction;

	/**
	 * 操作栏标题
	 */
	private TextView mActionTitle;

	/**
	 * 闹钟实例
	 */
	private AlarmClock mAlarmClock;

	/**
	 * 下次响铃时间提示控件
	 */
	private TextView mTimePickerTv;

	/**
	 * 响铃倒计时
	 */
	private String countDown;

	/**
	 * 周一按钮
	 */
	private ToggleButton mMonday;

	/**
	 * 周二按钮
	 */
	private ToggleButton mTuesday;

	/**
	 * 周三按钮
	 */
	private ToggleButton mWednesday;

	/**
	 * 周四按钮
	 */
	private ToggleButton mThursday;

	/**
	 * 周五按钮
	 */
	private ToggleButton mFriday;

	/**
	 * 周六按钮
	 */
	private ToggleButton mSaturday;

	/**
	 * 周日按钮
	 */
	private ToggleButton mSunday;

	/**
	 * 周一按钮状态，默认未选中
	 */
	private Boolean isMondayChecked = false;

	/**
	 * 周二按钮状态，默认未选中
	 */
	private Boolean isTuesdayChecked = false;

	/**
	 * 周三按钮状态，默认未选中
	 */
	private Boolean isWednesdayChecked = false;

	/**
	 * 周四按钮状态，默认未选中
	 */
	private Boolean isThursdayChecked = false;

	/**
	 * 周五按钮状态，默认未选中
	 */
	private Boolean isFridayChecked = false;

	/**
	 * 周六按钮状态，默认未选中
	 */
	private Boolean isSaturdayChecked = false;

	/**
	 * 周日按钮状态，默认未选中
	 */
	private Boolean isSundayChecked = false;

	/**
	 * 保存重复描述信息String
	 */
	private StringBuilder mRepeatStr;

	/**
	 * 重复描述组件
	 */
	private TextView mRepeatDescribe;

	/**
	 * 按键值顺序存放重复描述信息
	 */
	private TreeMap<Integer, String> mMap;

	/**
	 * 标签描述控件
	 */
	private EditText mTag;

	/**
	 * 铃声控件
	 */
	private ViewGroup mRing;

	/**
	 * 铃声描述
	 */
	private TextView mRingDescribe;

	/**
	 * 音量控制seekBar
	 */
	private SeekBar mVolumeSkBar;

	/**
	 * 振动
	 */
	private ToggleButton mVibrateBtn;

	/**
	 * 小睡
	 */
	private ToggleButton mNapBtn;

	/**
	 * 小睡组件
	 */
	private ViewGroup mNap;

	/**
	 * 天气提示
	 */
	private ToggleButton mWeaPromptBtn;

	/**
	 * 上一次铃声选择或小睡设置点击时间
	 */
	private long mLastClickTime = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAlarmClock = new AlarmClock();
		// 闹钟默认开启
		mAlarmClock.setOnOff(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fm_alarm_clock_new_edit,
				container, false);
		// 设置界面背景
		setBackground(view);
		// 初始化操作栏
		initActionBar(view);
		// 初始化时间选择
		initTimeSelect(view);
		// 初始化重复
		initRepeat(view);
		// 初始化标签
		initTag(view);
		// 初始化铃声
		initRing(view);
		// 初始化音量
		initVolume(view);
		// 初始化振动、小睡、天气提示
		initToggleBotton(view);
		return view;
	}

	private void initVolume(View view) {
		// 音量选择SeekBar
		mVolumeSkBar = (SeekBar) view.findViewById(R.id.volumn_sk);
		mVolumeSkBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 保存设置的音量
				mAlarmClock.setVolume(seekBar.getProgress());

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		// 初始化闹钟实例的音量
		mAlarmClock.setVolume(mVolumeSkBar.getProgress());

	}

	/**
	 * 设置界面背景
	 * 
	 * @param view
	 */
	private void setBackground(View view) {
		mViewGroup = (ViewGroup) view.findViewById(R.id.new_alarm_clock_llyt);
		// 设置页面背景
		mViewGroup.setBackgroundResource(MyUtil.getWallPaper(getActivity()));
	}

	/**
	 * 设置操作栏按钮
	 * 
	 * @param view
	 */
	private void initActionBar(View view) {
		// 取消按钮
		mCancelAction = (ImageView) view.findViewById(R.id.action_cancel);
		mCancelAction.setOnClickListener(this);
		// 确定按钮
		mAcceptAction = (ImageView) view.findViewById(R.id.action_accept);
		mAcceptAction.setOnClickListener(this);
		// 标题
		mActionTitle = (TextView) view.findViewById(R.id.action_title);
		mActionTitle.setText(getString(R.string.new_alarm_clock));
	}

	/**
	 * 设置时间选择
	 * 
	 * @param view
	 */
	private void initTimeSelect(View view) {
		// 下次响铃提示
		mTimePickerTv = (TextView) view.findViewById(R.id.time_picker_tv);
		countDown = getResources()
				.getString(R.string.countdown_day_hour_minute);
		// 设置下次响铃时间提示内容
		mTimePickerTv.setText(String.format(countDown, 1, 0, 0));

		// 闹钟时间选择器
		mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
		mTimePicker.setIs24HourView(true);
		int currentHour = mTimePicker.getCurrentHour();
		int currentMinute = mTimePicker.getCurrentMinute();
		// 初始化闹钟实例的小时
		mAlarmClock.setHour(currentHour);
		// 初始化闹钟实例的分钟
		mAlarmClock.setMinute(currentMinute);

		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// 保存闹钟实例的小时
				mAlarmClock.setHour(hourOfDay);
				// 保存闹钟实例的分钟
				mAlarmClock.setMinute(minute);
				// 计算倒计时显示
				displayCountDown();
			}

		});
	}

	/**
	 * 设置重复信息
	 * 
	 * @param view
	 */
	private void initRepeat(View view) {
		// 初始化闹钟实例的重复
		mAlarmClock.setRepeat(getString(R.string.repeat_once));
		mAlarmClock.setWeeks(null);

		// 重复描述
		mRepeatDescribe = (TextView) view.findViewById(R.id.repeat_describe);
		mRepeatStr = new StringBuilder();
		mMap = new TreeMap<Integer, String>();

		// 周选择按钮
		mMonday = (ToggleButton) view.findViewById(R.id.tog_btn_monday);
		mTuesday = (ToggleButton) view.findViewById(R.id.tog_btn_tuesday);
		mWednesday = (ToggleButton) view.findViewById(R.id.tog_btn_wednesday);
		mThursday = (ToggleButton) view.findViewById(R.id.tog_btn_thursday);
		mFriday = (ToggleButton) view.findViewById(R.id.tog_btn_friday);
		mSaturday = (ToggleButton) view.findViewById(R.id.tog_btn_saturday);
		mSunday = (ToggleButton) view.findViewById(R.id.tog_btn_sunday);

		mMonday.setOnCheckedChangeListener(this);
		mTuesday.setOnCheckedChangeListener(this);
		mWednesday.setOnCheckedChangeListener(this);
		mThursday.setOnCheckedChangeListener(this);
		mFriday.setOnCheckedChangeListener(this);
		mSaturday.setOnCheckedChangeListener(this);
		mSunday.setOnCheckedChangeListener(this);
	}

	/**
	 * 设置标签
	 * 
	 * @param view
	 */
	private void initTag(View view) {
		// 初始化闹钟实例的标签
		mAlarmClock.setTag(getString(R.string.alarm_clock));

		// 标签
		mTag = (EditText) view.findViewById(R.id.tag_edit_text);
		mTag.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.toString().equals("")) {
					mAlarmClock.setTag(s.toString());
				} else {
					mAlarmClock.setTag(getString(R.string.alarm_clock));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 设置铃声
	 * 
	 * @param view
	 */
	private void initRing(View view) {
		// 取得铃声选择配置信息
		SharedPreferences share = getActivity().getSharedPreferences(
				WeacConstants.EXTRA_WEAC_SHARE, Activity.MODE_PRIVATE);
		String ringName = share.getString(WeacConstants.RING_NAME,
				getString(R.string.default_ring));
		String ringUrl = share.getString(WeacConstants.RING_URL,
				WeacConstants.DEFAULT_RING_URL);

		// 初始化闹钟实例的铃声名
		mAlarmClock.setRingName(ringName);
		// 初始化闹钟实例的铃声播放地址
		mAlarmClock.setRingUrl(ringUrl);
		// 铃声
		mRing = (ViewGroup) view.findViewById(R.id.ring_llyt);
		mRingDescribe = (TextView) view.findViewById(R.id.ring_describe);
		mRingDescribe.setText(ringName);
		mRing.setOnClickListener(this);
	}

	/**
	 * 设置振动、小睡、天气提示
	 * 
	 * @param view
	 */
	private void initToggleBotton(View view) {
		// 初始化闹钟实例的振动，默认振动
		mAlarmClock.setVibrate(true);

		// 初始化闹钟实例的小睡信息
		// 默认小睡
		mAlarmClock.setNap(true);
		// 小睡间隔10分钟
		mAlarmClock.setNapInterval(10);
		// 小睡3次
		mAlarmClock.setNapTimes(3);

		// 初始化闹钟实例的天气提示，默认开启
		mAlarmClock.setWeaPrompt(true);

		// 振动
		mVibrateBtn = (ToggleButton) view.findViewById(R.id.vibrate_btn);

		// 小睡
		mNapBtn = (ToggleButton) view.findViewById(R.id.nap_btn);
		// 小睡组件
		mNap = (ViewGroup) view.findViewById(R.id.nap_llyt);
		mNap.setOnClickListener(this);

		// 天气提示
		mWeaPromptBtn = (ToggleButton) view.findViewById(R.id.wea_prompt_btn);

		mVibrateBtn.setOnCheckedChangeListener(this);
		mNapBtn.setOnCheckedChangeListener(this);
		mWeaPromptBtn.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 当点击取消按钮
		case R.id.action_cancel:
			getActivity().finish();
			drawAnimation();
			break;
		// 当点击确认按钮
		case R.id.action_accept:
			Intent data = new Intent();
			data.putExtra(WeacConstants.ALARM_CLOCK, mAlarmClock);
			getActivity().setResult(Activity.RESULT_OK, data);
			getActivity().finish();
			drawAnimation();
			break;
		// 当点击铃声
		case R.id.ring_llyt:
			// 不响应重复点击
			if (isFastDoubleClick()) {
				return;
			}
			// 铃声选择界面
			Intent i = new Intent(getActivity(), RingSelectActivity.class);
			startActivityForResult(i, REQUEST_RING_SELECT);
			break;
		// 当点击小睡
		case R.id.nap_llyt:
			// 不响应重复点击
			if (isFastDoubleClick()) {
				return;
			}
			// 小睡界面
			Intent nap = new Intent(getActivity(), NapEditActivity.class);
			nap.putExtra(WeacConstants.NAP_INTERVAL,
					mAlarmClock.getNapInterval());
			nap.putExtra(WeacConstants.NAP_TIMES, mAlarmClock.getNapTimes());
			startActivityForResult(nap, REQUEST_NAP_EDIT);
			break;
		}
	}

	/**
	 * 结束新建闹钟界面时开启渐变缩小效果动画
	 */
	private void drawAnimation() {
		getActivity().overridePendingTransition(0, R.anim.zoomout);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		// 铃声选择界面返回
		case REQUEST_RING_SELECT:
			// 铃声名
			String name = data.getStringExtra(WeacConstants.RING_NAME);
			// 铃声地址
			String url = data.getStringExtra(WeacConstants.RING_URL);
			// 铃声界面
			int ringPager = data.getIntExtra(WeacConstants.RING_PAGER, 0);

			mRingDescribe.setText(name);

			mAlarmClock.setRingName(name);
			mAlarmClock.setRingUrl(url);
			mAlarmClock.setRingPager(ringPager);
			break;
		// 小睡编辑界面返回
		case REQUEST_NAP_EDIT:
			// 小睡间隔
			int napInterval = data.getIntExtra(WeacConstants.NAP_INTERVAL, 10);
			// 小睡次数
			int napTimes = data.getIntExtra(WeacConstants.NAP_TIMES, 3);
			mAlarmClock.setNapInterval(napInterval);
			mAlarmClock.setNapTimes(napTimes);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		// 选中周一
		case R.id.tog_btn_monday:
			if (isChecked) {
				isMondayChecked = true;
				mMap.put(1, getString(R.string.one_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isMondayChecked = false;
				mMap.remove(1);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周二
		case R.id.tog_btn_tuesday:
			if (isChecked) {
				isTuesdayChecked = true;
				mMap.put(2, getString(R.string.two_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isTuesdayChecked = false;
				mMap.remove(2);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周三
		case R.id.tog_btn_wednesday:
			if (isChecked) {
				isWednesdayChecked = true;
				mMap.put(3, getString(R.string.three_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isWednesdayChecked = false;
				mMap.remove(3);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周四
		case R.id.tog_btn_thursday:
			if (isChecked) {
				isThursdayChecked = true;
				mMap.put(4, getString(R.string.four_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isThursdayChecked = false;
				mMap.remove(4);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周五
		case R.id.tog_btn_friday:
			if (isChecked) {
				isFridayChecked = true;
				mMap.put(5, getString(R.string.five_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isFridayChecked = false;
				mMap.remove(5);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周六
		case R.id.tog_btn_saturday:
			if (isChecked) {
				isSaturdayChecked = true;
				mMap.put(6, getString(R.string.six_h));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isSaturdayChecked = false;
				mMap.remove(6);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 选中周日
		case R.id.tog_btn_sunday:
			if (isChecked) {
				isSundayChecked = true;
				mMap.put(7, getString(R.string.day));
				setRepeatDescribe();
				displayCountDown();
			} else {
				isSundayChecked = false;
				mMap.remove(7);
				setRepeatDescribe();
				displayCountDown();
			}
			break;
		// 振动
		case R.id.vibrate_btn:
			if (isChecked) {
				MyUtil.vibrate(getActivity());
				mAlarmClock.setVibrate(true);
			} else {
				mAlarmClock.setVibrate(false);
			}
			break;
		// 小睡
		case R.id.nap_btn:
			if (isChecked) {
				mAlarmClock.setNap(true);
			} else {
				mAlarmClock.setNap(false);
			}
			break;
		// 天气提示
		case R.id.wea_prompt_btn:
			if (isChecked) {
				mAlarmClock.setWeaPrompt(true);
			} else {
				mAlarmClock.setWeaPrompt(false);
			}
			break;
		}

	}

	/**
	 * 设置重复描述的内容
	 */
	private void setRepeatDescribe() {
		// 全部选中
		if (isMondayChecked & isTuesdayChecked & isWednesdayChecked
				& isThursdayChecked & isFridayChecked & isSaturdayChecked
				& isSundayChecked) {
			mRepeatDescribe.setText(getResources()
					.getString(R.string.every_day));
			mAlarmClock.setRepeat(getString(R.string.every_day));
			// 响铃周期
			mAlarmClock.setWeeks("2,3,4,5,6,7,1");
			// 周一到周五全部选中
		} else if (isMondayChecked & isTuesdayChecked & isWednesdayChecked
				& isThursdayChecked & isFridayChecked & !isSaturdayChecked
				& !isSundayChecked) {
			mRepeatDescribe.setText(getString(R.string.week_day));
			mAlarmClock.setRepeat(getString(R.string.week_day));
			mAlarmClock.setWeeks("2,3,4,5,6");
			// 周六、日全部选中
		} else if (!isMondayChecked & !isTuesdayChecked & !isWednesdayChecked
				& !isThursdayChecked & !isFridayChecked & isSaturdayChecked
				& isSundayChecked) {
			mRepeatDescribe.setText(getString(R.string.week_end));
			mAlarmClock.setRepeat(getString(R.string.week_end));
			mAlarmClock.setWeeks("7,1");
			// 没有选中任何一个
		} else if (!isMondayChecked & !isTuesdayChecked & !isWednesdayChecked
				& !isThursdayChecked & !isFridayChecked & !isSaturdayChecked
				& !isSundayChecked) {
			mRepeatDescribe.setText(getString(R.string.repeat_once));
			mAlarmClock.setRepeat(getResources()
					.getString(R.string.repeat_once));
			mAlarmClock.setWeeks(null);

		} else {
			mRepeatStr.setLength(0);
			mRepeatStr.append(getString(R.string.week));
			Collection<String> col = mMap.values();
			Iterator<String> i = col.iterator();
			while (i.hasNext()) {
				mRepeatStr.append(i.next()).append("、");
			}
			// 去掉最后一个"、"
			mRepeatStr.setLength(mRepeatStr.length() - 1);
			mRepeatDescribe.setText(mRepeatStr.toString());
			mAlarmClock.setRepeat(mRepeatStr.toString());

			mRepeatStr.setLength(0);
			if (isMondayChecked) {
				mRepeatStr.append("2,");
			}
			if (isTuesdayChecked) {
				mRepeatStr.append("3,");
			}
			if (isWednesdayChecked) {
				mRepeatStr.append("4,");
			}
			if (isThursdayChecked) {
				mRepeatStr.append("5,");
			}
			if (isFridayChecked) {
				mRepeatStr.append("6,");
			}
			if (isSaturdayChecked) {
				mRepeatStr.append("7,");
			}
			if (isSundayChecked) {
				mRepeatStr.append("1,");
			}
			mAlarmClock.setWeeks(mRepeatStr.toString());
		}

	}

	/**
	 * 计算显示倒计时信息
	 * 
	 */
	protected void displayCountDown() {
		// 取得下次响铃时间
		long nextTime = MyUtil.calculateNextTime(mAlarmClock.getHour(),
				mAlarmClock.getMinute(), mAlarmClock.getWeeks());
		// 系统时间
		long now = System.currentTimeMillis();
		// 距离下次响铃间隔毫秒数
		long ms = nextTime - now;

		// 单位秒
		int ss = 1000;
		// 单位分
		int mm = ss * 60;
		// 单位小时
		int hh = mm * 60;
		// 单位天
		int dd = hh * 24;

		// 不计算秒，故响铃间隔加一分钟
		ms += mm;
		// 剩余天数
		long remainDay = ms / dd;
		// 剩余小时
		long remainHour = (ms - remainDay * dd) / hh;
		// 剩余分钟
		long remainMinute = (ms - remainDay * dd - remainHour * hh) / mm;

		// 当剩余天数大于0时显示【X天X小时X分】格式
		if (remainDay > 0) {
			countDown = getString(R.string.countdown_day_hour_minute);
			mTimePickerTv.setText(String.format(countDown, remainDay,
					remainHour, remainMinute));
			// 当剩余小时大于0时显示【X小时X分】格式
		} else if (remainHour > 0) {
			countDown = getResources()
					.getString(R.string.countdown_hour_minute);
			mTimePickerTv.setText(String.format(countDown, remainHour,
					remainMinute));
		} else {
			// 当剩余分钟不等于0时显示【X分钟】格式
			if (remainMinute != 0) {
				countDown = getString(R.string.countdown_minute);
				mTimePickerTv.setText(String.format(countDown, remainMinute));
				// 当剩余分钟等于0时，显示【1天0小时0分】
			} else {
				countDown = getString(R.string.countdown_day_hour_minute);
				mTimePickerTv.setText(String.format(countDown, 1, 0, 0));
			}

		}
	}

	/**
	 * 是否连续铃声选择多次
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