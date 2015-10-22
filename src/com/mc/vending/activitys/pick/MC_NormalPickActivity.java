package com.mc.vending.activitys.pick;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.activitys.MC_ImagePlayerActivity;
import com.mc.vending.activitys.MainActivity;
import com.mc.vending.activitys.VersionActivity;
import com.mc.vending.activitys.setting.MC_SettingActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ConversionData;
import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.data.VendingPasswordData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.data.VendingProLinkData;
import com.mc.vending.data.VersionData;
import com.mc.vending.db.ConversionDbOper;
import com.mc.vending.db.ProductCardPowerDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.db.VendingPasswordDbOper;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.db.VendingProLinkDbOper;
import com.mc.vending.parse.VersionDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.service.DataServices;
import com.mc.vending.service.GeneralMaterialService;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import com.zillionstar.tools.ZillionLog;

/**
 * 一般领料
 * 
 * @author apple
 * 
 */
public class MC_NormalPickActivity extends BaseActivity implements
		MC_SerialToolsListener, RequestDataFinishListener,
		DataParseRequestListener {
	public DataServices dataServices;
	private ImageView iv_sku; // 商品图片
	private RelativeLayout layout_step1; // 步骤1布局
	private RelativeLayout layout_step2; // 步骤2布局
	private RelativeLayout layout_step3; // 步骤3布局
	private RelativeLayout layout_step_set; // 进入设置步骤
	private EditText password; // 密码输入框
	private EditText et_channle_number; // 步骤1输入框
	private EditText et_pick_number; // 步骤2输入框
	private EditText et_card_password; // 步骤3输入框
	private EditText et_card_password_set; // 步骤3输入框

	private TextView alert_msg_title; // 提示标题
	private TextView alert_msg; // 提示内容
	private Button btn_out; // 隐藏按钮
	private Button btn_version; // 更新按钮
	private OPERATE_STEP operateStep; // 操作步骤。默认为0
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

	private enum OPERATE_STEP {
		OPERATE_STEP_1, OPERATE_STEP_2, OPERATE_STEP_3, OPERATE_STEP_SET
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acticity_normal_pick);
		ActivityManagerTool.getActivityManager().add(this);

		requestGetClientVersionServer();
		getParam();
		initComponents();
		initObject();
		resetViews();
		startService();
		//yjjtestfoo();
		yjjtestfoo2();
	}
	
	private void yjjtestfoo()
	{
		String cardId = "8874d922-a9e8-4cba-8d90-a4de009ba09d";
		String vendingId = "12";
		int inputQty = 1;
		String skuId = "b83ebde2-fa9f-451b-8757-3cd8a9d6f692";
		String proportion = "1";// 换算比例
		String operation = "个";// 操作方式
		ProductCardPowerDbOper dbOper = new ProductCardPowerDbOper();

        VendingProLinkData vendingProLink = new VendingProLinkDbOper().getVendingProLinkByVidAndSkuId(
                vendingId, skuId);
        if (vendingProLink == null) {
            throw new BusinessException("售货机产品 不存在!");
        }
        String vp1Id = vendingProLink.getVp1Id();

        ProductCardPowerData powerData = dbOper.getVendingProLinkByVidAndSkuId(cardId, vp1Id);

        if (powerData != null) {
            String pm1Power = powerData.getPc1Power();
            if (ProductMaterialPowerData.MATERIAL_POWER_NO.equals(pm1Power)) {
                throw new BusinessException("输入的卡号或密码无产品领料权限,请重新输入!");
            }

            int onceQty = Integer.valueOf(powerData.getPc1OnceQty());
            String period = powerData.getPc1Period();
            String intervalStart = powerData.getPc1IntervalStart();
            String intervalFinish = powerData.getPc1IntervalFinish();
            String startDate = powerData.getPc1StartDate();
//            String cardId = powerData.getPc1CD1_ID();
            int periodQty = Double.valueOf(powerData.getPc1PeriodQty()).intValue();

            if (onceQty == 0 || inputQty <= onceQty) {
                if (!StringHelper.isEmpty(period, true)) {
                    UsedRecordDbOper usedRecordDbOper = new UsedRecordDbOper();
                    Date date = this.getDate(period, intervalStart, intervalFinish, startDate);
                    int transQtyTotal = 0;// 个人已领用数
                    if (date != null) {
                        try {
                            transQtyTotal = usedRecordDbOper.getTransQtyCount(cardId, skuId);
                        } catch (Exception e) {
                            // L.e(e.getMessage());
                            // e.printStackTrace();
                            throw new BusinessException("产品领料权限检查错误,卡产品领用数：" + transQtyTotal);
                        }
                    }

                    int total = transQtyTotal * (-1);
                    if (inputQty > periodQty) {
                        throw new BusinessException("你的领料权限是" + periodQty + "，输入了" + inputQty + "，不允许超领！");
                    }
                    if (total + inputQty > periodQty) {
                        throw new BusinessException("你的领料权限是" + periodQty + "，你已领取" + total + "，不允许超领！");
                    }
                }
            } else {
                throw new BusinessException("一次最多领取 " + onceQty + " 数量,请重新输入!");
            }
        }
	}
	
	private void yjjtestfoo2()
	{
		SerialTools.getInstance().openFW(02);
	}
	
	/**
     * 根据期间设置，间隔开始，间隔结束，起始时间确定查询时间
     * Temp, added by junjie.you
     * @param periodStr
     * @param intervalStartStr
     * @param intervalFinishStr
     * @param startDateStr
     * @return
     */
    public Date getDate(String periodStr, String intervalStartStr, String intervalFinishStr,
            String startDateStr) {
        if (StringHelper.isEmpty(startDateStr, true))
            return null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date startDate = DateHelper.parse(startDateStr, pattern);
        int period = ConvertHelper.toInt(periodStr, 0);
        if (period >= Constant.YEAR && period <= Constant.HOUR) {
            Date date = null;
            Date tmpDate = new Date();
            // 期间设置为年时
            int intervalStart = ConvertHelper.toInt(intervalStartStr, 0);
            switch (period) {
            case Constant.YEAR:
                date = DateHelper.add(tmpDate, Calendar.YEAR, -intervalStart); // date减intervalStart
                break;
            case Constant.MONTH:
                // 期间设置为月时
                date = DateHelper.add(tmpDate, Calendar.MONTH, -intervalStart); // month减intervalStart
                break;
            case Constant.DAY:
                // 期间设置为日时
                date = DateHelper.add(tmpDate, Calendar.DAY_OF_MONTH, -intervalStart); // date减intervalStart

                // 更改设置为当天的零点，不再是24小时之前 forever add
                date = DateHelper.getDateZero(DateHelper.add(date, Calendar.DAY_OF_MONTH, 1));

                break;
            case Constant.HOUR:
                // 期间设置为小时时
                date = DateHelper.add(tmpDate, Calendar.HOUR, -intervalStart); // hour减intervalStart
                break;
            default:
                break;
            }
            if (date.before(startDate)) {
                return startDate;
            } else {
                return date;
            }
        } else if (period == Constant.TIME) {
            // 期间设置为时间段时
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                return null;
            }
            String startYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
            Date intervalStart = DateHelper.parse(startYMD + " " + intervalStartStr, "yyyy-MM-dd HH:mm:ss");
            Date intervalFinish = DateHelper.parse(startYMD + " " + intervalFinishStr, "yyyy-MM-dd HH:mm:ss");
            // 如果当前时间在起始时间之前或者在结束时间之后，忽略，否则取起始时间
            if (currentDate.before(intervalStart) || currentDate.after(intervalFinish)) {
                return null;
            } else {
                return intervalStart;
            }
        }
        return null;
    }

	private void requestGetClientVersionServer() {
		btn_version = (Button) this.findViewById(R.id.btn_version);
		btn_version.setVisibility(View.INVISIBLE);

		VersionDataParse parse = new VersionDataParse();
		parse.setListener(this);
		parse.requestVersionData(Constant.HTTP_OPERATE_TYPE_GETDATA,
				Constant.METHOD_WSID_VERSION);
	}

	private VersionData versionData = new VersionData();

	@Override
	public void parseRequestFinised(BaseData baseData) {
		if (baseData.isSuccess()) {
			if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
				versionData = (VersionData) baseData.getUserObject();
				String version = versionData.getVersion().replace(".", "");
				String locaVersion = Constant.HEADER_VALUE_CLIENTVER.replace(
						".", "");
				if (Integer.parseInt(version) > Integer.parseInt(locaVersion)) {
					// resetAlertMsg("有新版本，请更新！");
					Intent intent = new Intent(MC_NormalPickActivity.this,
							VersionActivity.class);
					intent.putExtra("url", versionData.getDownloadURL());
					intent.putExtra("vermsg", versionData.getVersion() + "");
					startActivity(intent);
					finish();
					//
					// btn_version.setVisibility(View.VISIBLE);
					// btn_version.setOnClickListener(new OnClickListener() {
					// @Override
					// public void onClick(View v) {
					//
					// ZillionLog.e("btn_version.setOnClickListener");
					// Intent intent = new Intent(MC_NormalPickActivity.this,
					// VersionActivity.class);
					// intent.putExtra("url", versionData.getDownloadURL());
					// intent.putExtra("vermsg", versionData.getVersion() + "");
					// startActivity(intent);
					// finish();
					// }
					// });
				} else {
					btn_version.setVisibility(View.INVISIBLE);
				}
			}
		}

	}

	@Override
	public void parseRequestFailure(BaseData baseData) {

		ZillionLog.e("normal parseRequestFailure" + "==>"
				+ Constant.WSIDNAMEMAP.get(baseData.getRequestURL()));
	}

	/**
	 * 初始化参数
	 */
	private void getParam() {
		vendCode = getIntent().getStringExtra("vendCode");
	}

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
	 * 重置累计轮训时间
	 */
	private void resetTimer() {
		imagePlayerTimeOut = 0;
	}

	/**
	 * 初始化对象
	 */
	private void initComponents() {
		iv_sku = (ImageView) this.findViewById(R.id.iv_sku);
		layout_step1 = (RelativeLayout) this.findViewById(R.id.layout_step1);
		layout_step2 = (RelativeLayout) this.findViewById(R.id.layout_step2);
		layout_step3 = (RelativeLayout) this.findViewById(R.id.layout_step3);
		layout_step_set = (RelativeLayout) this
				.findViewById(R.id.layout_step_set);
		et_channle_number = (EditText) this
				.findViewById(R.id.et_channle_number);
		et_pick_number = (EditText) this.findViewById(R.id.et_pick_number);
		et_card_password = (EditText) this.findViewById(R.id.et_card_password);
		et_card_password_set = (EditText) this
				.findViewById(R.id.et_card_password_set);
		alert_msg_title = (TextView) this.findViewById(R.id.alert_msg_title);
		alert_msg = (TextView) this.findViewById(R.id.alert_msg);
		btn_out = (Button) this.findViewById(R.id.btn_out);
	}

	/**
	 * 初始化变量对象
	 */
	private void initObject() {
		operateStep = OPERATE_STEP.OPERATE_STEP_1;

		btn_out.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				password = new EditText(MC_NormalPickActivity.this);
				password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				final Builder dialog = new AlertDialog.Builder(
						MC_NormalPickActivity.this)
						.setTitle("请输入密码")
						.setView(password)
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (!StringHelper.isEmpty(password
												.getText().toString(), true)) {
											VendingPasswordData data = new VendingPasswordDbOper()
													.getVendingPasswordByPassword(password
															.getText()
															.toString());
											if (data == null) {
												resetAlertMsg("密码错误");
											} else {
												ActivityManagerTool
														.getActivityManager()
														.exit();
												finish();
											}
										}
									}
								}).setNegativeButton("取消", null);
				dialog.show();

				return false;
			}
		});
	}

	/**
	 * 清空文本输入框
	 */
	private void clearEditText() {
		et_channle_number.setText("");
		et_pick_number.setText("");
		et_card_password.setText("");
		et_card_password_set.setText("");
	}

	/**
	 * 重置页面状态
	 */
	private void resetInputStatus() {
		operateStep = OPERATE_STEP.OPERATE_STEP_1;
		clearEditText();
		hiddenAlertMsg();
		resetViews();
		resetSKUImage();

	}

	private void resetSKUImage() {
		iv_sku.setImageResource(R.drawable.default_home);
		VendingPictureData data = new VendingPictureDbOper()
				.getDefaultVendingPicture();
		if (data != null) {
			String imageURL = data.getVp2FilePath();
			loadImage(imageURL);
		}
	}

	private void openKeyBoard() {
		SerialTools.getInstance().addToolsListener(this);
		SerialTools.getInstance().openKeyBoard();
	}

	private void closeKeyBoard() {

		try {
			SerialTools.getInstance().addToolsListener(this);
			SerialTools.getInstance().closeKeyBoard();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
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

	private void closeVender() {
		try {
			SerialTools.getInstance().closeVender();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	private void closeStore() {
		try {
			SerialTools.getInstance().closeStore();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 进入待机界面
	 */
	private void goImagePlayerAcitvity() {
		VendingPictureDbOper db = new VendingPictureDbOper();
		List<VendingPictureData> pictrueList = db.findVendingPicture();
		if (pictrueList.size() > 0) {
			Intent intent = new Intent();
			intent.setClass(MC_NormalPickActivity.this,
					MC_ImagePlayerActivity.class);
			startActivity(intent);
		}
	}

	private void startTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				imagePlayerTimeOut += 1000;
				if (imagePlayerTimeOut == imagePlayerTimeCount) {
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

	private void cancelImageTask() {
		mTimerTask.cancel();
	}

	/**
	 * 初始化ui
	 */
	private void resetViews() {
		switch (operateStep) {
		case OPERATE_STEP_1:
			layout_step1.setVisibility(View.VISIBLE);
			layout_step2.setVisibility(View.INVISIBLE);
			layout_step3.setVisibility(View.INVISIBLE);
			layout_step_set.setVisibility(View.INVISIBLE);
			break;
		case OPERATE_STEP_2:
			layout_step1.setVisibility(View.INVISIBLE);
			layout_step2.setVisibility(View.VISIBLE);
			layout_step3.setVisibility(View.INVISIBLE);
			layout_step_set.setVisibility(View.INVISIBLE);
			break;
		case OPERATE_STEP_3:
			layout_step1.setVisibility(View.INVISIBLE);
			layout_step2.setVisibility(View.INVISIBLE);
			layout_step3.setVisibility(View.VISIBLE);
			layout_step_set.setVisibility(View.INVISIBLE);
			break;
		case OPERATE_STEP_SET:
			layout_step1.setVisibility(View.INVISIBLE);
			layout_step2.setVisibility(View.INVISIBLE);
			layout_step3.setVisibility(View.INVISIBLE);
			layout_step_set.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
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
		cancelImageTask();
		super.onPause();
	}

	@Override
	protected void onResume() {
		openKeyBoard();
		startTimerTask();
		resetInputStatus();
		closeRFID();
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
		this.unbindService(conn);
	}

	/**
	 * 串口返回
	 * 
	 * @param value
	 *            返回值
	 * @param serialType
	 *            串口类型
	 */
	@Override
	public void serialReturn(String value, int serialType) {

		Message msg = new Message();
		msg.what = serialType;
		msg.obj = value;
		// 判断串口类型
		switch (serialType) {
		case SerialTools.MESSAGE_LOG_mKeyBoard:
			// 当串口类型为键盘时，判断是否为功能键
			if (isOperating) {
				return;
			}
			resetTimer();
			handler.sendMessage(msg);
			isRFID = false;
			break;
		case SerialTools.MESSAGE_LOG_mRFIDReader:
			value = MyFunc.getRFIDSerialNo(value);
			if (!StringHelper.isEmpty(value, true)) {
				resetTimer();
				resetTextView(value, serialType);
				isRFID = true;
				closeRFID(); // rfid读取完成关闭rfid
				handler.sendMessage(msg);
			}
			break;
		case SerialTools.MESSAGE_LOG_mVender:
			// resetTimer();
			// handler.sendMessage(msg);
			break;
		case SerialTools.MESSAGE_LOG_mVender_check:
			resetTimer();
			handler.sendMessage(msg);
			break;
		case SerialTools.MESSAGE_LOG_mStore_check:
			handler.sendMessage(msg);
			break;
		case SerialTools.MESSAGE_LOG_mStore:
			// resetTimer();
			// handler.sendMessage(msg);
			break;
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SerialTools.MESSAGE_LOG_mKeyBoard:
				hiddenAlertMsg();
				keyBoardReturn((String) msg.obj, msg.what);
				break;
			case SerialTools.MESSAGE_LOG_mRFIDReader:
				hiddenAlertMsg();
				if (operateStep == OPERATE_STEP.OPERATE_STEP_3) {
					cardPasswordValidate();
				} else if (operateStep == OPERATE_STEP.OPERATE_STEP_SET) {
					setValidate();
				}
				break;
			case SerialTools.MESSAGE_LOG_mVender:
				// hiddenAlertMsg();
				// openVender((String) msg.obj);

				break;
			case SerialTools.MESSAGE_LOG_mVender_check:
				openVender((String) msg.obj);

				break;
			case SerialTools.MESSAGE_LOG_mStore:
				// checkStore();
				// hiddenAlertMsg();
				// openStore((String) msg.obj);
				break;
			case SerialTools.MESSAGE_LOG_mStore_check:
				openedStore((String) msg.obj);
				break;
			case MC_NormalPickActivity.MESSAGE_Image_player:
				goImagePlayerAcitvity();
			default:
				break;
			}
		}
	};

	/**
	 * 键盘按下判断方法
	 * 
	 * @param value
	 * @param serialType
	 */
	private void keyBoardReturn(String value, int serialType) {

		if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {
			// 功能键－－组合
			if (operateStep == OPERATE_STEP.OPERATE_STEP_1) {
				// 判断售货机状态
				ServiceResult<VendingData> result = CompositeMaterialService
						.getInstance().checkVending();
				if (!result.isSuccess()) {
					resetAlertMsg(result.getMessage());
					return;
				}
				vendData = result.getResult();
				Intent intent = new Intent();
				intent.putExtra("vendData", vendData);
				intent.setClass(MC_NormalPickActivity.this,
						MC_CombinationPickActivity.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.anim_from_right,
				// R.anim.anim_to_left);
			}
		} else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {
			// 功能键－－借
			if (operateStep == OPERATE_STEP.OPERATE_STEP_1) {

				ServiceResult<VendingData> result = CompositeMaterialService
						.getInstance().checkVending();
				if (!result.isSuccess()) {
					resetAlertMsg(result.getMessage());
					return;
				}
				vendData = result.getResult();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("operateType",
						MC_BorrowBackAcitvity.OPERATE_BORROW); // 0为借，1为还
				intent.putExtras(bundle);
				intent.setClass(MC_NormalPickActivity.this,
						MC_BorrowBackAcitvity.class);
				startActivity(intent);

				// overridePendingTransition(R.anim.anim_from_right,
				// R.anim.anim_to_left);
			}
		} else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {
			// 功能键－－还
			if (operateStep == OPERATE_STEP.OPERATE_STEP_1) {
				ServiceResult<VendingData> result = CompositeMaterialService
						.getInstance().checkVending();
				if (!result.isSuccess()) {
					resetAlertMsg(result.getMessage());
					return;
				}
				vendData = result.getResult();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("operateType", MC_BorrowBackAcitvity.OPERATE_BACK); // 0为借，1为还
				intent.putExtras(bundle);
				intent.setClass(MC_NormalPickActivity.this,
						MC_BorrowBackAcitvity.class);
				startActivity(intent);
			}
		} else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {
			// 功能键－－设置
			if (operateStep == OPERATE_STEP.OPERATE_STEP_1) {
				operateStep = OPERATE_STEP.OPERATE_STEP_SET;
				resetViews();
				openRFID();
			}
		} else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
			// 功能键－－取消
			hiddenAlertMsg();
			switch (operateStep) {
			case OPERATE_STEP_1:
				if (!StringHelper.isEmpty(et_channle_number.getText()
						.toString(), true)) {
					et_channle_number.setText("");
				}
				break;
			case OPERATE_STEP_2:

				if (vendingChn.getVc1Type().equals(
						VendingChnData.VENDINGCHN_TYPE_CELL)) {
					// 如果货到类型为格子机，点击取消，直接返回上一步
					operateStep = OPERATE_STEP.OPERATE_STEP_1;
					resetViews();
				} else {
					if (!StringHelper.isEmpty(et_pick_number.getText()
							.toString(), true)) {
						et_pick_number.setText("");
					} else {
						operateStep = OPERATE_STEP.OPERATE_STEP_1;
						resetViews();
					}
				}

				break;
			case OPERATE_STEP_3:
				if (!StringHelper.isEmpty(
						et_card_password.getText().toString(), true)) {
					et_card_password.setText("");
					openRFID(); // 进入步骤3，打开rfid
				} else {
					closeRFID(); // 返回步骤2，关闭rfid
					operateStep = OPERATE_STEP.OPERATE_STEP_2;
					resetViews();
				}
				break;
			case OPERATE_STEP_SET:
				if (!StringHelper.isEmpty(et_card_password_set.getText()
						.toString(), true)) {
					et_card_password_set.setText("");
					openRFID(); // 进入步骤3，打开rfid
				} else {
					closeRFID(); // 返回步骤2，关闭rfid
					operateStep = OPERATE_STEP.OPERATE_STEP_1;
					resetViews();
				}
				break;

			default:
				break;
			}
		} else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
			// 功能键－－确认
			switch (operateStep) {
			case OPERATE_STEP_1:
				channelValidate();
				break;
			case OPERATE_STEP_2:
				pickNumberValidate();
				break;
			case OPERATE_STEP_3:
				cardPasswordValidate();
				break;
			case OPERATE_STEP_SET:
				setValidate();

				break;

			default:
				break;
			}
		} else {
			resetTextView(SerialTools.getInstance().getKeyValue(value),
					serialType);
		}

	}

	/**
	 * 货到验证
	 * 
	 * @return
	 */
	private void channelValidate() {
		et_pick_number.setText("");
		if (StringHelper.isEmpty(et_channle_number.getText().toString(), true)) {
			resetAlertMsg(getResources().getString(
					R.string.placeholder_channle_number));
		} else {
			// 判断输入是否为客服电话，如果输入为特殊字符－－客服电话责进入同步页面
			if (et_channle_number.getText().toString()
					.equals(Constant.SERVICE_TEL)) {
				startSynData();
			} else {
				ServiceResult<VendingData> result = GeneralMaterialService
						.getInstance().checkVending();
				// 判断售货机状态－－验证是否可用
				if (!result.isSuccess()) {
					resetAlertMsg(result.getMessage());
					return;
				}
				// 判断货到状态－类型，返回商品id，货道id
				ServiceResult<VendingChnData> vendingResult = GeneralMaterialService
						.getInstance().checkVendingChn(
								et_channle_number.getText().toString(),
								VendingChnData.VENDINGCHN_METHOD_GENERAL);
				if (!vendingResult.isSuccess()) {
					resetAlertMsg(vendingResult.getMessage());
					return;
				}
				vendingChn = vendingResult.getResult();
				// 查询商品图片
				ProductPictureData picData = GeneralMaterialService
						.getInstance().getPictureBySkuId(
								vendingChn.getVc1Pd1Id());
				if (picData != null) {
					// 加载图片
					Log.i(this.getClass().getName().toString(),
							"pp1_filePath===" + picData.getPp1FilePath());
					loadImage(picData.getPp1FilePath());
					// todo...
				}

				stockCount = GeneralMaterialService.getInstance()
						.getVendingChnStock(vendingChn.getVc1Vd1Id(),
								vendingChn.getVc1Code());
				if (stockCount <= 0) {
					resetAlertMsg("库存为：" + stockCount + ",库存量不足,不能领料");
					return;
				}

				if (vendingChn.getVc1Type().equals(
						VendingChnData.VENDINGCHN_TYPE_CELL)) {
					// 格子机,查询货到对应库存，显示再界面，此时，键盘输入不可修改数量
					et_pick_number.setText(String.valueOf(stockCount));
					resetAlertMsg("格子机器库存数量：" + stockCount + ", 格子机默认全部领取");
					if (stockCount > 0) {
						// 售货机可用，货到状态正常，进入下一步输入领料数
						operateStep = OPERATE_STEP.OPERATE_STEP_2;
						resetViews();
					}
					return;
				} else {
					operateStep = OPERATE_STEP.OPERATE_STEP_2;
					resetViews();
					hiddenAlertMsg();
				}
			}
		}
	}

	/**
	 * 步骤2验证，领料数量
	 */
	private void pickNumberValidate() {
		if (StringHelper.isEmpty(et_pick_number.getText().toString(), true)) {
			resetAlertMsg(getResources().getString(
					R.string.placeholder_pick_number));
		} else {
			if (vendingChn.getVc1Type().equals(
					VendingChnData.VENDINGCHN_TYPE_VENDING)) {
				// 如果售货机货道类型＝＝售货机，责判断领料数量
				int inputCount = ConvertHelper.toInt(et_pick_number.getText()
						.toString(), 0);
				if (!(inputCount != 0 && inputCount <= stockCount)) {
					resetAlertMsg("货道号" + vendingChn.getVc1Code() + "的库存数量="
							+ stockCount + "，库存不足，请重新输入！");
					return;
				}
			}

			openRFID(); // 进入步骤3，打开rfid
			operateStep = OPERATE_STEP.OPERATE_STEP_3;
			resetViews();
			hiddenAlertMsg();

		}
	}

	/**
	 * 步骤3验证，密码或刷卡验证
	 */
	private void cardPasswordValidate() {
		closeRFID();
		if (StringHelper.isEmpty(et_card_password.getText().toString(), true)) {
			resetAlertMsg(getResources().getString(
					R.string.placeholder_card_pwd));
		} else {
			// 检查卡/密码-权限
			ServiceResult<VendingCardPowerWrapperData> result = GeneralMaterialService
					.getInstance().checkCardPowerOut(
							isRFID ? CardData.CARD_SERIALNO_PARAM
									: CardData.CARD_PASSWORD_PARAM,
							et_card_password.getText().toString(),
							vendingChn.getVc1Vd1Id());
			if (!result.isSuccess()) {
				resetAlertMsg(result.getMessage());
				return;
			}
			wrapperData = result.getResult();
			resetAlertMsg(vendingChn.getVc1Vd1Id()
					+ "+"
					+ vendingChn.getVc1Pd1Id()
					+ ","
					+ wrapperData.getVendingCardPowerData().getVc2Cu1Id()
					+ ","
					+ wrapperData.getVendingCardPowerData().getVc2Id()
					+ ","
					+ ConvertHelper.toInt(et_pick_number.getText().toString(),
							0) + "," + vendingChn.getVc1Code() + ","
					+ wrapperData.getCardPuductPowerType() + ","
					+ wrapperData.getVendingCardPowerData().getVc2Cd1Id());
			// 查询领料权限
			ServiceResult<Boolean> materialResult = GeneralMaterialService
					.getInstance()
					.checkProductMaterialPower(
							vendingChn.getVc1Vd1Id(),
							vendingChn.getVc1Pd1Id(),
							wrapperData.getVendingCardPowerData().getVc2Cu1Id(),
							wrapperData.getVendingCardPowerData().getVc2Id(),
							ConvertHelper.toInt(et_pick_number.getText()
									.toString(), 0), vendingChn.getVc1Code(),
							wrapperData.getCardPuductPowerType(),
							wrapperData.getVendingCardPowerData().getVc2Cd1Id());
			if (!materialResult.isSuccess()) {
				resetAlertMsg(materialResult.getMessage());
				return;
			}

			hiddenAlertMsg();
			// 步骤三确认，进行后续判断,发起领料动作，

			if (vendingChn.getVc1Type().equals(
					VendingChnData.VENDINGCHN_TYPE_VENDING)) {
				// 售货机
				vendingChn.setInputQty(ConvertHelper.toInt(et_pick_number
						.getText().toString(), 0));
				isOperating = true;
				openVender(null);

			} else {
				// 格子机器
				isStoreChecked = false;
				SerialTools.getInstance().addToolsListener(this);
				SerialTools.getInstance().openStore(
						ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
						ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0),
						ConvertHelper.toInt(vendingChn.getVc1Height(), 0));
				// SerialTools.getInstance().openStore(2, 1, 1);
			}
		}

	}

	/**
	 * 开启售货机
	 * 
	 * @param status
	 */
	private void openVender(String status) {
		if (status == null || status.contains("31") || status.contains("32")) {
			if (status != null
					&& (status.contains("31") || status.contains("32"))) {
				if (status.contains("31")) {
					pickSuccess(1, vendingChn);// 每次成功－1
				} else {
					vendingChn.setFailureQty(vendingChn.getFailureQty() + 1);
				}
				vendingChn.setInputQty(vendingChn.getInputQty() - 1);

				if (vendingChn.getInputQty() <= 0) {
					closeVender();
					resetInputStatus();
					if (vendingChn.getFailureQty() > 0) {
						resetAlertMsg("货道状态错误,失败数量："
								+ vendingChn.getFailureQty());
					} else {
						resetAlertMsg("领料成功！");
					}

					isOperating = false;
					vendingChn = null;
					wrapperData = null;
					stockCount = 0;

					isRFID = false;
					return;
				}
			}
			SerialTools.getInstance().openVender(
					ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
					ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0));
			try {
				Thread.sleep(Constant.TIME_INTERNAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// /**
	// * 检查格子机状态
	// */
	// private void checkStore() {
	// Thread downLoadData = new Thread(new Runnable() {
	// public void run() {
	// try {
	// int i = 5;
	// while (i > 0 && !isStoreChecked) {
	// SerialTools.getInstance().addToolsListener(MC_NormalPickActivity.this);
	// SerialTools.getInstance().checkStore(
	// ConvertHelper.toInt(vendingChn.getVc1LineNum(), 0),
	// ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), 0));
	// Thread.sleep(300);
	// i--;
	// }
	//
	// } catch (Exception e) {
	// }
	// }
	// });
	// downLoadData.start();
	// }

	/**
	 * 打开格子机返回
	 * 
	 * @param input
	 */
	private void openedStore(String input) {
		if (input != null && !isStoreChecked) {
			String[] array = input.split(" ");
			if (array.length > 7
					&& array.length >= ConvertHelper.toInt(
							vendingChn.getVc1Height(), 0) + 7) {
				if (array[ConvertHelper.toInt(vendingChn.getVc1Height(), 0) + 4]
						.equals("00")) {
					pickSuccess(stockCount, vendingChn);
					resetInputStatus();
					resetAlertMsg("领料成功！");
					stockCount = 0;
					isStoreChecked = true;
				} else if (array[ConvertHelper.toInt(vendingChn.getVc1Height(),
						0) + 4].equals("01")) {
					resetInputStatus();
					resetAlertMsg("领料失败！");
				}
				isOperating = false;
			}

		}
	}

	/**
	 * 点击设置后输入的密码验证
	 */
	private void setValidate() {
		closeRFID();
		if (StringHelper.isEmpty(et_card_password_set.getText().toString(),
				true)) {
			resetAlertMsg(getResources().getString(
					R.string.placeholder_card_pwd));
		} else {
			ServiceResult<VendingData> vendResult = GeneralMaterialService
					.getInstance().checkVending();
			// 判断售货机状态－－验证是否可用
			if (!vendResult.isSuccess()) {
				resetAlertMsg(vendResult.getMessage());
				return;
			}
			vendData = vendResult.getResult();
			// 检查卡/密码-权限,进入设置只能刷卡
			ServiceResult<VendingCardPowerWrapperData> result = ReplenishmentService
					.getInstance().checkCardPowerInner(
							et_card_password_set.getText().toString(),
							vendData.getVd1Id());
			if (!result.isSuccess()) {
				resetAlertMsg(result.getMessage());
				return;
			}
			wrapperData = result.getResult();

			hiddenAlertMsg();

			Intent intent = new Intent();
			Bundle bundle = new Bundle();

			intent.putExtra("wrapperData", wrapperData);
			intent.putExtra("vendData", vendData);

			intent.putExtras(bundle);
			intent.setClass(MC_NormalPickActivity.this,
					MC_SettingActivity.class);
			startActivityForResult(intent, 1000);
			// startActivity(intent);

		}
	}

	/**
	 * 领料成功
	 */
	private void pickSuccess(int count, VendingChnData vendingChn) {
		// 保存领料数据
		isStoreChecked = false;
		GeneralMaterialService.getInstance().saveStockTransaction(count,
				vendingChn, wrapperData);
		// 恢复默认状态
	}

	private void pickFailure() {

	}

	/**
	 * 异步加载图片
	 * 
	 * @param imageURL
	 */
	private void loadImage(String imageURL) {
		asyncImageLoader = new AsyncImageLoader();
		Drawable cachedImage = asyncImageLoader.loadDrawable(imageURL,
				new AsyncImageLoader.ImageCallback() {
					@Override
					public void imageLoaded(final Drawable imageDrawable,
							final String imageUrl, String tag) {
						iv_sku.setImageDrawable(imageDrawable);
					}
				});
		if (cachedImage != null) {
			iv_sku.setImageDrawable(cachedImage);
		}
	}

	/**
	 * 重置输入框文本
	 * 
	 * @param value
	 * @param serialType
	 */
	private void resetTextView(String value, int serialType) {
		switch (operateStep) {
		case OPERATE_STEP_1:
			et_channle_number.setText(et_channle_number.getText().toString()
					+ value);
			break;
		case OPERATE_STEP_2:
			if (vendingChn.getVc1Type().equals(
					VendingChnData.VENDINGCHN_TYPE_VENDING)) {
				// 如果货到类型是售货机，才接受输入
				et_pick_number.setText(et_pick_number.getText().toString()
						+ value);
			}

			break;
		case OPERATE_STEP_3:
			if (SerialTools.MESSAGE_LOG_mKeyBoard == serialType) {
				et_card_password.setText(et_card_password.getText().toString()
						+ value);
			} else if (SerialTools.MESSAGE_LOG_mRFIDReader == serialType) {
				if (!StringHelper.isEmpty(value, true)) {
					et_card_password.setText(value);
				}
			}
			break;
		case OPERATE_STEP_SET:
			if (SerialTools.MESSAGE_LOG_mKeyBoard == serialType) {
				et_card_password_set.setText(et_card_password_set.getText()
						.toString() + value);
			} else if (SerialTools.MESSAGE_LOG_mRFIDReader == serialType) {
				if (!StringHelper.isEmpty(value, true)) {
					et_card_password_set.setText(value);
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000) {
			if (resultCode == 1003) {
				startSynData();
			} else if (resultCode == 9999) {
				goMainActivity();
				this.finish();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 输入400电话直接同步数据
	 */
	private void startSynData() {
		if (!isAccessNetwork()) {
			resetAlertMsg("没有链接到网络，请检查网络链接！");
			return;
		}
		if (dataServices != null) {
			startLoading();
			resetAlertMsg("数据同步中...");
			dataServices.setNormalListener(this);
			dataServices.synData();
		} else {
			resetAlertMsg("程序异常");// 测试代码
		}
	}

	private void goMainActivity() {
		Intent intent = new Intent();
		intent.setClass(MC_NormalPickActivity.this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void serialReturn(String value, int serialType, Object userInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestFinished() {
		stopLoading();
		resetInputStatus();
		resetAlertMsg("数据同步完成");
		dataServices.setNormalListener(null);
	}

	@Override
	public void requestFailure() {
		stopLoading();
		resetInputStatus();
		resetAlertMsg("数据同步失败");
		dataServices.setNormalListener(null);
	}

}
