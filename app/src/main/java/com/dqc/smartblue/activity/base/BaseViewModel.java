package com.dqc.smartblue.activity.base;

import android.app.Activity;
import android.app.Application;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dqc.qlibrary.activity.IBaseViewModel;
import com.dqc.smartblue.R;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

/**
 * 基础 ViewModel
 *
 * @param <Act> Activity
 * @author DragonsQC
 */
public abstract class BaseViewModel<Act extends Activity> extends AndroidViewModel implements IBaseViewModel {

    public Act mActivity;

    public MaterialDialog mDialog = null;


    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public void setActivity(Act activity) {
        mActivity = activity;
    }


    public void showLoading() {
        if (mDialog != null) {
            mDialog = new MaterialDialog.Builder(mActivity)
                    .content(mActivity.getString(R.string.net_loading))
                    .cancelable(false)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }

    }

    public void dismissLoading() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mDialog = null;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

}
