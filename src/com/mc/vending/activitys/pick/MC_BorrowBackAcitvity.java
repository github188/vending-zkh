package com.mc.vending.activitys.pick;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.CardData;
import com.mc.vending.data.TransactionWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.service.BorrowMaterialService;
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
 * 借还
 * 
 * @author apple
 *
 */
public class MC_BorrowBackAcitvity extends BaseActivity implements MC_SerialToolsListener {

    private static final String        TAG                       = MC_BorrowBackAcitvity.class.getName();
    private enum OPERATE_STEP {
        OPERATE_STEP_1, OPERATE_STEP_2
    }

    private Bundle                      selectBundle;
    public final static int             OPERATE_BORROW = 0; // 借料
    public final static int             OPERATE_BACK   = 1; // 还料
    private TextView                    tv_public_title;   // 公共头部标题
    private RelativeLayout              layout_step1;      // 步骤1布局
    private RelativeLayout              layout_step2;      // 步骤2布局

    private EditText                    et_channle_number; // 步骤1输入框
    private EditText                    et_card_password;  // 步骤2输入框

    private TextView                    alert_msg_title;   // 提示标题
    private TextView                    alert_msg;         // 提示内容

    private OPERATE_STEP                operateStep;       // 操作步骤。默认为0
    private int                         operateType;
    private String                      operateTypeStr;

    private VendingChnData              vendingChn;        // 售货机货道对象
    private VendingCardPowerWrapperData wrapperData;       // 卡密码权限对象
    private TransactionWrapperData      transcationData;   // 货道借还状态
    private boolean                     isRFID;            // 是否rfid操作
    private boolean                     isStoreChecked;    // 格子机验证返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_back);
        ActivityManagerTool.getActivityManager().add(this);
        initParam();
        initComponents();
        initObject();
        resetViews();
        stopLoading();
    }

    private void initParam() {

        selectBundle = getIntent().getExtras();

        if (selectBundle != null) {
            // 请求列表来源
            if (selectBundle.containsKey("operateType")) {
                operateType = selectBundle.getInt("operateType");
            }
        }
    }

    /**
     * 初始化对象
     */
    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        layout_step1 = (RelativeLayout) this.findViewById(R.id.layout_step1);
        layout_step2 = (RelativeLayout) this.findViewById(R.id.layout_step2);
        et_channle_number = (EditText) this.findViewById(R.id.et_channle_number);
        et_card_password = (EditText) this.findViewById(R.id.et_card_password);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);

    }

    /**
     * 初始化变量对象
     */
    private void initObject() {
        operateStep = OPERATE_STEP.OPERATE_STEP_1;
        switch (operateType) {
        case MC_BorrowBackAcitvity.OPERATE_BORROW:
            operateTypeStr = VendingChnData.VENDINGCHN_COMMAND_BORROW;
            tv_public_title.setText(getResources().getString(R.string.operate_borrow));
            break;
        case MC_BorrowBackAcitvity.OPERATE_BACK:
            operateTypeStr = VendingChnData.VENDINGCHN_COMMAND_RETURN;
            tv_public_title.setText(getResources().getString(R.string.operate_back));
            break;

        default:
            break;
        }

    }

    /**
     * 清空文本输入框
     */
    private void clearEditText() {
        et_channle_number.setText("");
        et_card_password.setText("");
    }

    /**
     * 重置页面状态
     */
    private void resetInputStatus() {
        operateStep = OPERATE_STEP.OPERATE_STEP_1;
        clearEditText();
        hiddenAlertMsg();
        resetViews();
    }

    /**
     * 初始化ui
     */
    private void resetViews() {
        switch (operateStep) {
        case OPERATE_STEP_1:
            layout_step1.setVisibility(View.VISIBLE);
            layout_step2.setVisibility(View.INVISIBLE);
            break;
        case OPERATE_STEP_2:
            layout_step1.setVisibility(View.INVISIBLE);
            layout_step2.setVisibility(View.VISIBLE);
            break;

        default:
            break;
        }
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
        resetInputStatus();
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
//            e.printStackTrace();
            ZillionLog.e(TAG,e.getMessage(),e);
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

        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        // 判断串口类型
        switch (serialType) {
        case SerialTools.MESSAGE_LOG_mKeyBoard:
            // 当串口类型为键盘时，判断是否为功能键
            handler.sendMessage(msg);
            isRFID = false;

            break;
        case SerialTools.MESSAGE_LOG_mRFIDReader:
            value = MyFunc.getRFIDSerialNo(value);

            if (!StringHelper.isEmpty(value, true)) {
                resetTextView(value, serialType);
                closeRFID(); // rfid读取完成关闭rfid
                isRFID = true;
                handler.sendMessage(msg);
            }
            break;
        case SerialTools.MESSAGE_LOG_mVender:
            handler.sendMessage(msg);
            break;
        case SerialTools.MESSAGE_LOG_mStore:
            // handler.sendMessage(msg);
            break;
        case SerialTools.MESSAGE_LOG_mStore_check:
            handler.sendMessage(msg);
            break;
        }
    }

    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                            case SerialTools.MESSAGE_LOG_mKeyBoard:
                                hiddenAlertMsg();
                                keyBoardReturn((String) msg.obj, msg.what);
                                break;
                            case SerialTools.MESSAGE_LOG_mRFIDReader:
                                hiddenAlertMsg();
                                cardPasswordValidate();
                                break;
                            case SerialTools.MESSAGE_LOG_mVender:
                                break;
                            case SerialTools.MESSAGE_LOG_mStore:
                                // hiddenAlertMsg();
                                // // openStore((String) msg.obj);
                                // checkStore();
                                break;

                            case SerialTools.MESSAGE_LOG_mStore_check:
                                openedStore((String) msg.obj);
                                break;
                            default:
                                break;
                            }
                        }
                    };

    private void keyBoardReturn(String value, int serialType) {

        if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {

        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            // 功能键－－取消
            hiddenAlertMsg();
            switch (operateStep) {
            case OPERATE_STEP_1:
                if (!StringHelper.isEmpty(et_channle_number.getText().toString(), true)) {
                    et_channle_number.setText("");
                } else {
                    finish();
                }
                break;
            case OPERATE_STEP_2:
                if (!StringHelper.isEmpty(et_card_password.getText().toString(), true)) {
                    et_card_password.setText("");
                    openRFID(); // 进入步骤3，打开rfid
                } else {
                    closeRFID();
                    operateStep = OPERATE_STEP.OPERATE_STEP_1;
                    resetViews();
                }

                break;
            default:
                break;
            }

        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            // 功能键－－确认
            switch (operateStep) {
            case OPERATE_STEP_1:
                channelValidate();

                break;
            case OPERATE_STEP_2:
                cardPasswordValidate();
                break;
            default:

                break;
            }
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
        switch (operateStep) {
        case OPERATE_STEP_1:
            et_channle_number.setText(et_channle_number.getText().toString() + value);
            break;
        case OPERATE_STEP_2:
            if (SerialTools.MESSAGE_LOG_mKeyBoard == serialType) {
                et_card_password.setText(et_card_password.getText().toString() + value);
            } else if (SerialTools.MESSAGE_LOG_mRFIDReader == serialType) {
                if (!StringHelper.isEmpty(value, true)) {
                    et_card_password.setText(value);
                }
            }
            break;

        default:
            break;
        }
    }

    /**
     * 货到验证
     * 
     * @return
     */
    private void channelValidate() {
        if (StringHelper.isEmpty(et_channle_number.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_channle_number));
        } else {
            // 判断货到状态－类型，返回商品id，货道id
            ServiceResult<VendingChnData> vendingResult = BorrowMaterialService.getInstance()
                    .checkVendingChn(et_channle_number.getText().toString(),
                            VendingChnData.VENDINGCHN_METHOD_BORROW);
            if (!vendingResult.isSuccess()) {
                resetAlertMsg(vendingResult.getMessage());
                return;
            }
            vendingChn = vendingResult.getResult();

            // 查询货道借还状态
            transcationData = BorrowMaterialService.getInstance().getStockTransaction(vendingChn);

            ServiceResult<Boolean> result = BorrowMaterialService.getInstance().checkBorrowStatus(vendingChn,
                    transcationData, operateTypeStr);

            if (!result.isSuccess()) {
                resetAlertMsg(result.getMessage());
                return;
            }

            // 售货机可用，货到状态正常，进入下一步输入领料数
            operateStep = OPERATE_STEP.OPERATE_STEP_2;
            resetViews();
            hiddenAlertMsg();
            openRFID();
        }
    }

    /**
     * 步骤2验证，密码或刷卡验证
     */
    private void cardPasswordValidate() {
        closeRFID();
        if (StringHelper.isEmpty(et_card_password.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_card_pwd));
        } else {
            // 检查卡/密码-权限
            ServiceResult<VendingCardPowerWrapperData> result = BorrowMaterialService.getInstance()
                    .checkCardPowerOut(isRFID ? CardData.CARD_SERIALNO_PARAM : CardData.CARD_PASSWORD_PARAM,
                            et_card_password.getText().toString(), vendingChn.getVc1Vd1Id());
            if (!result.isSuccess()) {
                resetAlertMsg(result.getMessage());
                return;
            }
            wrapperData = result.getResult();

            // 检查是否原借出人归还
            ServiceResult<Boolean> borrowResult = BorrowMaterialService.getInstance().checkBorrowCustomer(
                    vendingChn.getVc1BorrowStatus(), wrapperData.getCusEmpId(), operateTypeStr,
                    transcationData);
            if (!borrowResult.isSuccess()) {
                resetAlertMsg(borrowResult.getMessage());
                return;
            }

            hiddenAlertMsg();
            // 步骤2确认，进行后续判断,发起领料动作，
            if (vendingChn.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_CELL)) {
                // 格子机器
                isStoreChecked = false;
                SerialTools.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                        ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
                        ConvertHelper.toInt(vendingChn.getVc1Height(), 0));
                //不判断返回信号直接开门
                openStoreDirect();
            }

        }
    }
    
    /**
     * 不判断返回信号直接开门
     */
    private void openStoreDirect() {
        pickSuccess();
        resetInputStatus();
        if (MC_BorrowBackAcitvity.OPERATE_BORROW == operateType) {
            resetAlertMsg("借料成功！");
        } else {
            resetAlertMsg("还料成功！");
        }
        isStoreChecked = true;
        successBack();
        return;
    }

    /**
     * 打开格子机
     * 
     * @param input
     */
    private void openedStore(String input) {
//        // System.out.println("openedStore==========" + input);
//        // 04-18 11:09:45.240: W/System.err(3838): openedStore=====00 02 00 01
//        // A2 00 01 01 01 01 01 01 01 01 01 01 01 01 01 01 01 01 01 00 00 00 B6
////        ZillionLog.i(TAG,"storeMsg:"+SerialTools.getInstance().storeMsg.toString().replace("[", "").replace("]", "").replace(",", ""));
//        
//        boolean success = false;
//        List<String> storeMsgList = SerialTools.getInstance().storeMsg;
//        String [] storeMsg = storeMsgList.toString().replace("[", "").replace("]", "").replace(",", "").split(" ");
////        00 02 00 01 A1 01 00 A5 
////        00 02 00 01 A2 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A6 
////        00 02 00 01 A2 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 A6
//        
//        if (storeMsgList != null && storeMsgList.size() > 0 && (storeMsg.length - 8) % 27 == 0) {
//            for (int i = 0; i < (storeMsg.length - 8) / 27; i++) {
//                String[] arrayB = Arrays.copyOfRange(storeMsg, 8 + 27 * i, 8 + 27 * (i + 1));
//                ZillionLog.i(TAG, "i=" + i + " " + Arrays.asList(arrayB).toString());
//                if (vendingChn == null) {
//                    ZillionLog.i(TAG,"vendingChn null");
//                    return;
//                }
//                success = arrayB[ConvertHelper.toInt(vendingChn.getVc1Height(), 0) + 4].equals("00");
//                if (success) {
//                    pickSuccess();
//                    resetInputStatus();
//                    if (MC_BorrowBackAcitvity.OPERATE_BORROW == operateType) {
//                        resetAlertMsg("借料成功！");
//                    } else {
//                        resetAlertMsg("还料成功！");
//                    }
//                    isStoreChecked = true;
//                    successBack();
//                    return;
//                }
//            }
//        }
////        if (input != null && !isStoreChecked) {
////            String[] array = input.split(" ");
////            if (array.length > 7 && array.length >= (ConvertHelper.toInt(vendingChn.getVc1Height(), 0) + 7)) {
////                success = array[ConvertHelper.toInt(vendingChn.getVc1Height(), 0) + 4].equals("00");
//////                boolean fail = array[ConvertHelper.toInt(vendingChn.getVc1Height(), 0) + 4].equals("01");
////                if (success) {
////                    pickSuccess();
////                    resetInputStatus();
////                    if (MC_BorrowBackAcitvity.OPERATE_BORROW == operateType) {
////                        resetAlertMsg("借料成功！");
////                    } else {
////                        resetAlertMsg("还料成功！");
////                    }
////                    isStoreChecked = true;
////                    successBack();
////                }
////            }
////        }
//        if (!success) {
//            resetInputStatus();
//            if (MC_BorrowBackAcitvity.OPERATE_BORROW == operateType) {
//                resetAlertMsg("借料失败！");
//            } else {
//                resetAlertMsg("还料失败！");
//            }
//        }
    }

    /**
     * 领料成功
     */
    private void pickSuccess() {
        isStoreChecked = true;
        // 保存领料数据
        BorrowMaterialService.getInstance().saveStockTransaction(vendingChn, wrapperData, operateTypeStr);
        // 恢复默认状态
        vendingChn = null;
        wrapperData = null;
        transcationData = null;
        isRFID = false;
    }

    private void pickFailure() {

    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {
        // TODO Auto-generated method stub

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
//                    e.printStackTrace();
                    ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
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
        finish();
    }
}
