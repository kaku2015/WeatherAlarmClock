/*
 * Copyright (c) 2016. Kaku咖枯 Inc. All rights reserved.
 */
package com.kaku.weac.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kaku.weac.R;
import com.kaku.weac.util.MyUtil;
import com.umeng.fb.fragment.FeedbackFragment;

/**
 * 意见反馈Activity
 *
 * @author 咖枯
 * @version 1.0 2016/2/15
 */
public class FeedbackActivity extends BaseActivity {
    FeedbackFragment mFeedbackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        ViewGroup background = (ViewGroup) findViewById(R.id.background);
        MyUtil.setBackground(background, this);

        String conversationId = getIntent().getStringExtra(FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID);
        mFeedbackFragment = FeedbackFragment.newInstance(conversationId);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mFeedbackFragment)
                .commit();

        assignViews();
    }

    private void assignViews() {
        ImageView actionBack = (ImageView) findViewById(R.id.action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
