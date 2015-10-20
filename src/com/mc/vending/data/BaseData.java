package com.mc.vending.data;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mc.vending.tools.ConvertHelper;
import com.zillionstar.tools.ZillionLog;

public class BaseData {

    private JSONObject json;

    public Header[]    responseHeader;

    private String     returnCode;
    private String     returnMessage;
    private int        total;
    private String     requestURL;
    private String     optType;
    private boolean    deleteFlag;    // 0全表时不需要删除原数据-false。1表示需要删除-true
    private boolean    isSuccess;
    public int         HTTP_STATUS;
    private Object     userObject;

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public String getReturnCode() {
        try {
            returnCode = json.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    public String getReturnMessage() {
        try {
            returnMessage = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnMessage;
    }

    public Boolean isSuccess() {
        if ("0".equals(getReturnCode())) {
            isSuccess = true;
        }
        return isSuccess;
    }

    public int getTotal() {
        try {
            total = ConvertHelper.toInt(json.getString("total"), 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return total;
    }

    public Boolean getDeleteFlag() {
        try {
            String deleteFlagStr = json.getString("deleteflag");
            if ("0".equals(deleteFlagStr)) {
                deleteFlag = false;
            } else {
                deleteFlag = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return deleteFlag;
    }

    public void init(JSONObject json) {
        this.json = json;
    }

    public JSONArray getData() {
        try {
            if (json.get("data") == null) {
                return null;
            }
            if (json.get("data") instanceof JSONArray) {
                return ((JSONObject) json.getJSONArray("data").get(0)).getJSONArray("rows");
            }
            return json.getJSONObject("data").getJSONArray("rows");
        } catch (JSONException e) {
//            e.printStackTrace();
            ZillionLog.e(e.getMessage());
        }
        return null;
    }

    public JSONArray getWsidData() {
        try {
            return ((JSONObject) json.getJSONArray("data").get(1)).getJSONArray("rows");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转对象
     * 
     * @deprecated
     * @param json
     * @param pojo
     * @return
     * @throws Exception
     */
    @Deprecated
    public static Object fromJsonObjectToBean(JSONObject json, Class pojo) throws Exception {
        // 首先得到pojo所定义的字段
        Field[] fields = pojo.getDeclaredFields();
        // 根据传入的Class动态生成pojo对象
        Object obj = pojo.newInstance();
        for (Field field : fields) {
            // 设置字段可访问（必须，否则报错）
            field.setAccessible(true);
            // 得到字段的属性名
            String name = field.getName();
            // 这一段的作用是如果字段在JSONObject中不存在会抛出异常，如果出异常，则跳过。
            try {
                json.get(name);
            } catch (Exception ex) {
                continue;
            }
            if (json.get(name) != null && !"".equals(json.getString(name))) {
                // 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。
                if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    field.set(obj, Long.parseLong(json.getString(name)));
                } else if (field.getType().equals(String.class)) {
                    field.set(obj, json.getString(name));
                } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                    field.set(obj, Double.parseDouble(json.getString(name)));
                } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    field.set(obj, Integer.parseInt(json.getString(name)));
                } else if (field.getType().equals(java.util.Date.class)) {
                    field.set(obj, Date.parse(json.getString(name)));
                } else {
                    continue;
                }
            }
        }
        return obj;
    }

    @Override
    public String toString() {
        final int maxLen = 10;
        return "BaseData [json="
                + json
                + ", responseHeader="
                + (responseHeader != null ? Arrays.asList(responseHeader).subList(0,
                        Math.min(responseHeader.length, maxLen)) : null) + ", returnCode=" + returnCode
                + ", returnMessage=" + returnMessage + ", total=" + total + ", requestURL=" + requestURL
                + ", optType=" + optType + ", deleteFlag=" + deleteFlag + ", isSuccess=" + isSuccess
                + ", HTTP_STATUS=" + HTTP_STATUS + ", userObject=" + userObject + "]";
    }

}
