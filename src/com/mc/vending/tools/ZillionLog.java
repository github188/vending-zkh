package com.mc.vending.tools;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.DataParseHelper;
import com.mc.vending.tools.utils.DES;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 带日志文件输入的，又可控�?关的日志调试
 * 
 * @author BaoHang
 * @version 1.0
 * @data 2012-2-20
 */
public class ZillionLog {
    // private static String MYLOG_PATH_SDCARD_DIR =
    // "/data/data/com.mc.vending/files/log/vending.log"; // 日志文件在sdcard中的路径

    private static Logger gLogger;
    private static String configUrl;
    private static String vendingCode;
    private static int logSize = 500;

    public void configLog() {
        Log.i("ZillionLog", "configLog");
        final LogConfigurator logConfigurator = new LogConfigurator();

        // String status = Environment.getExternalStorageState();

        String MYLOG_PATH_SDCARD_DIR = "/mnt/sdcard1/com.zillionstar/log/vending.log"; // 日志文件在sdcard中的路径

        if (!(new File("/mnt/sdcard1/").exists() && new File("/mnt/sdcard1/").list() != null && new File(
                "/mnt/sdcard1/").list().length > 0)) {
//            MYLOG_PATH_SDCARD_DIR = "/data/data/com.mc.vending/logs/vending.log";
            MYLOG_PATH_SDCARD_DIR = "/storage/sdcard0/Download/vendinglog/vending.txt"; // 日志文件在sdcard中的路径
        }

        logConfigurator.setFileName(MYLOG_PATH_SDCARD_DIR);
        logConfigurator.setFilePattern("%d::%t::%c::%m%n");
        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        // logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setMaxFileSize(1024 * 1024 * 10);

        logConfigurator.configure();

        // gLogger = Logger.getLogger(this.getClass());

        Context context = CustomApplication.getContext();
        SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_CONFIG, context.MODE_PRIVATE);
        configUrl = shared.getString(Constant.SHARED_CONFIG_URL, "");

        shared = context.getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, Context.MODE_PRIVATE);
        vendingCode = shared.getString(Constant.SHARED_VEND_CODE, "");
    }

    public static void d(String msg) {
        gLogger = Logger.getLogger("");
        gLogger.debug(msg);
    }

    public static void i(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.info(msg);

        String eString = msg + "";
        sendLog(eString, 2);
    }

    public static void i(String tag, Object msg) {
        gLogger = Logger.getLogger(tag);
        gLogger.info(msg);

        String eString = tag + ":" + msg;
        sendLog(eString, 2);
    }

    private static void sendLog(String eString, int level) {

        Context context = CustomApplication.getContext();
        SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_VEND_CODE_KEY,
                context.MODE_PRIVATE);
        String vendingDebugStatus = shared.getString(Constant.SHARED_VEND_DEBUG_STATUS, "");
        if (vendingDebugStatus != null && !vendingDebugStatus.equals("")
                && Integer.valueOf(vendingDebugStatus) >= level) {
            requestToParse(eString.length() > logSize ? eString.substring(0, logSize) : eString);
        }
    }

    public static void e(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.error(msg);

        String eString = msg + "";
        sendLog(eString, 1);
    }

    public static void e(String tag, Object msg) {
        gLogger = Logger.getLogger("E:" + tag);
        gLogger.error(msg);

        String eString = tag + ":" + msg;
        sendLog(eString, 1);
    }

    public static void e(String tag, Object msg, Throwable e) {
        gLogger = Logger.getLogger("E:" + tag);
        gLogger.error(msg, e);

        String eString = tag + ":" + msg + Tools.getStackTrace(e);
        sendLog(eString, 1);

    }

    private static ZillionLog instance = new ZillionLog();

    private ZillionLog() {
        configLog();
    }

    public static ZillionLog getInstance() {
        return instance;
    }

    private static void requestToParse(final String errorMsg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject param = new JSONObject();
                    param.put("optype", Constant.HTTP_OPERATE_TYPE_INSERT);
                    param.put("tbname", "Value");

                    JSONArray rows = new JSONArray();

                    JSONObject aa = new JSONObject();
                    aa.put("IF2_VD1_ID", vendingCode);
                    aa.put("IF2_WSID", "");
                    aa.put("IF2_ErrorMessage", errorMsg);

                    rows.put(aa);
                    param.put("rows", rows);

                    // 封装传送的数据
                    List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
                    JSONObject json = new JSONObject();
                    json.put(Constant.BODY_KEY_METHOD, Constant.METHOD_WSID_VENDINGRUNERROR);
                    json.put(Constant.BODY_KEY_UDID, Constant.BODY_VALUE_UDID);
                    json.put(Constant.BODY_KEY_APP, Constant.BODY_VALUE_APP);
                    json.put(Constant.BODY_KEY_USER, Constant.BODY_VALUE_USER);
                    json.put(Constant.BODY_KEY_PWD, DES.getEncrypt());

                    if (param != null) {
                        JSONArray array = new JSONArray();
                        array.put(param);
                        json.put(Constant.BODY_PARAM_KEY_DATA, array);
                    }
                    postData.add(new BasicNameValuePair(Constant.BODY_XML_INPUT, json.toString()));
                    System.setProperty("http.keepAlive", "false");
                    i("zillionlog configUrl", configUrl);
                    HttpPost httpPost = new HttpPost(configUrl);
                    httpPost.setHeader(Constant.HEADER_KEY_CONTENT_TYPE,
                            HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CONTENT_TYPE));
                    httpPost.setHeader(Constant.HEADER_KEY_CLIENTVER,
                            HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CLIENTVER));

                    BasicHttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, Constant.REQUEST_TIMEOUT);
                    HttpConnectionParams.setSoTimeout(httpParams, Constant.SO_TIMEOUT);

                    HttpClient httpClient = new DefaultHttpClient(httpParams);
                    HttpResponse response = null;

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, HTTP.UTF_8);
                    httpPost.setEntity(entity);

                    try {
                        response = httpClient.execute(httpPost);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    } else {
                        httpPost.abort();
                    }

                } catch (Exception e) {
                    ZillionLog.i("Exception e", e.getMessage());
                }
            }
        }).start();
    }

}
