package com.dqc.smartblue.activity.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.dqc.qlibrary.activity.BaseAppCompatActivity;
import com.dqc.smartblue.R;


public class BaseActivity extends BaseAppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }


    public void onClick_base(View view) {
        switch (view.getId()) {
            //后退
            case R.id.navi_back:
                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

}
