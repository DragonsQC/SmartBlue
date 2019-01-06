package com.dqc.smartblue.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dqc.qlibrary.utils.ToastUtils;
import com.dqc.smartblue.R;
import com.dqc.smartblue.activity.base.BaseActivity;
import com.dqc.smartblue.databinding.ActivityBlueEventBinding;
import com.dqc.smartblue.entity.BlueData;
import com.dqc.smartblue.utils.RealmUtils;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author DragonsQC
 */
public class BlueEventActivity extends BaseActivity<ActivityBlueEventBinding, BlueEventViewModel> implements Switch.OnCheckedChangeListener, View.OnClickListener {

    public static String KEY = "KEY";

    private BluetoothDevice mBluetoothDevice;
    private BlueData        mBlueData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_blue_event;
    }

    @Override
    public BlueEventViewModel initViewModel() {
        return ViewModelProviders.of(this).get(BlueEventViewModel.class);
    }

    @Override
    public void onInit(Bundle savedInstanceState) {
        mBluetoothDevice = getIntent().getExtras().getParcelable(KEY);
        if (null == mBluetoothDevice) {
            finish();
            return;
        }
        mBinding.tvNaviTitle.setText(mBluetoothDevice.getName() + " 事件设置");

        mBlueData = RealmUtils.getInstance().find(mBluetoothDevice.getAddress());
        if (null != mBlueData) {
            mBinding.swConnected.setChecked(mBlueData.getConnected());
            mBinding.swDisconnect.setChecked(mBlueData.getDisconnect());
            mBinding.tvConnectedMsg.setText(mBlueData.getConnectedMsg());
            mBinding.tvDisconnectMsg.setText(mBlueData.getDisconnectMsg());
        }

        mBinding.swConnected.setOnCheckedChangeListener(this);
        mBinding.swDisconnect.setOnCheckedChangeListener(this);
        mBinding.tvConnectedMsg.setOnClickListener(this);
        mBinding.tvDisconnectMsg.setOnClickListener(this);
    }

    /**
     * 设置通知数据弹框
     */
    private void showNotifyDataDialog(BluetoothDevice device, boolean isConnected) {
        String   content, hint = "";
        BlueData result        = RealmUtils.getInstance().find(device.getAddress());
        if (isConnected) {
            content = "请输入 " + device.getName() + " 连接 时的通知提示内容";
            if (null != result) {
                hint = result.getConnectedMsg();
            }
        } else {
            content = "请输入 " + device.getName() + " 断开 时的通知提示内容";
            if (null != result) {
                hint = result.getDisconnectMsg();
            }
        }

        new MaterialDialog.Builder(BlueEventActivity.this)
                .title(device.getName())
                .content(content)
                .canceledOnTouchOutside(false)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .inputRange(1, 50)
                .negativeText(R.string.cancel)
                .negativeColorRes(R.color.grey_500)
                .positiveText(R.string.ok)
                .positiveColorRes(R.color.blue_500)
                .input("请输入通知提示内容", hint, false, (dialog, input) -> {
                    if (!TextUtils.isEmpty(input.toString().trim())) {
                        BlueData blueData = new BlueData();
                        blueData.setAddress(device.getAddress());
                        if (isConnected) {
                            blueData.setConnected(mBinding.swConnected.isChecked());
                            blueData.setConnectedMsg(input.toString().trim());
                            if (null != result) {
                                blueData.setDisconnect(result.getDisconnect());
                                blueData.setDisconnectMsg(result.getDisconnectMsg());
                            }
                        } else {
                            blueData.setDisconnect(mBinding.swDisconnect.isChecked());
                            blueData.setDisconnectMsg(input.toString().trim());
                            if (null != result) {
                                blueData.setConnected(result.getConnected());
                                blueData.setConnectedMsg(result.getConnectedMsg());
                            }
                        }

                        RealmUtils.getInstance().saveOrUpdate(blueData);
                        if (isConnected) {
                            ToastUtils.showDefault(device.getName() + " 连接时通知注册成功");
                            mBinding.tvConnectedMsg.setText(input.toString().trim());
                        } else {
                            ToastUtils.showDefault(device.getName() + " 断开时通知注册成功");
                            mBinding.tvDisconnectMsg.setText(input.toString().trim());
                        }
                    }
                })
                .show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mBlueData = RealmUtils.getInstance().find(mBluetoothDevice.getAddress());
        BlueData blueData = new BlueData();
        switch (buttonView.getId()) {
            case R.id.sw_connected:
                if (null == mBlueData) {
                    ToastUtils.showDefault("请先输入通知内容");
                    showNotifyDataDialog(mBluetoothDevice, true);
                } else {
                    blueData.setAddress(mBlueData.getAddress());
                    blueData.setConnected(isChecked);
                    blueData.setConnectedMsg(mBlueData.getConnectedMsg());
                    blueData.setDisconnect(mBlueData.getDisconnect());
                    blueData.setDisconnectMsg(mBlueData.getDisconnectMsg());
                    RealmUtils.getInstance().saveOrUpdate(blueData);
                    if (isChecked) {
                        ToastUtils.showDefault(mBluetoothDevice.getName() + " 连接通知已开启");
                    } else {
                        ToastUtils.showDefault(mBluetoothDevice.getName() + " 连接通知已关闭");
                    }
                }
                break;
            case R.id.sw_disconnect:
                if (null == mBlueData) {
                    ToastUtils.showDefault("请先输入通知内容");
                    showNotifyDataDialog(mBluetoothDevice, false);
                } else {
                    blueData.setAddress(mBlueData.getAddress());
                    blueData.setConnected(mBlueData.getConnected());
                    blueData.setConnectedMsg(mBlueData.getConnectedMsg());
                    blueData.setDisconnect(isChecked);
                    blueData.setDisconnectMsg(mBlueData.getDisconnectMsg());
                    RealmUtils.getInstance().saveOrUpdate(blueData);
                    if (isChecked) {
                        ToastUtils.showDefault(mBluetoothDevice.getName() + " 断开通知已开启");
                    } else {
                        ToastUtils.showDefault(mBluetoothDevice.getName() + " 断开通知已关闭");

                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_connected_msg:
                showNotifyDataDialog(mBluetoothDevice, true);
                break;
            case R.id.tv_disconnect_msg:
                showNotifyDataDialog(mBluetoothDevice, false);
                break;
            default:
                break;
        }
    }

}
