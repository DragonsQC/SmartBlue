package com.dqc.smartblue.application;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.dqc.qlibrary.utils.QLog;
import com.dqc.qlibrary.utils.SPUtils;
import com.dqc.smartblue.BuildConfig;
import com.dqc.smartblue.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * @author DragonsQC
 */
public class SBApplication extends Application {

    public static boolean isDebug = true;
    private static SBApplication _instance;

    public static SBApplication getInstance() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //====== init ======
        _instance = this;
        isDebug = BuildConfig.DEBUG;

        TypefaceProvider.registerDefaultIconSets(); //AndroidBootstrap Typeface 初始化

        QLog.init(isDebug, getString(R.string.app_name));   //Log debug

        SPUtils.init(getString(R.string.app_name));  //SPUtil 初始化

        //Realm 初始化
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                //.encryptionKey(key)   //加密key 64字节
                .name(getString(R.string.app_name))
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        //LeakCanary.install(this);   //LeakCanary
        //====== init ======

    }

}
