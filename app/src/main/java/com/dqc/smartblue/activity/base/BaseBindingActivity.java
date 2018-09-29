package com.dqc.smartblue.activity.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

public abstract class BaseBindingActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateBinding();
    }

    public abstract void onCreateBinding();

}
