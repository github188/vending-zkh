package com.mc.vending.parse;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class StockTransactionDataParse implements DataParseListener {
    private static StockTransactionDataParse instance = null;
    private DataParseRequestListener        listener;
    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public boolean isSync = false;

    public StockTransactionDataParse() {
    }

    public static StockTransactionDataParse getInstance() {
        if (instance == null) {
            instance = new StockTransactionDataParse();
        }
        return instance;
    }

    /**
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestStockTransactionData(String optType, String requestURL, String vendingId) {

        isSync = true;
        ZillionLog.i(this.getClass().getName(),"上传交易记录01："+isSync);
        JSONArray jsonArray = null;
        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();

        try {
            jsonArray = new JSONArray();
            for (StockTransactionData data : datas) {
                JSONObject json = new JSONObject();
                json.put("ID", data.getTs1Id());
                json.put("TS1_M02_ID", data.getTs1M02Id());
                json.put("TS1_BillType", data.getTs1BillType());
                json.put("TS1_BillCode", data.getTs1BillCode());
                json.put("TS1_CD1_ID", data.getTs1Cd1Id());
                json.put("TS1_VD1_ID", data.getTs1Vd1Id());
                json.put("TS1_PD1_ID", data.getTs1Pd1Id());
                json.put("TS1_VC1_CODE", data.getTs1Vc1Code());
                json.put("TS1_TransQty", data.getTs1TransQty());
                json.put("TS1_TransType", data.getTs1TransType());
                json.put("TS1_SP1_CODE", data.getTs1Sp1Code());
                json.put("TS1_SP1_Name", data.getTs1Sp1Name());
                json.put("CreateUser", data.getTs1CreateUser());
                json.put("CreateTime", data.getTs1CreateTime());
                jsonArray.put(json);
            }
            if (jsonArray.length() > 0) {
                ZillionLog.i(this.getClass().getName(),"上传交易记录02："+isSync);
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
            } else {
                isSync = false;
                ZillionLog.i(this.getClass().getName(),"上传交易记录03："+isSync);
            }
        } catch (Exception e) {
            isSync = false;
            ZillionLog.i(this.getClass().getName(),"上传交易记录04："+isSync);
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }
    

    /**
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestStockTransactionData(String optType, String requestURL, String vendingId,List<StockTransactionData> datas) {

        //ZillionLog.i(this.getClass().getName(),"上传交易记录11："+isSync);
        isSync = true;
        ZillionLog.i(this.getClass().getName(),"上传交易记录12："+isSync);
        JSONArray jsonArray = null;
//        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();

        try {
            jsonArray = new JSONArray();
            for (StockTransactionData data : datas) {
                JSONObject json = new JSONObject();
                json.put("ID", data.getTs1Id());
                json.put("TS1_M02_ID", data.getTs1M02Id());
                json.put("TS1_BillType", data.getTs1BillType());
                json.put("TS1_BillCode", data.getTs1BillCode());
                json.put("TS1_CD1_ID", data.getTs1Cd1Id());
                json.put("TS1_VD1_ID", data.getTs1Vd1Id());
                json.put("TS1_PD1_ID", data.getTs1Pd1Id());
                json.put("TS1_VC1_CODE", data.getTs1Vc1Code());
                json.put("TS1_TransQty", data.getTs1TransQty());
                json.put("TS1_TransType", data.getTs1TransType());
                json.put("TS1_SP1_CODE", data.getTs1Sp1Code());
                json.put("TS1_SP1_Name", data.getTs1Sp1Name());
                json.put("CreateUser", data.getTs1CreateUser());
                json.put("CreateTime", data.getTs1CreateTime());
                jsonArray.put(json);
            }
            if (jsonArray.length() > 0) {
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
            } else {
                isSync = false;
                ZillionLog.i(this.getClass().getName(),"上传交易记录13："+isSync);
            }
        } catch (Exception e) {
            isSync = false;
            ZillionLog.i(this.getClass().getName(),"上传交易记录14："+isSync);
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    

    @SuppressWarnings("unchecked")
    @Override
    public void parseJson(BaseData baseData) {
        ZillionLog.i(this.getClass().getName(),baseData);
        ZillionLog.i(this.getClass().getName(),"上传交易记录24："+isSync);
        
        if (!baseData.isSuccess()) {
            
            ZillionLog.i(this.getClass().getName(),"上传交易记录22："+isSync);
            
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            baseData = null;
            isSync = false;
            return;
        }
        
        ZillionLog.i(this.getClass().getName(),"上传交易记录23："+isSync);
        
        List<StockTransactionData> datas = (List<StockTransactionData>) baseData.getUserObject();
        if (datas.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            baseData = null;
            isSync = false;
            return;
        }
        boolean flag = new StockTransactionDbOper().batchUpdateUploadStatus(datas);
        if (flag) {
            Log.i("[stockTransaction]:", "======>>>>>库存交易记录上传状态批量更新成功!" + datas.size());
        } else {
            ZillionLog.e("[stockTransaction]:", "==========>>>>库存交易记录上传状态批量增加失败!");
        }
//        baseData = null;
        isSync = false;
        ZillionLog.i(this.getClass().getName(),"上传交易记录："+isSync);

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }

}
