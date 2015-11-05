package com.mc.vending.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.config.Constant;

/**
 * Android AutoUpdate.
 * 
 * lazybone/2010.08.20
 * 
 * 1.Set apkUrl.
 * 
 * 2.check().
 * 
 * 3.add delFile() method in resume()\onPause().
 */
public class UpdateAPK {
    public Activity             activity            = null;
    public int                  versionCode         = 0;
    public String               versionName         = "";
    private static final String TAG                 = "AutoUpdate";
    private String              currentTempFilePath = "";
    private String              fileEx              = "";
    private String              fileNa              = "";
    public String               strURL;
    private ProgressDialog      dialog;
    Button                      tv;
    ProgressBar                 pb;
    TextView                    num;
    long                        length;
    long                        totalLength;

    String                      cacheFilePath;

    public UpdateAPK(Activity activity, String strURL) {

        this.activity = activity;
        pb = (ProgressBar) activity.findViewById(R.id.progress);
        num = (TextView) activity.findViewById(R.id.progressNum);
        this.strURL = strURL;
        //        this.strURL = "http://developer.zillionstar.com:8090/Upload/apk/vending.apk";
        this.cacheFilePath = "/mnt/sdcard0/Download/";
        //        this.cacheFilePath = String.format("/data/data/%s/download/",
        //            this.activity.getApplicationInfo().packageName);
        //        this.cacheFilePath = Environment.getExternalStorageDirectory() + "/ckh_evm/apk/";

    }

    final Handler handler = new Handler() {
                              @Override
                              public void handleMessage(Message msg) {
                                  if (msg.what == 1) {
                                      pb.setProgress((int) (length * 100 / totalLength));
                                      num.setText((length * 100 / totalLength) + "%  "
                                              + String.format("%1$04.2f", length / 1024F) + "KB/"
                                              + String.format("%1$04.2f", totalLength / 1024F) + "KB");
                                  }
                              }
                          };

    public void check() {
        if (isNetworkAvailable(this.activity) == false) {
            return;
        }
        /*
         * if (true) {// Check version. showUpdateDialog(); }
         */
        downloadTheFile(strURL);

        if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) == 205) {
//            uploadCheckApk();
        }

    }

    String checkApk;

    private void uploadCheckApk() {
        checkApk = strURL.substring(0, strURL.lastIndexOf("/")) + "/vendingCheck.apk";
        ZillionLog.i(checkApk);

        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL myURL = new URL(checkApk);
                        URLConnection conn = myURL.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        totalLength = conn.getContentLength();
                        if (totalLength < 0) {
                            totalLength = 1887437;
                        }
                        if (is == null) {
                            throw new RuntimeException("stream is null");
                        }
                        File path = new File(cacheFilePath);
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                        File myTempFile = new File(cacheFilePath + "vendingCheck.apk");
                        currentTempFilePath = myTempFile.getAbsolutePath();

                        FileOutputStream fos = new FileOutputStream(myTempFile);
                        byte buf[] = new byte[1024];
                        do {
                            int numread = is.read(buf);
                            if (numread <= 0) {
                                break;
                            }
                            fos.write(buf, 0, numread);
                            length = myTempFile.length();
                            handler.sendMessage(handler.obtainMessage(1));
                        } while (true);
                        //dialog.cancel();   
                        //dialog.dismiss();  
                        //            try {
                        //                String command = "chmod 777 " + myTempFile.getAbsolutePath();
                        //                Log.i("zyl", "command = " + command);
                        //                Runtime runtime = Runtime.getRuntime();
                        //                Process proc = runtime.exec(command);
                        //            } catch (IOException e) {
                        //                Log.i("zyl", "chmod fail!!!!");
                        //                e.printStackTrace();
                        //            }
                        openFile(myTempFile);
                        try {
                            is.close();
                        } catch (Exception ex) {
                            Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            activity.setResult(9998, intent);
            activity.finish();
        }

    }

    public static boolean isNetworkAvailable(Context ctx) {
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showUpdateDialog() {
        @SuppressWarnings("unused")
        AlertDialog alert = new AlertDialog.Builder(this.activity).setTitle("提示")
                .setIcon(R.drawable.ic_launcher).setMessage("是否现在更新？?")
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadTheFile(strURL);
                        //showWaitDialog();   
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void showWaitDialog() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("等待更新...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void getCurrentVersion() {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            this.versionCode = info.versionCode;
            this.versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void downloadTheFile(final String strPath) {
        fileEx = strURL.substring(strURL.lastIndexOf(".") + 1, strURL.length()).toLowerCase();
        fileNa = strURL.substring(strURL.lastIndexOf("/") + 1, strURL.length());
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Tools.deleteAllFile(cacheFilePath);
                        doDownloadTheFile(strPath);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            activity.setResult(9998, intent);
            activity.finish();
        }
    }

    private void doDownloadTheFile(String strPath) throws Exception {
        if (!URLUtil.isNetworkUrl(strPath)) {
            Intent intent = new Intent();
            activity.setResult(9998, intent);
            activity.finish();
        } else {
            URL myURL = new URL(strPath);
            URLConnection conn = myURL.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            totalLength = conn.getContentLength();
            if (totalLength < 0) {
                totalLength = 1887437;
            }
            if (is == null) {
                throw new RuntimeException("stream is null");
            }
            File path = new File(cacheFilePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File myTempFile = new File(cacheFilePath + fileNa);
            currentTempFilePath = myTempFile.getAbsolutePath();

            FileOutputStream fos = new FileOutputStream(myTempFile);
            byte buf[] = new byte[1024];
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    break;
                }
                fos.write(buf, 0, numread);
                length = myTempFile.length();
                handler.sendMessage(handler.obtainMessage(1));
            } while (true);
            //dialog.cancel();   
            //dialog.dismiss();  
            //            try {
            //                String command = "chmod 777 " + myTempFile.getAbsolutePath();
            //                Log.i("zyl", "command = " + command);
            //                Runtime runtime = Runtime.getRuntime();
            //                Process proc = runtime.exec(command);
            //            } catch (IOException e) {
            //                Log.i("zyl", "chmod fail!!!!");
            //                e.printStackTrace();
            //            }
            openFile(myTempFile);
            try {
                is.close();
            } catch (Exception ex) {
                Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
            }
        }
    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        activity.startActivity(intent);
        //        activity.setResult(9998, intent);
        activity.finish();
    }

    public void delFile() {
        File myFile = new File(currentTempFilePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    private String getMIMEType(File f) {
        /*
         * String type = ""; String fName = f.getName(); String end = fName
         * .substring(fName.lastIndexOf(".") + 1, fName.length())
         * .toLowerCase(); if (end.equals("m4a") || end.equals("mp3") ||
         * end.equals("mid") || end.equals("xmf") || end.equals("ogg") ||
         * end.equals("wav")) { type = "audio"; } else if (end.equals("3gp") ||
         * end.equals("mp4")) { type = "video"; } else if (end.equals("jpg") ||
         * end.equals("gif") || end.equals("png") || end.equals("jpeg") ||
         * end.equals("bmp")) { type = "image"; } else if (end.equals("apk")) {
         * type = "application/vnd.android.package-archive"; } else { type =
         * "*"; } if (end.equals("apk")) { } else { type += "/*"; } return type;
         */
        return "application/vnd.android.package-archive";
    }
}