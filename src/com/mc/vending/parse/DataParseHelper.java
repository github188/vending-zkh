package com.mc.vending.parse;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.HttpHelper;
import com.mc.vending.tools.ZillionLog;

public class DataParseHelper {

    private Thread downLoadData;
    private BaseData baseData;
    private DataParseListener listener;

    /**
     * 初始化构造方法
     * 
     * @param pListener
     */
    public DataParseHelper(DataParseListener pListener) {
        baseData = new BaseData();
        listener = pListener;
    }

    /**
     * 读取本地VEND_CODE
     * 
     * @return
     */
    private static String getVendCode() {
        Context context = CustomApplication.getContext();
        final SharedPreferences shared = context.getSharedPreferences(Constant.SHARED_VEND_CODE_KEY,
                Context.MODE_PRIVATE);
        return shared.getString(Constant.SHARED_VEND_CODE, "");
    }

    /**
     * 异步网络请求
     * 
     * @param objArray
     *            请求数组
     * @param requestURL
     *            请求方法名称
     */
    public void requestSubmitServer(final String optType, final JSONArray objArray, final String requestURL) {
        downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");

                    JSONArray rows = new JSONArray();
                    for (int i = 0; i < objArray.length(); i++) {
                        JSONObject aa = (JSONObject) objArray.get(i);

                        String vd = getVendCode();
                        if (vd != null && !vd.equals("")) {
                            aa.put("VD1_CODE", vd);
                        }
                        aa.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);

                        rows.put(aa);
                    }
                    param.put("rows", rows);
                    // post方式请求,传给服务端

                    baseData = new BaseData();
                    baseData.setRequestURL(requestURL);
                    baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, baseData);
                } catch (Exception e) {
//                    e.printStackTrace();
                    ZillionLog.e(this.getClass().getName(), "异步网络请求异常!", e);
                }
                handler.sendEmptyMessage(baseData.HTTP_STATUS);
            }
        });
        downLoadData.start();
    }

    /**
     * 异步网络请求
     * 
     * @param obj
     *            请求单个对象
     * @param requestURL
     *            请求方法名称
     */
    public void requestSubmitServer(final String optType, final JSONObject obj, final String requestURL) {
        downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");
                    if (obj != null) {

                        String vd = getVendCode();
                        if (vd != null && !vd.equals("")) {
                            obj.put("VD1_CODE", vd);
                        }
                        obj.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);

                        JSONArray rows = new JSONArray();
                        rows.put(obj);
                        param.put("rows", rows);
                    }

                    // post方式请求,传给服务端
                    baseData = new BaseData();
                    baseData.setRequestURL(requestURL);
                    baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(this.getClass().toString(), "++++++++++++>>>>>异步网络请求异常!", e);
                }
                handler.sendEmptyMessage(baseData.HTTP_STATUS);
            }
        });
        downLoadData.start();
    }

    /**
     * 异步网络请求 (需要返回后请求参数做更新)
     * 
     * @param objArray
     *            请求数组
     * @param requestURL
     *            请求方法名称
     */
    public void requestSubmitServer(final String optType, final JSONArray objArray, final String requestURL,
            final Object userObject) {
        downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");

                    JSONArray rows = new JSONArray();
                    for (int i = 0; i < objArray.length(); i++) {
                        JSONObject aa = (JSONObject) objArray.get(i);

                        String vd = getVendCode();
                        if (vd != null && !vd.equals("")) {
                            aa.put("VD1_CODE", vd);
                        }
                        aa.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);

                        rows.put(aa);
                    }
                    param.put("rows", rows);
                    // post方式请求,传给服务端
                    baseData = new BaseData();
                    baseData.setUserObject(userObject);
                    baseData.setRequestURL(requestURL);
                    baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(this.getClass().toString(), "======>>>>>异步网络请求异常!",e);
                }
                handler.sendEmptyMessage(baseData.HTTP_STATUS);
            }
        });
        downLoadData.start();
    }

    public void sendLogVersion(String logVersion) {
//        ZillionLog.i("logVersion", logVersion);
        JSONObject json = new JSONObject();
        try {
            json.put("LogVersionID", logVersion);
            //requestSubmitServer("Update", json, Constant.METHOD_WSID_SYN_LOGVERSION);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>sendLogVersion请求数据异常!");
        }
    }

    /**
     * 异步网络请求(需要返回后请求参数做更新)
     * 
     * @param obj
     *            请求单个对象
     * @param requestURL
     *            请求方法名称
     */
    public void requestSubmitServer(final String optType, final JSONObject obj, final String requestURL,
            final Object userObject) {
        downLoadData = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");

                    if (obj != null) {
                        String vd = getVendCode();
                        if (vd != null && !vd.equals("")) {
                            obj.put("VD1_CODE", vd);
                        }
                        obj.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);

                        JSONArray rows = new JSONArray();
                        rows.put(obj);
                        param.put("rows", rows);
                    }

                    // post方式请求,传给服务端
                    baseData = new BaseData();
                    baseData.setUserObject(userObject);
                    baseData.setRequestURL(requestURL);
                    baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(this.getClass().toString(), "-------->>>>>异步网络请求异常!",e);
                }
                handler.sendEmptyMessage(baseData.HTTP_STATUS);
            }
        });
        downLoadData.start();
    }

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (baseData.getReturnCode().equals("1")) {
                ZillionLog.e(this.getClass().getName(),
                        baseData.getRequestURL() + ":" + baseData.getReturnMessage());
            }
            switch (baseData.HTTP_STATUS) {
            case HttpHelper.HTTP_STATUS_SUCCESS:
                if (listener != null) {
                    listener.parseJson(baseData);
                }
                break;
            case HttpHelper.HTTP_STATUS_SERVER_ERROR:
                if (listener != null) {
                    listener.parseRequestError(baseData);
                }
                break;

            case HttpHelper.HTTP_STATUS_PRASE_ERROR:
                if (listener != null) {
                    listener.parseRequestError(baseData);
                }
                break;

            default:
                break;
            }

        }
    };

}
