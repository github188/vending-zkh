/**
 * 
 */
package com.mc.vending.activitys.pick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author junjie.you
 *
 */
public class MC_DistancePickActivity extends BaseActivity
		implements MC_SerialToolsListener, RequestDataFinishListener, DataParseListener {
	public DataServices dataServices;
	public final int deviationScalar = 20;// 材料列表更新的重量摇摆标量
	public final double weightDeviationScalar = 0.1;
	public final int maxVendingCount = 49;// 售货机id，十进制

	/**
	 * 测距SP存储长度数据文件名称
	 */
	public final String RDDataList = "RDDataList";
	/**
	 * // 测距模块SP存储物品显示列表文件名称
	 */
	public final String RDIdNameList = "RDIdNameList";
	/**
	 * SP存储物品单位长度列表文件名称
	 */
	public final String RDUnitList = "RDUnitList";
	private Map<String, String> DISTANCELIST = new LinkedHashMap<String, String>();// 用来储存每个称重传感器存储的个数List
	private ArrayList<String> DistanceArr = new ArrayList<String>();
	public ListView distance_listview_datalist;
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
	public Button btn_setting_lock;
	private Button btn_setting_unlock;

	public TextView tv_distance_material;
	public EditText txt_distance_material;
	public Button btn_distance_material_save;
	public Button btn_distance_material_reset;
	public TextView tv_distance_unit_a;
	public EditText txt_distance_unit_a;
	public TextView tv_distance_unit_b;
	public EditText txt_distance_unit_b;
	public TextView tv_distance_unit_c;
	public EditText txt_distance_unit_c;
	public Button btn_distance_material_lock;
	public Button btn_distance_material_unlock;

	public RelativeLayout layout_setting; // 步骤1布局
	public RelativeLayout layout_show; // 步骤2布局

	private TextView alert_msg_title; // 提示标题
	private TextView alert_msg; // 提示内容
	private Button btn_exitWeight;// 结束领料
	private Button btn_clearlist; // 清屏
	private Button btn_return; // 开始补货
	private Button btn_setting_unit_zero;
	private Button btn_exitreturn;// 停止补货
	private boolean isRFID; // 是否rfid操作
	private String vendCode; // 售货机编号

	private Timer timer;
	private final int imagePlayerTimer = 1000; // 进入待机界面心跳。每秒钟执行一次
	private final int imagePlayerTimeCount = 1000 * 60; // 默认待机默认跳转时间1分钟
	private int imagePlayerTimeOut;

	private TimerTask mTimerTask;
	private final static int MESSAGE_Image_player = 99; // 跳转到待机
	boolean isOperating = false; // 是否再操作中
	private boolean isStoreChecked; // 格子机验证返回
	public boolean isSettingUnitWeight = false;
	public boolean isReturnMaterial = false;
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
		setContentView(R.layout.activity_distance);
		ActivityManagerTool.getActivityManager().add(this);
		// requestGetClientVersionServer();
		getParam();
		initComponents();
		initObject();
		// startService();
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
		case SerialTools.MESSAGE_LOG_mRD:
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
			case SerialTools.MESSAGE_LOG_mRD:
				String[] portRtnStrList = ((String) msg.obj).replaceAll(Constant.RDSERVETAILWITHBLANK, "")
						.split(Constant.RDSERVEHEADWITHBLANK);
				for (int i = 1; i <= portRtnStrList.length - 1; i++) {
					RdSerialPortReturnStrHandler(portRtnStrList[i]);
				}
				// openFW();
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
		layout_show = (RelativeLayout) this.findViewById(R.id.relout_weight_show);
		distance_listview_datalist = (ListView) this.findViewById(R.id.weight_listview_datalist);
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
		btn_setting_unlock = (Button) this.findViewById(R.id.btn_setting_unlock);
		btn_setting_lock = (Button) this.findViewById(R.id.btn_setting_lock);
		btn_setting_unit_zero = (Button) this.findViewById(R.id.btn_setting_unit_zero);
		btn_clearlist = (Button) this.findViewById(R.id.btn_clearlist);
		btn_return = (Button) this.findViewById(R.id.btn_return);
		btn_exitWeight = (Button) this.findViewById(R.id.btn_exitWeight);
		btn_exitreturn = (Button) this.findViewById(R.id.btn_exitreturn);

		distance_listview_datalist = (ListView) this.findViewById(R.id.weight_listview_datalist);
		btn_distance_material_save = (Button) this.findViewById(R.id.btn_distance_material_save);
		btn_distance_material_reset = (Button) this.findViewById(R.id.btn_distance_material_reset);
		btn_distance_material_lock = (Button) this.findViewById(R.id.btn_distance_material_lock);
		btn_distance_material_unlock = (Button) this.findViewById(R.id.btn_distance_material_unlock);
		tv_distance_material = (TextView) this.findViewById(R.id.tv_distance_material);
		txt_distance_material = (EditText) this.findViewById(R.id.txt_distance_material);
		tv_distance_unit_a = (TextView) this.findViewById(R.id.tv_distance_unit_a);
		txt_distance_unit_a = (EditText) this.findViewById(R.id.txt_distance_unit_a);
		tv_distance_unit_b = (TextView) this.findViewById(R.id.tv_distance_unit_b);
		txt_distance_unit_b = (EditText) this.findViewById(R.id.txt_distance_unit_b);
		tv_distance_unit_c = (TextView) this.findViewById(R.id.tv_distance_unit_c);
		txt_distance_unit_c = (EditText) this.findViewById(R.id.txt_distance_unit_c);

	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {
		// InitSPFWShowList();
		// UpdateUnitWeightForEditText();
		// btn_getWeight.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// DistanceArr.clear();
		//// openFW();
		// }
		// });
		// btn_setting.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// btn_setting_lock.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// isSettingUnitWeight = false;
		// try {
		// SerialTools.getInstance().closeFW();
		// } catch (SerialPortException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
		btn_distance_material_unlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openRdSetUnit();
			}
		});
		// btn_setZero.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// SerialTools.getInstance().openFW(0, Constant.FW_SET_ZERO);
		// }
		// });
		// btn_setting_unit_zero.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// SetZeroSPUnitWeightForFW();
		// }
		// });
		// btn_clearlist.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// distance_listview_datalist.setAdapter(null);
		// }
		// });
		// btn_return.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// isReturnMaterial = true;
		// // openFW();
		// }
		// });
		// btn_exitWeight.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// try {
		// SerialTools.getInstance().closeFW();
		// } catch (SerialPortException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
		// btn_exitreturn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// isReturnMaterial = false;
		// try {
		// SerialTools.getInstance().closeFW();
		// } catch (SerialPortException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });
	}

	private void resetViews() {
		layout_setting.setVisibility(View.INVISIBLE);
		layout_show.setVisibility(View.VISIBLE);
	}

	private void openRD() {
		SerialTools.getInstance().addToolsListener(this);
		try {
			SerialTools.getInstance().openALLRD();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openRdSetUnit() {
		isSettingUnitWeight = true;
		openRD();
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
				// 取出“第三位”，由Hex转为二进制，并且不足8位的不足8位，例如：7位，1010000
				String theThirdByteStr = StringHelper.HexStringToBinaryString(strArrayReturnHex[2]);
				if (!StringHelper.isEmpty(theThirdByteStr)) {
					// 对“第三位”状态位进行解析
					char[] theThirdByteArr = theThirdByteStr.toCharArray();
					decimalPointPosition = Integer.valueOf(theThirdByteStr.substring(0, 3), 2);
					isScallingError = Integer.valueOf(theThirdByteArr[3] + "", 2) == 1 ? true : false;
					isPositive = Integer.valueOf(theThirdByteArr[4] + "", 2) == 0 ? true : false;
					isStable = Integer.valueOf(theThirdByteArr[5] + "", 2) == 1 ? true : false;
					isOverload = Integer.valueOf(theThirdByteArr[6] + "", 2) == 1 ? true : false;
					// 高、中、低位重量数据
					String weightValue = "" + (strArrayReturnHex[5] + strArrayReturnHex[4] + strArrayReturnHex[3]);
					if (isStable && !isOverload) {
						SaveSharedPreferencesForFW(portId, weightValue);
					}
				}
			}
		}
	}

	/**
	 * 用以处理测距模块串口传回的数据解析
	 * 
	 * @author junjie.you
	 * @param pReturnString
	 *            称重模块串口传回的数据
	 */
	private void RdSerialPortReturnStrHandler(String pReturnString) {
		int portId = -1;
		if (pReturnString.isEmpty()) {

		} else {
			String[] strArrayReturnHex = pReturnString.split(" ");
			if (strArrayReturnHex.length != 0 && strArrayReturnHex != null) {
				// [, 01, CC, 00, 00, CD]
				portId = Integer.parseInt(strArrayReturnHex[1], 16);
				// 取出“第三位”，由Hex转为二进制，并且不足8位的不足8位，例如：7位，1010000
				String theThirdByteStr = StringHelper.HexStringToBinaryString(strArrayReturnHex[2]);
				if (!StringHelper.isEmpty(theThirdByteStr)) {
					// 对“第三位”状态位进行解析
					char[] theThirdByteArr = theThirdByteStr.toCharArray();
					decimalPointPosition = Integer.valueOf(theThirdByteStr.substring(0, 3), 2);
					isScallingError = Integer.valueOf(theThirdByteArr[3] + "", 2) == 1 ? true : false;
					isPositive = Integer.valueOf(theThirdByteArr[4] + "", 2) == 0 ? true : false;
					isStable = Integer.valueOf(theThirdByteArr[5] + "", 2) == 1 ? true : false;
					isOverload = Integer.valueOf(theThirdByteArr[6] + "", 2) == 1 ? true : false;
					// 高、中、低位重量数据
					String weightValue = "" + (strArrayReturnHex[5] + strArrayReturnHex[4] + strArrayReturnHex[3]);
					if (isStable && !isOverload) {
						SaveSharedPreferencesForFW(portId, weightValue);
					}
				}
			}
		}
	}

	/**
	 * 将取得的id与重量的值保存至对应的SP中，这里要分清是设置单位重量还是获取实际领取重量
	 * 
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID
	 * @param pWeight
	 *            模块读取到的重量数值
	 */
	private void SaveSharedPreferencesForFW(int pId, String pWeight) {
		if (isSettingUnitWeight) {
			UpdateSPUnitWeightForFW(pId, ConvertHelper.toInt(pWeight, 0));
			UpdateUnitWeightForEditText();
		} else {
			final SharedPreferences sp = getSharedPreferences(RDDataList, MODE_PRIVATE);
			// 获取之前该id内存储的重量
			String preWeight = sp.getString(pId + "", "0");
			sp.edit().putString(pId + "", pWeight).commit();
			if (!isReturnMaterial) {
				int preWeightInt = ConvertHelper.toInt(preWeight, 0);
				int nowWeightInt = ConvertHelper.toInt(pWeight, deviationScalar);
				// 给出一个误差范围：如果之前的重量在现在重量加减误差标量之间则不更新材料列表
				if ((nowWeightInt - deviationScalar) > preWeightInt
						|| (nowWeightInt + deviationScalar) < preWeightInt) {
					UpdateMaterialList(pId + "", ConvertHelper.toInt(preWeight, 0) - ConvertHelper.toInt(pWeight, 0));
				}
			}
		}
	}

	/**
	 * 更新界面显示的单位重量
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void UpdateUnitWeightForEditText() {
		final SharedPreferences spUnit = getSharedPreferences(RDUnitList, MODE_PRIVATE);
		String pWeight = "";
		for (int i = 1; i <= maxVendingCount; i++) {
			pWeight = spUnit.getString(i + "", "100");
			switch (i) {
			case 1:
				txt_weight_a.setText(pWeight);
				break;
			case 2:
				txt_weight_b.setText(pWeight);
				break;
			case 3:
				txt_weight_c.setText(pWeight);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 更新每个秤盘单位重量
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void UpdateSPUnitWeightForFW(int pId, int pWeight) {
		final SharedPreferences spUnit = getSharedPreferences(RDUnitList, MODE_PRIVATE);
		spUnit.edit().putString(pId + "", "" + pWeight).commit();
	}

	/**
	 * 置零每个秤盘单位重量
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void SetZeroSPUnitWeightForFW() {
		// 先清空SP内的单位重量List
		final SharedPreferences spUnit = getSharedPreferences(RDUnitList, MODE_PRIVATE);
		spUnit.edit().clear().commit();
		// 再调用更新方法来更新单位重量显示
		UpdateUnitWeightForEditText();
	}

	private void InitSPFWShowList() {
		final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
		for (int i = 1; i <= maxVendingCount; i++) {
			sp.edit().putString(i + "", i + "号托盘").commit();
		}
	}

	/**
	 * 更新最终显示的List，例如： 称重模块A X2 称重模块B X1
	 * 
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID
	 * @param pDifWeight
	 *            变化的重量数值
	 */
	private void UpdateMaterialList(String pId, int pDifWeight) {
		try {
			// 获取显示物品列表文件
			final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
			// 获取该id在之前的显示列表内的个数，没有则为0
			String idName = sp.getString(pId, "0");
			if (idName.equals("0")) {
				idName = pId + "号模块文件名获取失败";
			}
			// 获取物品单位标准重量
			final SharedPreferences spUnit = getSharedPreferences(RDUnitList, MODE_PRIVATE);// 这语句会不会频繁开关SP?是不是影响性能？

			// 获取该物品锁对应的单位重量，没有则为0
			String idUnitWeight = spUnit.getString(pId, "1");// 之前该id内存储的值
			int preCount = ConvertHelper.toInt(DISTANCELIST.get(pId), 0);
			// 根据重量变化值和单位重量计算出变化的个数
			float denominator = ConvertHelper.toFloat(idUnitWeight, (float) 0.00);
			float afterCount = Math.abs(pDifWeight) / denominator;
			if (afterCount != 0) {
				afterCount = WeightCountCalculator(afterCount);
				// pDifWeight为正是放回物品，为负是取走物品
				if (pDifWeight > 0) {
					afterCount -= preCount;
				} else {
					afterCount += preCount;
				}
				// 将变化的个数更新该ID对应的显示个数
				DISTANCELIST.put(pId, "" + afterCount);// 把显示LIST中的对应数据进行更新
			}
			ShowMaterialList();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将WEIGHTLIST绑定到ListView上，同时更新界面
	 */
	private void ShowMaterialList() {
		DistanceArr.clear();
		Iterator<Entry<String, String>> it = DISTANCELIST.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			DistanceArr.add(entry.getKey() + "号托盘		X" + entry.getValue());
		}

		distance_listview_datalist
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DistanceArr));

	}

	/**
	 * 计算变化个数
	 * 
	 * @author junjie.you
	 * @param pNum
	 * @return
	 */
	private int WeightCountCalculator(float pNum) {
		int intPart = (int) pNum;
		if (FuzzyJudgment(pNum)) {
			intPart++;
		}
		return intPart;
	}

	/**
	 * 模糊判断，在范围内返回true
	 * 
	 * @author junjie.you
	 * @param pNum
	 *            输入用于比对的数字
	 * @return 为true则+1
	 */
	private boolean FuzzyJudgment(float pNum) {
		boolean flag = false;
		/*
		 * 向上取整用Math.ceil(double a) 向下取整用Math.floor(double a)
		 */
		double ceil = Math.ceil(pNum);
		double floor = Math.floor(pNum);
		if (((pNum + weightDeviationScalar) > ceil || (pNum - weightDeviationScalar) > floor) && ceil != floor) {
			flag = true;
		}
		return flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.DataParseListener#parseJson(com.mc.vending.
	 * data.BaseData)
	 */
	@Override
	public void parseJson(BaseData baseData) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.DataParseListener#parseRequestError(com.mc.
	 * vending.data.BaseData)
	 */
	@Override
	public void parseRequestError(BaseData baseData) {
		// TODO Auto-generated method stub

	}

}
