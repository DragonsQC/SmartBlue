package com.dqc.smartblue.activity;

import android.app.Application;

import com.dqc.smartblue.activity.base.BaseViewModel;

import androidx.annotation.NonNull;

public class MainViewModel extends BaseViewModel<MainActivity> {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }
}
