package com.mc.vending.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;

import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author zoutiao
 * 
 */
public class Tools {

    public int responseValue; // 1代表正常0代表其它错误2代表服务端相应错误，3代表解析错误
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f' };

    public static String removeLine(String val) {
        if (val.startsWith("{&&}")) {
            val = val.substring(4);
            val = val.trim();
            val = removeLine(val);
        }
        if (val.endsWith("{&&}")) {
            val = val.substring(0, val.length() - 4);
            val = val.trim();
            val = removeLine(val);
        }
        if (val.contains("{&&}{&&}")) {
            val = val.replace("{&&}{&&}", "{&&}");
            val = removeLine(val);
        }
        val = val.replace("{&&}", "<br />");
        return val;
    }
    public static String getMacAddress() {
		String result = "123";

		return result.trim();
	}
    /**
     * 检测设备是不是手机号码
     * 
     * @param activityContext
     * @return
     */
    // public static boolean isTabletDevice(Context activityContext) {
    // // Verifies if the Generalized Size of the device is XLARGE to be
    // // considered a Tablet
    // TelephonyManager manager =
    // (TelephonyManager)activityContext.getSystemService(Context.TELEPHONY_SERVICE);
    // if(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
    // { // it has no phone
    // return true;
    // }
    // boolean xlarge =
    // ((activityContext.getResources().getConfiguration().screenLayout &
    // Configuration.SCREENLAYOUT_SIZE_MASK) ==
    // Configuration.SCREENLAYOUT_SIZE_XLARGE);
    //
    // // If XLarge, checks if the Generalized Density is at least MDPI
    // // (160dpi)
    // if (xlarge) {
    // DisplayMetrics metrics = new DisplayMetrics();
    // Activity activity = (Activity) activityContext;
    // activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    //
    // // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
    // // DENSITY_TV=213, DENSITY_XHIGH=320
    // if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
    // || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
    // || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
    // || metrics.densityDpi == DisplayMetrics.DENSITY_TV
    // || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
    //
    // // Yes, this is a tablet!
    // return true;
    // }
    // }
    // // No, this is not a tablet!
    // return false;
    // }
    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成
    {
        int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
                                                                    // bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把 drawable 内容画到画布中
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float) w / width); // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回
    }

    public static Bitmap zoomDrawableToBitmap(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float) w / width); // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return newbmp; // 把 bitmap 转换成 drawable 并返回
    }

    public static boolean hasTelphoneMode(Context activityContext) {
        TelephonyManager manager = (TelephonyManager) activityContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) { // it
                                                                          // has
                                                                          // no
                                                                          // phone
            return false;
        }
        return true;
    }

    public static String nullToStr(String str) {
        if (isNull(str)) {
            return "";
        } else {
            return str;
        }
    }

    /*
     * 获取sdk版本信息
     */
    public static String getSDKVersion(Context context) {
        String ver = "";
        String verInt = android.os.Build.VERSION.SDK;
        if ("2".equals(verInt)) {
            ver = "1.1";
        } else if ("3".equals(verInt)) {
            ver = "1.5";
        } else if ("4".equals(verInt)) {
            ver = "1.6";
        } else if ("5".equals(verInt)) {
            ver = "2.0";
        } else if ("6".equals(verInt)) {
            ver = "2.0.1";
        } else if ("7".equals(verInt)) {
            ver = "2.1";
        } else if ("8".equals(verInt)) {
            ver = "2.2";
        } else if ("9".equals(verInt)) {
            ver = "2.3.1";
        } else if ("10".equals(verInt)) {
            ver = "2.3.3";
        } else if ("11".equals(verInt)) {
            ver = "3.0";
        } else if ("12".equals(verInt)) {
            ver = "3.1";
        } else if ("13".equals(verInt)) {
            ver = "3.2";
        } else if ("14".equals(verInt)) {
            ver = "4.0";
        } else if ("15".equals(verInt)) {
            ver = "4.0.3";
        } else if ("16".equals(verInt)) {
            ver = "4.1.2";
        } else if ("17".equals(verInt)) {
            ver = "4.2.2";
        } else {
            ver = "other";
        }
        return ver;
    }

    /*
     * 获取运营商信息
     */
    public static String getCarrier(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephony.getSubscriberId();
        if (imsi != null && !"".equals(imsi)) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                return "ChinaMobile";
            } else if (imsi.startsWith("46001")) {
                return "ChinaUnicom";
            } else if (imsi.startsWith("46003")) {
                return "ChinaTelecom";
            } else {
                return "othes";
            }
        }

        return null;
    }

    /*
     * 读取手机串号
     */
    public static String readTelephoneSerialNum(Context con) {
        TelephonyManager telephonyManager = (TelephonyManager) con
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /*
     * MD5加密字符
     */
    public static String md5(String source) {
        /*
         * if (true) { // true表示在测试阶段 return source; } else {
         */
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(source.getBytes());
            byte[] mess = digest.digest();
            return toHexString(mess);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return source;
        // }

    }

    public static String toHexString(byte[] b) { // byte to String
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /*
     * 获取路径中的文件
     */
    public static String getFileNameFromURL(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return fileName;
    }

    /*
     * 根据URL得到图片
     */
    public static Bitmap getBitmapFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            inputStream.close();
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 保存文件到指定路径，后缀名修改为.dat
     */
    public static void saveImageToSDAndChangePostfix(Bitmap bitmap, String path, String fileName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        File imageFile = new File(path + name + ".dat");
        try {
            bos = new BufferedOutputStream(new FileOutputStream(imageFile));
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
     * 保存文件到指定路径，后缀名不修改
     */
    @SuppressWarnings("hiding")
    public static Drawable saveImageToSD(Bitmap bitmap, String path, String fileName) {
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imageFile = new File(path + "/" + fileName);
        try {
            bos = new BufferedOutputStream(new FileOutputStream(imageFile));
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Drawable d = null;
        if (bitmap.getWidth() > 1280 | bitmap.getHeight() > 1280) {
            d = changeImageSize(path, fileName, bitmap);
        } else {
            d = new BitmapDrawable(bitmap);
        }

        return d;
    }

    public static Drawable changeImageSize(String path, String fileName, Bitmap bitmap) {
        int xw = bitmap.getWidth();
        int xh = bitmap.getHeight();
        float x = 1280.0f / xw;
        float y = 1280.0f / xh;
        float bit = x;
        if (x > y) {
            bit = y;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(bit, bit); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
        BufferedOutputStream bos = null;
        File imageFile = new File(path + "/aa_" + fileName);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        try {
            imageFile = new File(path + "/" + fileName);
            bos = new BufferedOutputStream(new FileOutputStream(imageFile));
            if (resizeBmp != null) {
                resizeBmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Bitmap bits = null;
        try {
            InputStream is = new FileInputStream(imageFile);
            bits = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        BitmapDrawable bd = new BitmapDrawable(bits);
        return bd;
    }

    @SuppressWarnings("hiding")
    public static void saveImageToSDThum(InputStream is, String path, String fileName) {
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String srcPath = path + "/" + fileName;
        try {
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            OutputStream os = new FileOutputStream(path + "/" + fileName);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 250f;// 这里设置高度为800f
        float ww = 250f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        saveBitmap(bitmap, path + "/thumb", fileName);
    }

    public static void saveBitmap(Bitmap argbitmap, String path, String fileName) {
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imageFile = new File(path + "/" + fileName);
        try {
            bos = new BufferedOutputStream(new FileOutputStream(imageFile));
            if (argbitmap != null) {
                argbitmap.compress(Bitmap.CompressFormat.PNG, 20, bos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 获取路径中的文件
     */
    public static String getFileName(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return fileName;
    }

    public static String fileNameToMd5(String url) {
        return md5(url);
    }

    /**
     * 获取地图文件
     * 
     * @param fileName
     * @param context
     * @return
     */
    public static File getMapFile(String fileName, Context context) {
        String path = context.getExternalFilesDir(null) + File.separator + fileName;
        File tileFile = new File(path);
        InputStream is;
        if (!tileFile.exists()) {
            try {
                is = context.getAssets().open(fileName);
                if (is == null) {
                    return tileFile;
                }
                FileOutputStream fo = new FileOutputStream(path);
                byte[] b = new byte[1024];
                int length;
                while ((length = is.read(b)) != -1) {
                    fo.write(b, 0, length);
                }

                fo.flush();
                fo.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tileFile;
    }

    /*
     * 从asset中得到source id
     */
    public static String getSourceIdFromAsset(Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("sourid.txt");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();
            baos.close();
            inputStream.close();
            return baos.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /*
     * 判断网络是否可用̬
     */
    public static boolean isAccessNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null && connectivity.getActiveNetworkInfo().isAvailable()) {
            return true;
        }
        return false;
    }

    /*
     * 获取网络信息
     */
    public static String getAccessNetworkType(Context context) {
        int type = 0;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null) {
            return null;
        }
        type = info.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            return "3G/GPRS";
        }
        return null;
    }

    /*
     * 获取SD卡剩余空间，无卡则返回-1
     */
    public static long getSDSize() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(path);
            long l = statFs.getBlockSize();
            return statFs.getAvailableBlocks() * l;
        }

        return -1;
    }

    /*
     * 获取当前时间
     */
    public static String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getNowTimeStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getSendTimeStr(String date) {

        String cd = getNowTimeStr();
        cd = cd.substring(0, 10);
        String dd = date.substring(0, 10);
        if (!cd.equals(dd)) {
            return dd;
        } else {
            Date cudate = null;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                cudate = formatDate.parse(date);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                return format.format(cudate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static String getNowDateStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(Calendar.getInstance().getTime());
    }

    public static Date stringToDate(String date) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date createDate = null;
        try {
            createDate = simpledateformat.parse(date);
        } catch (Exception ce) {
            //System.out.println(ce.getMessage());
        }
        return createDate;
    }

    public static String getNowTimeStr(int num) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        long times = date.getTime();
        long add = num * (24 * 60 * 60 * 1000);
        times = times + add;
        date = new Date(times);
        return format.format(date);
    }

    public static String getNowTimeStrPad(int num) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        Date date = new Date();
        long times = date.getTime();
        long add = num * (24 * 60 * 60 * 1000);
        times = times + add;
        date = new Date(times);
        return format.format(date);
    }

    // 删除指定目录下所有文件
    public static void deleteAllFile(final String filePath) {
        Thread t = new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                File display = new File(filePath);
                if (!display.exists()) {
                    return;
                }
                File[] items = display.listFiles();
                int i = display.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (items[j].isFile()) {
                        items[j].delete();// 删除文件
                    }
                }
            }
        });
        t.start();
    }

    // 清空指定路径下的的所有文件
    public static void deleteAllFiles(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int len = files.length;
            for (int i = 0; i < len; i++) {
                deleteAllFiles(files[i].getPath());
            }
        } else {
            file.delete();
        }

    }

    /**
     * 测试
     * 
     * @param listView
     */
    public void setListViewHeightBasedOnChildren2(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight() + 10; // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 获得本地ip地址
     * 
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 获得联网类型
     * 
     * @param context
     * @return
     */
    public static String getNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        String typeName = info.getTypeName();
        return typeName;

    }

    /**
     * 获得屏幕尺寸
     * 
     * @param context
     * @return
     */
    public static String getScreenSize(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels + "x" + metrics.heightPixels;
    }

    /**
     * Mac地址
     * 
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) ((Activity) context).getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 查询系统当前语言
     * 
     * @return
     */
    public static String getLanguageCode() {
        String languageCoade = "en-GB";
        languageCoade = Locale.getDefault().getLanguage();
        if ("zh".equals(languageCoade)) {
            languageCoade = "zh-CN";
        } else if ("en".equals(languageCoade)) {
            languageCoade = "en-GB";
        } else if ("fr".equals(languageCoade)) {
            languageCoade = "fr-FR";
        } else if ("it".equals(languageCoade)) {
            languageCoade = "it-IT";
        } else if ("de".equals(languageCoade)) {
            languageCoade = "de-DE";
        }
        return languageCoade;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 计算两点距离
     */
    public static final double EARTH_RADIUS = 6378137.0; // 取WGS84标准参考椭球中的地球长半径(单位:m)

    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 距离计算转换成KM 或M
     * 
     * @param distance
     * @return
     */
    public static String distance2Str(double distance) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (distance >= 1000) {
            return df.format(distance / 1000) + " Km";
        }
        return df.format(distance) + " m";
    }

    /**
     * 判断字符串是否为空
     * 
     * @param inputStr
     * @return
     */
    public static Boolean isNull(String inputStr) {
        return (inputStr == null || "".equals(inputStr.trim())) ? true : false;
    }

    /**
     * 字符串转如期
     * 
     * @param inputStr
     * @return
     */
    public static Date String2Date(String inputStr) {
        if (Tools.isNull(inputStr)) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(inputStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
        return date;
    }

    /**
     * 图片圆角
     * 
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // /**
    // * 图片格式转换
    // * @param drawable
    // * @return
    // */
    // public static Bitmap drawableToBitmap(Drawable drawable) {
    // Bitmap bitmap = Bitmap
    // .createBitmap(
    // drawable.getIntrinsicWidth(),
    // drawable.getIntrinsicHeight(),
    // drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
    // : Bitmap.Config.RGB_565);
    //
    // Canvas canvas = new Canvas(bitmap);
    // // canvas.setBitmap(bitmap);
    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
    // drawable.getIntrinsicHeight());
    // drawable.draw(canvas);
    // return bitmap;
    // }

    /**
     * bitmap2drawable
     * 
     * @param b
     * @return
     */
    public static Drawable Bitmap2Drawable(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(context.getResources(), bitmap);
        } else {
            return null;
        }
    }

    /**
     * byte[] 2bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 2 bytes
     * 
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 获得字体文件
     * 
     * @param sendCount
     * @return
     */
    public static Typeface getFonts(Context ac) {
        return Typeface.createFromAsset(ac.getAssets(), "fonts/Avenir.ttc");
    }

    public static void openCalendar(Activity activity, String title, String content) {
        Intent i = new Intent();
        ComponentName cn = null;
        try {
            cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
            i.setComponent(cn);
            activity.startActivityForResult(i, 0000);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
            i.setComponent(cn);
            activity.startActivityForResult(i, 0000);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得随机数不超过max
     * 
     * @param max
     * @return
     */
    public static int getRandomStr(int max) {
        return new Random().nextInt(max) % (max + 1);

    }

    public static Object copy(Object oldObj) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(oldObj);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    public static String getCustomStackTrace(Throwable aThrowable) {
        //add the class name and any message passed to constructor
        final StringBuilder result = new StringBuilder("BOO-BOO: ");
        result.append(aThrowable.toString());
        final String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);

        //add each element of the stack trace
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }
        return result.toString();
    }

    /**
     * 读取本地VEND_CODE
     * 
     * @return
     */
    public static String getVendCode() {
        Context context = CustomApplication.getContext();
        final SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_VEND_CODE_KEY,
                Context.MODE_PRIVATE);
        return shared.getString(Constant.SHARED_VEND_CODE, "");
    }
    /**
     * 读取本地URL
     * 
     * @return
     */
    public static String getConfigUrl() {
        Context context = CustomApplication.getContext();
        final SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_CONFIG, Context.MODE_PRIVATE);
        return shared.getString(Constant.SHARED_CONFIG_URL, "");
    }
    
}
