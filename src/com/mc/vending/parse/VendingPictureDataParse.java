package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class VendingPictureDataParse implements DataParseListener {

    private static VendingPictureDataParse instance = null;
    private DataParseRequestListener       listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingPictureDataParse() {
    }

    public static VendingPictureDataParse getInstance() {
        if (instance == null) {
            instance = new VendingPictureDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingPictureData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机待机图片网络请求数据异常!");
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
        // 全表
        List<VendingPictureData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }

        VendingPictureDbOper vendingPictureDbOper = new VendingPictureDbOper();
        // 先删除再插入
        boolean deleteFlag = vendingPictureDbOper.deleteAll();
        if (deleteFlag) {
            boolean addFlag = vendingPictureDbOper.batchAddVendingPicture(list);
            if (addFlag) {
//                Log.i("[vendingPicture]:", "======>>>>>售货机待机图片批量增加成功!" + list.size());
                if (list != null && list.size() > 0) {
                    DataParseHelper parseHelper = new DataParseHelper(this);
                    parseHelper.sendLogVersion(list.get(0).getLogVersion());
                }
            } else {
                ZillionLog.e("[vendingPicture]:", "==========>>>>>售货机待机图片批量增加失败!");
            }
        }
        // System.out.println(vendingPictureDbOper.findVendingPicture());

        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }

    }

    //
    // private void sendLogVersion(String logVersion) {
    // JSONObject json = new JSONObject();
    // try {
    // json.put("LogVersionID", logVersion);
    // DataParseHelper helper = new DataParseHelper(this);
    // helper.requestSubmitServer("Update", json,
    // Constant.METHOD_WSID_SYN_LOGVERSION);
    // } catch (Exception e) {
    // e.printStackTrace();
    // Log.i(this.getClass().toString(), "======>>>>>sendLogVersion请求数据异常!");
    // }
    // }

    public List<VendingPictureData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<VendingPictureData>(0);
        }
        List<VendingPictureData> list = new ArrayList<VendingPictureData>();
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

                VendingPictureData data = new VendingPictureData();
                data.setVp2Id(jsonObj.getString("ID"));
                data.setVp2M02Id(jsonObj.getString("VP2_M02_ID"));
                data.setVp2Seq(jsonObj.getString("VP2_Seq"));
                data.setVp2FilePath(jsonObj.getString("VP2_FilePath"));
                data.setVp2RunTime(ConvertHelper.toInt(jsonObj.getString("VP2_RunTime"), 0));
                data.setVp2Type(jsonObj.getString("VP2_Type"));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVp2CreateUser(createUser);
                data.setVp2CreateTime(createTime);
                data.setVp2ModifyUser(modifyUser);
                data.setVp2ModifyTime(modifyTime);
                data.setVp2RowVersion(rowVersion);
                list.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
