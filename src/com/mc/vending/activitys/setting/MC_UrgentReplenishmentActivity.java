package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_UrgentReplenishmentAdapter;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;

/**
 * 紧急补货
 * @author apple
 *
 */
public class MC_UrgentReplenishmentActivity extends BaseActivity implements MC_SerialToolsListener {
    private TextView                           tv_public_title; //公共头部标题
    private Button                             back;           //公共头部返回键
    private Button                             operate;

    private TextView                           alert_msg_title; //提示标题
    private TextView                           alert_msg;      //提示内容

    private TextView                           tv_number_title;
    private EditText                           et_order_number;
    private ListView                           listView;       //列表
    private MC_UrgentReplenishmentAdapter      adapter;
    private List<VendingChnProductWrapperData> dataList;
    private VendingCardPowerWrapperData        wrapperData;    //权限对象

    private String                             orderNumber;    //补货单号r2015      盘点i2015    正向退货t2015

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_replenishment);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    /**
     * 获取参数
     */
    private void getParam() {
        wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        back = (Button) this.findViewById(R.id.back);
        operate = (Button) this.findViewById(R.id.operate);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
        et_order_number = (EditText) this.findViewById(R.id.et_order_number);
        tv_number_title = (TextView) this.findViewById(R.id.tv_number_title);
        listView = (ListView) this.findViewById(R.id.listView);
    }

    /**
     * 初始化数据
     */
    private void initObject() {
        orderNumber = "R" + DateHelper.format(new Date(), "yyyy");
        tv_public_title.setText(getResources().getString(R.string.set_urgent_replenishment));
        tv_number_title.setText(getResources().getString(R.string.urgent_replenishment_order_id)
                                + " " + orderNumber);
        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        operate.setVisibility(View.VISIBLE);
        operate.setEnabled(true);
        operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        dataList = new ArrayList<VendingChnProductWrapperData>();

        et_order_number.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_order_number, 4);
            }
        });

        et_order_number.setOnKeyListener(onKey);
        requestLst();
    }

    private void requestLst() {

        Thread downLoadData = new Thread(new Runnable() {
            public void run() {
                dataList = ReplenishmentService.getInstance().findChnCodeProductName();
                handler.sendEmptyMessage(MC_SettingActivity.SET_SUCCESS);
            }
        });
        downLoadData.start();
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

    public void backClicked(View view) {
        finish();
    }

    /**
     * 点击保存
     * @param view
     */
    public void saveClicked(View view) {
        hiddenKeyBoard(et_order_number);
        if (StringHelper.isEmpty(et_order_number.getText().toString(), true)) {
            resetAlertMsg("请输入紧急补货单号！");
            return;
        }
        startLoading();
        Thread downLoadData = new Thread(new Runnable() {
            public void run() {

                ServiceResult<Boolean> result = ReplenishmentService.getInstance()
                    .emergencyReplenishment(
                        orderNumber
                                + StringHelper.autoCompletionCode(StringHelper.trim(et_order_number
                                    .getText().toString())), dataList, wrapperData);
                if (!result.isSuccess()) {
                    Message msg = handler.obtainMessage();
                    msg.obj = result.getMessage();
                    msg.what = MC_SettingActivity.SET_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.obj = "紧急补货完成";
                msg.what = MC_SettingActivity.SET_UrgentReplenishment;
                handler.sendMessage(msg);
            }
        });
        downLoadData.start();
    }

    /**
     * handler 回调处理
     */
    private Handler handler = new Handler() {

                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case MC_SettingActivity.SET_ERROR:
                                            stopLoading();
                                            resetAlertMsg((String) msg.obj);
                                            break;
                                        case MC_SettingActivity.SET_UrgentReplenishment:
                                            stopLoading();
                                            resetAlertMsg((String) msg.obj);
                                            successBack();
                                            break;
                                        case MC_SettingActivity.SET_SUCCESS:
                                            adapter = new MC_UrgentReplenishmentAdapter(
                                                MC_UrgentReplenishmentActivity.this, dataList,
                                                listView);
                                            listView.setAdapter(adapter);
                                            stopLoading();
                                            break;
                                        default:
                                            break;
                                    }

                                }
                            };

    /**
     * 加
     * @param view
     */
    public void sumClicked(View view) {
        //System.out.println("sumClicked＝＝＝" + String.valueOf(view.getTag()));
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        //System.out.println("sumClicked" + index);
        VendingChnProductWrapperData data = dataList.get(index);

        int number = data.getActQty();
        number += 1;
        data.setActQty(number);
        reloadTableWithLine(index);
    }

    /**
     * 减
     * @param view
     */
    public void subClicked(View view) {
        //System.out.println("subClicked＝＝＝" + view.getTag());
        int index = Integer.parseInt(String.valueOf(view.getTag()));
        //System.out.println("subClicked" + index);
        VendingChnProductWrapperData data = dataList.get(index);
        int number = data.getActQty();
        if (number > 0) {
            number -= 1;
        }
        data.setActQty(number);
        reloadTableWithLine(index);
    }

    /**
     * 更新表格
     * @param index
     */
    private void reloadTableWithLine(int index) {
        adapter.reloadViewHolder(index);
    }

    /**
     * 键盘监听
     */
    OnKeyListener onKey = new OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                                    hiddenKeyBoard(et_order_number);
                                    return true;
                                }
                                return false;
                            }
                        };

    private void openKeyBoard() {
        SerialTools.getInstance().addToolsListener(this);
        SerialTools.getInstance().openKeyBoard();
    }

    private void closeKeyBoard() {

        try {
            SerialTools.getInstance().addToolsListener(this);
            SerialTools.getInstance().closeKeyBoard();
        } catch (SerialPortException e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }
    }

    /**
     * 串口返回
     * @param value 返回值
     * @param serialType 串口类型
     */
    @Override
    public void serialReturn(String value, int serialType) {

        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        //判断串口类型
        switch (serialType) {
            case SerialTools.MESSAGE_LOG_mKeyBoard:
                //当串口类型为键盘时，判断是否为功能键

                mhandler.sendMessage(msg);

                break;
            case SerialTools.MESSAGE_LOG_mRFIDReader:
                value = MyFunc.getRFIDSerialNo(value);

                break;
            case SerialTools.MESSAGE_LOG_mVender:

                break;
            case SerialTools.MESSAGE_LOG_mStore:

                break;
        }

    }

    Handler mhandler = new Handler() {
                         @Override
                         public void handleMessage(Message msg) {
                             super.handleMessage(msg);
                             switch (msg.what) {
                                 case SerialTools.MESSAGE_LOG_mKeyBoard:
                                     //System.out.println("MESSAGE_LOG_mKeyBoard===="+ (String) msg.obj);
                                     keyBoardReturn((String) msg.obj, msg.what);
                                     break;
                                 case SerialTools.MESSAGE_LOG_mRFIDReader:

                                     break;
                                 case SerialTools.MESSAGE_LOG_mVender:

                                     break;
                                 case SerialTools.MESSAGE_LOG_mStore:

                                     break;

                                 default:
                                     break;
                             }
                         }
                     };

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {
        // TODO Auto-generated method stub

    }

    /**
     * 键盘按下判断方法
     * @param value
     * @param serialType
     */
    private void keyBoardReturn(String value, int serialType) {

        if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {
            //功能键－－组合

        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {
            //功能键－－借

        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {
            //功能键－－还

        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {
            //功能键－－设置

        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            //功能键－－取消
            et_order_number.setText("");
        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {

        } else {
            resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
        }

    }

    /**
     * 重置输入框文本
     * @param value
     * @param serialType
     */
    private void resetTextView(String value, int serialType) {
        et_order_number.setText(et_order_number.getText().toString() + value);
    }

    /**
     * 检查格子机状态
     */
    private void successBack() {
        Thread downLoadData = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
                    e.printStackTrace();
                }
                closeMe();
            }
        });
        downLoadData.start();
    }

    /**
     * 点击返回
     * @param view
     */
    public void closeMe() {
        finish();
    }
}
