package com.dqc.smartblue.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseSwipeBackBindingActivity extends BaseSwipeBackActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateBinding();
    }

    public abstract void onCreateBinding();

}
