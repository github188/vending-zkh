package com.mc.vending.parse;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.ZillionLog;

public class UsedRecordUploadDataParse implements DataParseListener {
    private static UsedRecordUploadDataParse instance = null;

    public UsedRecordUploadDataParse() {
    }

    public static UsedRecordUploadDataParse getInstance() {
        if (instance == null) {
            instance = new UsedRecordUploadDataParse();
        }
        return instance;
    }

    /**
     * @param optType
     * @param requestURL
     * @param vendingId
     */
    public void requestStockTransactionData(String optType, String requestURL, String vendingId, String cardId) {
        JSONArray jsonArray = null;
        List<UsedRecordData> datas = new UsedRecordDbOper().findDataToUpload();

        try {
            jsonArray = new JSONArray();
            for (UsedRecordData data : datas) {
                JSONObject json = new JSONObject();
                json.put("ID", data.getUr1ID());
                jsonArray.put(json);
            }
            if (jsonArray.length() > 0) {
                DataParseHelper helper = new DataParseHelper(this);
                helper.requestSubmitServer(optType, jsonArray, requestURL, datas);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            baseData = null;
            return;
        }
//        if (baseData==null || baseData.getData() == null || baseData.getData().length()==0) {
//            return ;
//        }
        List<StockTransactionData> datas = (List<StockTransactionData>) baseData.getUserObject();
        if (datas.isEmpty()) {
            baseData = null;
            return;
        }
        boolean flag = new StockTransactionDbOper().batchUpdateUploadStatus(datas);
        if (flag) {
            Log.i("[stockTransaction]:", "======>>>>>库存交易记录上传状态批量更新成功!" + datas.size());
        } else {
            ZillionLog.e("[stockTransaction]:", "==========>>>>库存交易记录上传状态批量增加失败!");
        }
        baseData = null;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        // TODO Auto-generated method stub

    }

}
