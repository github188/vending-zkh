package com.mc.vending.activitys.pick;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillionstar.tools.ZillionLog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MC_WeightPickActivity extends BaseActivity
		implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {
	public DataServices dataServices;
	public final int deviationScalar = 20;// 材料列表更新的重量摇摆标量
	public final int vendingId01 = 1;// 售货机id，十进制
	public final int vendingId02 = 2;
	public final int vendingId03 = 3;
	public final String FWDataList = "FWDataList";// 称重模块SP存储数据文件名称
	public final String FWShowList = "FWShowList";// 称重模块SP存储物品显示列表文件名称
	public final String FWUnitList = "FWUnitList";// 称重模块SP存储物品单位重量列表文件名称
	private Map<String, String> WEIGHTLIST = new LinkedHashMap<String, String>();// 用来储存每个称重传感器存储的个数List
	public ListView weight_datalist;
	public Button btn_noSkin = null;
	public Button btn_setZero = null;
	public Button btn_setting = null;
	public Button btn_getWeight = null;
	public TextView tv_weight_a;
	public EditText txt_weight_a;
	public TextView tv_weight_b;
	public EditText txt_weight_b;
	public TextView tv_weight_c;
	public EditText txt_weight_c;
	public Button btn_setting_ok;
	private Button btn_setting_back;

	private ImageView iv_sku; // 商品图片
	public RelativeLayout layout_setting; // 步骤1布局
	public RelativeLayout layout_show; // 步骤2布局
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

	/**
	 * 为小数点位置（0-5）
	 * 
	 * @author junjie.you
	 */
	private int decimalPointPosition = 0;

	/**
	 * 标定参数错误，是否需要重新标定
	 * 
	 * @author junjie.you
	 */
	private boolean isScallingError = false;
	/**
	 * 重量为负正
	 * 
	 * @author junjie.you
	 */
	private boolean isPositive = false;
	/**
	 * 是否稳定
	 * 
	 * @author junjie.you
	 */
	private boolean isStable = false;
	/**
	 * 是否超载
	 * 
	 * @author junjie.you
	 */
	private boolean isOverload = false;

	@Override
	public void parseRequestFinised(BaseData baseData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void parseRequestFailure(BaseData baseData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestFinished() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);
		ActivityManagerTool.getActivityManager().add(this);
		// requestGetClientVersionServer();
		getParam();
		initComponents();
		initObject();
		startService();
		// resetViews();
		// foo();
	}

	/**
	 * 启动绑定服务
	 */
	private void startService() {
		Intent intent1 = new Intent(this, DataServices.class);
		bindService(intent1, conn, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection conn = new ServiceConnection() {
		/** 获取服务对象时的操作 */
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			dataServices = ((DataServices.ServiceBinder) service).getService();
			ZillionLog.i(this.getClass().getName(), "onServiceConnected");
			resetVendStatus();
		}

		/** 无法获取到服务对象时的操作 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			dataServices = null;
		}

	};

	/**
	 * 初始化售货机状态
	 */
	private void resetVendStatus() {
		if (!isAccessNetwork()) {
			resetAlertMsg("没有链接到网络，请检查网络链接！");
			return;
		}
		VendingData vending = new VendingDbOper().getVending();
		if (vending == null) {
			startLoading();
			dataServices.setNormalListener(this);
			dataServices.resetVending(vendCode);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void requestFailure() {
		// TODO Auto-generated method stub

	}

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
				FWSerialPortReturnStrHandler((String) msg.obj);
				openFW();
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
		layout_setting = (RelativeLayout) this.findViewById(R.id.relout_weight_setting);
		layout_show = (RelativeLayout) this.findViewById(R.id.relout_weight_show);
		weight_datalist = (ListView) this.findViewById(R.id.weight_listview_datalist);
		btn_noSkin = (Button) this.findViewById(R.id.btn_noSkin);
		btn_setZero = (Button) this.findViewById(R.id.btn_setZero);
		btn_setting = (Button) this.findViewById(R.id.btn_setting);
		btn_getWeight = (Button) this.findViewById(R.id.btn_getWeight);
		tv_weight_a = (TextView) this.findViewById(R.id.tv_weight_a);
		txt_weight_a = (EditText) this.findViewById(R.id.txt_weight_a);
		tv_weight_b = (TextView) this.findViewById(R.id.tv_weight_b);
		txt_weight_b = (EditText) this.findViewById(R.id.txt_weight_b);
		tv_weight_c = (TextView) this.findViewById(R.id.tv_weight_c);
		txt_weight_c = (EditText) this.findViewById(R.id.txt_weight_c);
		btn_setting_back = (Button) this.findViewById(R.id.btn_setting_back);
		btn_setting_ok = (Button) this.findViewById(R.id.btn_setting_ok);
	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {

		btn_getWeight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openFW();
			}
		});
		btn_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goSettingViews();

			}
		});
		btn_setting_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetViews();
			}
		});
	}

	private void resetViews() {
		layout_setting.setVisibility(View.INVISIBLE);
		layout_show.setVisibility(View.VISIBLE);
	}

	private void goSettingViews() {
		layout_show.setVisibility(View.INVISIBLE);
		layout_setting.setVisibility(View.VISIBLE);
	}

	private void openFW() {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openFW(vendingId01);
		SerialTools.getInstance().openFW(vendingId02);
		SerialTools.getInstance().openFW(vendingId03);
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
	 * 用以处理称重模块串口传回的数据解析 第1字节： D0~D7 —— 0FFH（起始位） 第2字节： D0~D7 ——
	 * ID号（有效范围为1-200，0为特殊用途） 第3字节： D0~D2 —— 为小数点位置（0-5） D3 —— 1表示标定参数错误，需要重新标定
	 * D4 —— 1表示重量为负，0表示重量为正 D5 —— 1表示重量稳定，0表示重量不稳定 D6 —— 1表示重量超载，0表示重量未超载 第4字节：
	 * D0~D7 —— BCD1（显示数值的最低字节） 第5字节： D0~D7 —— BCD2（显示数值的中间字节） 第6字节： D0~D7 ——
	 * BCD3（显示数值的最高字节） 第7字节： D0~D7 —— 对前面的6个字节进行异或检验，如果校验结果和第一个字节相同，则变为0x7F。
	 * 
	 * @author junjie.you
	 * @param pReturnString
	 *            称重模块串口传回的数据
	 */
	private void FWSerialPortReturnStrHandler(String pReturnString) {
		int portId = -1;
		if (pReturnString.isEmpty()) {

		} else {
			String[] strArrayReturnHex = pReturnString.split(" ");
			if (strArrayReturnHex.length != 0 && strArrayReturnHex != null) {
				portId = Integer.parseInt(strArrayReturnHex[1], 16);
				String theThirdByteStr = StringHelper.HexStringToBinaryString(strArrayReturnHex[2]);// 7位，1010000
				if (!StringHelper.isEmpty(theThirdByteStr)) {
					// theThirdByteStr =
					// StringHelper.autoCompletionCode(theThirdByteStr);//
					// 8位，01010000
					char[] theThirdByteArr = theThirdByteStr.toCharArray();
					decimalPointPosition = Integer.valueOf(theThirdByteStr.substring(0, 3), 2);
					isScallingError = Integer.valueOf(theThirdByteArr[3] + "", 2) == 1 ? true : false;
					isPositive = Integer.valueOf(theThirdByteArr[4] + "", 2) == 0 ? true : false;
					isStable = Integer.valueOf(theThirdByteArr[5] + "", 2) == 1 ? true : false;
					isOverload = Integer.valueOf(theThirdByteArr[6] + "", 2) == 1 ? true : false;
					String weightValue = "" + Integer.parseInt(strArrayReturnHex[5], 16)
							+ Integer.parseInt(strArrayReturnHex[4], 16) + Integer.parseInt(strArrayReturnHex[3], 16);
					if (isStable && !isOverload) {
						SaveSharedPreferencesForFW(portId + "", weightValue);
					}
				}
			}
		}
	}

	/**
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID
	 * @param pWeight
	 *            模块读取到的重量数值
	 */
	private void SaveSharedPreferencesForFW(String pId, String pWeight) {
		final SharedPreferences sp = getSharedPreferences(FWDataList, MODE_PRIVATE);
		String preWeight = sp.getString(pId, "0");// 之前该id内存储的值
		sp.edit().putString(pId, pWeight).commit();
		int preWeightInt = ConvertHelper.toInt(preWeight, 0);
		int nowWeightInt = ConvertHelper.toInt(pWeight, deviationScalar);
		// 给出一个误差范围：如果之前的重量在现在重量加减误差标量之间则不更新材料列表
		if ((nowWeightInt - deviationScalar) < preWeightInt || (nowWeightInt + deviationScalar) > preWeightInt) {
			UpdateMaterialList(pId, ConvertHelper.toInt(pWeight, 0) - ConvertHelper.toInt(preWeight, 0));
		}

	}

	/**
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID
	 * @param pDifWeight
	 *            变化的重量数值
	 */
	private void UpdateMaterialList(String pId, int pDifWeight) {
		try {
			// 获取显示物品列表文件
			final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
			// 获取该id在之前的显示列表内的个数，没有则为0
			String preCount = sp.getString(pId, "0");
			// 获取物品单位标准重量
			final SharedPreferences spUnit = getSharedPreferences(FWUnitList, MODE_PRIVATE);
			// 获取该物品锁对应的单位重量，没有则为0
			String idUnitWeight = spUnit.getString(pId, "100");// 之前该id内存储的值
			// 根据重量变化值和单位重量计算出变化的个数
			int afterCount = pDifWeight / ConvertHelper.toInt(idUnitWeight, 0);
			// 将变化的个数更新该ID对应的显示个数
			WEIGHTLIST.put(pId, afterCount + preCount);// 把显示LIST中的对应数据进行更新
			// Message m = Message.obtain(handler, obtain);
			// m.obj = data;
			// handler.sendMessage(m);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
