package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_DifferenceReplenishmentAdapter;
import com.mc.vending.data.ReplenishmentDetailWrapperData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;

/**
 * 差异补货
 * @author apple
 *
 */
public class MC_DifferenceReplenishmentActivity extends BaseActivity {

    private TextView                             tv_public_title; //公共头部标题
    private Button                               back;           //公共头部返回键
    private Button                               operate;
    private ListView                             listView;       //列表
    private TextView                             alert_msg_title; //提示标题
    private TextView                             alert_msg;      //提示内容
    private MC_DifferenceReplenishmentAdapter    adapter;
    private List<ReplenishmentDetailWrapperData> dataList;
    private ReplenishmentHeadData                headData;       //补货单主表
    private VendingCardPowerWrapperData          wrapperData;    //卡密码权限对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_replenishment);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        headData = (ReplenishmentHeadData) getIntent()
            .getSerializableExtra("ReplenishmentHeadData");
        wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        tv_public_title = (TextView) this.findViewById(R.id.tv_public_title);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
        back = (Button) this.findViewById(R.id.back);
        operate = (Button) this.findViewById(R.id.operate);
        listView = (ListView) this.findViewById(R.id.listView);
    }

    /**
     * 初始化数据
     */
    private void initObject() {
        tv_public_title.setText(getResources().getString(R.string.set_difference_replenishment));
        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        operate.setVisibility(View.VISIBLE);
        operate.setEnabled(true);
        operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        dataList = new ArrayList<ReplenishmentDetailWrapperData>();

        requestLst();
    }

    private void requestLst() {

        Thread downLoadData = new Thread(new Runnable() {
            public void run() {
                dataList = ReplenishmentService.getInstance().findReplenishmentDetail(
                    headData.getRh1Id());
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SUCCESS;
                handler.sendMessage(msg);
            }
        });
        downLoadData.start();
    }

    @Override
    protected void onPause() {

        super.onPause();
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
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
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
     * 点击返回
     * @param view
     */
    public void backClicked(View view) {
        Intent intent = new Intent(MC_DifferenceReplenishmentActivity.this,
            MC_DifferenceReplenishmentOrderActivity.class);
        this.setResult(1002, intent);
        finish();
    }

    /**
     * 成功返回
     */
    private void goBack() {
        Intent intent = new Intent(MC_DifferenceReplenishmentActivity.this,
            MC_DifferenceReplenishmentOrderActivity.class);
        this.setResult(1001, intent);

        this.finish();
    }

    public void saveClicked(View view) {
        startLoading();
        Thread downLoadData = new Thread(new Runnable() {
            public void run() {

                ServiceResult<Boolean> result = ReplenishmentService.getInstance()
                    .updateReplenishmentDetail(headData, dataList, wrapperData,StockTransactionData.BILL_TYPE_DIFF);
                if (!result.isSuccess()) {

                    Message msg = handler.obtainMessage();
                    msg.obj = result.getMessage();
                    msg.what = MC_SettingActivity.SET_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.obj = "差异补货完成";
                msg.what = MC_SettingActivity.SET_DifferenceReplenishment;

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
                                            resetAlertMsg((String) msg.obj);
                                            break;
                                        case MC_SettingActivity.SET_DifferenceReplenishment:
                                            resetAlertMsg((String) msg.obj);
                                            goBack();
                                            break;

                                        case MC_SettingActivity.SET_SUCCESS:
                                            adapter = new MC_DifferenceReplenishmentAdapter(
                                                MC_DifferenceReplenishmentActivity.this, dataList,
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
        ReplenishmentDetailWrapperData data = dataList.get(index);

        int number = data.getReplenishmentDetail().getRh2DifferentiaQty();
        number += 1;
        data.getReplenishmentDetail().setRh2DifferentiaQty(number);
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
        ReplenishmentDetailWrapperData data = dataList.get(index);
        int number = data.getReplenishmentDetail().getRh2DifferentiaQty();
        if (number > -data.getReplenishmentDetail().getRh2ActualQty()) {
            number -= 1;
        }
        data.getReplenishmentDetail().setRh2DifferentiaQty(number);
        reloadTableWithLine(index);
    }

    private void reloadTableWithLine(int index) {
        adapter.reloadViewHolder(index);

    }
}
