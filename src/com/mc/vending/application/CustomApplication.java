/**
 * 
 */
package com.mc.vending.application;

import android.app.Application;
import android.content.Intent;

import com.mc.vending.activitys.MainActivity;
import com.zillionstar.tools.ZillionLog;

/**
 * @author Forever
 * @date 2015年7月22日
 * @email forever.wang@zillionstar.com
 */
public class CustomApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        ZillionLog.i("CustomApplication", "onCreate");
        //设置Thread Exception Handler 
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ZillionLog.i("uncaughtException", ex);
//        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
