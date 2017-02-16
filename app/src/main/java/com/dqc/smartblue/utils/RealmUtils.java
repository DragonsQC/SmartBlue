package com.dqc.smartblue.utils;

import com.dqc.smartblue.entity.BlueData;

import io.realm.Realm;

/**
 * Realm 工具类
 */

public class RealmUtils {
    private static RealmUtils _instance = null;
    public         Realm      mRealm    = Realm.getDefaultInstance();

    public static RealmUtils getInstance() {
        if (_instance == null) {
            _instance = new RealmUtils();
        }
        return _instance;
    }


    /**
     * 保存或更新
     */
    public void saveOrUpdate(BlueData blueData) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(blueData));
    }

    /**
     * 查询一个
     *
     * @param address
     * @return
     */
    public BlueData find(String address) {
        return mRealm.where(BlueData.class).equalTo("address", address).findFirst();
    }

    /**
     * 删除一个
     *
     * @param address
     */
    public void delete(String address) {
        BlueData result = find(address);
        if (null != result) {
            mRealm.executeTransaction(realm -> result.deleteFromRealm());
        }
    }
}
