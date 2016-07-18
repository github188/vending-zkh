package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.VendingProLinkData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.VendingProLinkDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

public class VendingProLinkDataParse implements DataParseListener {
    private static VendingProLinkDataParse instance = null;
    private DataParseRequestListener       listener;

    public DataParseRequestListener getListener() {
        return listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public VendingProLinkDataParse() {
    }

    public static VendingProLinkDataParse getInstance() {
        if (instance == null) {
            instance = new VendingProLinkDataParse();
        }
        return instance;
    }

    /**
     * 网络请求,下载数据
     */
    public void requestVendingProLinkData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            DataParseHelper helper = new DataParseHelper(this);
            helper.requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机产品网络请求数据异常!");
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
        List<VendingProLinkData> list = parse(baseData.getData());
        // 0全表时不需要删除原数据-false。1表示需要删除true
        if (list.isEmpty() && !baseData.getDeleteFlag()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
            return;
        }
		List<VendingProLinkData> addList = new ArrayList<VendingProLinkData>();
		List<VendingProLinkData> updateList = new ArrayList<VendingProLinkData>();
		List<VendingProLinkData> deleteList = new ArrayList<VendingProLinkData>();
		for (VendingProLinkData vendingProLinkData : list) {
			if (vendingProLinkData.getCRUD().equals("D")) {
				deleteList.add(vendingProLinkData);
			} else if (vendingProLinkData.getCRUD().equals("C")) {
				addList.add(vendingProLinkData);
			} else if (vendingProLinkData.getCRUD().equals("U")) {
				updateList.add(vendingProLinkData);
			}
		}
		VendingProLinkDbOper vendingProLinkDbOper = new VendingProLinkDbOper();
		if (!addList.isEmpty()) {
			boolean addFlag = vendingProLinkDbOper.batchAddVendingProLink(addList);
			if (addFlag) {
				Log.i("[vendingProLink]:", "售货机产品批量增加成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[vendingProLink]:", "售货机产品批量增加失败!");
			}
		}
		if (!deleteList.isEmpty()) {
			boolean deleteFlag = vendingProLinkDbOper.batchDeleteVendingProLink(deleteList);
			if (deleteFlag) {
				Log.i("[vendingProLink]:", "售货机产品批量删除成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[vendingProLink]:", "售货机产品批量删除失败!");
			}
		}
		if (!updateList.isEmpty()) {
			boolean deleteFlag = vendingProLinkDbOper.batchDeleteVendingProLink(updateList);
			boolean addFlag = vendingProLinkDbOper.batchAddVendingProLink(updateList);
			if (deleteFlag && addFlag) {
				Log.i("[vendingProLink]:", "售货机产品批量修改成功!" + "======" + list.size());
				DataParseHelper parseHelper = new DataParseHelper(this);
				parseHelper.sendLogVersion(list.get(0).getLogVersion());
			} else {
				ZillionLog.e("[vendingProLink]:", "售货机产品批量修改失败!");
			}
		}
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
    public List<VendingProLinkData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<VendingProLinkData>(0);
        }
        List<VendingProLinkData> list = new ArrayList<VendingProLinkData>();
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

                VendingProLinkData data = new VendingProLinkData();
                data.setVp1Id(jsonObj.getString("ID"));
                data.setCRUD(jsonObj.getString("CRUD"));
                data.setVp1M02Id(jsonObj.getString("VP1_M02_ID"));
                data.setVp1Vd1Id(jsonObj.getString("VP1_VD1_ID"));
                data.setVp1Pd1Id(jsonObj.getString("VP1_PD1_ID"));
                data.setVp1PromptValue(ConvertHelper.toInt(jsonObj.getString("VP1_PromptValue"), 0));
                data.setVp1WarningValue(ConvertHelper.toInt(jsonObj.getString("VP1_WarningValue"), 0));
                data.setLogVersion(jsonObj.getString("LogVision"));
                data.setVp1CreateUser(createUser);
                data.setVp1CreateTime(createTime);
                data.setVp1ModifyUser(modifyUser);
                data.setVp1ModifyTime(modifyTime);
                data.setVp1RowVersion(rowVersion);
                list.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(this.getClass().toString(), "======>>>>>售货机产品解析数据异常!");
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
