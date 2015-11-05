package com.mc.vending.activitys.pick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_CombinationPickDetailAdapter;
import com.mc.vending.config.Constant;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.ProductGroupWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;

/**
 * 组合领料详情
 * 
 * @author apple
 *
 */
public class MC_CombinationPickDetailActivity extends BaseActivity implements MC_SerialToolsListener {

    private TextView                        tv_public_title;    //公共头部标题
    private EditText                        et_password;
    private ListView                        listView;           //列表
    private TextView                        alert_msg_title;    //提示标题
    private TextView                        alert_msg;          //提示内容
    private ProductGroupHeadWrapperData     headWrapperData;    //组合商品对象，主表＋从表
    private VendingCardPowerWrapperData     wrapperData;        //组合权限对象
    private VendingData                     vendData;           //售货机对象
    private String                          combinationNumber;  //组合编号
    private MC_CombinationPickDetailAdapter adapter;
    private boolean                         isRFID;
    private List<VendingChnData>            vendChnList;
    boolean                                 isOperating = false; //是否再操作中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_pick_detail);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        stopLoading();
    }

    private void getParam() {
        headWrapperData = (ProductGroupHeadWrapperData) getIntent().getSerializableExtra(
                "ProductGroupHeadWrapperData");
        vendData = (VendingData) getIntent().getSerializableExtra("vendData");
        combinationNumber = (String) getIntent().getSerializableExtra("combinationNumber");
    }

    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        et_password = (EditText) this.findViewById(R.id.et_password);
        listView = (ListView) this.findViewById(R.id.listView);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);

    }

    private void initObject() {
        adapter = new MC_CombinationPickDetailAdapter(MC_CombinationPickDetailActivity.this,
                headWrapperData.getWrapperList(), listView);
        listView.setAdapter(adapter);
        tv_public_title.setTextSize(30);
        tv_public_title.setText("组合编号：" + combinationNumber);
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
        openRFID();
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

    private void openRFID() {
        SerialTools.getInstance().openRFIDReader();
        SerialTools.getInstance().addToolsListener(this);
    }

    private void closeRFID() {
        try {
            SerialTools.getInstance().closeRFIDReader();
        } catch (SerialPortException e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }
    }

    private void closeVender() {
        try {
            SerialTools.getInstance().closeVender();
        } catch (SerialPortException e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }
    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {

        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        //判断串口类型
        switch (serialType) {
        case SerialTools.MESSAGE_LOG_mKeyBoard:
            //当串口类型为键盘时，判断是否为功能键
            if (isOperating) {
                return;
            }
            handler.sendMessage(msg);
            isRFID = false;
            break;
        case SerialTools.MESSAGE_LOG_mRFIDReader:
            value = MyFunc.getRFIDSerialNo(value);

            if (!StringHelper.isEmpty(value, true)) {
                resetTextView(value, serialType);
                closeRFID(); //rfid读取完成关闭rfid
                isRFID = true;
                handler.sendMessage(msg);
            }
            break;
        case SerialTools.MESSAGE_LOG_mVender:
            //                Map<String, Object> map = new HashMap<String, Object>();
            //                map.put("keyValue", value);
            //                map.put("vendingChn", userInfo);
            //                msg.obj = map;
            //                handler.sendMessage(msg);
            break;
        case SerialTools.MESSAGE_LOG_mVender_check:
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("keyValue", value);
            map.put("vendingChn", userInfo);
            msg.obj = map;
            handler.sendMessage(msg);
            break;
        case SerialTools.MESSAGE_LOG_mStore:
            handler.sendMessage(msg);
            break;
        }

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

    }

    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                            case SerialTools.MESSAGE_LOG_mKeyBoard:
                                keyBoardReturn((String) msg.obj, msg.what);
                                break;
                            case SerialTools.MESSAGE_LOG_mRFIDReader:
                                cardPasswordValidate();
                                break;
                            case SerialTools.MESSAGE_LOG_mVender:
                                //                                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                                //                                    openVender((String) map.get("keyValue"),
                                //                                        (VendingChnData) map.get("vendingChn"));

                                break;
                            case SerialTools.MESSAGE_LOG_mVender_check:
                                Map<String, Object> map = (Map<String, Object>) msg.obj;
                                openVender((String) map.get("keyValue"),
                                        (VendingChnData) map.get("vendingChn"));

                                break;
                            case SerialTools.MESSAGE_LOG_mStore:

                            default:
                                break;
                            }
                        }
                    };

    private void keyBoardReturn(String value, int serialType) {

        if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {
            //组合
        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {
            //借
        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {
            //还
        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {
            //设置
        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            //功能键－－取消
            hiddenAlertMsg();
            if (!StringHelper.isEmpty(et_password.getText().toString(), true)) {
                et_password.setText("");
                openRFID();
            } else {
                closeRFID();
                finish();
            }

        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            //功能键－－确认
            cardPasswordValidate();
        } else {
            resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
        }
    }

    /**
     * 重置输入框文本
     * 
     * @param value
     * @param serialType
     */
    private void resetTextView(String value, int serialType) {
        if (SerialTools.MESSAGE_LOG_mKeyBoard == serialType) {
            et_password.setText(et_password.getText().toString() + value);
        } else if (SerialTools.MESSAGE_LOG_mRFIDReader == serialType) {
            if (!StringHelper.isEmpty(value, true)) {
                et_password.setText(value);
            }
        }
    }

    /**
     * 验证卡密码权限
     */
    private void cardPasswordValidate() {
        //卡密码权限验证
        closeRFID();
        if (StringHelper.isEmpty(et_password.getText().toString(), true)) {
            return;
        }
        ServiceResult<VendingCardPowerWrapperData> wrapperResult = CompositeMaterialService.getInstance()
                .checkCardPowerOut(isRFID ? CardData.CARD_SERIALNO_PARAM : CardData.CARD_PASSWORD_PARAM,
                        et_password.getText().toString(), vendData.getVd1Id());
        if (!wrapperResult.isSuccess()) {
            resetAlertMsg(wrapperResult.getMessage());
            return;
        }

        wrapperData = wrapperResult.getResult();

        // 验证产品组合权限
        ServiceResult<Boolean> result = CompositeMaterialService.getInstance().checkProductGroupPower(
                wrapperData, headWrapperData, vendData.getVd1Id());
        if (!result.isSuccess()) {
            resetAlertMsg(result.getMessage());
            return;
        }
        vendChnList = new ArrayList<VendingChnData>();
        for (ProductGroupWrapperData data : headWrapperData.getWrapperList()) {

            for (VendingChnWrapperData wrapperData : data.getChnWrapperList()) {
                VendingChnData vendingChn = wrapperData.getChnStock().getVendingChn();

                int qty = wrapperData.getQty();
                //步骤三确认，进行后续判断,发起领料动作，
                if (vendingChn.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_VENDING)) {
                    //售货机
                    vendingChn.setInputQty(qty);
                    vendChnList.add(vendingChn);

                }

            }
        }
        isOperating = true;
        openVender(null, null);
    }

    /**
     * 开启售货机
     * 
     * @param status
     */
    private void openVender(String status, VendingChnData mVendingChn) {

        if (status == null && vendChnList.size() > 0) {
            VendingChnData vendingChn = vendChnList.get(0);
            SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0), vendingChn);
            try {
                Thread.sleep(Constant.TIME_INTERNAL);
            } catch (InterruptedException e) {
                ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
                e.printStackTrace();
            }
        }

        if (status != null && (status.contains("31") || status.contains("32"))) {
            for (int i = 0; i < vendChnList.size(); i++) {
                VendingChnData vendingChn = vendChnList.get(i);
                if (mVendingChn.getVc1Code().equals(vendingChn.getVc1Code())) {
                    if (vendingChn.getInputQty() > 0) {
                        if (status.contains("31")) {
                            pickSuccess(vendingChn);//每次成功－1
                        } else {
                            vendingChn.setFailureQty(vendingChn.getFailureQty() + 1);
                        }
                        vendingChn.setInputQty(vendingChn.getInputQty() - 1);
                        break;
                    }
                }
            }

            boolean flag = false;
            int failureCount = 0;
            for (int i = 0; i < vendChnList.size(); i++) {
                VendingChnData vendingChn = vendChnList.get(i);
                failureCount += vendingChn.getFailureQty();
                if (vendingChn.getInputQty() > 0) {
                    SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                            ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0), vendingChn);
                    try {
                        Thread.sleep(Constant.TIME_INTERNAL);
                    } catch (InterruptedException e) {
                        ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
                        e.printStackTrace();
                    }
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                isOperating = false;
                closeVender();
                if (failureCount > 0) {
                    resetAlertMsg("领料成功,失败数量：" + failureCount);
                } else {
                    resetAlertMsg("领料成功！");
                }
                et_password.setText("");
                isRFID = false;
                successBack();
            }

        }

    }

    /**
     * 领料成功
     */
    private void pickSuccess(VendingChnData vendingChn) {
        //保存领料数据
        CompositeMaterialService.getInstance().saveStockTransaction(1, vendingChn, wrapperData);

    }

    private void pickFailure() {

    }

    /**
     * 检查格子机状态
     */
    private void successBack() {
        Thread downLoadData = new Thread(new Runnable() {
            @Override
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
     * 
     * @param view
     */
    public void closeMe() {
        Intent intent = new Intent(MC_CombinationPickDetailActivity.this, MC_CombinationPickActivity.class);
        this.setResult(1001, intent);
        finish();
    }

}
