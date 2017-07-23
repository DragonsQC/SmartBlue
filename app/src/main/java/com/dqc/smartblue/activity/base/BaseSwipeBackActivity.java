package com.dqc.smartblue.activity.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.dqc.qlibrary.activity.BaseSwipeBackCompatActivity;
import com.dqc.smartblue.R;


public class BaseSwipeBackActivity extends BaseSwipeBackCompatActivity {

    private TransitionMode mTransitionMode = TransitionMode.RIGHT;
    private boolean        mIsTransition   = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return mIsTransition;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return mTransitionMode;
    }

    /**
     * 设置 Activity 是否有切换动画，默认true
     *
     * @param isTransition boolean
     */
    public void setTransition(boolean isTransition) {
        mIsTransition = isTransition;
    }

    /**
     * 设置 Activity 切换的过度动画模式，不设置时默认为TransitionMode.RIGHT
     *
     * @param transitionMode TransitionMode
     */
    public void setTransitionMode(TransitionMode transitionMode) {
        mTransitionMode = transitionMode;
    }

    public void onClick_base(View view) {
        switch (view.getId()) {
            //后退
            case R.id.navi_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

}
