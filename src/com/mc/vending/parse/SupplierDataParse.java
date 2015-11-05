package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.SupplierData;
import com.mc.vending.db.SupplierDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

public class SupplierDataParse implements DataParseListener {
    private static SupplierDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public SupplierDataParse() {
    }

    public static SupplierDataParse getInstance() {
        if (instance == null) {
            instance = new SupplierDataParse();
        }
        return instance;
    }

    /**
     * 网络请求，下载数据
     */
    public void requestSupplierData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>供应商网络请求数据异常!");
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
            if (listener != null) {
                listener.parseRequestFailure(baseData);
            }
            return ;
        }
        // 增量
        List<SupplierData> list = parse(baseData.getData());
        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        SupplierDbOper supplierDbOper = new SupplierDbOper();
        // 测试时加上这句话
        // supplierDbOper.deleteAll();
        Map<String, String> map = supplierDbOper.findAllMap();
        List<SupplierData> addList = new ArrayList<SupplierData>();
        List<SupplierData> updateList = new ArrayList<SupplierData>();
        for (SupplierData supplier : list) {
            if (map.get(supplier.getSp1Id()) == null) {
                // 数据库中不存在
                addList.add(supplier);
            } else {
                updateList.add(supplier);
            }
        }

        if (!addList.isEmpty()) {
            // 批量增加货主
            boolean flag = supplierDbOper.batchAddSupplier(addList);
            if (flag) {
                Log.i("[supplier]:", "======>>>>>供应商批量增加成功!" + addList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[supplier]:", "==========>>>>供应商批量增加失败!");
            }
        } else if (!updateList.isEmpty()) {
            boolean flag_ = supplierDbOper.batchUpdateSupplier(updateList);
            if (flag_) {
                Log.i("[supplier]:", "==========>>>>>供应商批量更新成功!" + "==========" + updateList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                ZillionLog.e("[supplier]:", "==========>>>>>供应商批量更新失败!");
            }
        } else {
            DataParseHelper parseHelper = new DataParseHelper(this);
            parseHelper.sendLogVersion(list.get(0).getLogVersion());
        }

        // System.out.println(supplierDbOper.findAll());
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
    public List<SupplierData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<SupplierData>(0);
        }
        List<SupplierData> list = new ArrayList<SupplierData>();
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

                SupplierData data = new SupplierData();
                data.setSp1Id(jsonObj.getString("ID"));
                data.setSp1M02Id(jsonObj.getString("SP1_M02_ID"));
                data.setSp1Code(jsonObj.getString("SP1_CODE"));
                data.setSp1Name(jsonObj.getString("SP1_Name"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setSp1CreateUser(createUser);
                data.setSp1CreateTime(createTime);
                data.setSp1ModifyUser(modifyUser);
                data.setSp1ModifyTime(modifyTime);
                data.setSp1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>供应商解析数据异常!");
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
