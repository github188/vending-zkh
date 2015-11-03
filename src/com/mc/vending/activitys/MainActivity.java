package com.mc.vending.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.pick.MC_NormalPickActivity;
import com.mc.vending.activitys.pick.MC_WeightPickActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VersionData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.parse.AutherDataParse;
import com.mc.vending.parse.InitDataParse;
import com.mc.vending.parse.VendingDataExistParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.service.TaskService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.HttpHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.SystemUiHider;
import com.mc.vending.tools.Tools;
import com.zillionstar.tools.ZillionLog;

public class MainActivity extends Activity implements DataParseRequestListener {
    private RelativeLayout      layout_init;    // 初始化启动layout
    private RelativeLayout      layout_image;   // 封面显示layout
    private EditText            et_server_url;  // 服务器地址控件
    private EditText            et_vend_code;   // 售货机编号
    private SystemUiHider       mSystemUiHider;
    private String              configURL;      // 服务器地址
    private String              vendCode;       // 售货机编号
    private String              pwdKey;
    private AlertDialog.Builder adb;
    private ProgressDialog      progressDialog;
    private TextView            alert_msg_title; // 提示标题
    private TextView            alert_msg;      // 提示内容
    private boolean             isUserOperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ZillionLog.i("main", "onCreate");
        startTrafficService();

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        initComponse();
        initObject();

        AssetsDatabaseManager.initManager(this); // 初始化数据库路径
        HttpHelper.getHeaderMap(); // 初始化http头
        ActivityManagerTool.getActivityManager().add(this); // 当前activity加入activityManager
        requestGetClientVersionServer();
        // initConfigURL();
        isUserOperate = false;

        try {
            TaskService.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startTrafficService() {
        Intent intent1 = new Intent(this, TaskService.class);
        startService(intent1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initComponse() {
        layout_init = (RelativeLayout) this.findViewById(R.id.layout_init);
        layout_image = (RelativeLayout) this.findViewById(R.id.layout_image);
        et_server_url = (EditText) this.findViewById(R.id.et_server_url);
        et_vend_code = (EditText) this.findViewById(R.id.et_vend_code);
    }

    /**
     * 初始化对象
     */
    private void initObject() {
        Constant.BODY_VALUE_UDID = Tools.readTelephoneSerialNum(this);
        // et_server_url.setText("http://developer.zillionstar.com:8081/WSRR.asHx");
        // et_vend_code.setText("V0030");
        layout_image.setVisibility(View.VISIBLE);
        // layout_image.setVisibility(View.INVISIBLE);

        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
        et_server_url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    if (et_server_url == v) {
                        inputManager.showSoftInput(et_server_url, 4);
                    } else {
                        inputManager.showSoftInput(et_vend_code, 4);
                    }

                }
            }
        });
        configURL = getConfigURL();
        pwdKey = getPwdKey();
        vendCode = getVendCode();
        Constant.SERVER_URL = configURL;
    }

    /**
     * 现实提示信息
     */
    private void resetAlertMsg(String msg) {
        alert_msg.setText(msg);
        alert_msg_title.setVisibility(View.VISIBLE);
        alert_msg.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏提示信息
     */
    private void hiddenAlertMsg() {
        alert_msg.setText("");
        alert_msg_title.setVisibility(View.INVISIBLE);
        alert_msg.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化按钮点击
     * 
     * @param view
     */
    public void initClicked(View view) {
        isUserOperate = true;
        if (!Tools.isAccessNetwork(MainActivity.this)) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
            return;
        }
        if (StringHelper.isEmpty(et_server_url.getText().toString(), true)) {
            resetAlertMsg("请输入服务器地址！");
            return;
        }
        if (StringHelper.isEmpty(et_vend_code.getText().toString(), true)) {
            resetAlertMsg("请输入售货机编号！");
            return;
        }
        savePwdKey("");
        saveConfigURL("");
        saveVendCode("");
        configURL = et_server_url.getText().toString();
        vendCode = et_vend_code.getText().toString();
        pwdKey = getPwdKey();
        Constant.SERVER_URL = configURL;
        hiddenAlertMsg();
        initPwdKey();
    }

    /**
     * 初始化服务器地址
     */
    private void initConfigURL() {
        configURL = getConfigURL();
        pwdKey = getPwdKey();
        vendCode = getVendCode();

        et_server_url.setText(configURL);
        et_vend_code.setText(vendCode);
        // 测试代码
        // et_server_url.setText("http://developer.zillionstar.com:8081/WSRR.asHx");
        // et_vend_code.setText("V0030");

        if (StringHelper.isEmpty(configURL, true) || StringHelper.isEmpty(pwdKey, true)) {
            layout_init.setVisibility(View.VISIBLE);
            layout_image.setVisibility(View.INVISIBLE);
            return;
        }
        Constant.SERVER_URL = configURL;
        initPwdKey();
    }

    /**
     * 初始化加密key
     */
    private void initPwdKey() {
        if (StringHelper.isEmpty(pwdKey, true)) {
            requestPwd();
            return;
        }

        Constant.BODY_VALUE_PWD = pwdKey;
        goNormalPickAcitivity();
    }

    /**
     * 进入一般领料页面
     */
    private void goNormalPickAcitivity() {
        Intent intent = new Intent();
        intent.putExtra("vendCode", vendCode);
//        intent.setClass(MainActivity.this,MC_NormalPickActivity.class);
        intent.setClass(MainActivity.this, MC_WeightPickActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 读取本地服务器地址
     * 
     * @return
     */
    private String getConfigURL() {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_CONFIG, MODE_PRIVATE);
        return shared.getString(Constant.SHARED_CONFIG_URL, "");
    }

    /**
     * 保存本地服务器地址
     * 
     * @return
     */
    private void saveConfigURL(String key) {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_CONFIG, MODE_PRIVATE);
        shared.edit().putString(Constant.SHARED_CONFIG_URL, key).commit();
    }

    /**
     * 读取本地pwdkey
     * 
     * @return
     */
    private String getVendCode() {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, MODE_PRIVATE);
        return shared.getString(Constant.SHARED_VEND_CODE, "");
    }

    /**
     * 保存本地服务器地址
     * 
     * @return
     */
    private void saveVendCode(String key) {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, MODE_PRIVATE);
        shared.edit().putString(Constant.SHARED_VEND_CODE, key).commit();
    }

    /**
     * 读取本地pwdkey
     * 
     * @return
     */
    private String getPwdKey() {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_PWD, MODE_PRIVATE);
        return shared.getString(Constant.SHARED_PWD_KEY, "");
    }

    /**
     * 保存 加密key
     * 
     * @param device_no
     */
    private void savePwdKey(String key) {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_PWD, MODE_PRIVATE);
        shared.edit().putString(Constant.SHARED_PWD_KEY, key).commit();
    }

    /**
     * 请求加密key
     */
    private void requestPwd() {
        AutherDataParse parse = new AutherDataParse();
        parse.setListener(this);
        parse.requestAutherData(Constant.HTTP_OPERATE_TYPE_DESGET, Constant.METHOD_WSID_AUTHER,
                Tools.readTelephoneSerialNum(this));
    }

    /**
     * 请求售货机数据
     */
    public void requestVendingExistParse() {
        VendingDataExistParse parse = new VendingDataExistParse();
        parse.setListener(this);
        parse.requestVendingExistData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING,
                vendCode, false);
    }

    @Override
    public void parseRequestFinised(BaseData baseData) {
        ZillionLog.i("parseRequestFinised");
        if (!baseData.isSuccess() && Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("售货机已经初始化，不允许重复初始化，请与管理员联系！");
            return;
        }

        if (baseData.isSuccess()) {
            if (Constant.METHOD_WSID_AUTHER.equals(baseData.getRequestURL())) {
                pwdKey = (String) baseData.getUserObject();
                requestVendingExistParse();
            } else if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
                processUpdate(baseData);
            } else if (Constant.METHOD_WSID_VENDING.equals(baseData.getRequestURL())) {
                boolean existFlag = (Boolean) baseData.getUserObject();
                if (!existFlag) {
                    this.layout_image.setVisibility(View.INVISIBLE);
                    resetAlertMsg("售货机编号不正确，请联系系统管理员");
                    return;
                }
                if (isUserOperate) {
                    requestInitPermission();
                    return;
                }
                savePwdKey(pwdKey);
                saveConfigURL(configURL);
                saveVendCode(vendCode);
                Constant.BODY_VALUE_PWD = pwdKey;
                initPwdKey();
            } else if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
                savePwdKey(pwdKey);
                saveConfigURL(configURL);
                saveVendCode(vendCode);
                Constant.BODY_VALUE_PWD = pwdKey;
                initPwdKey();
            }

        }
    }

    /**
     * 版本检查接口返回处理
     * 
     * @param baseData
     */
    private void processUpdate(BaseData baseData) {
        try {
            ZillionLog.i("processUpdate");
            final VersionData versionData = (VersionData) baseData.getUserObject();
            String version = versionData.getVersion().replace(".", "");
            String locaVersion = Constant.HEADER_VALUE_CLIENTVER.replace(".", "");
//            if (Integer.parseInt(version) > Integer.parseInt(locaVersion)) {
//                Intent intent = new Intent(MainActivity.this, VersionActivity.class);
//                intent.putExtra("url", versionData.getDownloadURL());
//                intent.putExtra("vermsg", versionData.getVersion() + "");
//                startActivity(intent);
//                finish();
//
//            } else {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initConfigURL();
//                    }
//                }, 10);
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initConfigURL();
                }
            }, 10);
        } catch (Exception e) {
            initConfigURL();
        }

    }

    @Override
    public void parseRequestFailure(BaseData baseData) {
        ZillionLog.i("main parseRequestFailure==>" + Constant.WSIDNAMEMAP.get(baseData.getRequestURL()));
        this.layout_image.setVisibility(View.INVISIBLE);
        if (baseData.HTTP_STATUS != HttpHelper.HTTP_STATUS_SUCCESS) {

            resetAlertMsg("服务器连接异常，请检查网络连接或服务器地址是否正确！");
            return;
        }
        if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
            initConfigURL();
        } else if (Constant.METHOD_WSID_AUTHER.equals(baseData.getRequestURL())) {
            resetAlertMsg("认证连接异常，请检查网络连接或服务器地址是否正确！");
        } else if (Constant.METHOD_WSID_VENDING.equals(baseData.getRequestURL())) {
            // if (baseData.getData() == null || baseData.getData().length() ==
            // 0) {
            boolean existFlag = (Boolean) baseData.getUserObject();
            if (!existFlag) {
                resetAlertMsg("售货机编号不正确，请联系系统管理员");
                return;
            }
        } else if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("请求初始化权限失败！");
        }
    }

    /**
     * 获取最新版本号
     */
    private void requestGetClientVersionServer() {
        if (!Tools.isAccessNetwork(MainActivity.this)) {
            initConfigURL();
            return;
        }
        if (StringHelper.isEmpty(configURL, true)) {
            this.layout_image.setVisibility(View.INVISIBLE);
            return;
        }
        initConfigURL();
//        VersionDataParse parse = new VersionDataParse();
//        parse.setListener(this);
//        parse.requestVersionData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VERSION);
    }

    public void finishClicked(View view) {
        Dialog dialog = new AlertDialog.Builder(this).setMessage(getString(R.string.PUBLIC_EXIST_TITLE))
                .setPositiveButton(getString(R.string.PUBLIC_EXIST), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishApp();
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

    /**
     * 退出应用程序
     */
    private void finishApp() {
        ActivityManagerTool.getActivityManager().exit();
    }

    private void requestInitPermission() {
        InitDataParse parse = new InitDataParse();
        parse.setListener(this);
        parse.requestInitData(Constant.HTTP_OPERATE_TYPE_CHECK, Constant.METHOD_WSID_CHECK_INIT, et_vend_code
                .getText().toString());
    }
}
