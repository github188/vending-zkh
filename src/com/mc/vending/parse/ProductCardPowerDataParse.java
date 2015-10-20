package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.db.ProductCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class ProductCardPowerDataParse implements DataParseListener {
    private static ProductCardPowerDataParse instance = null;
    private DataParseRequestListener         listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ProductCardPowerDataParse() {
    }

    public static ProductCardPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductCardPowerDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestProductCardPowerData(String optType, String requestURL, String vendingId) {
        Log.i(this.getClass().getName(), vendingId);
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>卡与产品权限网络请求数据异常!");
        }
    }

    @Override
    public void parseJson(BaseData baseData) {
        Log.i(this.getClass().getName(), baseData.toString());
        if (!baseData.isSuccess()) {
            if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
            return;
        }
        // 全表
        List<ProductCardPowerData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        ProductCardPowerDbOper cardDbOper = new ProductCardPowerDbOper();
        List<String> dbPra = new ArrayList<String>();
        for (ProductCardPowerData data : list) {
            dbPra.add(data.getPc1CD1_ID() + "," + data.getPc1VD1_ID());
        }
        boolean deleteFlag = cardDbOper.batchDeleteVendingCard(dbPra);
        if (deleteFlag) {
            Log.i("[ProductCardPower]:", "卡与产品权限批量删除成功!" + "======" + dbPra);
            boolean addFlag = cardDbOper.batchAddProductCardPower(list);
            if (addFlag) {
                Log.i("[ProductCardPower]:", "卡与产品权限批量增加成功!" + "======" + list.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[ProductCardPower]:", "卡与产品权限批量增加失败!");
            }
        }
        // System.out.println(cardDbOper.findAll());

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    /**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public List<ProductCardPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ProductCardPowerData>(0);
        }
        List<ProductCardPowerData> list = new ArrayList<ProductCardPowerData>();
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

                ProductCardPowerData data = new ProductCardPowerData();
                data.setPc1ID(jsonObj.getString("ID"));
                data.setPc1M02_ID(jsonObj.getString("PC1_M02_ID"));
                data.setPc1VD1_ID(jsonObj.getString("PC1_VD1_ID"));
                data.setPc1CU1_ID(jsonObj.getString("PC1_CU1_ID"));
                data.setPc1CD1_ID(jsonObj.getString("PC1_CD1_ID"));
                data.setPc1VP1_ID(jsonObj.getString("PC1_PD1_ID"));
                data.setPc1Power(jsonObj.getString("PC1_Power"));
                data.setPc1OnceQty(jsonObj.getString("PC1_OnceQty"));
                data.setPc1Period(jsonObj.getString("PC1_Period"));
                data.setPc1IntervalStart(jsonObj.getString("PC1_IntervalStart"));
                data.setPc1IntervalFinish(jsonObj.getString("PC1_IntervalFinish"));
                data.setPc1StartDate(jsonObj.getString("PC1_StartDate"));
                data.setPc1PeriodQty(jsonObj.getString("PC1_PeriodQty"));

                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setCreateUser(createUser);
                data.setCreateTime(createTime);
                data.setModifyUser(modifyUser);
                data.setModifyTime(modifyTime);
                data.setRowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>卡与产品权限解析网络数据异常!");
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
