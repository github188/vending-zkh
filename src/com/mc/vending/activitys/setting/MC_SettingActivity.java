package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_SetAdapter;
import com.mc.vending.config.Constant;
import com.mc.vending.config.MC_Config;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.InitDataParse;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.parse.VendingChnStockDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;

/**
 * 设置页面
 * 
 * @author apple
 *
 */
public class MC_SettingActivity extends BaseActivity implements MC_SerialToolsListener,
        RequestDataFinishListener, DataParseRequestListener {
    private TextView tv_public_title; // 公共头部标题
    private ListView listView; // 列表
    private Button back;
    private VendingCardPowerWrapperData wrapperData; // 卡密码权限对象
    private VendingData vendData; // 售货机对象
    private List<String> dataList;
    private MC_SetAdapter adapter;
    private TextView alert_msg_title; // 提示标题
    private TextView alert_msg; // 提示内容

    public static final int SET_SUCCESS = 999;
    public static final int SET_SELECT = 998;
    public static final int SET_ERROR = 0;
    public static final int SET_replenishment = 1;
    public static final int SET_DifferenceReplenishment = 2;
    public static final int SET_UrgentReplenishment = 3;
    public static final int SET_Inventory = 4;
    public static final int SET_ReturnsForward = 5;
    public static final int SET_ReturnsReverse = 6;
    public static final int SET_PickTest = 7;
    public static final int SET_SynchronousStock = 8;
    public static final int SET_Synchronous = 9;
    public static final int SET_START_LOADING = 10;
    private TimerTask task;
    private DataServices dataServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityManagerTool.getActivityManager().add(this);
        initParam();
        initComponents();
        initObject();
        adapter = new MC_SetAdapter(MC_SettingActivity.this, dataList, listView);
        listView.setAdapter(adapter);
        stopLoading();

    }

    private void initParam() {
        wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
        vendData = (VendingData) getIntent().getSerializableExtra("vendData");

    }

    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        back = (Button) this.findViewById(R.id.back);
        listView = (ListView) this.findViewById(R.id.listView);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
    }

    /**
     * 初始化变量对象
     */
    private void initObject() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_public_title.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(150, 5, 20, 5);
        tv_public_title.setLayoutParams(lp);
        tv_public_title.setTextSize(25.0f);
        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        dataList = new ArrayList<String>();
        dataList.add(getResources().getString(R.string.set_replenishment));
        dataList.add(getResources().getString(R.string.set_difference_replenishment));
        dataList.add(getResources().getString(R.string.set_urgent_replenishment));
        dataList.add(getResources().getString(R.string.set_inventory));
//        dataList.add(getResources().getString(R.string.set_returns_forward));
        dataList.add(getResources().getString(R.string.set_returns_forward_list));
        dataList.add(getResources().getString(R.string.set_returns_reverse));
        dataList.add(getResources().getString(R.string.set_pick_test));
        if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= Constant.VERSION_STOCK_SYNC) {
        } else {
            dataList.add(getResources().getString(R.string.set_synchronous_stock));
        }
        dataList.add(getResources().getString(R.string.set_synchronous));
        dataList.add(getResources().getString(R.string.set_init));
        dataList.add(getResources().getString(R.string.set_finish));

        tv_public_title
                .setText(getResources().getString(R.string.set_vending_number) + vendData.getVd1Code());

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // System.out.println(position);
                hiddenAlertMsg();
                
                switch (position) {
                case 0:
                    setReplenishment();
                    break;
                case 1:
                    setDifferenceReplenishment();
                    break;
                case 2:
                    setUrgentReplenishment();
                    break;
                case 3:
                    setInventory();
                    break;
                case 4:
//                    setReturnsForward();
                    setReturnForwardList();
                    break;
                case 5:
                    setReturnsReverse();
                    break;
                case 6:
                    setPickTest();
                    break;
                case 7:
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= Constant.VERSION_STOCK_SYNC) {
                        setSynchronous();
                    }else {
                        setSynchronousStock();
                    }
                    break;
                case 8:
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= Constant.VERSION_STOCK_SYNC) {
                        setInit();
                    }else {
                        setSynchronous();
                    }
                    break;
                case 9:
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= Constant.VERSION_STOCK_SYNC) {
                        setFinishApp();
                    }else {
                        setInit();
                    }
                    break;
                case 10:
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= Constant.VERSION_STOCK_SYNC) {
                    }else {
                        setFinishApp();
                    }
                    break;
//                case 11:
//                    setReturnForwardList();
//                    break;

                default:
                    break;
                }
            }

        });
    }

    public void backClicked(View view) {
        finish();
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
     * 一键补货方法
     */
    private void setReplenishment() {
        startLoading();
        Thread downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                ServiceResult<Boolean> result = ReplenishmentService.getInstance().oneKeyReplenishment(
                        wrapperData);
                if (!result.isSuccess()) {

                    Message msg = handler.obtainMessage();
                    msg.obj = result.getMessage();
                    msg.what = SET_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.obj = "一键补货完成,补货单号：" + result.getMessage();
                msg.what = SET_replenishment;
                handler.sendMessage(msg);

            }
        });
        downLoadData.start();
    }

    /**
     * handler 回调处理
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SET_ERROR:
                stopLoading();
                resetAlertMsg((String) msg.obj);
                break;
            case SET_replenishment:
                stopLoading();
                resetAlertMsg((String) msg.obj);
                break;
            case SET_Synchronous:
                startService();
                break;
            case SET_SynchronousStock:
                break;
            case SET_START_LOADING:
                stopLoading();
                resetAlertMsg("数据同步完成");
                break;

            default:
                break;
            }

        }
    };

    /**
     * 差异补货方法
     */
    private void setDifferenceReplenishment() {
        Intent intent = new Intent();

        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_DifferenceReplenishmentOrderActivity.class);
        startActivity(intent);
    }

    /**
     * 退货单列表
     */
    private void setReturnForwardList() {
        Intent intent = new Intent();

        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_ReturnForwardListActivity.class);
        startActivity(intent);
    }

    /**
     * 紧急补货方法
     */
    private void setUrgentReplenishment() {
        Intent intent = new Intent();

        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_UrgentReplenishmentActivity.class);
        startActivity(intent);
    }

    /**
     * 盘点方法
     */
    private void setInventory() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_InventoryActivity.class);
        startActivity(intent);
    }

    /**
     * 正向退货
     */
    private void setReturnsForward() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_ReturnsForwardActivity.class);
        startActivity(intent);
    }

    /**
     * 反向退货
     */
    private void setReturnsReverse() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_SettingActivity.this, MC_ReturnsReverseActivity.class);
        startActivity(intent);
    }

    /**
     * 领料测试
     */
    private void setPickTest() {
        Intent intent = new Intent();
        intent.setClass(MC_SettingActivity.this, MC_PickTestActivity.class);
        startActivity(intent);
    }

    /**
     * 库存 同步
     */
    private void setSynchronousStock() {
        if (!isAccessNetwork()) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
            return;
        }
        startLoading();
        synchronousStock();

//        StockTransactionDataParse.getInstance().isSync = true;
//
//
//        VendingData vending = new VendingDbOper().getVending();
//        VendingChnStockDataParse parse = new VendingChnStockDataParse();
//        parse.setListener(this);
//        parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYN_STOCK,
//                vending.getVd1Id());

    }

    private void synchronousStock() {
//        VendingData vending = new VendingDbOper().getVending();
        ZillionLog.i("上传交易记录--同步库存："+StockTransactionDataParse.getInstance().isSync);
        if (!StockTransactionDataParse.getInstance().isSync) { //没有上传交易记录任务在跑
            //上传交易记录
            List<StockTransactionData> datas = new StockTransactionDbOper()
                    .findStockTransactionDataToUpload();
            if (datas == null || datas.size() == 0) {
                ZillionLog.i("没有交易记录，直接同步库存");
                //上传完之后同步库存
                VendingChnStockDataParse parse = new VendingChnStockDataParse();
                parse.setListener(this);
                parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                        Constant.METHOD_WSID_SYN_STOCK, vendData.getVd1Id());
            } else {
                ZillionLog.i("有交易记录，先同步交易记录");
                StockTransactionDataParse stockTransactionDataParse = StockTransactionDataParse.getInstance();
                stockTransactionDataParse.setListener(this);
                stockTransactionDataParse.requestStockTransactionData(Constant.HTTP_OPERATE_TYPE_INSERT,
                        Constant.METHOD_WSID_STOCKTRANSACTION, vendData.getVd1Id(), datas);
            }
        } else {//有的话等会儿再检查一下
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
            }
            ZillionLog.i("再次同步库存");
            //再次同步库存
            synchronousStock();
        }
    }

    /**
     * 数据同步
     */
    private void setSynchronous() {
        if (!isAccessNetwork()) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
            return;
        }
        Message message = new Message();
        message.what = SET_Synchronous;
        handler.sendMessage(message);
    }

    /**
     * 初始化
     */
    private void setInit() {
        if (!isAccessNetwork()) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
            return;
        }

        startLoading();
        VendingData vending = new VendingDbOper().getVending();
        InitDataParse parse = new InitDataParse();
        parse.setListener(this);
        parse.requestInitData(Constant.HTTP_OPERATE_TYPE_CHECK, Constant.METHOD_WSID_CHECK_INIT,
                vending.getVd1Code());
    }

    /**
     * 初始化
     */
    private void setFinishApp() {
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

    /**
     * 删除数据库，返回到初始化页面
     */
    private void initFinishMe() {
        AssetsDatabaseManager.getManager().closeDatabase(MC_Config.SQLITE_DATABASE_NAME);
        AssetsDatabaseManager.getManager().delDatabase();
        savePwdKey("");
        Intent intent = new Intent();
        setResult(9999, intent);
        this.finish();
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
     * 读取本地pwdkey
     * 
     * @return
     */
    private String getPwdKey() {
        final SharedPreferences shared = getSharedPreferences(Constant.SHARED_PWD, MODE_PRIVATE);
        return shared.getString(Constant.SHARED_PWD_KEY, "");
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        openKeyBoard();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    /**
     * 打开键盘
     */
    private void openKeyBoard() {
        SerialTools.getInstance().openKeyBoard();
        SerialTools.getInstance().addToolsListener(this);
    }

    /**
     * 串口返回
     * 
     * @param value
     *            返回值
     * @param serialType
     *            串口类型
     */
    @Override
    public void serialReturn(String value, int serialType) {

        // 判断串口类型
        switch (serialType) {
        case SerialTools.MESSAGE_LOG_mKeyBoard:
            // 当串口类型为键盘时，判断是否为功能键
            keyBoardReturn(value, serialType);
            break;
        case SerialTools.MESSAGE_LOG_mRFIDReader:
            break;
        case SerialTools.MESSAGE_LOG_mVender:
            break;
        case SerialTools.MESSAGE_LOG_mStore:
            break;
        }
    }

    private void keyBoardReturn(String value, int serialType) {

        if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            // 功能键－－取消
            finish();

        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            // 功能键－－确认

        } else {
            // resetTextView(SerialTools.getInstance().getKeyValue(value),
            // serialType);
        }

    }

    /**
     * 启动绑定服务
     */
    private void startService() {
        if (dataServices != null) {
            startSynData();
            return;
        }
        Intent intent1 = new Intent(MC_SettingActivity.this, DataServices.class);
        bindService(intent1, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        /** 获取服务对象时的操作 */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dataServices = ((DataServices.ServiceBinder) service).getService();
            startSynData();
        }

        /** 无法获取到服务对象时的操作 */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataServices = null;
        }

    };

    /**
     * 数据同步方法
     */
    private void startSynData() {
        if (dataServices != null) {
            startLoading();
            resetAlertMsg("数据同步中...");
            dataServices.setSettingListener(this);
            dataServices.synData();
        }
    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {

    }

    @Override
    public void requestFinished() {
        stopLoading();
        dataServices.setSettingListener(null);
        resetAlertMsg("数据同步完成。");

    }

    @Override
    public void requestFailure() {
        stopLoading();
        dataServices.setSettingListener(null);
        resetAlertMsg("数据同步失败。");

    }

    @Override
    public void parseRequestFinised(BaseData baseData) {
        stopLoading();
        if (!baseData.isSuccess() && Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("售货机已经初始化，不允许重复初始化，请与管理员联系！");
            return;
        }

        if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_init_msg))
                    .setPositiveButton(getString(R.string.BTN_INIT), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initFinishMe();
                        }
                    })
                    .setNegativeButton(getString(R.string.PUBLIC_CANCEL),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            }).create();
            dialog.show();
        } else if (Constant.METHOD_WSID_SYN_STOCK.equals(baseData.getRequestURL())) {
            resetAlertMsg("库存同步成功！");
        } else if (Constant.METHOD_WSID_STOCKTRANSACTION.equals(baseData.getRequestURL())) {

//            startLoading();
//            ZillionLog.i("上传完之后同步库存");
            //上传完之后同步库存
            VendingChnStockDataParse parse = new VendingChnStockDataParse();
            parse.setListener(this);
            parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                    Constant.METHOD_WSID_SYN_STOCK, vendData.getVd1Id());
        }

    }

    /**
     * 一般是网络请求中断或失败才会进入到此方法
     * 
     * @param baseData
     * @see com.mc.vending.parse.listener.DataParseRequestListener#parseRequestFailure(com.mc.vending.data.BaseData)
     */
    @Override
    public void parseRequestFailure(BaseData baseData) {
        stopLoading();
        if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("请求初始化权限失败！");
        } else if (Constant.METHOD_WSID_SYN_STOCK.equals(baseData.getRequestURL())) {
            String message = baseData.getReturnMessage();
            if (message != null && !message.equals("")) {
                resetAlertMsg(message);
            } else {
                resetAlertMsg("同步库存操作失败！");
            }
        }

    }
}
