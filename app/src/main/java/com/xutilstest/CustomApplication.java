package com.xutilstest;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;


/**
 * @author Dong
 * @date 2016-07-03
 */
public class CustomApplication extends Application {

    public static CustomApplication mInstance;
    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    public static synchronized CustomApplication getInstance() {
        return mInstance;
    }

    public static void setmInstance(CustomApplication mInstance) {
        CustomApplication.mInstance = mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("", "application");
        mInstance = this;

        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

    }

    public DbManager.DaoConfig getDaoConfig() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("my.db")
                // 不设置dbDir时, 默认存储在app的私有目录.
                .setDbDir(new File("/test"))
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        return daoConfig;
    }

    public void setDaoConfig(DbManager.DaoConfig daoConfig) {
        this.daoConfig = daoConfig;
    }

    public DbManager getDb() {
        return db;
    }

    public void setDb(DbManager db) {
        this.db = db;
    }

    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

}
