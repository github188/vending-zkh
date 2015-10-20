/**
 * 
 */
package com.mc.vending.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.IBinder;

import com.zillionstar.tools.ZillionLog;

/**
 * @author Forever
 * @date 2015年7月9日
 * @email forever.wang@zillionstar.com
 */
public class TrafficService extends Service implements Runnable {

    private Thread       traThread;
    private List<String> packageNames = new ArrayList<String>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ZillionLog.i("TrafficService", "create");
        // ArrayList appList = new ArrayList(); // 用来存储获取的应用信息数据
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            packageNames.add(packageInfo.packageName);
        }
    }

    private int getUid(String packageName) {

        int uid = 0;
        ApplicationInfo ai;
        PackageManager pm = getPackageManager();
        try {
            ai = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);

            uid = ai.uid;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        traThread = new Thread(this);
        traThread.start();
    }

    protected void getTraffic() {

        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        // c = TrafficStats.getMobileRxBytes();
        // MyLog.i("TrafficService", "获取手机通过 2G/3G 接收的字节流量总数=" + c);
        /** 获取手机通过 2G/3G 接收的数据包总数 */
        // c = TrafficStats.getMobileRxPackets();
        /** 获取手机通过 2G/3G 发出的字节流量总数 */
        // c = TrafficStats.getMobileTxBytes();
        // MyLog.i("TrafficService", "获取手机通过 2G/3G 发出的字节流量总数 =" + c);
        /** 获取手机通过 2G/3G 发出的数据包总数 */
        // c = TrafficStats.getMobileTxPackets();
        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
        long rxall = TrafficStats.getTotalRxBytes();
        // MyLog.i("TrafficService", "all rx =" + c);
        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
        // c = TrafficStats.getTotalRxPackets();
        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
        long txall = TrafficStats.getTotalTxBytes();
        // MyLog.i("TrafficService", "all  =" + c);
        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
        // c = TrafficStats.getTotalTxPackets();

        long aa = (rxall + txall);
        ZillionLog.i("TrafficService", "all = " + rxall + "/" + txall + "/" + aa);
        for (String packageName : packageNames) {
            int uid = getUid(packageName);

            /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
            long rxuid = TrafficStats.getUidRxBytes(uid);
            // MyLog.i("TrafficService",
            // "获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi)" + uid + " =" + c);
            /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
            long txuid = TrafficStats.getUidTxBytes(uid);
            // MyLog.i("TrafficService",
            // "获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi)" + uid + " =" + c);

            long all = (rxuid + txuid);
            if (all > 0) {
                ZillionLog.i("TrafficService", packageName + "(" + uid + ")" + " = " + rxuid + "/" + txuid
                        + "/" + all);
            }

        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                getTraffic();

                Thread.currentThread();
                Thread.sleep(1000 * 60 * 60);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
