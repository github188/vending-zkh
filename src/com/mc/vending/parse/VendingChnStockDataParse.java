package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class VendingChnStockDataParse implements DataParseListener {
    private static VendingChnStockDataParse instance = null;
    private DataParseRequestListener        listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingChnStockDataParse() {
    }

    public static VendingChnStockDataParse getInstance() {
        if (instance == null) {
            instance = new VendingChnStockDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingChnStockData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机货道库存网络请求数据异常!");
        }
    }

    @Override
    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }
        if (baseData==null || baseData.getData() == null || baseData.getData().length()==0) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return ;
        }
        //全表
        List<VendingChnStockData> list = parse(baseData.getData());
        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
        boolean deleteFlag = vendingChnStockDbOper.deleteAll();
        if (deleteFlag) {
            //批量增加售货机货道记录与售货机货道库存记录
            boolean addflag = vendingChnStockDbOper.batchAddVendingChnStock(list);
            if (addflag) {
                Log.i("[vendingChnStock]:", "======>>>>>售货机货道库存批量增加成功!" + list.size());

                if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) > 218) {
                    DataParseHelper parseHelper = new DataParseHelper(this);
                    parseHelper.sendLogVersion(list.get(0).getLogVersion());
                }
                
            } else {
                ZillionLog.e("[vendingChnStock]:", "==========>>>>>售货机货道库存批量增加失败!");
            }
        }

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    public List<VendingChnStockData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<VendingChnStockData>(0);
        }
        List<VendingChnStockData> list = new ArrayList<VendingChnStockData>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null)
                    continue;

                String createUser = jsonObj.getString("CreateUser");
                String createTime = jsonObj.getString("CreateTime");
                String modifyUser = jsonObj.getString("ModifyUser");
                String modifyTime = jsonObj.getString("ModifyTime");
                String rowVersion = new Date().getTime() + "";

                VendingChnStockData data = new VendingChnStockData();
                data.setVs1Id(jsonObj.getString("ID"));
                data.setVs1M02Id(jsonObj.getString("VS1_M02_ID"));
                data.setVs1Vd1Id(jsonObj.getString("VS1_VD1_ID"));
                data.setVs1Vc1Code(jsonObj.getString("VS1_VC1_CODE"));
                data.setVs1Pd1Id(jsonObj.getString("VS1_PD1_ID"));
                data.setVs1Quantity(ConvertHelper.toInt(jsonObj.getString("VS1_Quantity"), 0));
                
                if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) > 218) {
                    data.setLogVersion(jsonObj.getString("LogVision"));
                }
                data.setVs1CreateUser(createUser);
                data.setVs1CreateTime(createTime);
                data.setVs1ModifyUser(modifyUser);
                data.setVs1ModifyTime(modifyTime);
                data.setVs1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机货道库存解析数据异常!");
        }
        return list;
    }

    @Override
    public void parseRequestError(BaseData baseData) {
        if (listener != null) {
            listener.parseRequestFailure(baseData);
        }

    }
}
