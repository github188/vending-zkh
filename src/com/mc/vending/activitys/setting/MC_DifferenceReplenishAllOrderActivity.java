package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_DifferenceReplenishAllOrderAdapter;
import com.mc.vending.adapter.MC_DifferenceReplenishmentOrderAdapter;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;

public class MC_DifferenceReplenishAllOrderActivity extends BaseActivity {
    private TextView                               tv_public_title; //公共头部标题
    private Button                                 back;           //公共头部返回键
    private ListView                               listView;       //列表
    private MC_DifferenceReplenishAllOrderAdapter adapter;
    private List<ReplenishmentHeadData>            dataList;
    private VendingCardPowerWrapperData            wrapperData;    //卡密码权限对象
    private final static int                       DETAIL = 0;
    private TextView                               alert_msg_title; //提示标题
    private TextView                               alert_msg;      //提示内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difference_replenishall_order);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
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
    }

    /**
     * 初始化数据
     */
    private void initObject() {
        tv_public_title.setText(getResources().getString(R.string.set_difference_replenishAll));

        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        listView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SELECT;
                msg.obj = String.valueOf(position);
                handler.sendMessage(msg);

            }

        });
        dataList = new ArrayList<ReplenishmentHeadData>();
        requestLst();
    }

    private void requestLst() {

        Thread downLoadData = new Thread(new Runnable() {
            public void run() {
                ServiceResult<List<ReplenishmentHeadData>> result = ReplenishmentService
                    .getInstance().getReplenishmentHead(ReplenishmentHeadData.RH_TYPE_All);
                if (!result.isSuccess()) {
                    //todo...
                    return;
                }
                dataList = result.getResult();
                Message msg = new Message();
                msg.what = MC_SettingActivity.SET_SUCCESS;
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
                                        case MC_SettingActivity.SET_SUCCESS:
                                            if (dataList.size() > 0) {
                                                adapter = new MC_DifferenceReplenishAllOrderAdapter(
                                                    MC_DifferenceReplenishAllOrderActivity.this,
                                                    dataList, listView);
                                                listView.setAdapter(adapter);
                                            } else {
                                                resetAlertMsg("没有补满单，请联系管理员或先进行一键补满！");
                                            }
                                            stopLoading();
                                            break;
                                        case MC_SettingActivity.SET_SELECT:
                                            goDifferenceReplenishAllActivity(Integer
                                                .parseInt((String) msg.obj));
                                            break;

                                        default:
                                            break;
                                    }

                                }
                            };

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
     * 进入详情
     * @param position
     */
    private void goDifferenceReplenishAllActivity(int position) {
        startLoading();
        ReplenishmentHeadData data = dataList.get(position);
        Intent intent = new Intent();
        intent.putExtra("ReplenishmentHeadData", data);
        intent.putExtra("wrapperData", wrapperData);
        intent.setClass(MC_DifferenceReplenishAllOrderActivity.this,
            MC_DifferenceReplenishAllActivity.class);
        startActivityForResult(intent, 1000);

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

    public void backClicked(View view) {
        finish();
    }

    public void saveClicked(View view) {

    }
}
