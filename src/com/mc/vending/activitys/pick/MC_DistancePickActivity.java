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
import com.mc.vending.data.VendingData;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
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
	public final int deviationScalar = 7;// 材料列表更新的重量摇摆标量
	public final double lengthDeviationScalar = 0.15;
	public final int maxVendingCount = 49;// 售货机id，十进制

	/**
	 * 测距SP存储每次比较的基准长度
	 */
	public final String RDDataList = "RDDataList";
	/**
	 * // 测距模块SP存储物品显示列表文件名称
	 */
	public final String RDIdNameList = "RDIdNameList";
	/**
	 * SP存储测距模块单位长度列表文件名称
	 */
	public final String RDUnitList = "RDUnitList";
	/**
	 * SP存储物品单位长度列表文件名称
	 */
	public final String RDMaterialUnitList = "RDMaterialUnitList";
	/**
	 * 存放各模块0长度的值
	 */
	public final String RdZeroList = "RDZeroList";

	/**
	 * 存放货道库存个数
	 */
	public final String RDMaterialChnList = "RDMaterialChnList";

	private Map<String, String> DISTANCELIST = new LinkedHashMap<String, String>();// 用来储存每个测距传感器电路板返回的所有货道数据的List
	private Map<String, String> DISTANCECHNCOUNTLIST = new LinkedHashMap<String, String>();// 用来储存每个测距传感器库存个数List
	private Map<String, String> DISTANCECOUNTLIST = new LinkedHashMap<String, String>();// 用来储存每个测距传感器领料个数List
	private Map<String, String> DISTANCEMEMERY = new LinkedHashMap<String, String>();// 用来储存每个测距传感器补货或领料开始的距离数
	private ArrayList<String> DistanceArr = new ArrayList<String>();// 领料的Array
	private ArrayList<String> DistanceChnArr = new ArrayList<String>();// 库存的Array
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
	public TextView tv_distance_show;
	public EditText txt_distance_show;
	public Button btn_distance_material_lock;
	public Button btn_distance_material_unlock;
	public Button btn_distance_material_setzero;
	public Button btn_distance_material_getdistance;
	public Button btn_distance_material_stopgetdistance;
	public Button btn_distance_return;
	public Button btn_distance_exitreturn;
	public Button btn_clear_distance_vendingchn;
	public ListView distance_listview_vendingchnlist;

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
	private final int distanceStableTimer = 50; // 进入待机界面心跳。每秒钟执行一次
	private final int distanceStableTimeCount = 500; // 默认待机默认跳转时间1分钟
	private int distanceStableTimeOut;

	private TimerTask mTimerTask;
	private final static int MESSAGE_Image_player = 99; // 跳转到待机
	private final static String[] TotalVendingChn = { "49" };// 总共有多少测距货道
	boolean isOperating = false; // 是否再操作中
	private boolean isStoreChecked; // 格子机验证返回
	public boolean isSettingUnitDistance = false;// 是否校对测距模块单位毫米电压数
	public boolean isSettingMaterialUnitDistance = false;// 是否校对测量物品单位长度
	public boolean isReturnMaterial = false;// 是否在做补货
	public boolean isSettingUnitZero = false;// 是否在对模块校对为0
	public boolean isDistanceStable = false;
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

	private boolean isNeedUpdateDistanceMemery = true;

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
		ShowUnitDistance4EditText(maxVendingCount + "");
		ShowMaterialUnitDistance4EditText(maxVendingCount + "");
		ShowVendingChnList();
		openRD();
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
		startTimerTask();
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
				if (isSettingUnitZero) {
					try {
						InitFlag();
						SerialTools.getInstance().closeRD();

					} catch (SerialPortException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (isSettingUnitDistance) {
					openRdSetUnit();
				} else {
					openRD();
				}
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
		distance_listview_datalist = (ListView) this.findViewById(R.id.distance_listview_datalist);
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
		btn_distance_material_setzero = (Button) this.findViewById(R.id.btn_distance_material_setzero);

		distance_listview_datalist = (ListView) this.findViewById(R.id.distance_listview_datalist);
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
		tv_distance_show = (TextView) this.findViewById(R.id.tv_distance_show);
		txt_distance_show = (EditText) this.findViewById(R.id.txt_distance_show);
		btn_distance_material_getdistance = (Button) this.findViewById(R.id.btn_distance_material_getdistance);
		btn_distance_material_stopgetdistance = (Button) this.findViewById(R.id.btn_distance_material_stopgetdistance);
		btn_distance_return = (Button) this.findViewById(R.id.btn_distance_return);
		btn_distance_exitreturn = (Button) this.findViewById(R.id.btn_distance_exitreturn);
		btn_clear_distance_vendingchn = (Button) this.findViewById(R.id.btn_clear_distance_vendingchn);
		distance_listview_vendingchnlist = (ListView) this.findViewById(R.id.distance_listview_vendingchnlist);

	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {
		InitSPRDShowList();
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
		btn_distance_material_lock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitFlag();
				try {
					SerialTools.getInstance().closeRD();

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_distance_material_unlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSettingUnitDistance = true;
				openRdSetUnit();
			}
		});
		btn_distance_material_setzero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSettingUnitZero = true;
				openRD(49);

			}
		});

		btn_distance_material_getdistance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitFlag();
				openRD();
			}
		});
		btn_distance_material_stopgetdistance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitFlag();
				try {
					SerialTools.getInstance().closeRD();

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_distance_material_reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSettingMaterialUnitDistance = true;
				openRD();
			}
		});
		btn_distance_material_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitFlag();
				try {
					SerialTools.getInstance().closeRD();

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_distance_return.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InitFlag();
				isReturnMaterial = true;
				isNeedUpdateDistanceMemery = false;
				openRD();
			}
		});
		btn_distance_exitreturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitFlag();
				try {
					// UpdateMaterialListForRD(pId, different);
					isNeedUpdateDistanceMemery = true;
					SerialTools.getInstance().closeRD();

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_clear_distance_vendingchn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DISTANCECHNCOUNTLIST.clear();
				DistanceChnArr.clear();
				ClearSP(RDMaterialChnList);
				distance_listview_vendingchnlist.setAdapter(null);
			}
		});
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
		SerialTools.getInstance().openALLRD();
	}

	private void openRD(int pId) {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openRd(pId);
	}

	private void openRdSetUnit() {
		isSettingUnitDistance = true;
		openRD();
	}

	private void InitFlag() {
		isReturnMaterial = false;
		isSettingUnitDistance = false;
		isSettingUnitZero = false;
		isSettingMaterialUnitDistance = false;
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
	 * 用以处理测距模块串口传回的数据解析
	 * 
	 * @author junjie.you
	 * @param pReturnString
	 *            称重模块串口传回的数据
	 */
	private void RdSerialPortReturnStrHandler(String pReturnString) {

		if (pReturnString.length() == 370 || pReturnString.length() == 16) {
			pReturnString = pReturnString.substring(6);
			if (isSettingUnitZero) {
				DISTANCELIST.put(maxVendingCount + "", pReturnString.substring(0, 6));
			} else {
				for (int i = 0; i < 60; i++) {
					DISTANCELIST.put((i + 1) + "", pReturnString.substring(6 * i, 6 * i + 6));
				}
			}
			pReturnString = null;
			for (String i : TotalVendingChn) {
				if (DISTANCELIST.containsKey(i)) {
					// ZillionLog.i("yjjportvalue", DISTANCELIST.get(i));
					String mockDistanceV16 = DISTANCELIST.get(i).replaceAll(" ", "");// 找到对应的距离参数，16进制
					String mockDistanceV10 = Integer.valueOf(mockDistanceV16, 16).toString();
					float afterCount = CalcDistance(i, mockDistanceV10);
					if (isSettingUnitDistance) {
						UpdateUnitDistanceForEditText(i, afterCount + "");
					} else if (isSettingUnitZero) {
						SetSP(RdZeroList, i, mockDistanceV10);
					} else if (isSettingMaterialUnitDistance) {
						ShowMaterialUnitDistance(i, afterCount + "");
					} else if (isReturnMaterial) {
						ShowReturnMaterialDataList(i, afterCount + "");
					} else {
						ShowReturnMaterialDataList(i, afterCount + "");
					}
					ShowDistance4EditText(i, afterCount + "");
				}
			}
		}
	}

	// SaveSharedPreferencesForFW(portId, weightValue);

	/**
	 * 将取得的id与重量的值保存至对应的SP中，这里要分清是设置单位重量还是获取实际领取重量
	 * 
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID
	 * @param pLength
	 *            模块读取到的重量数值
	 */
	private void SaveSharedPreferencesForRD(String pId, String pLength) {

		// 获取之前该id内存储的长度
		String preLength = GetSP(RDDataList, pId, "0");
		int different = 0;
		// if (!isReturnMaterial) {
		int preLengthInt = ConvertHelper.toInt(preLength, 0);
		int nowLengthInt = ConvertHelper.toInt(pLength, deviationScalar);
		// 给出一个误差范围：如果之前的重量在现在重量加减误差标量之间则不更新材料列表
		if ((nowLengthInt - deviationScalar) > preLengthInt || (nowLengthInt + deviationScalar) < preLengthInt) {
			if (isNeedUpdateDistanceMemery) {
				SetSP(RDDataList, pId, pLength);
			} else {
				different = preLengthInt - nowLengthInt;
				UpdateMaterialListForRD(pId, different);
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
	private void UpdateUnitDistanceForEditText(String pId, String pNowLength) {
		float unitDistance = (float) 0.0;
		// for (String i : TotalVendingChn) {
		// if (DISTANCELIST.containsKey(i)) {
		// String mockDistanceV16 = DISTANCELIST.get(i).replaceAll(" ", "");//
		// 找到对应的距离参数，16进制
		// String mockDistanceV10 = Integer.valueOf(mockDistanceV16,
		// 16).toString();
		// int acNum = ConvertHelper.toInt(mockDistanceV10, 1) -
		// ConvertHelper.toInt(GetSP(RdZeroList, i), 1);
		// if (acNum < 0) {
		// acNum = 0;
		// }
		// mockDistanceV10 = acNum + "";
		// String denominator = txt_distance_unit_a.getText().toString();
		// txt_distance_unit_b.setText(mockDistanceV10);
		// unitDistance = (Integer.parseInt(mockDistanceV10) /
		// ConvertHelper.toFloat(denominator, (float) 1) / 10);
		// txt_distance_unit_c.setText(unitDistance + "");
		// SetSP(RDUnitList, i, unitDistance + "");
		// // ShowDistance4EditText(mockDistanceV10);
		// }
		// }
		String denominator = txt_distance_unit_a.getText().toString();
		txt_distance_unit_b.setText(pNowLength);
		unitDistance = (ConvertHelper.toInt(pNowLength, 0) / ConvertHelper.toFloat(denominator, (float) 1) / 10);
		txt_distance_unit_c.setText(unitDistance + "");
		SetSP(RDUnitList, pId, unitDistance + "");
	}

	/**
	 * 置零每个秤盘单位重量
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void InitSP(String pSPName) {
		// 先清空SP内的单位重量List
		final SharedPreferences spUnit = getSharedPreferences(pSPName, MODE_PRIVATE);
		spUnit.edit().clear().commit();
		// 再调用更新方法来更新单位重量显示
		// UpdateUnitWeightForEditText();
	}

	/**
	 * 获取测距模块单位长度
	 * 
	 * @author junjie.you
	 * @param pId
	 * 
	 */
	private String GetSP(String pSPName, String pId) {
		String retStr = null;
		// 先清空SP内的单位重量List
		final SharedPreferences spUnit = getSharedPreferences(pSPName, MODE_PRIVATE);
		if (spUnit.contains(pId)) {
			retStr = spUnit.getString(pId, "");
		}
		// 再调用更新方法来更新单位重量显示
		// UpdateUnitWeightForEditText();
		return retStr;
	}

	/**
	 * 获取测距模块单位长度
	 * 
	 * @author junjie.you
	 * @param pId
	 * 
	 */
	private String GetSP(String pSPName, String pId, String defaultValue) {
		String retStr = defaultValue;
		// 先清空SP内的单位重量List
		final SharedPreferences spUnit = getSharedPreferences(pSPName, MODE_PRIVATE);
		if (spUnit.contains(pId)) {
			retStr = spUnit.getString(pId, defaultValue);
		}
		// 再调用更新方法来更新单位重量显示
		// UpdateUnitWeightForEditText();
		return retStr;
	}

	/**
	 * 获取测距模块单位长度
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void SetSP(String pSPName, String pId, String pData) {
		// 先清空SP内的单位重量List
		final SharedPreferences spUnit = getSharedPreferences(pSPName, MODE_PRIVATE);
		spUnit.edit().putString(pId, pData).commit();
	}

	private void InitSPRDShowList() {
		final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
		for (int i = 1; i <= maxVendingCount; i++) {
			sp.edit().putString(i + "", i + "号货道").commit();
		}
	}

	private void ClearSP(String pSPName) {

		final SharedPreferences spUnit = getSharedPreferences(pSPName, MODE_PRIVATE);
		spUnit.edit().clear().commit();
	}

	/**
	 * 更新最终显示的List，例如： 称重模块A X2 称重模块B X1
	 * 
	 * @author junjie.you
	 * @param pId
	 *            测距模块ID
	 * @param pDifLength
	 *            变化的长度数值
	 */
	private boolean UpdateMaterialListForRD(String pId, int pDifLength) {
		try {
			// 获取显示物品列表文件
			final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
			// 获取该id在之前的显示列表内的个数，没有则为0
			String idName = sp.getString(pId, "0");
			if (idName.equals("0")) {
				idName = pId + "号模块文件名获取失败";
			}

			// 获取该物品锁对应的单位重量，没有则为1
			String idUnitLength = GetSP(RDMaterialUnitList, pId, "1");// 之前该id内存储的值
			int preCount = 0;
			if (!isReturnMaterial) {
				preCount = ConvertHelper.toInt(DISTANCECOUNTLIST.get(pId), 0);
			} else {
				preCount = ConvertHelper.toInt(DISTANCECHNCOUNTLIST.get(pId), 0);
			}
			// 根据重量变化值和单位重量计算出变化的个数
			float denominator = ConvertHelper.toFloat(idUnitLength, (float) 0.00);
			float afterCount = pDifLength / denominator;
			int difCount = 0;
			if (afterCount != 0) {
				afterCount = LengthCountCalculator(afterCount);
				difCount = Math.abs((int) afterCount);
				// if (pDifWeight > 0) {
				// afterCount = preCount + afterCount;
				// } else {
				if (!isReturnMaterial) {
					afterCount = preCount - afterCount;
				} else {
					afterCount += preCount;
				}

				// }
				if (isReturnMaterial) {
					// 将变化的个数更新该ID对应的显示个数
					if (afterCount == 0) {
						DISTANCECHNCOUNTLIST.remove(pId);
					} else {
						DISTANCECHNCOUNTLIST.put(pId, "" + afterCount);// 把显示LIST中的对应数据进行更新
					}
					SetSP(RDMaterialChnList, pId, afterCount + "");
				} else {
					// 将变化的个数更新该ID对应的显示个数
					if (afterCount == 0) {
						DISTANCECOUNTLIST.remove(pId);
					} else {
						DISTANCECOUNTLIST.put(pId, "" + afterCount);// 把显示LIST中的对应数据进行更新
					}
					ShowMaterialList();
					UpdateVendingChnList(pId, difCount, afterCount, preCount, pDifLength > 0 ? false : true);
				}
				ShowVendingChnList();

			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 更新货到库存列表
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param afterCount
	 */
	private void ShowVendingChnList() {
		DistanceChnArr.clear();
		DISTANCECHNCOUNTLIST.clear();
		GetVendingChnFromSP2List();
		Iterator<Entry<String, String>> it = DISTANCECHNCOUNTLIST.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			if (!entry.getValue().equals("0.0")) {
				DistanceChnArr.add(entry.getKey() + "号货道		X" + entry.getValue());
			}

		}

		distance_listview_vendingchnlist
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DistanceChnArr));

	}

	private void GetVendingChnFromSP2List() {
		for (int i = maxVendingCount; i <= maxVendingCount; i++) {
			DISTANCECHNCOUNTLIST.put(i + "", GetSP(RDMaterialChnList, maxVendingCount + "", "0"));
		}
	}

	/**
	 * 用户领料后触发的更新货道物品数量
	 * 
	 * @author junjie.you
	 * @param pId
	 *            变化货道Id
	 * @param pDifCount
	 *            本次领料个数变化值
	 * @param pCount
	 *            用户最终领料个数
	 * @param pPreCount
	 *            之前托盘个数变化值
	 * @param isActive
	 *            是否为正数
	 */
	private void UpdateVendingChnList(String pId, int pDifCount, float pCount, int pPreCount, boolean isActive) {
		if (DISTANCECHNCOUNTLIST.containsKey(pId)) {
			float preVendingChnCount = ConvertHelper.toFloat(DISTANCECHNCOUNTLIST.get(pId), (float) 0);
			float count = 0;
			if (isActive) {
				count = preVendingChnCount - pCount + pPreCount;
			} else {
				count = preVendingChnCount + pDifCount;
			}
			DISTANCECHNCOUNTLIST.put(pId, count + "");
			SetSP(RDMaterialChnList, pId, count + "");
		}
	}

	/**
	 * 显示当前毫米数
	 * 
	 * @author junjie.you
	 * @param pNowLength
	 */
	private void ShowDistance4EditText(String pId, String pNowLength) {
		float afterCount = CalcDistanceCount(pId, ConvertHelper.toFloat(pNowLength, (float) 0));
		txt_distance_show.setText(afterCount + "");
	}

	/**
	 * 计算当前实际毫米数
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pNowLength
	 * @return
	 */
	private float CalcDistance(String pId, String pNowLength) {
		String zeroLength = GetSP(RdZeroList, maxVendingCount + "");
		float nowLength = ConvertHelper.toInt(pNowLength, 0) - ConvertHelper.toInt(zeroLength, 0);
		return nowLength;
	}

	private float CalcDistanceCount(String pId, float pNowLength) {
		String unit = GetSP(RDUnitList, pId);
		float denominator = ConvertHelper.toFloat(unit, (float) 0.00);
		float afterCount = Math.abs(pNowLength) / denominator;
		return afterCount;
	}

	/**
	 * 显示物品单位长度
	 * 
	 * @author junjie.you
	 * @param pNowLength
	 */
	private void ShowMaterialUnitDistance(String pId, String pNowLength) {
		float NowLength = CalcDistanceCount(pId, ConvertHelper.toFloat(pNowLength, (float) 0));
		SetSP(RDMaterialUnitList, pId, NowLength + "");
		ShowMaterialUnitDistance4EditText(pId);

	}

	/**
	 * 显示补货流程
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pNowLength
	 */
	private void ShowReturnMaterialDataList(String pId, String pNowLength) {
		float afterCount = CalcDistanceCount(pId, ConvertHelper.toFloat(pNowLength, (float) 0));

		SaveSharedPreferencesForRD(pId, afterCount + "");
	}

	/**
	 * 显示物品单位长度
	 * 
	 * @author junjie.you
	 * @param pNowLength
	 */
	private void ShowMaterialUnitDistance4EditText(String pId) {
		String afterCount = GetSP(RDMaterialUnitList, pId);
		// CalcDistance(pId, GetSP(RDMaterialUnitList, pId));
		// CalcDistanceCount(afterCount);
		txt_distance_material.setText(afterCount);
	}

	/**
	 * 显示校准长度
	 * 
	 * @author junjie.you
	 * @param pId
	 */
	private void ShowUnitDistance4EditText(String pId) {
		String unit = GetSP(RDUnitList, pId);
		txt_distance_unit_c.setText(unit + "");
	}

	/**
	 * 将DISTANCECHNCOUNTLIST绑定到ListView上，同时更新界面
	 */
	private void ShowMaterialList() {
		DistanceArr.clear();
		Iterator<Entry<String, String>> it = DISTANCECOUNTLIST.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			DistanceArr.add(entry.getKey() + "号货道		X" + entry.getValue());
		}

		distance_listview_datalist
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DistanceArr));

	}

	/**
	 * 重置累计轮训时间
	 */
	private void resetTimer() {
		distanceStableTimeOut = 0;
	}

	private void startTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				distanceStableTimeOut += 50;
				isDistanceStable = false;
				if (distanceStableTimeOut == distanceStableTimeCount) {
					isDistanceStable = true;
				}
			}
		};
		distanceStableTimeOut = 0;
		timer = new Timer();
		timer.schedule(mTimerTask, 1, distanceStableTimer);
	}

	private void cancelImageTask() {
		mTimerTask.cancel();
	}

	/**
	 * 计算变化个数
	 * 
	 * @author junjie.you
	 * @param pNum
	 * @return
	 */
	private int LengthCountCalculator(float pNum) {
		int intPart = (int) pNum;
		if (FuzzyJudgment(pNum)) {
			if (pNum > 0) {
				intPart++;
			} else {
				intPart--;
			}
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
		pNum = Math.abs(pNum);
		/*
		 * 向上取整用Math.ceil(double a) 向下取整用Math.floor(double a)
		 */
		double ceil = Math.ceil(pNum);
		double floor = Math.floor(pNum);
		if ((pNum + lengthDeviationScalar) > ceil && ceil != floor) {
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
