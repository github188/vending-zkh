package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.db.ProductPictureDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

public class ProductPictureDataParse implements DataParseListener {
    private static ProductPictureDataParse instance = null;
    private DataParseRequestListener       listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public ProductPictureDataParse() {
    }

    public static ProductPictureDataParse getInstance() {
        if (instance == null) {
            instance = new ProductPictureDataParse();
        }
        return instance;
    }

    /**
     * 网络请求，下载数据
     */
    public void requestProductPictureData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>产品图片网络请求数据异常!");
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
        List<ProductPictureData> list = parse(baseData.getData());
        if (list.isEmpty()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
        ProductPictureDbOper productPictureDbOper = new ProductPictureDbOper();
        // 测试时加上这句话
        // productPictureDbOper.deleteAll();

        Map<String, String> map = productPictureDbOper.findAllMap();
        List<ProductPictureData> addList = new ArrayList<ProductPictureData>();
        List<ProductPictureData> updateList = new ArrayList<ProductPictureData>();
        for (ProductPictureData productPicture : list) {
            if (map.get(productPicture.getPp1Id()) == null) {
                // 数据库中不存在
                addList.add(productPicture);
            } else {
                updateList.add(productPicture);
            }
        }
        if (!addList.isEmpty()) {
            // 批量增加产品图片
            boolean flag = productPictureDbOper.batchAddProductPicture(addList);
            if (flag) {
                Log.i("[productPicture]:", "======>>>>>产品图片批量增加成功!" + "==========" + addList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[productPicture]:", "==========>>>>>产品图片批量增加失败!");
            }
        } else if (!updateList.isEmpty()) {
            boolean flag_ = productPictureDbOper.batchUpdateProductPicture(updateList);
            if (flag_) {
                Log.i("[productPicture]:", "==========>>>>>产品图片批量更新成功!" + "==========" + updateList.size());
                DataParseHelper parseHelper = new DataParseHelper(this);
                parseHelper.sendLogVersion(list.get(0).getLogVersion());
            } else {
                Log.i("[productPicture]:", "==========>>>>>产品图片批量更新失败!");
            }
        } else {
            DataParseHelper parseHelper = new DataParseHelper(this);
            parseHelper.sendLogVersion(list.get(0).getLogVersion());
        }

        // System.out.println(productPictureDbOper.findAll());
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
    public List<ProductPictureData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ProductPictureData>(0);
        }
        List<ProductPictureData> list = new ArrayList<ProductPictureData>();
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
                ProductPictureData data = new ProductPictureData();
                data.setPp1Id(jsonObj.getString("ID"));
                data.setPp1M02Id(jsonObj.getString("PP1_M02_ID"));
                data.setPp1Pd1Id(jsonObj.getString("PP1_PD1_ID"));
                data.setPp1FilePath(jsonObj.getString("PP1_FilePath"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setPp1CreateUser(createUser);
                data.setPp1CreateTime(createTime);
                data.setPp1ModifyUser(modifyUser);
                data.setPp1ModifyTime(modifyTime);
                data.setPp1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(this.getClass().toString(), "======>>>>>产品图片解析数据异常!");
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
