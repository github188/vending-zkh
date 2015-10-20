package com.mc.vending.activitys;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.mc.vending.R;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import com.zillionstar.tools.L;

public class MC_ImagePlayerActivity extends BaseActivity implements MC_SerialToolsListener {

    private ImageView                image_player;     // 图片显示控件
    private List<VendingPictureData> pictrueList;      // 图片数组
    private int                      currentindex;     // 当前播放图片索引
    private final int                IMAGE_PLAY = 1001;
    private AsyncImageLoader         asyncImageLoader;
    boolean                          isStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stopLoading();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_player);
        ActivityManagerTool.getActivityManager().add(this);
        initComponents();
        initObject();
    }

    private void initComponents() {
        image_player = (ImageView) this.findViewById(R.id.image_player);
        asyncImageLoader = new AsyncImageLoader();
    }

    private void initObject() {
        currentindex = 0;
        isStop = false;
        VendingPictureDbOper db = new VendingPictureDbOper();
        pictrueList = db.findVendingPicture();
        showNext();
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

    @Override
    protected void onResume() {
        openKeyBoard();
        super.onResume();
    }

    /**
     * 显示图片
     */
    private void showNext() {

        L.v("showNext " + pictrueList.size());
        if (!isStop && pictrueList.size() > 0) {
            VendingPictureData data = pictrueList.get(currentindex);
            loadImage(data.getVp2FilePath());

            Message msg = new Message();
            msg.what = IMAGE_PLAY;
            mHander.sendMessageDelayed(msg, 1000 * data.getVp2RunTime());
            if (currentindex < pictrueList.size() - 1) {
                currentindex += 1;
            } else {
                currentindex = 0;
            }
        }
    }

    /**
     * 异步加载图片
     * 
     * @param imageURL
     */
    private void loadImage(String imageURL) {
        L.v("loadImage " + imageURL);

        Drawable cachedImage = asyncImageLoader.loadDrawable(imageURL, new AsyncImageLoader.ImageCallback() {

            @Override
            public void imageLoaded(final Drawable imageDrawable, final String imageUrl, String tag) {
                L.v("loadImage imageLoaded " + imageDrawable);
                image_player.setImageDrawable(imageDrawable);
            }
        });
        if (cachedImage != null) {
            image_player.setImageDrawable(cachedImage);
        }
    }

    final public Handler mHander = new Handler() {
                                     @Override
                                     public void handleMessage(Message msg) {
                                         int what = msg.what;
                                         switch (what) {
                                         case IMAGE_PLAY:
                                             showNext();
                                             break;
                                         case SerialTools.MESSAGE_LOG_mKeyBoard:
                                             String value = (String) msg.obj;
                                             if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)
                                                     || SerialTools.FUNCTION_KEY_BORROW.equals(value)
                                                     || SerialTools.FUNCTION_KEY_BACK.equals(value)
                                                     || SerialTools.FUNCTION_KEY_SET.equals(value)
                                                     || SerialTools.FUNCTION_KEY_CANCEL.equals(value)
                                                     || SerialTools.FUNCTION_KEY_CONFIRM.equals(value)
                                                     || "0".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "1".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "2".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "3".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "4".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "5".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "6".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "7".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "8".equals(SerialTools.getInstance().getKeyValue(
                                                             value))
                                                     || "9".equals(SerialTools.getInstance().getKeyValue(
                                                             value))) {
                                                 isStop = true;
                                                 finish();
                                             }
                                         default:
                                             break;
                                         }
                                     }
                                 };

    public void imagePlayerClicked(View view) {
        isStop = true;
        this.finish();
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

        // 判断串口类型
        switch (serialType) {
        case SerialTools.MESSAGE_LOG_mKeyBoard:
            // 当串口类型为键盘时，判断是否为功能键
            Message msg = new Message();
            msg.what = serialType;
            msg.obj = value;
            mHander.sendMessage(msg);
            break;
        case SerialTools.MESSAGE_LOG_mRFIDReader:
            break;
        case SerialTools.MESSAGE_LOG_mVender:
            break;
        case SerialTools.MESSAGE_LOG_mStore:
            break;
        }

    }

    @Override
    public void serialReturn(String value, int serialType, Object userInfo) {

    }
}
