/**
 * 
 */
package com.mc.vending.activitys.pick;

import java.util.Timer;
import java.util.TimerTask;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 称重模块领料
 * 
 * @author junjie.you
 *
 */
public class MC_WeightPickActivity extends BaseActivity
		implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {

	public DataServices dataServices;
	private ImageView iv_sku; // 商品图片
	private RelativeLayout layout_step1; // 步骤1布局
	private RelativeLayout layout_step2; // 步骤2布局
	private EditText et_channle_number; // 步骤1输入框
	private EditText et_pick_number; // 步骤2输入框
	private TextView txt_weight_msg; // 重量数值内容

	private TextView alert_msg_title; // 提示标题
	private TextView alert_msg; // 提示内容
	private Button btn_out; // 隐藏按钮
	private Button btn_get_weight; // 获得重量数据
	private Button btn_version; // 更新按钮
	private VendingData vendData; // 售货机对象
	private VendingChnData vendingChn; // 售货机货道对象
	private VendingCardPowerWrapperData wrapperData; // 卡密码权限对象
	private int stockCount; // 库存数量
	private boolean isRFID; // 是否rfid操作
	private AsyncImageLoader asyncImageLoader;
	private String vendCode; // 售货机编号

	private Timer timer;
	private final int imagePlayerTimer = 1000; // 进入待机界面心跳。每秒钟执行一次
	private final int imagePlayerTimeCount = 1000 * 60; // 默认待机默认跳转时间1分钟
	private int imagePlayerTimeOut;

	private TimerTask mTimerTask;
	private final static int MESSAGE_Image_player = 99; // 跳转到待机
	boolean isOperating = false; // 是否再操作中
	private boolean isStoreChecked; // 格子机验证返回

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);
		// requestGetClientVersionServer();
		getParam();
		initComponents();
		initObject();
		// resetViews();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.parse.listener.DataParseRequestListener#
	 * parseRequestFinised(com.mc.vending.data.BaseData)
	 */
	@Override
	public void parseRequestFinised(BaseData baseData) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.parse.listener.DataParseRequestListener#
	 * parseRequestFailure(com.mc.vending.data.BaseData)
	 */
	@Override
	public void parseRequestFailure(BaseData baseData) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.RequestDataFinishListener#requestFinished()
	 */
	@Override
	public void requestFinished() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.RequestDataFinishListener#requestFailure()
	 */
	@Override
	public void requestFailure() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.tools.utils.MC_SerialToolsListener#serialReturn(java.lang.
	 * String, int)
	 */
	@Override
	public void serialReturn(String value, int serialType) {
		Message msg = new Message();
		msg.what = serialType;
		msg.obj = value;
		// 判断串口类型
		switch (serialType) {
		case SerialTools.MESSAGE_LOG_mFw:
			// resetTimer();
			handler.sendMessage(msg);
			isRFID = false;
			break;
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SerialTools.MESSAGE_LOG_mFw:
				// hiddenAlertMsg();
				txt_weight_msg.setText((String)msg.obj);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 隐藏提示信息
	 */
	private void hiddenAlertMsg() {
		alert_msg.setText("");
		alert_msg_title.setVisibility(View.INVISIBLE);
		alert_msg.setVisibility(View.INVISIBLE);
	}

	/**
	 * 重置累计轮训时间
	 */
	private void resetTimer() {
		imagePlayerTimeOut = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.tools.utils.MC_SerialToolsListener#serialReturn(java.lang.
	 * String, int, java.lang.Object)
	 */
	@Override
	public void serialReturn(String value, int serialType, Object userInfo) {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化参数
	 */
	private void getParam() {
		vendCode = getIntent().getStringExtra("vendCode");
	}

	/**
	 * 初始化对象
	 */
	private void initComponents() {
		// iv_sku = (ImageView) this.findViewById(R.id.iv_sku);
		// layout_step1 = (RelativeLayout) this.findViewById(R.id.layout_step1);
		// layout_step2 = (RelativeLayout) this.findViewById(R.id.layout_step2);
		// layout_step3 = (RelativeLayout) this.findViewById(R.id.layout_step3);
		// layout_step_set = (RelativeLayout) this
		// .findViewById(R.id.layout_step_set);
		// et_channle_number = (EditText) this
		// .findViewById(R.id.et_channle_number);
		// et_pick_number = (EditText) this.findViewById(R.id.et_pick_number);
		// et_card_password = (EditText)
		// this.findViewById(R.id.et_card_password);
		// et_card_password_set = (EditText) this
		// .findViewById(R.id.et_card_password_set);
		txt_weight_msg = (TextView) this.findViewById(R.id.txt_showweight);
		alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
		alert_msg = (TextView) this.findViewById(R.id.alert_msg);
		btn_out = (Button) this.findViewById(R.id.btn_out);
		btn_get_weight = (Button) this.findViewById(R.id.btn_getweight);
	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {

		btn_get_weight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SerialTools.getInstance().openFW(2);
			}
		});
	}

	/**
	 * 现实提示信息
	 */
	private void resetAlertMsg(String msg) {
		alert_msg.setText(msg);
		alert_msg_title.setVisibility(View.VISIBLE);
		alert_msg.setVisibility(View.VISIBLE);
	}
}
