/**
 * 
 */
package com.mc.vending.activitys.pick;

import com.mc.vending.R;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortEvent;
import com.zillion.evm.jssc.SerialPortEventListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author junjie.you
 *
 */
public class testActivity extends Activity implements SerialPortEventListener, MC_SerialToolsListener {
	Toast toast;
	private SeekBar test_sb_seekbar1 = null;
	private SeekBar test_sb_seekbar2 = null;

	public EditText test_et_rollSingleVending = null;
	public EditText test_et_openSingleStore = null;

	public TextView test_value_card = null;
	public TextView test_value_keyboard = null;

	public Button test_btn_rollAllVending = null;
	public Button test_btn_rollSingleVending = null;
	public Button test_btn_openAllStore = null;
	public Button test_btn_openSingleStore = null;
	public Button test_btn_exit = null;

	public final int seekBar1MaxValue = 59;
	public final int seekBar2MaxValue = 17;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mc.vending.activitys.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		test_sb_seekbar1 = (SeekBar) findViewById(R.id.test_sb_seekbar1);
		test_sb_seekbar1.setMax(seekBar1MaxValue);
		test_sb_seekbar1.setOnSeekBarChangeListener(new OnSeekBar1ChangeListenerImp());

		test_sb_seekbar2 = (SeekBar) findViewById(R.id.test_sb_seekbar2);
		test_sb_seekbar2.setMax(seekBar2MaxValue);
		test_sb_seekbar2.setOnSeekBarChangeListener(new OnSeekBar2ChangeListenerImp());

		test_et_rollSingleVending = (EditText) findViewById(R.id.test_et_rollSingleVending);
		test_et_openSingleStore = (EditText) findViewById(R.id.test_tv_openSingleStore);

//		test_value_card = (TextView) findViewById(R.id.test_value_card);
//		test_value_keyboard = (TextView) findViewById(R.id.test_value_keyboard);

		test_btn_rollAllVending = (Button) findViewById(R.id.test_btn_rollAllVending);
		test_btn_rollAllVending.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 1; i < 7; i++) {
					for (int j = 0; j < 10; j++) {
						try {
							SerialTools.getInstance().openVender(i, j);
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		test_btn_rollSingleVending = (Button) findViewById(R.id.test_btn_rollSingleVending);
		test_btn_rollSingleVending.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String vendingNum = "";
				vendingNum = test_et_rollSingleVending.getText().toString();
				if (!vendingNum.isEmpty()) {
					if (ConvertHelper.toInt(vendingNum, 10)>69) {
						showToast("货道号不得大于69");
						return;
					}
					if (ConvertHelper.toInt(vendingNum, 10)<10) {
						showToast("货道号不得小于10");
						return;
					}
					int line = ConvertHelper.toInt(vendingNum.substring(0, 1), 1);
					int column = ConvertHelper.toInt(vendingNum.substring(1, 2), 0);
					SerialTools.getInstance().openVender(line, column);
				}else{
					showToast("请拖动进度条选择货道号");
				}
			}
		});
		test_btn_openAllStore = (Button) findViewById(R.id.test_btn_openAllStore);
		test_btn_openAllStore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int j = 1; j < 19; j++) {
					try {
						SerialTools.getInstance().openStore(2, 1, j);
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		test_btn_openSingleStore = (Button) findViewById(R.id.test_btn_openSingleStore);
		test_btn_openSingleStore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String storeNum = "";
				storeNum = test_et_openSingleStore.getText().toString();
				if (!storeNum.isEmpty()) {
					if (ConvertHelper.toInt(storeNum, 70)>87) {
						showToast("门号不得大于87");
						return;
					}
					if (ConvertHelper.toInt(storeNum, 70)<70) {
						showToast("门号不得小于70");
						return;
					}
					SerialTools.getInstance().openStore(2, 1, ConvertHelper.toInt(storeNum, 70)-69);
				}else{
					showToast("请拖动进度条选择门号");
				}
			}
		});
		test_btn_exit = (Button) findViewById(R.id.test_btn_exit);
		test_btn_exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zillion.evm.jssc.SerialPortEventListener#serialEvent(com.zillion.evm.
	 * jssc.SerialPortEvent)
	 */
	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		// TODO Auto-generated method stub

	}

	private class OnSeekBar1ChangeListenerImp implements SeekBar.OnSeekBarChangeListener {

		// 触发操作，拖动
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			test_et_rollSingleVending.setText(progress + 10 + "");
		}

		// 表示进度条刚开始拖动，开始拖动时候触发的操作
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		// 停止拖动时候
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}
	}

	private class OnSeekBar2ChangeListenerImp implements SeekBar.OnSeekBarChangeListener {

		// 触发操作，拖动
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			test_et_openSingleStore.setText(progress + 70 + "");
		}

		// 表示进度条刚开始拖动，开始拖动时候触发的操作
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		// 停止拖动时候
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
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
     * 显示toast提示信息
     * 
     * @param message
     */
    public void showToast(String message) {

        toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
