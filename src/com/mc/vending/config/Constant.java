/**  
 * Filename:    Parameter.java     
 * Copyright:   Copyright (c)2011 
 * @author:     wangfang  
 * @version:    1.0.0
 * Create at:   2011-6-20  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2011-6-20    wangfang             1.0.0        1.0.0 Version  
 */
package com.mc.vending.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangfang
 * 
 */
public class Constant {

    // public final static String SERVER_URL =
    // "http://172.16.18.144:10011/WSRR.ashx"; //测试

    public final static String              APP_NAME                                     = "Vending";                             // app名称
    public static String                    SERVER_URL                                   = "";                                    // 服务器线上地址
    public final static String              HEADER_KEY_CONTENT_TYPE                      = "Content-Type";                        // content-type
    public final static String              HEADER_VALUE_CONTENT_TYPE                    = "application/x-www-form-urlencoded";   // content-type

    public final static String              HEADER_KEY_CLIENTVER                         = "client_version";                      
    //版本号，同时修改AndroidManifest.xml，注意保持3位数的版本号
    public final static String              HEADER_VALUE_CLIENTVER                       = "2.4.1";                               
    public final static int                 VERSION_STOCK_SYNC                           = 250;                               
    public final static String              DOWNLOAD_URL                                 = "/mnt/sdcard0/Download/";                               
//    public final static String              DOWNLOAD_URL                                 = "/mnt/sdcard/Download/";                               

    public final static String              BODY_KEY_METHOD                              = "wsid";                                // method
    public final static String              BODY_KEY_UDID                                = "deviceid";                            // udid
    public final static String              BODY_KEY_APP                                 = "app";                                 // platform
    public final static String              BODY_KEY_USER                                = "user";                                // platform
    public final static String              BODY_KEY_PWD                                 = "pwd";                                 // platform

    public final static String              BODY_PARAM_KEY_DATA                          = "data";                                // data
    public final static String              BODY_XML_INPUT                               = "data";                                // data

    public final static String              BODY_VALUE_METHOD                            = "";                                    // method
    public static String                    BODY_VALUE_UDID                              = "ddddddd";                             // udid
    public final static String              BODY_VALUE_APP                               = "evmandroid";                          // platform
    public final static String              BODY_VALUE_USER                              = "";                                    // platform
    public static String                    BODY_VALUE_PWD                               = "T6R4BL8D";                            // platform

    public static final String              SHARED_PWD                                   = "pwd";                                 // 存放pwdKey
    public static final String              SHARED_PWD_KEY                               = "pwd_key";                             // 存放pwdKey
    public static final String              SHARED_VEND_CODE                             = "vend_code";                           // 存放pwdKey
    public static final String              SHARED_VEND_DEBUG_STATUS                     = "vend_debug_status";                           // 存放pwdKey
    public static final String              SHARED_VEND_CODE_KEY                         = "vend_code_key";                       // 存放pwdKey
    public static final String              SHARED_CONFIG                                = "config";                              // 存放服务器地址
    public static final String              SHARED_CONFIG_URL                            = "config_url";                          // 存放服务器地址
    public static final String              SHARED_CONFIG_DB_URL                         = "config_db";
    public final static String              DES_VI                                       = "ZKH12345";                            // 加密全局配置常量
    public final static long                TIME_INTERNAL                                = 7000;                                  // 发送指令间隔时间毫秒

    public final static String              HTTP_OPERATE_TYPE_DESGET                     = "DESGet";                              // HTTP获取DES类型
    public final static String              HTTP_OPERATE_TYPE_GETDATA                    = "GetData";                             // HTTP获取数据类型
    public final static String              HTTP_OPERATE_TYPE_UPDATESTATUS               = "UpdateStatus";                        // HTTP更新类型
    public final static String              HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY = "UpdateDetailDifferentiaQty";          // HTTP更新补货差异类型
    public final static String              HTTP_OPERATE_TYPE_INSERT                     = "Insert";                              // HTTP增加类型
    public final static String              HTTP_OPERATE_TYPE_CHECK                      = "CheckRestStatus";                     // HTTP检查类型

    // **************************************具体方法名称**************************************//
    public final static String              METHOD_WSID_PASSWORD                         = "2f744c20-e5d9-4392-9ac2-1f4a6c7bdd00"; // 超级密码接口
    public final static String              METHOD_WSID_CHECK_INIT                       = "71526d0a-7a6e-434d-b226-0a04feffe4ec"; // 检查是否可以初始化
    public final static String              METHOD_WSID_SYNDATA                          = "6e2164cc-45a9-46a4-85a4-b6e3377ebc33"; // 数据同步时请求接口
    public final static String              METHOD_WSID_VERSION                          = "e0a9f1eb-180b-4d3a-956b-e6df0b394a0f"; // 3.4
                                                                                                                                   // 版本信息接口
    public final static String              METHOD_WSID_AUTHER                           = "d5dd9d99-5fd3-4973-a4ad-ee6d516ca537"; // 3.4
                                                                                                                                   // 认证准入接口
    public final static String              METHOD_WSID_CONFIG                           = "5da83d05-e941-4059-ad49-85ebf2d32de3"; // 3.5
                                                                                                                                   // 接口配置接口
    public final static String              METHOD_WSID_VENDING                          = "0f1e740c-c41a-484f-afe3-8e7f2eff99ee"; // 3.6
                                                                                                                                   // 售货机接口
    public final static String              METHOD_WSID_VENDINGPICTURE                   = "c6c1dc3d-95f4-4e2f-80e8-6e3505d03895"; // 3.7
                                                                                                                                   // 待机界面图片接口
    public final static String              METHOD_WSID_VENDINGCHN                       = "7698adeb-d59b-4eb5-ba20-d635f988fa7c"; // 3.8
                                                                                                                                   // 售货机货道接口
    public final static String              METHOD_WSID_VENDINGPROLINK                   = "7395899e-a13b-4de9-8d6b-d48d09a39915"; // 3.9
                                                                                                                                   // 售货机产品接口
    public final static String              METHOD_WSID_PRODUCT                          = "c81e6175-a15c-47b8-a3e2-a6c2fbf9d98b"; // 3.10
                                                                                                                                   // 产品接口
    public final static String              METHOD_WSID_PRODUCTPICTURE                   = "0cec0063-a032-4a37-aa45-889d554023d8"; // 3.11
                                                                                                                                   // 产品图片接口
    public final static String              METHOD_WSID_SUPPLIER                         = "66b91d60-808b-4109-9dc2-2b9f08349bee"; // 3.12
                                                                                                                                   // 货主接口
    public final static String              METHOD_WSID_STATION                          = "72be83fe-24ca-4bf5-9851-248c3391d67a"; // 3.13
                                                                                                                                   // 站点接口
    public final static String              METHOD_WSID_VENDINGCARDPOWER                 = "c2da7bde-f824-4066-bf93-fcdf94690ac0"; // 3.14
                                                                                                                                   // 售货机卡/密码权限
    public final static String              METHOD_WSID_PRODUCTMATERIAKPOWER             = "f821ae2c-1f02-4e6c-a7c4-e11661b4da60"; // 3.15
                                                                                                                                   // 产品领料权限接口
    public final static String              METHOD_WSID_CARD                             = "4f4e0f23-9b81-4b82-885d-6b9e07ebec18"; // 3.16
                                                                                                                                   // 卡/密码接口
    public final static String              METHOD_WSID_CUSEMPCARDPOWER                  = "be98e646-5160-49ac-a986-05cd073393f7"; // 3.17
                                                                                                                                   // 客户员工卡/密码权限接口
    public final static String              METHOD_WSID_CUSTOMEREMPLINK                  = "f42b379f-b3c9-4b09-96e1-4bd21c05ae7f"; // 3.18
                                                                                                                                   // 客户员工接口
    public final static String              METHOD_WSID_CUSTOMER                         = "5090a50f-fa68-4691-a0bb-af1b38676500"; // 3.19
                                                                                                                                   // 客户接口
    public final static String              METHOD_WSID_PRODUCTGROUP                     = "de22c9b4-1be2-4301-a3cc-36c66a7aa9da"; // 3.20
                                                                                                                                   // 产品组合接口
    public final static String              METHOD_WSID_PRODUCTGROUPPOWER                = "e8030392-15d7-467f-97ef-897a59dc039a"; // 3.21
                                                                                                                                   // 产品组合权限接口
    public final static String              METHOD_WSID_REPLENISHMENT                    = "7f342da0-05be-4f3a-96c3-28072ec31e7a"; // 3.22
                                                                                                                                   // 补货单接口
    public final static String              METHOD_WSID_VENDING_STSATUS                  = "badc8989-320d-4122-8b40-1e5b4ec2541c"; // 3.23
                                                                                                                                   // 售货机在线状态更新接口
    public final static String              METHOD_WSID_REPLENISHMENT_STATUS             = "7f342da0-05be-4f3a-96c3-28072ec31e7a"; // 3.24
                                                                                                                                   // 补货单状态更新接口
    public final static String              METHOD_WSID_REPLENISHMENT_DIFF               = "7f342da0-05be-4f3a-96c3-28072ec31e7a"; // 3.25
                                                                                                                                   // 补货单差异接口
    public final static String              METHOD_WSID_INVENTORY                        = "7774bf40-e172-4daf-88cb-e7cecfdc86bd"; // 3.26
                                                                                                                                   // 盘点记录接口
    public final static String              METHOD_WSID_STOCKTRANSACTION                 = "f5051735-42a1-4003-afbf-b18b3d87c8f0"; // 3.27
                                                                                                                                   // 库存交易记录
    public final static String              METHOD_WSID_SYN_STOCK                        = "b7e4d092-5c3c-4f73-b793-caf197826340"; // 3。38
                                                                                                                                   // 同步库存
    public final static String              METHOD_WSID_SYN_LOGVERSION                   = "c7aff256-e10b-4ee1-bd2e-f3670208aed1"; // 3.39
                                                                                                                                   // APP接口调用返回
    public final static String              METHOD_WSID_RETURNS_FORWARD                  = "ae0ffec0-8af0-468f-9e11-2ec7bd95cc3c"; // 3.40退货单
    public final static String              METHOD_WSID_PRODUCTCARDPOWER                 = "610cc826-5da6-4d94-83e2-2b839f5a0299"; // 3.41卡产品权限
    public final static String              METHOD_WSID_USEDRECORD                       = "c3d76872-6905-4231-821f-67f575cf0e07"; // 3.42卡产品领用
    public final static String              METHOD_WSID_VENDINGRUNERROR                  = "564000F1-D704-476B-8E1B-D0218668B712"; // 3.43错误日志
    
    public final static String              METHOD_WSID_CONVERSION                       = "B090547D-CF3A-4F71-8121-1E22C8B7D093"; //单位换算关系表    

    // APP接口调用返回
    // 接口返回状态
    public final static String              RETURNCODE_SUCCESS                           = "0";                                   // 成功
    public final static String              SERVICE_TEL                                  = "4006809696";
    // 期间设置常量
    public static final int                 YEAR                                         = 0;                                     // 年
    public static final int                 MONTH                                        = 1;                                     // 月
    public static final int                 DAY                                          = 2;                                     // 日
    public static final int                 HOUR                                         = 3;                                     // 小时
    public static final int                 TIME                                         = 4;                                     // 时间段

    public static final int                 REQUEST_TIMEOUT                              = 30 * 1000;                             //设置请求超时10秒钟  
    public static final int                 SO_TIMEOUT                                   = 30 * 1000;                             //设置等待数据超时时间10秒钟  

    public final static Map<String, String> WSIDNAMEMAP                                  = new HashMap<String, String>() {
                                                                                             private static final long serialVersionUID = 1L;
                                                                                             {
                                                                                                 put("2f744c20-e5d9-4392-9ac2-1f4a6c7bdd00",
                                                                                                         "超级密码接口");
                                                                                                 put("71526d0a-7a6e-434d-b226-0a04feffe4ec",
                                                                                                         "检查是否可以初始化");
                                                                                                 put("6e2164cc-45a9-46a4-85a4-b6e3377ebc33",
                                                                                                         "数据同步时请求接口");
                                                                                                 put("e0a9f1eb-180b-4d3a-956b-e6df0b394a0f",
                                                                                                         "3.4版本信息接口");
                                                                                                 put("d5dd9d99-5fd3-4973-a4ad-ee6d516ca537",
                                                                                                         "3.4认证准入接口");
                                                                                                 put("5da83d05-e941-4059-ad49-85ebf2d32de3",
                                                                                                         "3.5接口配置接口");
                                                                                                 put("0f1e740c-c41a-484f-afe3-8e7f2eff99ee",
                                                                                                         "3.6售货机接口");
                                                                                                 put("c6c1dc3d-95f4-4e2f-80e8-6e3505d03895",
                                                                                                         "3.7待机界面图片接口");
                                                                                                 put("7698adeb-d59b-4eb5-ba20-d635f988fa7c",
                                                                                                         "3.8售货机货道接口");
                                                                                                 put("7395899e-a13b-4de9-8d6b-d48d09a39915",
                                                                                                         "3.9售货机产品接口");
                                                                                                 put("c81e6175-a15c-47b8-a3e2-a6c2fbf9d98b",
                                                                                                         "3.10产品接口");
                                                                                                 put("0cec0063-a032-4a37-aa45-889d554023d8",
                                                                                                         "3.11产品图片接口");
                                                                                                 put("66b91d60-808b-4109-9dc2-2b9f08349bee",
                                                                                                         "3.12货主接口");
                                                                                                 put("72be83fe-24ca-4bf5-9851-248c3391d67a",
                                                                                                         "3.13站点接口");
                                                                                                 put("c2da7bde-f824-4066-bf93-fcdf94690ac0",
                                                                                                         "3.14售货机卡/密码权限");
                                                                                                 put("f821ae2c-1f02-4e6c-a7c4-e11661b4da60",
                                                                                                         "3.15产品领料权限接口");
                                                                                                 put("4f4e0f23-9b81-4b82-885d-6b9e07ebec18",
                                                                                                         "3.16卡/密码接口");
                                                                                                 put("be98e646-5160-49ac-a986-05cd073393f7",
                                                                                                         "3.17客户员工卡/密码权限接口");
                                                                                                 put("f42b379f-b3c9-4b09-96e1-4bd21c05ae7f",
                                                                                                         "3.18客户员工接口");
                                                                                                 put("5090a50f-fa68-4691-a0bb-af1b38676500",
                                                                                                         "3.19客户接口");
                                                                                                 put("de22c9b4-1be2-4301-a3cc-36c66a7aa9da",
                                                                                                         "3.20产品组合接口");
                                                                                                 put("e8030392-15d7-467f-97ef-897a59dc039a",
                                                                                                         "3.21产品组合权限接口");
                                                                                                 put("7f342da0-05be-4f3a-96c3-28072ec31e7a",
                                                                                                         "3.22补货单接口");
                                                                                                 put("badc8989-320d-4122-8b40-1e5b4ec2541c",
                                                                                                         "3.23售货机在线状态更新接口");
                                                                                                 put("7f342da0-05be-4f3a-96c3-28072ec31e7a",
                                                                                                         "3.24补货单状态更新接口");
                                                                                                 put("7f342da0-05be-4f3a-96c3-28072ec31e7a",
                                                                                                         "3.25补货单接口");
                                                                                                 put("7774bf40-e172-4daf-88cb-e7cecfdc86bd",
                                                                                                         "3.26盘点记录接口");
                                                                                                 put("f5051735-42a1-4003-afbf-b18b3d87c8f0",
                                                                                                         "3.27库存交易记录");
                                                                                                 put("b7e4d092-5c3c-4f73-b793-caf197826340",
                                                                                                         "3.38同步库存");
                                                                                                 put("c7aff256-e10b-4ee1-bd2e-f3670208aed1",
                                                                                                         "3.39APP接口调用返回");
                                                                                                 put(METHOD_WSID_RETURNS_FORWARD,
                                                                                                         "3.40退货单接口");
                                                                                                 put(METHOD_WSID_USEDRECORD,
                                                                                                         "卡产品领用接口");
                                                                                                 put(METHOD_WSID_PRODUCTCARDPOWER,
                                                                                                         "卡产品权限接口");
                                                                                             }
                                                                                         };

}
