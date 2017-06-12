package com.xiaobailong.base;

import android.app.Application;

import com.xiaobailong.bean.DaoMaster;
import com.xiaobailong.bean.DaoSession;

/**
 * Created by dongyuangui on 2017/5/31.
 */

public class BaseApplication extends Application {
    public static BaseApplication app = null;

    public DaoMaster.DevOpenHelper devOpenHelper;
    public DaoSession daoSession;
    public DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "cms", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }

}
