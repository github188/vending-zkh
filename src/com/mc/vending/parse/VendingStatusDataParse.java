package com.mc.vending.parse;

import org.json.JSONObject;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.ZillionLog;

/**
 * 售货机数据请求与解析
 * 
 * @author apple
 *
 */
public class VendingStatusDataParse implements DataParseListener {

    private static VendingStatusDataParse instance = null;

    public VendingStatusDataParse() {
    }

    public static VendingStatusDataParse getInstance() {
        if (instance == null) {
            instance = new VendingStatusDataParse();
        }
        return instance;
    }

    /**
     * 请求回调方法
     */
    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            baseData = null;
            return;
        }
        baseData = null;
        //System.out.println("=============>>调用成功！");
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机联机状态网络请求数据异常!");
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }
}
