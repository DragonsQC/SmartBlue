package com.dqc.smartblue.activity.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dqc.qlibrary.activity.QBaseFragment;
import com.dqc.qlibrary.utils.QLog;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * 基础 DataBinding Fragment
 *
 * @param <Binding> XxxBinding
 * @author DragonsQC
 */
public abstract class BaseFragment<Binding extends ViewDataBinding> extends QBaseFragment {

    /**
     * DataBinding
     */
    public Binding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layoutId() == 0) {
            QLog.e("请传入正确的layoutId");
            return null;
        }
        mBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        onInit(savedInstanceState);
        return mBinding.getRoot();
    }

    /**
     * 绑定 layoutId 到 DataBinding
     *
     * @return layoutId
     */
    public abstract int layoutId();

    /**
     * 梦开始的地方
     *
     * @param savedInstanceState savedInstanceState
     */
    public abstract void onInit(Bundle savedInstanceState);
}
