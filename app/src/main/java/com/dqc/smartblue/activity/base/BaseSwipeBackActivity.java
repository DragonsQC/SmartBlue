package com.dqc.smartblue.activity.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dqc.qlibrary.activity.BaseSwipeBackCompatActivity;
import com.dqc.qlibrary.library.systembartint.SystemBarTintManager;
import com.dqc.smartblue.R;


public class BaseSwipeBackActivity extends BaseSwipeBackCompatActivity {
    public SystemBarTintManager mTintManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTintResource(R.color.colorPrimaryDark);
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    /**
     * 设置状态栏颜色
     *
     * @param resId 颜色资源id
     */
    protected void setStatusBarTintResource(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(resId);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window                     win       = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int                  bits      = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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
