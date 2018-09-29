package com.dqc.smartblue.activity.base;

import android.os.Bundle;

import com.dqc.smartblue.R;
import com.github.anzewei.parallaxbacklayout.ParallaxBack;

import androidx.core.content.ContextCompat;

@ParallaxBack
public abstract class BaseSwipeBackActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

    }
}
