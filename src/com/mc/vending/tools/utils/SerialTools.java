package com.mc.vending.tools.utils;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.mc.vending.activitys.pick.MC_CombinationPickDetailActivity;
import com.mc.vending.activitys.pick.MC_WeightPickActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.db.VendingDbOper;
import com.zillion.evm.jssc.SerialPort;
import com.zillion.evm.jssc.SerialPortEvent;
import com.zillion.evm.jssc.SerialPortEventListener;
import com.zillion.evm.jssc.SerialPortException;
import com.zillionstar.tools.ZillionLog;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SerialTools {
	private MC_SerialToolsListener toolsListener;
	private static Map<String, String> keymap = null;

	private static final String TAG = "MainActivity";
	// private static final String PortName_mVender = "/dev/ttyS0"; // 售货机
	// private static final String PortName_mRFIDReader = "/dev/ttyS1"; // 读卡器
	// private static final String PortName_mKeyBoard = "/dev/ttyS2"; // 键盘
	// private static final String PortName_mStore = "/dev/ttyS4"; // 格子机
	// private static final String PortName_mFw = "/dev/ttyO4"; // 称重模块 added by
	// junjie.you

	private static final String PortName_mVender = "/dev/ttyO2"; // 售货机
	private static final String PortName_mRFIDReader = "/dev/ttyO6"; // 读卡器
	private static final String PortName_mKeyBoard = "/dev/ttyO5"; // 键盘
	private static final String PortName_mStore = "/dev/ttyO3"; // 格子机
	private static final String PortName_mFw = "/dev/ttyO7"; // 称重模块

	public static final int MESSAGE_LOG_mKeyBoard = 1; // 键盘
	public static final int MESSAGE_LOG_mRFIDReader = 2; // 读卡器
	public static final int MESSAGE_LOG_mVender = 3; // 售货机
	public static final int MESSAGE_LOG_mStore = 4; // 格子机
	public static final int MESSAGE_LOG_mStore_check = 5; // 检查格子机
	public static final int MESSAGE_LOG_mVender_check = 6; // 检查售货机
	public static final int MESSAGE_LOG_mFw = 7; // 称重模块 added by junjie.you
	// 常用指令
	public static final String cmdOpenKeyBoard = "02303234353033343403"; // 指令打开键盘
	public static final String cmdCloseKeyBoard = "02303234353030343703"; // 指令关闭键盘
	public static final String cmdGetSerialNo = "01F0F0"; // 读取卡号
	public static final String cmdBeep = "030FFF00F0"; // 读卡器声音

	// 循环发送时间间隔
	private static final int iDelay = 500;
	private SendThread mSendThread; // 读卡器循环线程
	private final SerialPort mKeyBoard; // 初始化键盘监听对象
	private final SerialPort mRFIDReader; // 初始化读卡器监听对象
	private final SerialPort mStore; // 初始化格子机监听对象
	private final SerialPort mVender; // 售货机对象监听
	private final SerialPort mFw; // 初始化称重模块监听对象
	// 单例对象
	private static SerialTools instance = null;
	private Timer mStoreTimer;
	// public static final String FUNCTION_KEY_COMBINATION = "2E"; //组合
	// public static final String FUNCTION_KEY_BORROW = "30"; //借
	// public static final String FUNCTION_KEY_BACK = "30 30"; //还
	// public static final String FUNCTION_KEY_SET = "20"; //设置
	// public static final String FUNCTION_KEY_CANCEL = "1B"; //取消
	// public static final String FUNCTION_KEY_CONFIRM = "0D"; //确认

	public static final String FUNCTION_KEY_COMBINATION = "50"; // 组合
	public static final String FUNCTION_KEY_BORROW = "23"; // 借
	public static final String FUNCTION_KEY_BACK = "2A"; // 还
	public static final String FUNCTION_KEY_SET = "08"; // 设置
	public static final String FUNCTION_KEY_CANCEL = "1B"; // 取消
	public static final String FUNCTION_KEY_CONFIRM = "0D"; // 确认

	public Object userInfo;

	static {
		keymap = new HashMap<String, String>();
		keymap.put("30", "0");
		keymap.put("31", "1");
		keymap.put("32", "2");
		keymap.put("33", "3");
		keymap.put("34", "4");
		keymap.put("35", "5");
		keymap.put("36", "6");
		keymap.put("37", "7");
		keymap.put("38", "8");
		keymap.put("39", "9");
	}

	/**
	 * 获取数字键对应数值
	 * 
	 * @param key
	 * @return
	 */
	public String getKeyValue(String key) {
		if (keymap.containsKey(key)) {
			return keymap.get(key);
		}
		return "";

	}

	private SerialTools() {
		mKeyBoard = new SerialPort(PortName_mKeyBoard); // 初始化键盘监听对象
		mRFIDReader = new SerialPort(PortName_mRFIDReader); // 初始化读卡器监听对象
		mStore = new SerialPort(PortName_mStore);// 初始化格子机监听对象
		mVender = new SerialPort(PortName_mVender);
		mFw = new SerialPort(PortName_mFw);// 初始化称重模块监听对象 added by junjie.you

	}

	public static SerialTools getInstance() {
		if (instance == null) {
			instance = new SerialTools();
		}
		return instance;
	}

	/**
	 * 添加工具类到activity的监听
	 * 
	 * @param listener
	 */
	public void addToolsListener(MC_SerialToolsListener listener) {
		this.toolsListener = listener;
	}

	/**
	 * 打开键盘端口
	 * 
	 * @throws SerialPortException
	 */
	public void openKeyBoard() {
		try {
			if (mKeyBoard.isOpened() || mKeyBoard.openPort()) {
				try {
					mKeyBoard.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mKeyBoard.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				sendPortData(mKeyBoard, cmdOpenKeyBoard, true);
			}
		} catch (SerialPortException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * 关闭键盘端口
	 * 
	 * @throws SerialPortException
	 */
	public void closeKeyBoard() throws SerialPortException {

		if (mKeyBoard.isOpened()) {
			sendPortData(mKeyBoard, cmdCloseKeyBoard, true);
		}
		mKeyBoard.closePort();
	}

	/**
	 * 打开rfid端口
	 * 
	 * @throws SerialPortException
	 */
	public void openRFIDReader() {
		try {
			if (mRFIDReader.isOpened() || mRFIDReader.openPort()) {
				try {
					mRFIDReader.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}

				VendingDbOper vendingDbOper = new VendingDbOper();
				String cardtype = vendingDbOper.getVending().getVd1CardType();
				if (cardtype.equals("1")) {
					mRFIDReader.setParams(19200, 8, 1, 0); // 波特率、数据位、停止位、奇偶
					if (mSendThread == null) {
						mSendThread = new SendThread();
						mSendThread.start();
					}

					mSendThread.setResume(); // 线程唤醒，开始发送
				} else {
					mRFIDReader.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				}

			}
		} catch (SerialPortException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * 关闭rfid端口
	 * 
	 * @throws SerialPortException
	 */
	public void closeRFIDReader() throws SerialPortException {
		try {
			if (!mSendThread.isInterrupted()) {
				mSendThread.setSuspendFlag(); // 线程暂停
				// mSendThread.interrupt();
			}
			if (mRFIDReader.isOpened()) {
				mRFIDReader.closePort();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 格子机器开门
	 * 
	 * @param a
	 *            型号
	 * @param b
	 *            编号
	 * @param c
	 *            门号
	 */
	public void openStore(int a, int b, int c) {
		ZillionLog.i("格子机开门：", a + " " + b + " " + c);
		try {
			if (mStore.isOpened() || mStore.openPort()) {
				try {
					mStore.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mStore.setCheckA(a);
				mStore.setCheckB(b);
				mStore.setRequestMethod(SerialTools.MESSAGE_LOG_mStore);
				mStore.setParams(38400, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				sendPortData(mStore, MyFunc.cmdOpenStoreDoor(a, b, c), true); // 参数:设备类型，编号，门号
				// ZillionLog.i("sendPortData", MyFunc.cmdOpenStoreDoor(a, b,
				// c));
			}
		} catch (SerialPortException e) {
			ZillionLog.e("格子机开门：", a + " " + b + " " + c + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检查格子机状态
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public void checkStore(int a, int b) {
		try {
			if (mStore.isOpened()) {
				try {
					mStore.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mStore.setCheckA(a);
				mStore.setCheckB(b);
				mStore.setRequestMethod(SerialTools.MESSAGE_LOG_mStore_check);
				mStore.setParams(38400, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				sendPortData(mStore, MyFunc.cmdCheckStoreDoor(a, b), true); // 参数:设备类型，编号，门号
				Log.i(TAG, MyFunc.cmdCheckStoreDoor(a, b));
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭格子机
	 * 
	 * @throws SerialPortException
	 */
	public void closeStore() throws SerialPortException {

		if (mStore.isOpened()) {
			mStore.closePort();
		}

	}

	/**
	 * 打开售货机
	 * 
	 * @param a
	 *            列
	 * @param b
	 *            行
	 */
	public void openVender(int a, int b, Object mUserInfo) {
		this.userInfo = mUserInfo;
		try {
			if (mVender.isOpened() || mVender.openPort()) {
				try {
					mVender.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mVender.setCheckA(a);
				mVender.setCheckB(b);
				mVender.setRequestMethod(SerialTools.MESSAGE_LOG_mVender);
				mVender.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				sendPortData(mVender, MyFunc.cmdOpenVender(a, b), true); // 参数:设备类型，编号，门号
				Log.d(TAG, MyFunc.cmdOpenVender(a, b));
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开售货机
	 * 
	 * @param a
	 *            列
	 * @param b
	 *            行
	 */
	public void checkVender(int a, int b, Object mUserInfo) {
		this.userInfo = mUserInfo;
		try {
			if (mVender.isOpened() || mVender.openPort()) {
				try {
					mVender.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mVender.setCheckA(a);
				mVender.setCheckB(b);
				mVender.setRequestMethod(SerialTools.MESSAGE_LOG_mVender_check);
				mVender.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				sendPortData(mVender, MyFunc.cmdCheckVender(a, b), true); // 参数:设备类型，编号，门号
				Log.d(TAG, MyFunc.cmdCheckVender(a, b));
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开售货机
	 * 
	 * @param a
	 *            列
	 * @param b
	 *            行
	 */
	public void openVender(int a, int b) {
		try {
			if (mVender.isOpened() || mVender.openPort()) {
				try {
					mVender.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mVender.setCheckA(a);
				mVender.setCheckB(b);
				mVender.setRequestMethod(SerialTools.MESSAGE_LOG_mVender);
				mVender.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				// sendPortData(mVender, "0002000100010001010006", true);
				sendPortData(mVender, MyFunc.cmdOpenVender(a, b), true); // 参数:设备类型，编号，门号
				Log.d(TAG, MyFunc.cmdOpenVender(a, b));
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开售货机
	 * 
	 * @param a
	 *            列
	 * @param b
	 *            行
	 */
	public void checkVender(int a, int b) {
		try {
			if (mVender.isOpened() || mVender.openPort()) {
				try {
					mVender.addEventListener(listener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mVender.setCheckA(a);
				mVender.setCheckB(b);
				mVender.setRequestMethod(SerialTools.MESSAGE_LOG_mVender_check);
				mVender.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶
				// sendPortData(mVender, "0002000100010001010006", true);
				sendPortData(mVender, MyFunc.cmdCheckVender(a, b), true); // 参数:设备类型，编号，门号
				Log.d(TAG, MyFunc.cmdCheckVender(a, b));
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭格子机
	 * 
	 * @throws SerialPortException
	 */
	public void closeVender() throws SerialPortException {

		if (mVender.isOpened()) {
			mVender.closePort();
		}

	}

	/**
	 * 打开静载称重模块
	 * 
	 * @author junjie.you
	 * @param pId
	 *            称重模块ID号
	 * @throws SerialPortException
	 */
	public void openFW(int pId, int pMethodType) {
		ZillionLog.i("打开称重模块：", pId);
		try {
			if (mFw.isOpened() || mFw.openPort()) {
				try {
					mFw.addEventListener(mFWlistener);
				} catch (Exception e) {
					// TODO: handle exception
				}
				mFw.setRequestMethod(SerialTools.MESSAGE_LOG_mFw);

				mFw.setParams(9600, 8, 1, 0); // 波特率、数据位、停止位、奇偶

//				if (mSendThread == null) {
//					mSendThread = new SendThread();
//					mSendThread.start();
//				}

//				mSendThread.setResume(); // 线程唤醒，开始发送
				switch (pMethodType) {
				case Constant.FW_GET_WEIGHT:
					sendPortData(mFw, MyFunc.cmdOpenFW(pId), true);
					break;
				case Constant.FW_NET_WEIGHT:
					sendPortData(mFw, MyFunc.cmdNetWeightFW(pId), true);
					break;
				case Constant.FW_SET_ZERO:
					sendPortData(mFw, MyFunc.cmdSetZeroFW(pId), true);
					break;
				default:
					break;
				}

			}
		} catch (SerialPortException e) {
			ZillionLog.e("打开称重模块：", pId + " " + e.getExceptionType().toString());
			e.printStackTrace();
		}
	}

	/**
	 * 关闭静载称重模块
	 * 
	 * @author junjie.you
	 * @throws SerialPortException
	 */
	public void closeFW() throws SerialPortException {
		if (mFw.isOpened()) {
			mFw.closePort();
		}
	}

	private SerialPortEventListener mFWlistener = new SerialPortEventListener() {

		@Override
		public void serialEvent(SerialPortEvent event) {
			// TODO Auto-generated method stub
			// Log.i(TAG, "Receive " + event.getEventValue() + " Bytes: " +
			// event.getEventValue()+"EventType:"+event.getEventType());
			if (event.isRXCHAR()) {// If
									// data
									// is
									// available
				int obtain = 0;
				Log.i(TAG, "Receive " + event.getEventValue() + " Bytes: " + event.getEventValue() + "EventType:"
						+ event.getEventType());
				if (event.getEventValue() > 0) {
					try {
						String data = null;
						if (PortName_mFw.equals(event.getPortName())) { // added
							// by
							// junjie.you
							obtain = SerialTools.MESSAGE_LOG_mFw;
							data = mFw.readHexString(event.getEventValue());
							Log.i(TAG, "Receive " + data.length() + " Bytes: " + data);
						}

						Message m = Message.obtain(mHandler, obtain);
						m.obj = data;
						mHandler.sendMessage(m);

					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
			} else if (event.isCTS()) { /* cts */
			} else if (event.isDSR()) { /* dsr */
			}
			try {
				// SerialTools.getInstance().closeFW();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// catch (SerialPortException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

	};
	/**
	 * 返回数据监听
	 */
	private SerialPortEventListener listener = new SerialPortEventListener() {

		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR()) {// If
									// data
									// is
									// available
				int obtain = 0;
				if (event.getEventValue() > 0) {
					try {
						String data = null;
						if (PortName_mKeyBoard.equals(event.getPortName())) {
							data = mKeyBoard.readHexString(event.getEventValue());
							obtain = SerialTools.MESSAGE_LOG_mKeyBoard;
						} else if (PortName_mRFIDReader.equals(event.getPortName())) {
							data = mRFIDReader.readHexString(event.getEventValue());
							obtain = SerialTools.MESSAGE_LOG_mRFIDReader;
							Log.i(TAG, "Receive " + data.length() + " Bytes: " + data);
							// if (data != null && data.equals("18")) {
							// rfidReaderPote = 9600;
							// openRFIDReader();
							// }
						} else if (PortName_mVender.equals(event.getPortName())) {
							obtain = SerialTools.MESSAGE_LOG_mVender;
							data = mVender.readHexString(event.getEventValue());
							Log.i(TAG, "Receive " + data.length() + " Bytes: " + data);
						} else if (PortName_mStore.equals(event.getPortName())) {
							obtain = SerialTools.MESSAGE_LOG_mStore;
							data = mStore.readHexString(event.getEventValue());
							Log.i(TAG, "Receive " + data.length() + " Bytes: " + data);
						} else if (PortName_mFw.equals(event.getPortName())) { // added
							// by
							// junjie.you
							obtain = SerialTools.MESSAGE_LOG_mFw;
							int i = event.getEventValue();
							data = mFw.readHexString(i);
							Log.i(TAG, "Receive " + data.length() + " Bytes: " + data);
						}
						Message m = Message.obtain(mHandler, obtain);
						m.obj = data;
						mHandler.sendMessage(m);

					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
			} else if (event.isCTS()) { /* cts */
			} else if (event.isDSR()) { /* dsr */
			}
		}
	};

	/**
	 * 端口返回数据handler
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case SerialTools.MESSAGE_LOG_mFw:
				toolsListener.serialReturn((String) msg.obj, msg.what);
				break;
			case SerialTools.MESSAGE_LOG_mKeyBoard:
				if (toolsListener != null && toolsListener instanceof MC_CombinationPickDetailActivity) {
					toolsListener.serialReturn((String) msg.obj, msg.what, userInfo);

				} else {
					if (toolsListener != null) {
						toolsListener.serialReturn((String) msg.obj, msg.what);
					}
				}
				break;
			case SerialTools.MESSAGE_LOG_mRFIDReader:
				String data = (String) msg.obj;
				data = MyFunc.getRFIDSerialNo(data);
				if (data != null) {
					try {
						sendPortData(mRFIDReader, cmdBeep, true);
					} catch (SerialPortException e) {
						e.printStackTrace();
					}
				}
				if (toolsListener != null && toolsListener instanceof MC_CombinationPickDetailActivity) {
					toolsListener.serialReturn((String) msg.obj, msg.what, userInfo);

				} else {
					if (toolsListener != null) {
						toolsListener.serialReturn((String) msg.obj, msg.what);
					}
				}
				break;
			case SerialTools.MESSAGE_LOG_mVender:
				if (mVender.getRequestMethod() == SerialTools.MESSAGE_LOG_mVender) {
					checkVender(mVender.getCheckA(), mVender.getCheckB());
				} else {
					msg.what = mVender.getRequestMethod();
					if (toolsListener != null && toolsListener instanceof MC_CombinationPickDetailActivity) {
						toolsListener.serialReturn((String) msg.obj, msg.what, userInfo);

					} else {
						if (toolsListener != null) {
							toolsListener.serialReturn((String) msg.obj, msg.what);
						}
					}
				}
				break;
			case SerialTools.MESSAGE_LOG_mStore:
				if (mStore.getRequestMethod() == SerialTools.MESSAGE_LOG_mStore) {
					for (int i = 0; i < 5; i++) {
						checkStore(mStore.getCheckA(), mStore.getCheckB());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}

				} else {
					msg.what = mStore.getRequestMethod();
					if (toolsListener != null && toolsListener instanceof MC_CombinationPickDetailActivity) {
						toolsListener.serialReturn((String) msg.obj, msg.what, userInfo);

					} else {
						if (toolsListener != null) {
							toolsListener.serialReturn((String) msg.obj, msg.what);
						}
					}
				}
				break;
			}

		}
	};

	/**
	 * rfid 线程监听线程
	 * 
	 * @author apple
	 *
	 */
	private class SendThread extends Thread {
		public boolean suspendFlag = true; // 控制线程的执行

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				synchronized (this) {
					while (suspendFlag) {
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				try {
					sendPortData(mRFIDReader, cmdGetSerialNo, true);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(iDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// 线程暂停
		public void setSuspendFlag() {
			this.suspendFlag = true;
		}

		// 唤醒线程
		public synchronized void setResume() {
			this.suspendFlag = false;
			notify();
		}
	}

	/**
	 * 喘口发送
	 * 
	 * @param ComPort
	 * @param data
	 * @param isHex
	 * @throws SerialPortException
	 */
	private void sendPortData(SerialPort ComPort, String data, boolean isHex) throws SerialPortException {

		if (ComPort != null && ComPort.isOpened()) {
			if (isHex) {
				ComPort.writeBytes(MyFunc.HexToByteArr(data));
			} else {
				ComPort.writeString(data);

				// sendDataForFW(data.replaceAll(" ", ""));
			}
		}
	}

	public static OutputStream out;

	private void sendDataForFW(String Msg) {
		try {
			if (out != null) {
				for (int i = 0; i < Msg.length(); i++)
					out.write(Msg.charAt(i));
			}

		} catch (Exception e) {

			return;

		}
	}
}
