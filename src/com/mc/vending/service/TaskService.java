package com.mc.vending.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;

import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.db.RetreatHeadDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.zillionstar.tools.ZillionLog;

public class TaskService extends Service {
    private Context             context;

    private static final String exeTime = "01:00";

//    private static final String exeTime = "15:52";

    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        ZillionLog.i("context.getCacheDir():" + context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
            ZillionLog.i("context.getExternalCacheDir():" + context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件  
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据  
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据  
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件  
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * 
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";  
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        handler.postDelayed(task, 5 * 1000);//延迟调用  
        handler.post(task);//立即调用  

    }

    private void reboot() {

//        System.exit(0);
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

//        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        manager.restartPackage("com.mc.vending");

//        PowerManager manager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
//        manager.reboot("重新启动系统");
    }

    private void clearStockTransaction(String startDate) {
        new StockTransactionDbOper().clearStockTransaction(startDate);
    }

    private void clearReplenishment(String startDate) {
        new ReplenishmentHeadDbOper().clearReplenishment(startDate);
    }

    private void clearRetreat(String startDate) {
        new RetreatHeadDbOper().clearRetreat(startDate);
    }

    private static void clearLog() {
        deleteDir(new File("/data/data/com.mc.vending/logs/"));
    }

    private Handler  handler = new Handler();

    private Runnable task    = new Runnable() {
                                 @Override
                                 public void run() {
                                     // TODOAuto-generated method stub  
                                     handler.postDelayed(this, 60 * 1000);//设置延迟时间，此处是5秒  
                                     Date curr = new Date();
                                     String startDate = "";

                                     SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
                                     Calendar start_c = Calendar.getInstance();
                                     start_c.setTime(curr);
//                                     start_c.add(Calendar.DATE, -1);
                                     start_c.add(Calendar.MONTH, -1);
                                     startDate = dft.format(start_c.getTime());

                                     //需要执行的代码  

                                     if (exeTime.equals(new SimpleDateFormat("HH:mm").format(curr))) {

                                         try {
                                             clearAllCache(context);
                                             clearLog();
                                             clearStockTransaction(startDate);
                                             clearReplenishment(startDate);
                                             clearRetreat(startDate);
//                                             reboot();

                                         } catch (Exception e) {
                                             ZillionLog.e(e.getMessage());
//                                             e.printStackTrace();
                                         }
                                     }
                                 }
                             };

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

}