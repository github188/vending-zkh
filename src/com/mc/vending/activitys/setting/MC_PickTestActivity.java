package com.mc.vending.activitys.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.service.TestMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;

/**
 * 领料测试
 * 
 * @author apple
 *
 */
public class MC_PickTestActivity extends BaseActivity implements MC_SerialToolsListener {
    private Button         back;
    private Button         operate;
    private TextView       tv_public_title;  // 公共头部标题
    private RelativeLayout layout_step1;     // 步骤1布局

    private EditText       et_channle_number; // 步骤1输入框

    private TextView       alert_msg_title;  // 提示标题
    private TextView       alert_msg;        // 提示内容
    private VendingChnData vendingChn;       // 售货机货道

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_test);
        ActivityManagerTool.getActivityManager().add(this);
        initComponents();
        initObject();
        stopLoading();
    }

    /**
     * 初始化对象
     */
    private void initComponents() {
        back = (Button) this.findViewById(R.id.back);
        operate = (Button) this.findViewById(R.id.operate);
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        layout_step1 = (RelativeLayout) this.findViewById(R.id.layout_step1);
        et_channle_number = (EditText) this.findViewById(R.id.et_channle_number);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);

    }

    /**
     * 初始化变量对象
     */
    private void initObject() {
        tv_public_title.setText(getResources().getString(R.string.set_pick_test));
        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        operate.setVisibility(View.VISIBLE);
        operate.setEnabled(true);
        operate.setText(getResources().getString(R.string.btn_test));
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

    private void openKeyBoard() {
        SerialTools.getInstance().openKeyBoard();
        SerialTools.getInstance().addToolsListener(this);
    }

    private void closeKeyBoard() {

        try {
            SerialTools.getInstance().closeKeyBoard();
            SerialTools.getInstance().addToolsListener(this);
        } catch (SerialPortException e) {
            ZillionLog.e(this.getClass().getName(),e.getMessage(),e);
            e.printStackTrace();
        }
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

    public void saveClicked(View view) {
        // 功能键－－确认
        pickTest();
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
            // 功能键－－组合

        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {
            // 功能键－－借

        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {
            // 功能键－－还

        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {
            // 功能键－－设置

        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            // 功能键－－取消
            hiddenAlertMsg();
            if (!StringHelper.isEmpty(et_channle_number.getText().toString(), true)) {
                et_channle_number.setText("");
            }
        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            // 功能键－－确认
            pickTest();
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
            et_channle_number.setText(et_channle_number.getText().toString() + value);
        }
    }

    /**
     * 领料测试方法
     */
    private void pickTest() {
        ServiceResult<VendingChnData> result = TestMaterialService.getInstance().testMaterial(
                et_channle_number.getText().toString());
        if (!result.isSuccess()) {
            resetAlertMsg(result.getMessage());
            return;
        }
        vendingChn = result.getResult();
        // 出货todo...
        // 步骤三确认，进行后续判断,发起领料动作，
        if (vendingChn.getVc1Type().equals(VendingChnData.VENDINGCHN_TYPE_VENDING)) {
            // 售货机
            ZillionLog.i("售货机领料测试" + vendingChn.getVc1Code());
            SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0));

            // SerialTools.getInstance().openVender(3, 1);
        } else {
            // 格子机器
            ZillionLog.i("格子机领料测试" + vendingChn.getVc1Code());
            SerialTools.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
                    ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
                    ConvertHelper.toInt(vendingChn.getVc1Height(), 0));

            // SerialTools.getInstance().openStore(2, 1, 1);
            // resetAlertMsg("格子机不支持领料测试！");
        }
    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {
        // TODO Auto-generated method stub

    }
}
