package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductData;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class ProductDataParse implements DataParseListener {
    private static ProductDataParse  instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ProductDataParse() {
    }

    public static ProductDataParse getInstance() {
        if (instance == null) {
            instance = new ProductDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestProductData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "==========>>>>>产品网络请求异常!");
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
        // 增量
        List<ProductData> list = parse(baseData.getData());
        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        // 批量增加产品
        ProductDbOper productDbOper = new ProductDbOper();
        Map<String, String> map = productDbOper.findAllProduct();
        List<ProductData> addList = new ArrayList<ProductData>();
        List<ProductData> updateList = new ArrayList<ProductData>();
        for (ProductData product : list) {
            if (map.get(product.getPd1Id()) == null) {
                // 数据库中不存在
                addList.add(product);
            } else {
                updateList.add(product);
            }
        }
        // 测试时加上这句话
        // productDbOper.deleteAll();
        if (!addList.isEmpty()) {
            boolean flag = productDbOper.batchAddProduct(addList);
            if (flag) {
                Log.i("[product]:", "==========>>>>>产品批量增加成功!" + "==========" + addList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[product]:", "==========>>>>>产品批量增加失败!");
            }
        } else if (!updateList.isEmpty()) {
            boolean flag_ = productDbOper.batchUpdateProduct(updateList);
            if (flag_) {
                Log.i("[product]:", "==========>>>>>产品批量更新成功!" + "==========" + updateList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[product]:", "==========>>>>>产品批量更新失败!");
            }
        } else {
            DataParseHelper parseHelper = new DataParseHelper(this);
            parseHelper.sendLogVersion(list.get(0).getLogVersion());
        }
        // System.out.println(productDbOper.findAllProduct());
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
    public List<ProductData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ProductData>(0);
        }
        List<ProductData> list = new ArrayList<ProductData>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null)
                    continue;
                ProductData data = new ProductData();
                data.setPd1Id(jsonObj.getString("ID"));
                data.setPd1M02Id(jsonObj.getString("PD1_M02_ID"));
                data.setPd1Code(jsonObj.getString("PD1_CODE"));
                data.setPd1Name(jsonObj.getString("PD1_Name"));
                data.setPd1ManufactureModel(jsonObj.getString("PD1_ManufactureModel"));
                data.setPd1Size(jsonObj.getString("PD1_Size"));
                data.setPd1Brand(jsonObj.getString("PD1_Brand"));
                data.setPd1Package(jsonObj.getString("PD1_Package"));
                data.setPd1Unit(jsonObj.getString("PD1_Unit"));
                data.setPd1LastImportTime(jsonObj.getString("PD1_LastImportTime"));
                data.setPd1Description(jsonObj.getString("PD1_Description"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "==========>>>>>产品数据解析异常!");
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
