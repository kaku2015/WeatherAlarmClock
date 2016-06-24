/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.kaku.weac.activities;

import android.support.v4.app.Fragment;
import android.view.MotionEvent;

import com.kaku.weac.R;
import com.kaku.weac.fragment.WeatherAlarmFragment;

/**
 * 天气预警activity
 *
 * @author 咖枯
 * @version 1.0 2015/11/29
 */
public class WeatherAlarmActivity extends SingleFragmentDialogActivity {

    @Override
    protected Fragment createFragment() {
        return new WeatherAlarmFragment();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction() ||
                MotionEvent.ACTION_DOWN == event.getAction()) {
            onFinish();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onFinish();

    }

    /**
     * 完成退出
     */
    private void onFinish() {
        finish();
        overridePendingTransition(0, R.anim.zoomout);
    }
}