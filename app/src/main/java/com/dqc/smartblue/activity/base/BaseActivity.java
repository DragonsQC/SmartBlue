package com.dqc.smartblue.activity.base;

import android.os.Bundle;
import android.view.View;

import com.dqc.qlibrary.activity.QBaseActivity;
import com.dqc.smartblue.R;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


/**
 * 基础 Activity
 *
 * @param <Binding> 继承 @{link androidx.databinding.ViewDataBinding} 的 XxxBinding
 * @param <VM>      继承 @{link com.dqc.pmworld.activity.base.BaseViewModel} 的 XxxViewModel
 * @author DragonsQC
 */
public abstract class BaseActivity<Binding extends ViewDataBinding, VM extends BaseViewModel>
        extends QBaseActivity {

    /**
     * DataBinding
     */
    public Binding mBinding;

    /**
     * ViewModel
     */
    public VM mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), 0);

        mBinding = DataBindingUtil.setContentView(this, layoutId());
        mViewModel = initViewModel();
        //noinspection unchecked
        mViewModel.setActivity(this);
        getLifecycle().addObserver(mViewModel);
        onInit(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(mViewModel);
    }

    /**
     * 绑定 layoutId 到 DataBinding
     *
     * @return layoutId
     */
    public abstract int layoutId();

    /**
     * 初始化ViewModel
     * <p>
     * return ViewModelProviders.of(this).get(XxxViewModel.class);
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public abstract VM initViewModel();

    /**
     * 梦开始的地方
     *
     * @param savedInstanceState savedInstanceState
     */
    public abstract void onInit(Bundle savedInstanceState);

    public void onClickBase(View view) {
        switch (view.getId()) {
            //后退
            case R.id.navi_back:
                finish();
                break;
            default:
                break;
        }
    }

}
