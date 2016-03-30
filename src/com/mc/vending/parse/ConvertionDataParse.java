/**
 * 
 */
package com.mc.vending.parse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ConversionData;
import com.mc.vending.db.ConversionDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;

import android.util.Log;

/**
 * @author junjie.you
 *
 */
public class ConvertionDataParse implements DataParseListener {
	private static ConvertionDataParse instance = null;
	private DataParseRequestListener listener;
	
	   public DataParseRequestListener getListener() {
	        return listener;
	    }

	    public void setListener(DataParseRequestListener listener) {
	        this.listener = listener;
	    }

	    public ConvertionDataParse() {
	    }

	    public static ConvertionDataParse getInstance() {
	        if (instance == null) {
	            instance = new ConvertionDataParse();
	        }
	        return instance;
	    }

	    /**
	     * 网络请求,下载数据
	     */
	    public void requestConvertionData(String optType, String requestURL, String cn1Id) {
	        Log.i(this.getClass().getName(), cn1Id);
	        JSONObject json = new JSONObject();
	        try {
	            json.put("VD1_ID", cn1Id);
	            DataParseHelper helper = new DataParseHelper(this);
	            helper.requestSubmitServer(optType, json, requestURL);
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.i(this.getClass().toString(), "======>>>>>单位转换关系表网络请求数据异常!");
	        }
	    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.DataParseListener#parseJson(com.mc.vending.
	 * data.BaseData)
	 */
	@Override
	public void parseJson(BaseData baseData) {
		// TODO Auto-generated method stub
		 Log.i(this.getClass().getName(), baseData.toString());
	        if (!baseData.isSuccess()) {
	            if (this.listener != null) {
	                this.listener.parseRequestFailure(baseData);
	            }
	            return;
	        }
	        // 全表
	        List<ConversionData> list = parse(baseData.getData());
	        // 0全表时不需要删除原数据-false。1表示需要删除true
	        if (list.isEmpty() && !baseData.getDeleteFlag()) {
	            if (this.listener != null) {
	                this.listener.parseRequestFinised(baseData);
	            }
	            return;
	        }
	        ConversionDbOper conversion = new ConversionDbOper();
	        List<String> dbPra = new ArrayList<String>();
	        for (ConversionData data : list) {
	            dbPra.add(data.getCn1Upid() + "," + data.getCn1Cpid());
	        }
	        boolean deleteFlag = conversion.batchDeleteConversion(dbPra);
	        if (deleteFlag) {
	            Log.i("[Conversion]:", "转换关系批量删除成功!" + "======" + dbPra);
	            boolean addFlag = conversion.batchAddConversionData(list);
	            if (addFlag) {
	                Log.i("[Conversion]:", "转换关系批量增加成功!" + "======" + list.size());
	                DataParseHelper parseHelper = new DataParseHelper(this);
	                parseHelper.sendLogVersion(list.get(0).getLogVersion());
	            } else {
	                Log.i("[Conversion]:", "转换关系批量增加失败!");
	            }
	        }
	        // System.out.println(cardDbOper.findAll());

	        if (this.listener != null) {
	            this.listener.parseRequestFinised(baseData);
	        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mc.vending.parse.listener.DataParseListener#parseRequestError(com.mc.
	 * vending.data.BaseData)
	 */
	@Override
	public void parseRequestError(BaseData baseData) {
		// TODO Auto-generated method stub
		 if (listener != null) {
	            listener.parseRequestFailure(baseData);
	        }
	}
	
	/**
     * 数据解析
     * 
     * @param jsonArray
     * @return
     */
    public List<ConversionData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList<ConversionData>(0);
        }
        List<ConversionData> list = new ArrayList<ConversionData>();
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

                ConversionData data = new ConversionData();
                data.setCn1Id(jsonObj.getString("ID"));
                data.setCn1Upid(jsonObj.getString("CN1_Upid"));
                data.setCn1Cpid(jsonObj.getString("CN1_Cpid"));
                data.setCn1Proportion(jsonObj.getString("CN1_Proportion"));
                data.setCn1Operation(jsonObj.getString("CN1_Operation"));
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
            Log.i(this.getClass().toString(), "======>>>>>数据转换解析网络数据异常!");
        }
        return list;
    }

}
