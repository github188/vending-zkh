package com.mc.vending.activitys.setting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_UrgentReplenishmentAdapter;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReturnProductService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;

/**
 * 反向退货
 * @author apple
 *
 */
public class MC_ReturnsReverseActivity extends BaseActivity {
    private TextView                           tv_public_title;             //公共头部标题
    private Button                             back;                        //公共头部返回键
    private Button                             operate;
    private TextView                           set_difference_replenishment;
    private ListView                           listView;                    //列表
    private TextView                           alert_msg_title;             //提示标题
    private TextView                           alert_msg;                   //提示内容
    private MC_UrgentReplenishmentAdapter      adapter;
    private List<VendingChnProductWrapperData> dataList;
    private VendingCardPowerWrapperData        wrapperData;                 //卡密码权限对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns_reverse);
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
        set_difference_replenishment = (TextView) this
            .findViewById(R.id.set_difference_replenishment);
        alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
        alert_msg = (TextView) this.findViewById(R.id.alert_msg);
        listView = (ListView) this.findViewById(R.id.listView);
    }

    /**
     * 初始化数据
     */
    private void initObject() {
        tv_public_title.setText(getResources().getString(R.string.set_returns_reverse));
        set_difference_replenishment.setText(getResources().getString(R.string.returns_number));

        back.setVisibility(View.VISIBLE);
        back.setEnabled(true);
        operate.setVisibility(View.VISIBLE);
        operate.setEnabled(true);
        operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        dataList = new ArrayList<VendingChnProductWrapperData>();

        requestLst();
    }

    private void requestLst() {

        Thread downLoadData = new Thread(new Runnable() {
            public void run() {
                dataList = ReturnProductService.getInstance().findChnCodeProductName();
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

        startLoading();
        Thread downLoadData = new Thread(new Runnable() {
            public void run() {

                ServiceResult<Boolean> result = ReturnProductService.getInstance()
                    .reverseReturnStockTransaction(dataList, wrapperData);

                if (!result.isSuccess()) {
                    Message msg = handler.obtainMessage();
                    msg.obj = result.getMessage();
                    msg.what = MC_SettingActivity.SET_ERROR;
                    handler.sendMessage(msg);
                    return;
                }
                Message msg = handler.obtainMessage();
                msg.obj = "反向退货完成";
                msg.what = MC_SettingActivity.SET_ReturnsReverse;
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
                                        case MC_SettingActivity.SET_ReturnsReverse:
                                            stopLoading();
                                            resetAlertMsg((String) msg.obj);
                                            finish();
                                            break;

                                        case MC_SettingActivity.SET_SUCCESS:
                                            adapter = new MC_UrgentReplenishmentAdapter(
                                                MC_ReturnsReverseActivity.this, dataList, listView);
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
}
