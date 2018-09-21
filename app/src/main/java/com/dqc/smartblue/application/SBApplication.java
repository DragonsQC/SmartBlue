package com.dqc.smartblue.application;

import android.app.Application;

import com.dqc.qlibrary.QLibrary;
import com.dqc.smartblue.BuildConfig;
import com.dqc.smartblue.R;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * @author DragonsQC
 */
public class SBApplication extends Application {

    public static  boolean       sIsDebug = true;
    private static SBApplication sInstance;

    public static SBApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //====== init ======
        sInstance = this;
        sIsDebug = BuildConfig.DEBUG;

        //<editor-fold defaultstate="collapsed" desc="QLibrary初始化" >
        QLibrary.init(getInstance(), sIsDebug, getString(R.string.app_name), 4);
        //</editor-fold>

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
