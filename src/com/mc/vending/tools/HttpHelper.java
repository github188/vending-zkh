package com.mc.vending.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.tools.utils.DES;

public class HttpHelper {

    // 1代表正常0代表其它错误2代表服务端相应错误，3代表解析错误
    public static final int           HTTP_STATUS_SUCCESS      = 0;
    public static final int           HTTP_STATUS_ERROR        = 1;
    public static final int           HTTP_STATUS_SERVER_ERROR = 2;
    public static final int           HTTP_STATUS_PRASE_ERROR  = 3;

    public static Map<String, String> headerMap;

    public static Map<String, String> getHeaderMap() {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
            intHeader();
        }
        return headerMap;
    }

    public static void intHeader() {
        headerMap.put(Constant.HEADER_KEY_CONTENT_TYPE, Constant.HEADER_VALUE_CONTENT_TYPE);
        headerMap.put(Constant.HEADER_KEY_CLIENTVER, Constant.HEADER_VALUE_CLIENTVER);
    }

    public static void setHeader(String key, String value) {
        if ("".equals(key))
            return;
        headerMap.put(key, value);
    }
    
    public static BaseData requestToParse(Class className, String method, JSONObject param, BaseData baseData)
            throws JSONException {
        baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_ERROR;
        ZillionLog.i("request", Constant.WSIDNAMEMAP.get(method));
        // 封装传送的数据
        List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
        JSONObject json = new JSONObject();
        json.put(Constant.BODY_KEY_METHOD, method);
        json.put(Constant.BODY_KEY_UDID, Constant.BODY_VALUE_UDID);
        json.put(Constant.BODY_KEY_APP, Constant.BODY_VALUE_APP);
        json.put(Constant.BODY_KEY_USER, Constant.BODY_VALUE_USER);
        json.put(Constant.BODY_KEY_PWD, DES.getEncrypt());

        if (param != null) {

            JSONArray array = new JSONArray();
            array.put(param);
            json.put(Constant.BODY_PARAM_KEY_DATA, array);
        }
        // Log.i(className.getClass().toString(),
        // "=================================param start==================================");
        // Log.i(className.getClass().toString(), baseData.getRequestURL() +
        // "=====" + json.toString());
        // Log.i(className.getClass().toString(),
        // "=================================param end  ==================================");
        postData.add(new BasicNameValuePair(Constant.BODY_XML_INPUT, json.toString()));
        System.setProperty("http.keepAlive", "false");
        
        if(Constant.SERVER_URL == null || Constant.SERVER_URL.equals("")){

            ZillionLog.i("SERVER_URL NULL",Constant.SERVER_URL);
            Constant.SERVER_URL = Tools.getConfigUrl();
        }
        HttpPost httpPost = new HttpPost(Constant.SERVER_URL);
        httpPost.setHeader(Constant.HEADER_KEY_CONTENT_TYPE,
                HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CONTENT_TYPE));
        httpPost.setHeader(Constant.HEADER_KEY_CLIENTVER,
                HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CLIENTVER));

        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, Constant.REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, Constant.SO_TIMEOUT);

        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse response = null;

        // for (int i = 0; i < httpPost.getAllHeaders().length; i++) {
        // Log.e(className.getClass().toString(),
        // (httpPost.getAllHeaders()[i]).toString());
        // }

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postData, HTTP.UTF_8);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SUCCESS;
            } else {
                baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SERVER_ERROR;
                httpPost.abort();
            }

//        } catch (UnsupportedEncodingException e) {
//            // e.printStackTrace();
//            // L.e(e.getMessage());
//            ZillionLog.i("SERVER_URL",Constant.SERVER_URL);
//            ZillionLog.e("HttpHelp", e.getMessage(),e);
//            baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SERVER_ERROR;
//        } catch (IOException e) {
//            // L.e(e.getMessage());
//            // e.printStackTrace();
//            ZillionLog.i("SERVER_URL",Constant.SERVER_URL);
//            ZillionLog.e("HttpHelp", e.getMessage(),e);
//            baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SERVER_ERROR;
        } catch (Exception e) {
            // L.e(e.getMessage());
            // e.printStackTrace();
            if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STOCKTRANSACTION)) {
                StockTransactionDataParse.getInstance().isSync = false;
            }
            ZillionLog.i("SERVER_URL1",Constant.SERVER_URL);
            ZillionLog.e("HttpHelp1", e.getMessage(),e);
            baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SERVER_ERROR;
        }

        // 响应正常
        if (baseData.HTTP_STATUS != HttpHelper.HTTP_STATUS_SERVER_ERROR) {
            baseData.responseHeader = response.getAllHeaders();
            try {
                String result = EntityUtils.toString(response.getEntity());

//                ZillionLog.i("response", Constant.WSIDNAMEMAP.get(method) + "==" + result);

                // HttpEntity httpEntity = response.getEntity();
                // InputStream inputStream = httpEntity.getContent();
                // StringBuffer buff = new StringBuffer();
                // BufferedReader reader = new BufferedReader(new
                // InputStreamReader(inputStream));
                // String temp = null;
                // while ((temp = reader.readLine()) != null) {
                // buff.append(temp);
                // }
                // Log.i(className.getClass().toString(),
                // "#################################return start#################################");
                // Log.i(className.getClass().toString(),
                // baseData.getRequestURL() + "=====" + result);
                // Log.i(className.getClass().toString(),
                // "#################################return end  #################################");
                JSONObject object = new JSONObject(result);
                baseData.init(object);

                baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_SUCCESS;// 解析成功

            } catch (Exception e) {
                // e.printStackTrace();
                // L.e(e.getMessage());
                if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STOCKTRANSACTION)) {
                    StockTransactionDataParse.getInstance().isSync = false;
                }
                ZillionLog.i("SERVER_URL2",Constant.SERVER_URL);
                ZillionLog.e("HttpHelp2", e.getMessage(),e);
                baseData.HTTP_STATUS = HttpHelper.HTTP_STATUS_PRASE_ERROR;
            }
        }
        return baseData;
    }
}
