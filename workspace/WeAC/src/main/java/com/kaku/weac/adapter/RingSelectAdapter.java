package com.kaku.weac.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaku.weac.R;
import com.kaku.weac.bean.RingSelectItem;
import com.kaku.weac.common.WeacConstants;

import java.util.List;
import java.util.Map;

/**
 * 铃声选择适配器类
 * 
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class RingSelectAdapter extends ArrayAdapter<Map<String, String>> {
	/**
	 * activity上下文
	 */
	private final Context mContext;

	/**
	 * 当前铃声名标记位置
	 */
	private String mRingName;

	/**
	 * 铃声选择适配器
	 * 
	 * @param context
	 *            activity上下文
	 * @param list
	 *            铃声信息列表
	 * @param ringName
	 *            铃声名
	 */
	public RingSelectAdapter(Context context, List<Map<String, String>> list,
			String ringName) {
		super(context, 0, list);
		this.mContext = context;
		this.mRingName = ringName;
	}

	/**
	 * 更新选中的铃声名
	 * 
	 * @param ringName
	 *            选中的铃声名
	 */
	public void updateSelection(String ringName) {
		mRingName = ringName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_ring_select, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ringName = (TextView) convertView
					.findViewById(R.id.ring_list_display_name);
			viewHolder.markIcon = (ImageView) convertView
					.findViewById(R.id.ring_list_select_mark);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> map = getItem(position);
		viewHolder.ringName.setText(map.get(WeacConstants.RING_NAME));
		if (mRingName.equals(map.get(WeacConstants.RING_NAME))) {
			// 设置标记图标
			viewHolder.markIcon.setImageResource(R.drawable.ic_ring_mark);
			RingSelectItem.getInstance().setName(mRingName);
			RingSelectItem.getInstance()
					.setUrl(map.get(WeacConstants.RING_URL));
		} else {
			// 清除标记图标
			viewHolder.markIcon.setImageResource(0);
		}

		return convertView;

	}

	/**
	 * 保存控件实例
	 * 
	 */
	private final class ViewHolder {
		// 铃声名
		TextView ringName;
		// 标记图标
		ImageView markIcon;
	}
}
