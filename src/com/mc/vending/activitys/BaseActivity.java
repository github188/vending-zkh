package com.mc.vending.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mc.vending.R;
import com.mc.vending.data.BaseData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.HttpHelper;
import com.mc.vending.tools.SystemUiHider;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.ZillionLog;

/**
 * activity父类
 * 
 * @author yuhaiw
 *
 */
public class BaseActivity extends Activity {
    private AlertDialog.Builder     adb;                     // 对话框控件
    protected boolean               isSuccessRequest;        // 请求是否成功
    private Thread                  downLoadData;
    private BaseData                postClientData;
    protected final static int      SUCCESS          = 0000;
    protected final static int      ERROR            = 9999;
    protected final static int      FAILURE          = 8888;
    protected static ProgressDialog progressDialog;          // 加载框
    private Boolean                 isMainPage       = true; // 是否为一级页面
    protected SystemUiHider         mSystemUiHider;
    // 网络状态标记
    protected static boolean        isNetworkEnabled = false;
    public static Boolean           isPostClientInfo = false;
    private Toast                   toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        AssetsDatabaseManager.initManager(this); // 初始化数据库路径
    }

    protected void setIsMainPage(Boolean isMainPage) {
        this.isMainPage = isMainPage;
    }

    /**
     * 弹出提示对话框
     * 
     * @param message
     */
    public void sendAlertMessage(String message) {
        try {
            adb = new AlertDialog.Builder(BaseActivity.this);
            adb.setTitle(R.string.alert_msg_title);
            adb.setMessage(message);
            adb.setNegativeButton(getString(R.string.PUBLIC_OK), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            adb.show();
        } catch (Exception e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        startLoading();
        super.startActivity(intent);
    }

    // @Override
    // public void startActivityForResult(Intent intent, int requestCode) {
    // startLoading();
    // super.startActivityForResult(intent, requestCode);
    // }

    /**
     * 开始loading动画
     */
    public void startLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        } catch (Exception e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
        }

        try {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setMessage(getString(R.string.PUBLIC_LOADING));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
            progressDialog.setIndeterminate(false);// 设置进度条是否为不明确
            progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
            progressDialog.show();
        } catch (Exception e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }

    }

    /**
     * 停止loading动画
     */
    public void stopLoading() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }

    }

    /**
     * 请求服务器数据基类操作，启动loading动画，判断时候有网络
     * 
     * @return
     */
    public boolean requestServer() {
        isSuccessRequest = false;

        if (!Tools.isAccessNetwork(BaseActivity.this)) {
            this.sendAlertMessage(getString(R.string.PUBLIC_NETWORK_ERROR));
            return false;
        }
        this.startLoading();
        return true;
    }

    /**
     * 提示是否有网络链接
     * 
     * @return
     */
    public boolean isAccessNetwork() {
        if (!Tools.isAccessNetwork(BaseActivity.this)) {
            // this.sendAlertMessage(getString(R.string.PUBLIC_NETWORK_ERROR));
            return false;
        }
        return true;
    }

    /**
     * 服务器响应基类操作，停止动画，根据返回标示提示是否有错误
     * 
     * @param data
     * @return
     */
    protected boolean responseServer() {
        this.stopLoading();

        isSuccessRequest = true;
        return true;
    }

    /**
     * 服务器响应基类操作，停止动画，根据返回标示提示是否有错误
     * 
     * @param data
     * @returns
     */
    protected boolean responseServer(BaseData data) {
        this.stopLoading();
        if (data.HTTP_STATUS != HttpHelper.HTTP_STATUS_SUCCESS) {
            this.sendAlertMessage(getString(R.string.PUBLIC_REQUEST_SERVER_ERROR));
            return false;
        }

        isSuccessRequest = true;
        return true;
    }

    /**
     * 拨打电话
     * 
     * @param phoneNo
     */
    public void call(String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
        startActivity(intent);
    }

    /**
     * 显示toast提示信息
     * 
     * @param message
     */
    public void showToast(String message) {

        toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    /**
     * 弹出退出对话框
     */
    public void showExitDialog() {
        Dialog dialog = new AlertDialog.Builder(this).setMessage(getString(R.string.PUBLIC_EXIST_TITLE))
                .setPositiveButton(getString(R.string.PUBLIC_EXIST), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityManagerTool.getActivityManager().exit();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.PUBLIC_CANCEL), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                }).create();
        dialog.show();
    }

    public void hiddenSystemUI(View contentView) {
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, SystemUiHider.FLAG_HIDE_NAVIGATION);
        mSystemUiHider.setup();
        mSystemUiHider.hide();
        mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean visible) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
                    e.printStackTrace();
                }
                mSystemUiHider.hide();
            }
        });

    }

    /**
     * 隐藏键盘
     * 
     * @param v
     * @param et
     */
    public void hiddenKeyBoard(EditText et) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
