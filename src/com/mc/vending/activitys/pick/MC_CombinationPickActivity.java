package com.mc.vending.activitys.pick;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_CombinationPickAdapter;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;

/**
 * 组合领料列表
 * 
 * @author apple
 *
 */
public class MC_CombinationPickActivity extends BaseActivity implements MC_SerialToolsListener {

    private EditText                   et_number;
    private TextView                   tv_public_title;       //公共头部标题
    private Button                     back;                  //公共头部返回键
    private ListView                   listView;              //列表
    private TextView                   alert_msg_title;       //提示标题
    private TextView                   alert_msg;             //提示内容
    private MC_CombinationPickAdapter  adapter;
    private List<ProductGroupHeadData> dataList;
    private String                     combinationNumber = "";

    private VendingData                vendData;              //售货机对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_pick);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        stopLoading();
    }

    /**
     * 接收参数
     */
    private void getParam() {
        vendData = (VendingData) getIntent().getSerializableExtra("vendData");
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        back = (Button) this.findViewById(R.id.back);
        listView = (ListView) this.findViewById(R.id.listView);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
        et_number = (EditText) this.findViewById(R.id.et_number);

    }

    /**
     * 初始化数据
     */
    private void initObject() {
        tv_public_title.setText(getResources().getString(R.string.combination_pick));
        //查询组合数据
        ServiceResult<List<ProductGroupHeadData>> result = CompositeMaterialService.getInstance()
                .findAllProductGroupHead();
        if (!result.isSuccess()) {
            resetAlertMsg(result.getMessage());
            return;
        }

        dataList = result.getResult();
        adapter = new MC_CombinationPickAdapter(MC_CombinationPickActivity.this, dataList, listView);
        listView.setAdapter(adapter);
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
        combinationNumber = "";
        et_number.setText("");
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

        //判断串口类型
        switch (serialType) {
        case SerialTools.MESSAGE_LOG_mKeyBoard:
            //当串口类型为键盘时，判断是否为功能键
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
            if (!StringHelper.isEmpty(combinationNumber, true)) {
                combinationNumber = "";
                et_number.setText("");
            } else {
                finish();
            }

        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            //功能键－－确认
            if (!StringHelper.isEmpty(combinationNumber, true)) {
                checkProduct();
            } else {
                resetAlertMsg("请选择组合编号,再选择输入按键！");
            }

        } else {
            resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
            if (!StringHelper.isEmpty(SerialTools.getInstance().getKeyValue(value))) {
                combinationNumber = combinationNumber + SerialTools.getInstance().getKeyValue(value);
            }
        }

    }

    /**
     * 验证产品组合
     */
    private void checkProduct() {
        //5.    循环查询产品货道：6.    检查库存数量：
        ServiceResult<ProductGroupHeadWrapperData> result = CompositeMaterialService.getInstance()
                .checkProductGroupStock(combinationNumber);
        if (!result.isSuccess()) {
            resetAlertMsg(result.getMessage());
            combinationNumber = "";
            et_number.setText("");
            return;
        }

        ProductGroupHeadWrapperData data = result.getResult();
        if (data.getWrapperList() == null) {
            resetAlertMsg("组合产品为空！");
            combinationNumber = "";
            et_number.setText("");
            return;

        }
        hiddenAlertMsg();
        Intent intent = new Intent();
        intent.putExtra("ProductGroupHeadWrapperData", data);
        intent.putExtra("vendData", vendData);
        intent.putExtra("combinationNumber", combinationNumber);
        intent.putExtra("Pg1Id", data.getPg1Id());
        intent.setClass(MC_CombinationPickActivity.this, MC_CombinationPickDetailActivity.class);
        startActivityForResult(intent, 1000);

    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {
        // TODO Auto-generated method stub

    }

    /**
     * 重置输入框文本
     * 
     * @param value
     * @param serialType
     */
    private void resetTextView(String value, int serialType) {
        if (SerialTools.MESSAGE_LOG_mKeyBoard == serialType) {
            et_number.setText(et_number.getText().toString() + value);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == 1001) {
                finish();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
