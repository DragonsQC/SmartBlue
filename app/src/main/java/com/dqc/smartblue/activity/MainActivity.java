package com.dqc.smartblue.activity;

import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.dqc.qlibrary.utils.ToastUtils;
import com.dqc.qlibrary.utils.VibratorUtils;
import com.dqc.smartblue.R;
import com.dqc.smartblue.activity.base.BaseActivity;
import com.dqc.smartblue.databinding.ActivityMainBinding;
import com.dqc.smartblue.databinding.ItemDeviceListBinding;
import com.dqc.smartblue.entity.BlueData;
import com.dqc.smartblue.service.SmartBlueReceiver;
import com.dqc.smartblue.utils.AppConstant;
import com.dqc.smartblue.utils.AppUtils;
import com.dqc.smartblue.utils.NotifyUtil;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, BluetoothProfile.ServiceListener {

    private static MainActivity _instance;

    private ActivityMainBinding mBinding;
    private BluetoothAdapter    mBluetoothAdapter;
    private SmartBlueReceiver   mSmartBlueReceiver;

    public  DeviceListAdapter     mAdapter                 = null;
    private List<BluetoothDevice> mBluetoothDevices        = new ArrayList<>();
    public  List<String>          mConnectedDevicesAddress = new ArrayList<>();

    private Drawer mDrawer = null;

    private NotifyUtil mNotifyUtil;

    public static MainActivity getInstance() {
        return _instance;
    }

    /**
     * 蓝牙状态 BroadcastReceiver 监听蓝牙的打开与关闭
     */
    private BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            ToastUtils.showDefault(getApplicationContext(), "蓝牙已打开");
                            updateBluetoothState();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            ToastUtils.showDefault(getApplicationContext(), "蓝牙已关闭");
                            updateBluetoothState();
                            break;
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setViewModel(new ViewModel());

        _instance = this;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content("没有找到蓝牙设备")
                    .positiveText(R.string.exit)
                    .positiveColorRes(R.color.red_500)
                    .onPositive((dialog, which) -> {
                        finish();
                        AppUtils.exitApp();
                    })
                    .show();
            return;
        }

        //当蓝牙关闭时提示打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            //申请打开蓝牙设备
            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content("蓝牙未开启，是否开启？")
                    .negativeText(R.string.cancel)
                    .negativeColorRes(R.color.grey_500)
                    .positiveText(R.string.ok)
                    .positiveColorRes(R.color.red_500)
                    .onPositive((dialog, which) -> {
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(intent);
                    })
                    .show();

        }

        //注册事件广播
        mSmartBlueReceiver = new SmartBlueReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mSmartBlueReceiver, filter);

        //注册蓝牙状态监听广播
        IntentFilter filterState = new IntentFilter();
        filterState.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothStateReceiver, filterState);

        initDrawer(savedInstanceState);

        mAdapter = new DeviceListAdapter();
        mBinding.lv.setAdapter(mAdapter);
        mBinding.lv.setOnItemClickListener(this);

        updateBluetoothState();

        //获得已连接的设备
        mBluetoothAdapter.getProfileProxy(getApplicationContext(), MainActivity.this, BluetoothProfile.HEADSET);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSmartBlueReceiver);
        unregisterReceiver(mBluetoothStateReceiver);
        _instance = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                } else {
                    //返回到桌面
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                if (mDrawer.isDrawerOpen()) {
                    mDrawer.closeDrawer();
                } else {
                    mDrawer.openDrawer();
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 初始化 Drawer
     */
    private void initDrawer(Bundle savedInstanceState) {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.device_list).withIcon(GoogleMaterial.Icon.gmd_format_list_numbered).withIdentifier(AppConstant.DRAWER_TAG_DEVICE_LIST),
                        new DividerDrawerItem()
                ).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (null != drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            switch (((Nameable) drawerItem).getName().getTextRes()) {
                                //设备列表
                                case R.string.device_list:
                                    return false;
                            }
                        }
                    }
                    return true;
                })
                .build();
    }

    /**
     * 更新导航栏上蓝牙图标状态
     */
    public void updateBluetoothState() {
        if (mBluetoothAdapter.isEnabled()) {
            mBinding.tvBluetoothState.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
            mBinding.tvBluetoothState.setFontAwesomeIcon(FontAwesome.FA_BLUETOOTH);
        } else {
            mBinding.tvBluetoothState.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
            mBinding.tvBluetoothState.setFontAwesomeIcon(FontAwesome.FA_BLUETOOTH_B);
        }

        updateDeviceList();
    }

    /**
     * 初始化蓝牙设备列表
     */
    private void updateDeviceList() {

        //获取本机已连接的蓝牙设备
        mBluetoothDevices.clear();
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (null != devices && devices.size() > 0) {
            mBluetoothDevices.addAll(devices);
            if (mBluetoothDevices.size() > 0) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 显示通知
     */
    public void notification(BlueData data, BluetoothDevice device, boolean isConnected) {
        if (null != data) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pIntent   = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            int           smallIcon = R.mipmap.ic_launcher;
            String        ticker    = "您有一条新通知";
            String        title, content;
            if (isConnected) {
                title = device.getName() + " 连接蓝牙";
                content = data.getConnectedMsg();
            } else {
                title = device.getName() + " 断开蓝牙";
                content = data.getDisconnectMsg();
            }
            mNotifyUtil = new NotifyUtil(getApplicationContext(), 2, true);
            mNotifyUtil.notifyNormailMoreline(pIntent, smallIcon, ticker, title, content, true, false, false);
            VibratorUtils.vibrate(MainActivity.this, new long[]{1000, 1000, 1000, 1000, 1000, 1000}, false);
        }
    }


    /**
     * ListView itemClick 回调
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BluetoothDevice device = mBluetoothDevices.get(i);
        Bundle          bundle = new Bundle();
        bundle.putParcelable(BlueEventActivity.KEY, device);
        goToForResult(BlueEventActivity.class, AppConstant.REQUEST_CODE_EVENT, bundle, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

    }

    @Override
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        List<BluetoothDevice> connectedDevices = proxy.getConnectedDevices();
        mConnectedDevicesAddress.clear();
        if (null != connectedDevices && connectedDevices.size() > 0) {
            for (BluetoothDevice device : connectedDevices) {
                mConnectedDevicesAddress.add(device.getAddress());
            }
            if (mConnectedDevicesAddress.size() > 0) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onServiceDisconnected(int profile) {
    }

    public class ViewModel {

        /**
         * 导航栏 蓝牙状态图标
         */
        public View.OnClickListener getOnClickBluetoothState() {
            return view -> {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                } else {
                    mBluetoothAdapter.enable();
                }
            };
        }

    }

    public class DeviceListAdapter extends BaseAdapter {
        private ItemDeviceListBinding itemBinding;


        @Override
        public int getCount() {
            return mBluetoothDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mBluetoothDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final BluetoothDevice entity = mBluetoothDevices.get(i);
            if (view == null) {
                itemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_device_list, viewGroup, false);
                view = itemBinding.getRoot();
                view.setTag(itemBinding);
            } else {
                itemBinding = (ItemDeviceListBinding) view.getTag();
            }
            itemBinding.tvName.setText(entity.getName());
            switch (entity.getType()) {
                case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                case BluetoothDevice.DEVICE_TYPE_DUAL:
                    itemBinding.tvBluetoothState.setFontAwesomeIcon(FontAwesome.FA_HEADPHONES);
                    break;
                case BluetoothDevice.DEVICE_TYPE_LE:
                    itemBinding.tvBluetoothState.setFontAwesomeIcon(FontAwesome.FA_LAPTOP);
                    break;
                case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                default:
                    itemBinding.tvBluetoothState.setFontAwesomeIcon(FontAwesome.FA_BLUETOOTH);
                    break;
            }
            switch (entity.getBondState()) {
                case BluetoothDevice.BOND_BONDED:
                    itemBinding.tvState.setText("已匹配");
                    itemBinding.tvState.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    break;
                case BluetoothDevice.BOND_BONDING:
                    itemBinding.tvState.setText("正在匹配");
                    itemBinding.tvState.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_700));
                    break;
                case BluetoothDevice.BOND_NONE:
                    itemBinding.tvState.setText("未匹配");
                    itemBinding.tvState.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_400));
                    break;
            }

            if (mConnectedDevicesAddress.contains(entity.getAddress())) {
                itemBinding.tvState.setText("已连接");
                itemBinding.tvState.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            }

            return itemBinding.getRoot();
        }

    }

}
