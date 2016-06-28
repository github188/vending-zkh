/**
 * 
 */
package com.mc.vending.activitys.pick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.activitys.MC_ImagePlayerActivity;
import com.mc.vending.activitys.VersionActivity;
import com.mc.vending.activitys.setting.MC_SettingActivity;
import com.mc.vending.adapter.MC_IntelligencePickListAdapter;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ProductData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.data.VersionData;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.parse.ProductDataParse;
import com.mc.vending.parse.VendingPictureDataParse;
import com.mc.vending.parse.VersionDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.service.DataServices;
import com.mc.vending.service.GeneralMaterialService;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import com.zillionstar.tools.ZillionLog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnLongClickListener;
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
public class MC_IntelligencePickActivity extends BaseActivity
		implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		openRFID();
		super.onRestart();
	}

	public DataServices dataServices;
	public final int deviationScalar = 5;// 材料列表更新的重量摇摆标量
	public final double weightDeviationScalar = 0.1;
	public final int maxVendingCount = 69;// 售货机id，十进制

	public final String FWDataList = "FWDataList";// 称重模块SP存储数据文件名称
	public final String FWShowList = "FWShowList";// 称重模块SP存储物品显示列表文件名称
	public final String FWUnitList = "FWUnitList";// 称重模块SP存储物品单位重量列表文件名称
	public final String FWWeightDataList = "FWWeightDataList";// 测距模块SP存储数据文件名称
	private Map<String, String> WEIGHTLIST = new LinkedHashMap<String, String>();// 用来储存每个称重传感器存储的个数List
	private Map<String, String> VENDINGCHNLIST = new LinkedHashMap<String, String>();// 用来储存每个货道库存个数的List
	private ArrayList<String> WeightArr = new ArrayList<String>();// 用户领料物品名称、个数列表
	private ArrayList<String> VendingChnArr = new ArrayList<String>();// 货到存放物品名称、个数列表
	public ListView intelligence_listview_datalist;
	public ListView intelligence_listview_vendingchnlist;
	private RelativeLayout relativelayout_intelli_pick;

	private ImageView iv_sku; // 商品图片
	private ImageView iv_intelligence_img;// 展示的图片
	private int RDstartNum = 10;
	private int RDendNum = 69;
	private int FWstartNum = 70;
	private int FWendNum = 129;
	private Button btn_out; // 隐藏按钮
	private Button btn_intlli_confirm;
	private EditText password; // 密码输入框

	private VendingData vendData; // 售货机对象
	private VendingChnData vendingChn; // 售货机货道对象
	private VendingCardPowerWrapperData wrapperData; // 卡密码权限对象
	private int stockCount; // 库存数量
	private AsyncImageLoader asyncImageLoader;

	private final int imagePlayerTimer = 1000; // 进入待机界面心跳。每秒钟执行一次
	private final int imagePlayerTimeCount = 1000 * 60; // 默认待机默认跳转时间1分钟
	private int imagePlayerTimeOut;

	public boolean isSettingUnitWeight = false;
	public boolean isSettingZero = false;
	public final double lengthDeviationScalar = 0.15;

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
	// private ArrayList<String> DistanceArr = new ArrayList<String>();//
	// 领料的Array
	ArrayList<HashMap<String, Object>> pickItemList = new ArrayList<HashMap<String, Object>>();
	private ArrayList<String> DistanceChnArr = new ArrayList<String>();// 库存的Array
	/**
	 * List中存的是待检查的称重模块编号,都查完才能显示最终领料个数
	 */
	private List<String> ListOfCheckIfFWCanShow = new ArrayList<String>();
	public ListView distance_listview_datalist;
	private TextView alert_msg_title; // 提示标题
	private TextView tv_pick_result_title;
	private TextView alert_msg; // 提示内容
	private String vendCode; // 售货机编号

	private Timer timer;
	private List<VendingChnData> VendingChnDataList;
	private List<String> VendingChnNumList = new ArrayList<String>();

	private TimerTask mTimerTask;
	private final static int MESSAGE_Image_player = 99; // 跳转到待机
	boolean isOperating = false; // 是否再操作中
	private boolean isStoreChecked; // 格子机验证返回
	public boolean isReturnMaterial = false;// 是否在做补货
	public boolean isDistanceStable = false;
	public boolean isNeedDebugLog = false;
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

	private boolean isNeedUpdateDataMemery = true;

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
		setContentView(R.layout.activity_intelligence_pick);
		startService();
		ActivityManagerTool.getActivityManager().add(this);
		getParam();
		initComponents();
		InitList();
		initObject();
		InitFlag();
		InitView();

		openRD();
//		 openAllFW();
		openRFID();
		// startTimerTask(); // This Foo is run in OnResume()
	}

	private void requestGetClientVersionServer() {

		VersionDataParse parse = new VersionDataParse();
		parse.setListener(this);
		parse.requestVersionData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VERSION);
	}

	private void InitView() {
		iv_intelligence_img.setVisibility(View.VISIBLE);
		relativelayout_intelli_pick.setVisibility(View.GONE);
	}

	private void InitList() {
		DISTANCECOUNTLIST.clear();
		WEIGHTLIST.clear();
		intelligence_listview_datalist.setAdapter(null);
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
			showToast("没有链接到网络，请检查网络链接！");
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
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		startTimerTask();
		cancelTimerTask();
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
		ActivityManagerTool.getActivityManager().removeActivity(this);
		super.onDestroy();
		this.unbindService(conn);
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
		cancelTimerTask();
		resetTimer();
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
		case SerialTools.MESSAGE_LOG_mRFIDReader:
			// 查询卡类型：管理员还是普通员工
			stopLoading();
			value = MyFunc.getRFIDSerialNo(value);
			msg.obj = value;

			if (!StringHelper.isEmpty(value) && !value.equals("")) {
				ZillionLog.i("yjjtest", "当前领料卡号：：" + value);
				ZillionLog.i("yjjtestRFID", "当前领料卡号：：" + value);
				// 检查是不是管理员卡，是则跳转到管理员界面
				if (!setValidate(value)) {
					handler.sendMessage(msg);
				} else {
					openRFID();
				}
			}
			break;
		case SerialTools.MESSAGE_LOG_mRD:
			// resetTimer();
			handler.sendMessage(msg);
			break;
		case SerialTools.MESSAGE_LOG_mLocker:
			// resetTimer();
			handler.sendMessage(msg);
			break;
		case SerialTools.MESSAGE_LOG_mFw:
			// resetTimer();
			handler.sendMessage(msg);
			break;
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String[] portRtnStrList = null;
			switch (msg.what) {
			case SerialTools.MESSAGE_LOG_mRFIDReader:
				try {
					if (VendingChnDataList == null || VendingChnDataList.isEmpty()) {
						initObject();
					}
					// 将所有货道里面的产品加入查询列表
					for (VendingChnData vendingChnData : VendingChnDataList) {
						if (vendingChnData.getVc1Status().equals("0")) {
							VendingChnNumList.add(vendingChnData.getVc1Code());
						}
					}
					// 过滤所有标识为不可用的货道
					boolean validateResult = cardPasswordValidate(msg.obj.toString());
					if (validateResult) {
						InitList();
						// SerialTools.getInstance().closeFW();
						SerialTools.getInstance().closeRD();
						Thread.sleep(500);
						SerialTools.getInstance().openLocker();

						// SaveSharedPreferencesForFW(71, "8000");

						isNeedUpdateDataMemery = false;
					} else {
						openRFID();
					}

				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					openRD();
					// openFW();
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					openRD();
					e.printStackTrace();
				}
				break;
			case SerialTools.MESSAGE_LOG_mLocker:
				resetTimer();
				portRtnStrList = ((String) msg.obj).replaceAll(Constant.RDSERVETAILWITHBLANK, "")
						.split(Constant.RDSERVEHEADWITHBLANK);
				if (portRtnStrList != null && portRtnStrList.length > 1) {
					int result = LockerSerialPortReturnStrHandler(portRtnStrList[1]);
					// showToast("当前锁状态值是：" + result + "");
					ZillionLog.i("yjjtest", "当前锁result状态：：" + result);
					ZillionLog.i("yjjtestLockerStatus", "当前锁result状态：：" + result);
					isNeedUpdateDataMemery = false;

					SerialTools.getInstance().checkLocker();
					// 正常开门
					if (result == 0 || result == 8) {
						// showToast("当前锁状态值是：" + result + ""+"正常开门");
						// isNeedUpdateDataMemery = false;
						// SerialTools.getInstance().checkLocker();
					}
					// 正常关门
					if (result == 9) {
						// showToast("当前锁状态值是：" + result + "" + "电磁锁闭合，正常关门");
						try {
							SerialTools.getInstance().closeCheckLocker();
							SerialTools.getInstance().openALLRD();
							openRFID();
						} catch (SerialPortException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// SaveSharedPreferencesForRD("11", "1000");

						// SaveSharedPreferencesForFW(71, "80");
						// openRFID();
						// openRD();
						// if (isReturnMaterial) {
						// ShowChnMaterialList();
						// DISTANCECHNCOUNTLIST.clear();
						// VENDINGCHNLIST.clear();
						// } else {
						// ShowMaterialList();
						// DISTANCECOUNTLIST.clear();
						// WEIGHTLIST.clear();
						// }
						// UpdateVendingChnList(pId, difCount, afterCount,
						// preCount, pDifWeight > 0 ? true : false);
					}
					// 其他异常情况如下操作
					if (result == -1) {
						showToast("当前锁状态值是：" + result + "");
						try {
							isNeedUpdateDataMemery = true;
							SerialTools.getInstance().closeCheckLocker();
							Thread.sleep(500);
							openRD();
							openRFID();
						} catch (SerialPortException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			case SerialTools.MESSAGE_LOG_mRD:
				portRtnStrList = ((String) msg.obj).replaceAll(Constant.RDSERVETAILWITHBLANK, "")
						.split(Constant.RDSERVEHEADWITHBLANK);
				for (int i = 1; i <= portRtnStrList.length - 1; i++) {
					RdSerialPortReturnStrHandler(portRtnStrList[i]);
				}
				break;
			case SerialTools.MESSAGE_LOG_mFw:
				String strWithoutHeadTail = ((String) msg.obj).replace(Constant.FWSERVETAIL, "")
						.replace(Constant.FWSERVEHEAD, "");
				if (!MyFunc.CheckBccHandler(strWithoutHeadTail)) {
					break;
				}
				portRtnStrList = strWithoutHeadTail.split(" ");
				for (int i = 1; i <= portRtnStrList.length - 1; i++) {
					FWSerialPortReturnStrHandler(portRtnStrList[i]);
				}
				// openAllFW();
				break;
			case MESSAGE_Image_player:
				goImagePlayerAcitvity();

			default:
				break;
			}
		}
	};

	private boolean cardPasswordValidate(String cardValue) {
		closeRFID();
		if (StringHelper.isEmpty(cardValue, true)) {
			showToast(getResources().getString(R.string.placeholder_card_pwd));
			return false;
		} else {
			// 检查卡/密码-权限
			if (vendData != null) {
				ServiceResult<VendingCardPowerWrapperData> result = GeneralMaterialService.getInstance()
						.checkCardPowerOut(
								// isRFID ? CardData.CARD_SERIALNO_PARAM :
								// CardData.CARD_PASSWORD_PARAM,
								CardData.CARD_SERIALNO_PARAM, cardValue, vendData.getVd1Id());
				if (!result.isSuccess()) {
					showToast(result.getMessage());
					return false;
				}
				wrapperData = result.getResult();
				return true;
			} else {
				getParam();
				showToast("读取卡信息失败,请重新刷卡!");
				return false;
			}
		}
	}

	/**
	 * 点击设置后输入的密码验证
	 */
	private boolean setValidate(String cardValue) {
		closeRFID();
		ServiceResult<VendingData> result = CompositeMaterialService.getInstance().checkVending();
		if (!result.isSuccess()) {
			// showToast(result.getMessage());
			return false;
		}
		if (StringHelper.isEmpty(cardValue, true)) {
			// showToast(getResources().getString(R.string.placeholder_card_pwd));
			return false;
		} else {
			if (vendData == null) {
				return false;
			}
			// 检查卡/密码-权限,进入设置只能刷卡
			ServiceResult<VendingCardPowerWrapperData> VendingCardPowerWrapperDataResult = ReplenishmentService
					.getInstance().checkCardPowerInner(cardValue, vendData.getVd1Id());
			if (!VendingCardPowerWrapperDataResult.isSuccess()) {
				// showToast(VendingCardPowerWrapperDataResult.getMessage());
				return false;
			}
			closeRFID();
			try {
				SerialTools.getInstance().closeFW();
				SerialTools.getInstance().closeRD();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			wrapperData = VendingCardPowerWrapperDataResult.getResult();

			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			intent.putExtra("wrapperData", wrapperData);
			intent.putExtra("vendData", vendData);

			intent.putExtras(bundle);
			intent.setClass(MC_IntelligencePickActivity.this, MC_SettingActivity.class);
			startActivityForResult(intent, 1000);
			// startActivity(intent);
		}
		return true;
	}

	@Override
	public void serialReturn(String value, int serialType, Object userInfo) {
		// TODO Auto-generated method stub
	}

	/**
	 * 检查卡权限
	 * 
	 * @param pCardValue
	 *            卡号
	 * @return
	 */
	private boolean ValidateCardPower(String pCardValue) {
		if (pCardValue != null && pCardValue.equals("2960251873")) {
			return true;
		}
		return false;
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
		ServiceResult<VendingData> vendResult = GeneralMaterialService.getInstance().checkVending();
		// 判断售货机状态－－验证是否可用
		if (vendResult.isSuccess()) {
			vendData = vendResult.getResult();
		} else {
			ZillionLog.i("异常占位", "检查售货机状态出错,售货机编号:" + vendCode);
		}
	}

	/**
	 * 初始化对象
	 */
	private void initComponents() {
		intelligence_listview_datalist = (ListView) this.findViewById(R.id.intelligence_listview_datalist);
		relativelayout_intelli_pick = (RelativeLayout) this.findViewById(R.id.relativelayout_intelli_pick);
		iv_intelligence_img = (ImageView) this.findViewById(R.id.iv_intelligence_img);
		btn_out = (Button) this.findViewById(R.id.btn_intlli_out);
		btn_intlli_confirm = (Button) this.findViewById(R.id.btn_intlli_confirm);
		tv_pick_result_title = (TextView) this.findViewById(R.id.tv_pick_result_title);
	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {
		if (VendingChnDataList == null) {
			VendingChnDataList = new VendingChnDbOper().findAllUsefull();
		}
		InitSPFWShowList();
		InitSPRDShowList();
		btn_intlli_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitList();
				InitView();
				openRFID();
				openRD();
			}
		});

		btn_out.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				password = new EditText(MC_IntelligencePickActivity.this);
				password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				final Builder dialog = new AlertDialog.Builder(MC_IntelligencePickActivity.this).setTitle("请输入密码")
						.setView(password).setCancelable(false)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (!StringHelper.isEmpty(password.getText().toString(), true)) {

									ServiceResult<VendingData> vendResult = GeneralMaterialService.getInstance()
											.checkVending();
									// 判断售货机状态－－验证是否可用
									if (!vendResult.isSuccess()) {
										showToast(vendResult.getMessage());
										return;
									}
									vendData = vendResult.getResult();
									// 检查卡/密码-权限,进入设置只能刷卡
									ServiceResult<VendingCardPowerWrapperData> result = ReplenishmentService
											.getInstance()
											.checkCardPowerInner(password.getText().toString(), vendData.getVd1Id());
									if (!result.isSuccess()) {
										showToast(result.getMessage());
										return;
									}
									wrapperData = result.getResult();

									Intent intent = new Intent();
									Bundle bundle = new Bundle();

									intent.putExtra("wrapperData", wrapperData);
									intent.putExtra("vendData", vendData);

									intent.putExtras(bundle);
									intent.setClass(MC_IntelligencePickActivity.this, MC_SettingActivity.class);
									startActivityForResult(intent, 1000);
									// startActivity(intent);

									// VendingPasswordData data = new
									// VendingPasswordDbOper()
									// .getVendingPasswordByPassword(password.getText().toString());
									// if (data == null) {
									// showToast("密码错误");
									// } else {
									// ActivityManagerTool.getActivityManager().exit();
									// finish();
									// }
								} else {
									showToast("密码不能为空!");
								}
							}
						}).setNegativeButton("取消", null);
				dialog.show();

				return false;
			}
		});
	}

	private void openRFID() {
		SerialTools.getInstance().openRFIDReader();
	}

	private void closeRFID() {
		try {
			SerialTools.getInstance().closeRFIDReader();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	private void openRD() {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openALLRD();
	}

	private void openRD(int pId) {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openRd(pId);
	}

	private void InitFlag() {
		isReturnMaterial = false;
		isNeedUpdateDataMemery = true;
	}

	/**
	 * 用以处理锁模块传回的数据解析
	 * 
	 * @author junjie.you
	 * @param pReturnString
	 * @return 0：电磁锁开电控锁开有市电 1：电磁锁关 2：电控锁关 3电磁锁关电控锁关 ： 4：无市电 5：电磁锁关无市电 6：电控锁关无市电
	 *         7：电磁锁关电控锁关无市电 -1:开锁失败 8:正常开门 11：正常关门
	 */
	private int LockerSerialPortReturnStrHandler(String pReturnString) {
		int result = 0;
		if (pReturnString != null && pReturnString.length() == 19) {
			String[] strArrayReturnHex = pReturnString.split(" ");
			// 进行每个节点位数判断
			if (strArrayReturnHex[3].equals("1C")) {
				result += 1;
			}
			// if (strArrayReturnHex[4].equals("2C")) {
			// result += 2;
			// }
			if (strArrayReturnHex[5].equals("3C")) {
				result += 4;
			}
			if (strArrayReturnHex[2].equals("11")) {
				result += 8;
			}
			if (strArrayReturnHex[2].equals("0E")) {
				result = -1;
			}

		}
		return result;
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
			for (int i = RDstartNum; i <= RDendNum; i++) {
				DISTANCELIST.put(i + "", pReturnString.substring(6 * (i - RDstartNum), 6 * (i - RDstartNum) + 6));
			}
			pReturnString = null;
			Iterator<Entry<String, String>> it = DISTANCELIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				String mockDistanceV16 = DISTANCELIST.get(entry.getKey().toString()).replaceAll(" ", "");// 找到对应的距离参数，16进制
				String mockDistanceV10 = Integer.valueOf(mockDistanceV16, 16).toString();// 转为10进制
				float afterCount = CalcDistance(entry.getKey().toString(), mockDistanceV10);
				ShowReturnMaterialDataList(entry.getKey().toString(), afterCount + "");
			}
			if (ListOfCheckIfFWCanShow.isEmpty() && isNeedUpdateDataMemery == false) {
				// SaveSharedPreferencesForRD("11", "100");
				ShowMaterialList();
				DISTANCECOUNTLIST.clear();
				WEIGHTLIST.clear();
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
			if (isNeedUpdateDataMemery) {
				SetSP(RDDataList, pId, pLength);
			} else {
				different = preLengthInt - nowLengthInt;
				UpdateMaterialListForRD(pId, different);
			}
		}
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
		for (int i = RDstartNum; i <= RDendNum; i++) {
			VendingChnData vendingChnData = new VendingChnDbOper().getVendingChnByCode(i + "");
			if (vendingChnData != null) {
				ProductData productData = new ProductDbOper().getProductById(vendingChnData.getVc1Pd1Id());
				if (productData != null) {
					sp.edit().putString(i + "", productData.getPd1Name()).commit();
					continue;
				}
			}
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

			String idUnitLength = "50";
			// 获取该物品锁对应的单位长度，没有则为50
			VendingChnData vendingChnData = new VendingChnDbOper()
					.getVendingChnByCode(ConvertHelper.toInt(pId, 0) + "");// 61开始才是测距模块的第一个
			if (vendingChnData != null) {
				ProductData productData = new ProductDbOper().getProductById(vendingChnData.getVc1Pd1Id());
				if (productData != null && productData.getPd1Length() != null) {
					idUnitLength = productData.getPd1Length();
				}
			}
			int preCount = 0;
			if (!isReturnMaterial) {
				preCount = ConvertHelper.toInt(DISTANCECOUNTLIST.get(pId), 0);
				// preCount =
				// GeneralMaterialService.getInstance().getVendingChnStock(vendingChn.getVc1Vd1Id(),
				// pId);
				// if (preCount < 0) {
				// preCount = 0;
				// ZillionLog.i(this.getClass().getName(), "货道库存异常，为负数：" +
				// preCount);
				// }
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

				// if (difCount > preCount) {
				// difCount = preCount;
				// }
			}
			if (isReturnMaterial) {
				// 将变化的个数更新该ID对应的显示个数
				if (afterCount == 0) {
					DISTANCECHNCOUNTLIST.remove(pId);
				} else {
					DISTANCECHNCOUNTLIST.put(pId, "" + difCount);//
					// 把显示LIST中的对应数据进行更新
				}
				// SetSP(RDMaterialChnList, pId, afterCount + "");
			} else {
				// 将变化的个数更新该ID对应的显示个数
				if (afterCount == 0) {
					DISTANCECOUNTLIST.remove(pId);
				} else {
					DISTANCECOUNTLIST.put(pId, "" + difCount);// 把显示LIST中的对应数据进行更新
				}

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

		intelligence_listview_vendingchnlist
				.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, VendingChnArr));
	}

	private void GetVendingChnFromSP2List() {
		for (int i = maxVendingCount; i <= maxVendingCount; i++) {
			DISTANCECHNCOUNTLIST.put(i + "", GetSP(RDMaterialChnList, maxVendingCount + "", "0"));
		}
	}

	/**
	 * 保存领料记录
	 * 
	 * @author junjie.you
	 */
	private boolean SaveVendingStock() {
		if (!DISTANCECOUNTLIST.isEmpty() || !WEIGHTLIST.isEmpty()) {
			Iterator<Entry<String, String>> it = DISTANCECOUNTLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 判断货到状态－类型，返回商品id，货道id
				ServiceResult<VendingChnData> vendingResult = GeneralMaterialService.getInstance().checkVendingChn(
						(ConvertHelper.toInt(entry.getKey(), 1)) + "", VendingChnData.VENDINGCHN_METHOD_GENERAL);
				if (!vendingResult.isSuccess()) {
					showToast(vendingResult.getMessage());
					return false;
				}
				vendingChn = vendingResult.getResult();
				GeneralMaterialService.getInstance().saveStockTransaction(ConvertHelper.toInt(entry.getValue(), 0),
						vendingChn, wrapperData);
			}
			it = WEIGHTLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 判断货到状态－类型，返回商品id，货道id
				ServiceResult<VendingChnData> vendingResult = GeneralMaterialService.getInstance().checkVendingChn(
						ConvertHelper.toInt(entry.getKey(), 1) + "", VendingChnData.VENDINGCHN_METHOD_GENERAL);
				if (!vendingResult.isSuccess()) {
					showToast(vendingResult.getMessage());
					return false;
				}
				vendingChn = vendingResult.getResult();
				GeneralMaterialService.getInstance().saveStockTransaction(ConvertHelper.toInt(entry.getValue(), 0),
						vendingChn, wrapperData);
			}
		}
		return true;
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
		String unit = "6.5";
		float denominator = ConvertHelper.toFloat(unit, (float) 0.00);
		float afterCount = Math.abs(pNowLength) / denominator;
		return afterCount;
	}

	/**
	 * 测距模块返回值处理流程
	 * 
	 * @author junjie.you
	 * @param pId
	 * @param pNowLength
	 */
	private void ShowReturnMaterialDataList(String pId, String pNowLength) {
		if (VendingChnNumList.contains(pId)) {
			float afterCount = CalcDistanceCount(pId, ConvertHelper.toFloat(pNowLength, (float) 0));
			SaveSharedPreferencesForRD(pId, afterCount + "");
		}
	}

	/**
	 * 将ChnCOUNTLIST绑定到ListView上，同时更新界面
	 */
	private void ShowChnMaterialList() {
		pickItemList.clear();
		if (!DISTANCECHNCOUNTLIST.isEmpty() || !VENDINGCHNLIST.isEmpty()) {
			Iterator<Entry<String, String>> it = DISTANCECHNCOUNTLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 获取显示物品列表文件
				final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
				// 获取该id在之前的显示列表内的个数，没有则为0
				int stockNum = ConvertHelper.toInt(entry.getKey(), 61) + 60;
				String idName = sp.getString(stockNum + "", "0");
				if (idName.equals("0")) {
					idName = entry.getKey() + "号货道";
				}
				// DistanceArr.add(idName + " X" + entry.getValue());
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("stockNum", stockNum + "号货道");
				tempHashMap.put("pdName", idName);
				tempHashMap.put("quantity", "X" + "  " + entry.getValue());
				pickItemList.add(tempHashMap);
				tempHashMap.clear();
			}
			it = VENDINGCHNLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 获取显示物品列表文件
				final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
				int stockNum = ConvertHelper.toInt(entry.getKey(), 61);
				String idName = sp.getString(stockNum + "", "0");
				if (idName.equals("0")) {
					idName = entry.getKey() + "号货道";
				}
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("stockNum", stockNum + "号货道");
				tempHashMap.put("pdName", idName);
				tempHashMap.put("quantity", "X" + "  " + entry.getValue());
				pickItemList.add(tempHashMap);
				tempHashMap.clear();
			}
			if (pickItemList != null && !pickItemList.isEmpty()) {
				iv_intelligence_img.setVisibility(View.GONE);
				// tv_intelligence_dialog.setVisibility(View.GONE);
				relativelayout_intelli_pick.setVisibility(View.VISIBLE);
				MC_IntelligencePickListAdapter adapter = new MC_IntelligencePickListAdapter(this, pickItemList);
				intelligence_listview_datalist.setAdapter(adapter);
				isNeedUpdateDataMemery = true;
				isReturnMaterial = false;
			}
			RunningDelayTask();
		}
	}

	/**
	 * 将COUNTLIST绑定到ListView上，同时更新界面
	 */
	private void ShowMaterialList() {
		if (!DISTANCECOUNTLIST.isEmpty() || !WEIGHTLIST.isEmpty()) {
			pickItemList.clear();
			Iterator<Entry<String, String>> it = DISTANCECOUNTLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 获取显示物品列表文件
				final SharedPreferences sp = getSharedPreferences(RDIdNameList, MODE_PRIVATE);
				// 获取该id在之前的显示列表内的个数，没有则为0
				int stockNum = ConvertHelper.toInt(entry.getKey(), RDstartNum);
				String idName = sp.getString(stockNum + "", "0");
				if (idName.equals("0")) {
					idName = entry.getKey() + "号货道";
				}
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("stockNum", stockNum + "号货道");
				tempHashMap.put("pdName", idName);
				tempHashMap.put("quantity", "X" + "  " + entry.getValue());
				pickItemList.add(tempHashMap);
			}
			it = WEIGHTLIST.entrySet().iterator();
			while (it.hasNext()) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				// 获取显示物品列表文件
				final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
				int stockNum = ConvertHelper.toInt(entry.getKey(), FWstartNum);
				String idName = sp.getString(stockNum + "", "0");
				if (idName.equals("0")) {
					idName = entry.getKey() + "号";
				}
				HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
				tempHashMap.put("stockNum", stockNum + "号货道");
				tempHashMap.put("pdName", idName);
				tempHashMap.put("quantity", "X" + "  " + entry.getValue());
				pickItemList.add(tempHashMap);
			}
			if (pickItemList != null && !pickItemList.isEmpty()) {
				iv_intelligence_img.setVisibility(View.GONE);
				relativelayout_intelli_pick.setVisibility(View.VISIBLE);
				MC_IntelligencePickListAdapter adapter = new MC_IntelligencePickListAdapter(this, pickItemList);
				intelligence_listview_datalist.setAdapter(adapter);
				isNeedUpdateDataMemery = true;
			}
			RunningDelayTask();
		}
	}

	private void RunningDelayTask() {
		SaveVendingStock();
		new Handler().postDelayed(new Runnable() {

			public void run() {
				// execute the task
				InitList();
				InitView();
				openRFID();
				openRD();
			}
		}, 10000);
	}

	/**
	 * 重置累计轮训时间
	 */
	private void resetTimer() {
		imagePlayerTimeOut = 0;
	}

	private void startTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				imagePlayerTimeOut += 1000;
				isDistanceStable = false;
				ZillionLog.i("yjjtestImage", "当前imagePlayerTimeOut：" + imagePlayerTimeOut + "当前imagePlayerTimeCount："
						+ imagePlayerTimeCount);
				if (imagePlayerTimeOut >= imagePlayerTimeCount) {
					Message msg = new Message();
					msg.what = MESSAGE_Image_player;
					handler.sendMessage(msg);
				}
			}
		};
		imagePlayerTimeOut = 0;
		timer = new Timer();
		timer.schedule(mTimerTask, 1, imagePlayerTimer);
	}

	private void cancelTimerTask() {
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
		if (FuzzyJudgmentForLength(pNum)) {
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
	private boolean FuzzyJudgmentForLength(float pNum) {
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
			if (strArrayReturnHex != null && strArrayReturnHex.length == 14) {
				// // portId = Integer.parseInt(strArrayReturnHex[1], 16);
				// // 取出“第三位”，由Hex转为二进制，并且不足8位的不足8位，例如：7位，1010000
				// String theThirdByteStr =
				// StringHelper.HexStringToBinaryString(strArrayReturnHex[2]);
				// if (!StringHelper.isEmpty(theThirdByteStr) &&
				// strArrayReturnHex.length > 5) {
				// // 对“第三位”状态位进行解析
				// char[] theThirdByteArr = theThirdByteStr.toCharArray();
				// decimalPointPosition =
				// Integer.valueOf(theThirdByteStr.substring(5, 7), 2);
				// isScallingError = Integer.valueOf(theThirdByteArr[4] + "", 2)
				// == 1 ? true : false;
				// isPositive = Integer.valueOf(theThirdByteArr[3] + "", 2) == 0
				// ? true : false;
				// isStable = Integer.valueOf(theThirdByteArr[2] + "", 2) == 1 ?
				// true : false;
				// isOverload = Integer.valueOf(theThirdByteArr[1] + "", 2) == 1
				// ? true : false;
				// // 高、中、低位重量数据
				// String weightValue = "" + (strArrayReturnHex[5] +
				// strArrayReturnHex[4] + strArrayReturnHex[3]);
				// if (isStable && !isOverload && !isScallingError) {
				// SaveSharedPreferencesForFW(portId, weightValue);
				// }
				// }
				
				//从机-》主机响应格式：帧头---地址码---秤盘号---数据字---芯片温度---校验---帧尾
				//字节                                         4  +   1  +  1  +  10   +   1  +   1 +  4   = 22字节
				
				int addressNum = MyFunc.HexToInt(strArrayReturnHex[0]);
				int scaleNum = MyFunc.HexToInt(strArrayReturnHex[1]);
				int chipTemperature  = 0;
				String bcc  = "";
				if (scaleNum==6) {
					//及时用循环也和5行差不多
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+0, MyFunc.getFwValue(strArrayReturnHex[2],strArrayReturnHex[3]));
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+1, MyFunc.getFwValue(strArrayReturnHex[4],strArrayReturnHex[5]));
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+2, MyFunc.getFwValue(strArrayReturnHex[6],strArrayReturnHex[7]));
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+3, MyFunc.getFwValue(strArrayReturnHex[8],strArrayReturnHex[9]));
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+4, MyFunc.getFwValue(strArrayReturnHex[10],strArrayReturnHex[11]));
					chipTemperature = MyFunc.HexToInt(strArrayReturnHex[12]);
					bcc = strArrayReturnHex[13];
				}else{
					SaveSharedPreferencesForFW(70+(addressNum-1)*5+scaleNum-1, MyFunc.getFwValue(strArrayReturnHex[2],strArrayReturnHex[3]));
					chipTemperature = MyFunc.HexToInt(strArrayReturnHex[4]);
					bcc = strArrayReturnHex[5];
				}
				
				 
				
			}
			// openFW(portId);
		}
		// if (isReturnMaterial) {
		// ShowChnMaterialList();
		// DISTANCECHNCOUNTLIST.clear();
		// VENDINGCHNLIST.clear();
		// } else {
		// ShowMaterialList();
		// DISTANCECOUNTLIST.clear();
		// WEIGHTLIST.clear();
		// }
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
		if (VendingChnNumList.contains(pId + "")) {
			final SharedPreferences sp = getSharedPreferences(FWWeightDataList, MODE_PRIVATE);
			// 获取之前该id内存储的重量
			String preWeight = sp.getString(pId + "", "0");
			sp.edit().putString(pId + "", pWeight).commit();
			// if (!isReturnMaterial) {
			int preWeightInt = ConvertHelper.toInt(preWeight, 0);
			int nowWeightInt = ConvertHelper.toInt(pWeight, deviationScalar);
			int different = preWeightInt - nowWeightInt;
			// 给出一个误差范围：如果之前的重量在现在重量加减误差标量之间则不更新材料列表
			if ((nowWeightInt - deviationScalar) > preWeightInt || (nowWeightInt + deviationScalar) < preWeightInt) {
				if (isNeedUpdateDataMemery) {
					SetSP(FWWeightDataList, pId + "", pWeight);
					if (!ListOfCheckIfFWCanShow.contains(pId)) {
						ListOfCheckIfFWCanShow.add(pId + "");
					}
				} else {
					UpdateMaterialList(pId + "", different);
					if (ListOfCheckIfFWCanShow.contains(pId + "")) {
						ListOfCheckIfFWCanShow.remove(pId + "");
					}
					if (ListOfCheckIfFWCanShow.isEmpty()) {
						openRD();
					}

				}
			}
		}
	}

	private void openFW(int pId) {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openFW(pId, Constant.FW_GET_WEIGHT);
	}

	private void openAllFW() {
		SerialTools.getInstance().addToolsListener(this);
		if (VendingChnNumList.isEmpty()) {
			for (VendingChnData vendingChnData : VendingChnDataList) {
				VendingChnNumList.add(vendingChnData.getVc1Code().trim());
			}
		} else {
			// for (String i : VendingChnNumList) {
			// int intTypeOfi = ConvertHelper.toInt(i, 0);
			// // 筛选出货道内的所有的称重传感器
			// if (intTypeOfi >= FWstartNum && intTypeOfi <= FWendNum) {
			// SerialTools.getInstance().openFW(intTypeOfi,
			// Constant.FW_GET_WEIGHT);
			// Thread.sleep(42);
			// }
			// }
			SerialTools.getInstance().openAllFW(Constant.FW_GET_WEIGHT);
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

			// 获取该物品锁对应的单位重量，没有则为0
			String idUnitWeight = "100";// 之前该id内存储的值
			VendingChnData vendingChnData = new VendingChnDbOper().getVendingChnByCode(pId + "");
			if (vendingChnData != null) {
				ProductData productData = new ProductDbOper().getProductById(vendingChnData.getVc1Pd1Id());
				if (productData != null && productData.getPd1Weight() != null) {
					idUnitWeight = productData.getPd1Weight();
				}
			}
			int preCount = 0;
			if (!isReturnMaterial) {
				preCount = ConvertHelper.toInt(WEIGHTLIST.get(pId), 0);
			} else {
				preCount = ConvertHelper.toInt(VENDINGCHNLIST.get(pId), 0);
			}
			// 根据重量变化值和单位重量计算出变化的个数
			float denominator = ConvertHelper.toFloat(idUnitWeight, (float) 0.00);
			float afterCount = pDifWeight / denominator;
			int difCount = Math.abs((int) afterCount);
			if (afterCount != 0) {
				afterCount = WeightCountCalculator(afterCount);
				difCount = Math.abs((int) afterCount);
				if (pDifWeight > 0) {
					afterCount = preCount + difCount;
				} else {
					afterCount = preCount - difCount;
				}
			}
			if (isReturnMaterial) {
				// 将变化的个数更新该ID对应的显示个数
				if (afterCount <= 0) {
					VENDINGCHNLIST.remove(pId);
				} else {
					VENDINGCHNLIST.put(pId + "", "" + afterCount);// 把显示LIST中的对应数据进行更新
				}
			} else {
				// 将变化的个数更新该ID对应的显示个数
				if (afterCount <= 0) {
					WEIGHTLIST.remove(pId);
				} else {
					WEIGHTLIST.put(pId + "", "" + afterCount);// 把显示LIST中的对应数据进行更新
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 模糊判断，在范围内返回true
	 * 
	 * @author junjie.you
	 * @param pNum
	 *            输入用于比对的数字
	 * @return 为true则+1
	 */
	private boolean FuzzyJudgmentForweight(float pNum) {
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

	private void InitSPFWShowList() {
		final SharedPreferences sp = getSharedPreferences(FWShowList, MODE_PRIVATE);
		for (int i = FWstartNum; i <= FWendNum; i++) {
			if (i == 70) {
				int a = 0;
				a++;
			}
			VendingChnData vendingChnData = new VendingChnDbOper().getVendingChnByCode(i + "");
			if (vendingChnData != null) {

				ProductData productData = new ProductDbOper().getProductById(vendingChnData.getVc1Pd1Id());
				if (productData != null) {
					sp.edit().putString(i + "", productData.getPd1Name()).commit();
					continue;
				}
			}
			sp.edit().putString(i + "", i + "号货道").commit();
		}
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
		if (FuzzyJudgmentForweight(pNum)) {
			if (pNum > 0) {
				intPart++;
			} else {
				intPart--;
			}
		}
		return intPart;
	}

	private void UpdateFWDataList(String pId, float pAcount) {
		final SharedPreferences spUnit = getSharedPreferences(FWDataList, MODE_PRIVATE);
		spUnit.edit().putString(pId, "" + pAcount).commit();
	}

	private VersionData versionData = new VersionData();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.parse.listener.DataParseRequestListener#
	 * parseRequestFinised(com.mc.vending.data.BaseData)
	 */
	@Override
	public void parseRequestFinised(BaseData baseData) {
		if (baseData.isSuccess()) {
			if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
				versionData = (VersionData) baseData.getUserObject();
				String version = versionData.getVersion().replace(".", "");
				String locaVersion = Constant.HEADER_VALUE_CLIENTVER.replace(".", "");
				if (Integer.parseInt(version) > Integer.parseInt(locaVersion)) {
					// showToast("有新版本，请更新！");
					Intent intent = new Intent(MC_IntelligencePickActivity.this, VersionActivity.class);
					intent.putExtra("url", versionData.getDownloadURL());
					intent.putExtra("vermsg", versionData.getVersion() + "");
					startActivity(intent);
					finish();
				} else {
				}
			}
		}
	}

	/**
	 * 进入待机界面
	 */
	private void goImagePlayerAcitvity() {
		VendingPictureDbOper db = new VendingPictureDbOper();
		List<VendingPictureData> pictrueList = db.findVendingPicture();
		if (pictrueList.size() > 0) {
			closeRFID();
			Intent intent = new Intent();
			intent.setClass(MC_IntelligencePickActivity.this, MC_ImagePlayerActivity.class);
			startActivity(intent);
			finish();
		} else {
			if (isAccessNetwork()) {
				VendingData vending = new VendingDbOper().getVending();
				VendingPictureDataParse parse = new VendingPictureDataParse();
				parse.setListener(this);
				parse.requestVendingPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGPICTURE,
						vending.getVd1Id());
				// parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA,
				// Constant.METHOD_WSID_SYN_STOCK,
				// vending.getVd1Id());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.parse.listener.DataParseRequestListener#
	 * parseRequestFailure(com.mc.vending.data.BaseData)
	 */
	@Override
	public void parseRequestFailure(BaseData baseData) {
		ZillionLog.e("normal parseRequestFailure" + "==>" + Constant.WSIDNAMEMAP.get(baseData.getRequestURL()));
	}

}
