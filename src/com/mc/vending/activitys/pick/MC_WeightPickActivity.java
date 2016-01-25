package com.mc.vending.activitys.pick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.mc.vending.parse.listener.DataParseRequestListener;
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

public class MC_WeightPickActivity extends BaseActivity
		implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {
	public DataServices dataServices;
	public final int deviationScalar = 5;// 材料列表更新的重量摇摆标量
	public final double weightDeviationScalar = 0.1;
	public final int maxVendingCount = 3;// 售货机id，十进制

	public final String FWDataList = "FWDataList";// 称重模块SP存储数据文件名称
	public final String FWShowList = "FWShowList";// 称重模块SP存储物品显示列表文件名称
	public final String FWUnitList = "FWUnitList";// 称重模块SP存储物品单位重量列表文件名称
	public final String FWWeightDataList = "FWWeightDataList";// 测距模块SP存储数据文件名称
	private Map<String, String> WEIGHTLIST = new LinkedHashMap<String, String>();// 用来储存每个称重传感器存储的个数List
	private Map<String, String> VENDINGCHNLIST = new LinkedHashMap<String, String>();// 用来储存每个货道库存个数的List
	private ArrayList<String> WeightArr = new ArrayList<String>();// 用户领料物品名称、个数列表
	private ArrayList<String> VendingChnArr = new ArrayList<String>();// 货到存放物品名称、个数列表
	public ListView weight_datalist;
	public ListView weight_listview_vendingchnlist;
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
	private Button btn_exitWeight;// 结束领料
	private Button btn_clearlist; // 清屏
	private Button btn_return; // 开始补货
	private Button btn_setting_unit_zero;
	private Button btn_exitreturn;// 停止补货
	private Button btn_clear_vendingchn;
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
	public boolean isSettingUnitWeight = false;
	public boolean isSettingZero = false;
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
		// startService();
		ShowVendingChnList();
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
				String[] portRtnStrList = ((String) msg.obj).split("FF");
				for (int i = 1; i <= portRtnStrList.length - 1; i++) {
					FWSerialPortReturnStrHandler(portRtnStrList[i]);
				}
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
		layout_show = (RelativeLayout) this.findViewById(R.id.relout_weight_show);
		weight_datalist = (ListView) this.findViewById(R.id.weight_listview_datalist);
		weight_listview_vendingchnlist = (ListView) this.findViewById(R.id.weight_listview_vendingchnlist);
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
		btn_clear_vendingchn = (Button) this.findViewById(R.id.btn_clear_vendingchn);
	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {
		InitSPFWShowList();
		UpdateUnitWeightForEditText();
		btn_getWeight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WeightArr.clear();
				openFW();
			}
		});
		btn_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		btn_setting_lock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSettingUnitWeight = false;
				try {
					SerialTools.getInstance().closeFW();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_setting_unlock.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openFwSetUnit();
			}
		});
		btn_setZero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSettingZero = true;
				SerialTools.getInstance().openFW(0, Constant.FW_SET_ZERO);
				try {
					SerialTools.getInstance().closeFW();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_setting_unit_zero.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetZeroSPUnitWeightForFW();
			}
		});
		btn_clearlist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WeightArr.clear();
				ClearFWDataListFromSP();
				weight_datalist.setAdapter(null);
			}
		});
		btn_return.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isReturnMaterial = true;
				VendingChnArr.clear();
				WEIGHTLIST.clear();
				openFW();
			}
		});
		btn_exitWeight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					SerialTools.getInstance().closeFW();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_exitreturn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isReturnMaterial = false;
				try {
					SerialTools.getInstance().closeFW();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_clear_vendingchn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VendingChnArr.clear();
				VENDINGCHNLIST.clear();
				ClearVendingChnFromSP();
				weight_listview_vendingchnlist.setAdapter(null);
			}
		});
	}

	private void resetViews() {
		layout_setting.setVisibility(View.INVISIBLE);
		layout_show.setVisibility(View.VISIBLE);
	}

	private void openFW() {
		SerialTools.getInstance().addToolsListener(this);
		try {
			for (int i = 1; i <= maxVendingCount; i++) {
				SerialTools.getInstance().openFW(i, Constant.FW_GET_WEIGHT);
				Thread.sleep(30);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openFwSetUnit() {
		isSettingUnitWeight = true;
		openFW();
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
			if (strArrayReturnHex.length != 0 && strArrayReturnHex != null && strArrayReturnHex.length > 2) {
				portId = Integer.parseInt(strArrayReturnHex[1], 16);
				// 取出“第三位”，由Hex转为二进制，并且不足8位的不足8位，例如：7位，1010000
				String theThirdByteStr = StringHelper.HexStringToBinaryString(strArrayReturnHex[2]);
				if (!StringHelper.isEmpty(theThirdByteStr) && strArrayReturnHex.length > 5) {
					// 对“第三位”状态位进行解析
					char[] theThirdByteArr = theThirdByteStr.toCharArray();
					decimalPointPosition = Integer.valueOf(theThirdByteStr.substring(5, 7), 2);
					isScallingError = Integer.valueOf(theThirdByteArr[4] + "", 2) == 1 ? true : false;
					isPositive = Integer.valueOf(theThirdByteArr[3] + "", 2) == 0 ? true : false;
					isStable = Integer.valueOf(theThirdByteArr[2] + "", 2) == 1 ? true : false;
					isOverload = Integer.valueOf(theThirdByteArr[1] + "", 2) == 1 ? true : false;
					// 高、中、低位重量数据
					String weightValue = "" + (strArrayReturnHex[5] + strArrayReturnHex[4] + strArrayReturnHex[3]);
					if (isStable && !isOverload && !isScallingError) {
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
			final SharedPreferences sp = getSharedPreferences(FWWeightDataList, MODE_PRIVATE);
			// 获取之前该id内存储的重量
			String preWeight = sp.getString(pId + "", "0");
			sp.edit().putString(pId + "", pWeight).commit();
			// if (!isReturnMaterial) {
			int preWeightInt = ConvertHelper.toInt(preWeight, 0);
			int nowWeightInt = ConvertHelper.toInt(pWeight, deviationScalar);
			// 给出一个误差范围：如果之前的重量在现在重量加减误差标量之间则不更新材料列表
			if ((nowWeightInt - deviationScalar) > preWeightInt || (nowWeightInt + deviationScalar) < preWeightInt) {
				UpdateMaterialList(pId + "", ConvertHelper.toInt(preWeight, 0) - ConvertHelper.toInt(pWeight, 0));
			}
			// }
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
		final SharedPreferences spUnit = getSharedPreferences(FWUnitList, MODE_PRIVATE);
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

	private void UpdateFWDataList(String pId, float pAcount) {
		final SharedPreferences spUnit = getSharedPreferences(FWDataList, MODE_PRIVATE);
		spUnit.edit().putString(pId, "" + pAcount).commit();
	}

	/**
	 * 更新每个秤盘单位重量
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pWeight
	 */
	private void UpdateSPUnitWeightForFW(int pId, int pWeight) {
		final SharedPreferences spUnit = getSharedPreferences(FWUnitList, MODE_PRIVATE);
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
		final SharedPreferences spUnit = getSharedPreferences(FWUnitList, MODE_PRIVATE);
		spUnit.edit().clear().commit();
		// 再调用更新方法来更新单位重量显示
		UpdateUnitWeightForEditText();
	}

	private void InitSPFWShowList() {
		final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
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
			final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
			// 获取该id在之前的显示列表内的个数，没有则为0
			String idName = sp.getString(pId, "0");
			if (idName.equals("0")) {
				idName = pId + "号模块文件名获取失败";
			}
			// 获取物品单位标准重量
			final SharedPreferences spUnit = getSharedPreferences(FWUnitList, MODE_PRIVATE);// 这语句会不会频繁开关SP?是不是影响性能？

			// 获取该物品锁对应的单位重量，没有则为0
			String idUnitWeight = spUnit.getString(pId, "1");// 之前该id内存储的值
			int preCount = 0;
			if (!isReturnMaterial) {
				preCount = ConvertHelper.toInt(WEIGHTLIST.get(pId), 0);
			} else {
				preCount = ConvertHelper.toInt(VENDINGCHNLIST.get(pId), 0);
			}
			// 根据重量变化值和单位重量计算出变化的个数
			float denominator = ConvertHelper.toFloat(idUnitWeight, (float) 0.00);
			float afterCount = pDifWeight / denominator;
			int difCount = 0;
			if (afterCount != 0) {
				afterCount = WeightCountCalculator(afterCount);
				difCount = Math.abs((int) afterCount);
				// if (pDifWeight > 0) {
				// afterCount = preCount + afterCount;
				// } else {
				if (isReturnMaterial) {
					afterCount = preCount - afterCount;
				} else {
					afterCount += preCount;
				}

				// }
				if (isReturnMaterial) {
					// 将变化的个数更新该ID对应的显示个数
					if (afterCount == 0) {
						VENDINGCHNLIST.remove(pId);
					} else {
						VENDINGCHNLIST.put(pId, "" + afterCount);// 把显示LIST中的对应数据进行更新
					}
					UpdateVendingChnList(pId, afterCount);
				} else {
					// 将变化的个数更新该ID对应的显示个数
					if (afterCount == 0) {
						WEIGHTLIST.remove(pId);
					} else {
						WEIGHTLIST.put(pId, "" + afterCount);// 把显示LIST中的对应数据进行更新
					}
					ShowMaterialList();
					UpdateVendingChnList(pId, difCount, afterCount, preCount, pDifWeight > 0 ? true : false);
				}

				ShowVendingChnList();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将WEIGHTLIST绑定到ListView上，同时更新界面
	 */
	private void ShowMaterialList() {
		WeightArr.clear();
		Iterator<Entry<String, String>> it = WEIGHTLIST.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			WeightArr.add(entry.getKey() + "号托盘		X" + entry.getValue());
		}

		weight_datalist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, WeightArr));

	}

	/**
	 * 从数据源读取历史记录
	 * 
	 * @author junjie.you
	 */
	private void GetVendingChnFromSP2List() {
		final SharedPreferences fwDataList = getSharedPreferences(FWDataList, MODE_PRIVATE);// 这语句会不会频繁开关SP?是不是影响性能？

		for (int i = 1; i <= maxVendingCount; i++) {
			VENDINGCHNLIST.put(i + "", fwDataList.getString(i + "", "0.0"));
		}

	}

	/**
	 * 清空货道物品SP
	 * 
	 * @author junjie.you
	 */
	private void ClearVendingChnFromSP() {
		final SharedPreferences rdDataList = getSharedPreferences(FWDataList, MODE_PRIVATE);// 这语句会不会频繁开关SP?是不是影响性能？
		rdDataList.edit().clear().commit();
	}

	private void ClearFWDataListFromSP() {
		final SharedPreferences fwDataList = getSharedPreferences(FWDataList, MODE_PRIVATE);// 这语句会不会频繁开关SP?是不是影响性能？
		fwDataList.edit().clear().commit();
	}

	/**
	 * 更新货到库存列表
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param afterCount
	 */
	private void ShowVendingChnList() {
		VendingChnArr.clear();
		VENDINGCHNLIST.clear();
		GetVendingChnFromSP2List();
		Iterator<Entry<String, String>> it = VENDINGCHNLIST.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			if (!entry.getValue().equals("0.0")) {
				VendingChnArr.add(entry.getKey() + "号托盘		X" + entry.getValue());
			}

		}

		weight_listview_vendingchnlist
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, VendingChnArr));

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
		if (VENDINGCHNLIST.containsKey(pId)) {
			float preVendingChnCount = ConvertHelper.toFloat(VENDINGCHNLIST.get(pId), (float) 0);
			float count = 0;
			if (isActive) {
				count = preVendingChnCount - pCount + pPreCount;
			} else {
				count = preVendingChnCount + pDifCount;
			}
			VENDINGCHNLIST.put(pId, count + "");
			UpdateFWDataList(pId, count);
		}
	}

	private void UpdateVendingChnList(String pId, float pCount) {
		UpdateFWDataList(pId, pCount);
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
		/*
		 * 向上取整用Math.ceil(double a) 向下取整用Math.floor(double a)
		 */
		pNum = Math.abs(pNum);
		double ceil = Math.ceil(pNum);
		double floor = Math.floor(pNum);
		if (((pNum + weightDeviationScalar) > ceil) && ceil != floor) {
			if ((pNum - weightDeviationScalar) > 0) {
				flag = true;
			}

		}
		return flag;
	}
}