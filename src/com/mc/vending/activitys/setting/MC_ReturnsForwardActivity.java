package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_ReturnForwardDetailAdapter;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReturnProductService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;

/**
 * 正向退货
 * 
 * @author apple
 *
 */
public class MC_ReturnsForwardActivity extends BaseActivity {
    private TextView                           tv_public_title;             //公共头部标题
    private Button                             back;                        //公共头部返回键
    private Button                             operate;
    private TextView                           alert_msg_title;             //提示标题
    private TextView                           alert_msg;                   //提示内容
    private TextView                           tv_number_title;
    private TextView                           set_difference_replenishment;
    private EditText                           et_order_number;
    private ListView                           listView;                    //列表
    private MC_ReturnForwardDetailAdapter      adapter;
    private List<VendingChnProductWrapperData> dataList;
    private VendingCardPowerWrapperData        wrapperData;                 //卡密码权限对象
    private RetreatHeadData                    retreatHeadData;             //退货主表
    private String                             orderNumber;                 //补货单号r2015      盘点i2015    正向退货t2015

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
        retreatHeadData = (RetreatHeadData) getIntent().getSerializableExtra("retreatHeadData");
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
        set_difference_replenishment = (TextView) this.findViewById(R.id.set_difference_replenishment);
        listView = (ListView) this.findViewById(R.id.listView);
    }

    /**
     * 初始化数据
     */
    private void initObject() {
        orderNumber = "T" + DateHelper.format(new Date(), "yyyy");
        tv_public_title.setText(getResources().getString(R.string.set_returns_forward));
        tv_number_title.setText(getResources().getString(R.string.returns_order_id) + " " + orderNumber);
        set_difference_replenishment.setText(getResources().getString(R.string.returns_number));
        et_order_number.setText(retreatHeadData.getRt1Rtcode().substring(5));

        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        operate.setVisibility(View.VISIBLE);
        operate.setEnabled(true);
        operate.setText(getResources().getString(R.string.PUBLIC_SAVE));

        if (retreatHeadData.getRt1Status().equals("1")) {
            operate.setVisibility(View.INVISIBLE);
            operate.setEnabled(false);
        }

        dataList = new ArrayList<VendingChnProductWrapperData>();

//        et_order_number.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(et_order_number, 4);
//            }
//        });

//        et_order_number.setOnKeyListener(onKey);
        requestLst();
    }

    private void requestLst() {

        Thread downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {
//                dataList = ReturnProductService.getInstance().findChnCodeProductName();
                dataList = ReturnProductService.getInstance().getRetreatDetailsByRtId(
                        retreatHeadData.getRt1Id());
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
//        openKeyBoard();
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
        if (StringHelper.isEmpty(et_order_number.getText().toString(), true)) {
            resetAlertMsg("请输入退货(正向)单号！");
            return;
        }
        for (VendingChnProductWrapperData data : dataList) {
            if (data.getActQty() > data.getStock()) {
                Message msg = handler.obtainMessage();
                msg.obj = "货道" + data.getVendingChn().getVc1Code() + "退货数量大于库存";
                msg.what = MC_SettingActivity.SET_ERROR;
                handler.sendMessage(msg);
                return;
            }
        }
        startLoading();
        Thread downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                ServiceResult<Boolean> result = ReturnProductService.getInstance()
                        .positiveReturnStockTransaction(
                                orderNumber
                                        + StringHelper.autoCompletionCode(StringHelper.trim(et_order_number
                                                .getText().toString())), dataList, wrapperData,
                                retreatHeadData.getRt1Id());

                if (!result.isSuccess()) {
                    Message msg = handler.obtainMessage();
                    msg.obj = result.getMessage();
                    msg.what = MC_SettingActivity.SET_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.obj = "正向退货完成";
                msg.what = MC_SettingActivity.SET_ReturnsForward;
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
                                    case MC_SettingActivity.SET_ERROR:
                                        stopLoading();
                                        resetAlertMsg((String) msg.obj);
                                        //                                            showToast((String) msg.obj);
                                        break;
                                    case MC_SettingActivity.SET_ReturnsForward:
                                        stopLoading();
                                        resetAlertMsg((String) msg.obj);
                                        //                                            showToast((String) msg.obj);
                                        Intent intent = new Intent();
                                        intent.setClass(MC_ReturnsForwardActivity.this,
                                                MC_ReturnForwardListActivity.class);
                                        startActivityForResult(intent, 1000);
                                        finish();
                                        break;
                                    case MC_SettingActivity.SET_SUCCESS:
                                        adapter = new MC_ReturnForwardDetailAdapter(
                                                MC_ReturnsForwardActivity.this, dataList, listView,
                                                retreatHeadData);
                                        listView.setAdapter(adapter);
                                        stopLoading();
                                        break;

                                    default:
                                        break;
                                    }

                                }
                            };

    public void sumClicked(View view) {
        //System.out.println("sumClicked＝＝＝" + String.valueOf(view.getTag()));
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        //System.out.println("sumClicked" + index);
        VendingChnProductWrapperData data = dataList.get(index);
        int number = data.getActQty();
        number += 1;
        if (number > data.getStock()) {
            Message msg = handler.obtainMessage();
            msg.obj = "货道" + data.getVendingChn().getVc1Code() + "退货数量大于库存";
            msg.what = MC_SettingActivity.SET_ERROR;
            handler.sendMessage(msg);
            return;
        }
        data.setActQty(number);
        reloadTableWithLine(index);
    }

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

    private void reloadTableWithLine(int index) {
        adapter.reloadViewHolder(index);

    }

    /**
     * 重置输入框文本
     * 
     * @param value
     * @param serialType
     */
    private void resetTextView(String value, int serialType) {
        et_order_number.setText(et_order_number.getText().toString() + value);
    }
}
