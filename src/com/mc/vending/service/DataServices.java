package com.mc.vending.service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.db.VersionDbOper;
import com.mc.vending.parse.AutherDataParse;
import com.mc.vending.parse.CardDataParse;
import com.mc.vending.parse.ConfigDataParse;
import com.mc.vending.parse.CusEmpCardPowerDataParse;
import com.mc.vending.parse.CustomerDataParse;
import com.mc.vending.parse.CustomerEmpLinkDataParse;
import com.mc.vending.parse.InventoryHistoryDataParse;
import com.mc.vending.parse.ProductDataParse;
import com.mc.vending.parse.ProductGroupDataParse;
import com.mc.vending.parse.ProductGroupPowerDataParse;
import com.mc.vending.parse.ProductMaterialPowerDataParse;
import com.mc.vending.parse.ProductPictureDataParse;
import com.mc.vending.parse.ReplenishmentDataParse;
import com.mc.vending.parse.StationDataParse;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.parse.SupplierDataParse;
import com.mc.vending.parse.SynDataParse;
import com.mc.vending.parse.VendingCardPowerDataParse;
import com.mc.vending.parse.VendingChnDataParse;
import com.mc.vending.parse.VendingDataParse;
import com.mc.vending.parse.VendingPasswordDataParse;
import com.mc.vending.parse.VendingPictureDataParse;
import com.mc.vending.parse.VendingProLinkDataParse;
import com.mc.vending.parse.VendingStatusDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;

@SuppressLint("HandlerLeak")
public class DataServices extends Service implements DataParseRequestListener, Serializable {

    private RequestDataFinishListener normalListener;
    private RequestDataFinishListener settingListener;

    public RequestDataFinishListener getSettingListener() {
        return settingListener;
    }

    public void setSettingListener(RequestDataFinishListener settingListener) {
        this.settingListener = settingListener;
    }

    public RequestDataFinishListener getNormalListener() {
        return normalListener;
    }

    public void setNormalListener(RequestDataFinishListener normalListener) {
        this.normalListener = normalListener;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -8326654980490455778L;
    public final static int MESSAGE_AUTHER = 1; // 认证准入接口消息
    public final static int MESSAGE_CONFIG = 2; // 接口配置接口消息
    public final static int MESSAGE_VENDING = 3; // 售货机接口消息
    public final static int MESSAGE_VENDINGPICTURE = 4; // 待机界面图片接口消息
    public final static int MESSAGE_VENDINGCHN = 5; // 售货机货道接口消息
    public final static int MESSAGE_VENDINGPROLINK = 6; // 售货机产品接口消息
    public final static int MESSAGE_PRODUCT = 7; // 产品接口消息
    public final static int MESSAGE_PRODUCTPICTURE = 8; // 产品图片接口消息
    public final static int MESSAGE_SUPPLIER = 9; // 货主接口消息
    public final static int MESSAGE_STATION = 10; // 站点接口消息
    public final static int MESSAGE_VENDINGCARDPOWER = 11; // 售货机卡/密码权限消息
    public final static int MESSAGE_PRODUCTMATERIAKPOWER = 12; // 产品领料权限接口消息
    public final static int MESSAGE_CARD = 13; // 卡/密码接口消息
    public final static int MESSAGE_CUSEMPCARDPOWER = 14; // 客户员工卡/密码权限接口消息
    public final static int MESSAGE_CUSTOMEREMPLINK = 15; // 客户员工接口消息
    public final static int MESSAGE_CUSTOMER = 16; // 客户接口消息
    public final static int MESSAGE_PRODUCTGROUP = 17; // 产品组合接口消息
    public final static int MESSAGE_PRODUCTGROUPPOWER = 18; // 产品组合权限接口消息
    public final static int MESSAGE_REPLENISHMENT = 19; // 补货单接口消息
    public final static int MESSAGE_VENDING_STSATUS = 20; // 售货机在线状态更新接口消息
    public final static int MESSAGE_REPLENISHMENT_STATUS = 21; // 补货单状态更新接口消息
    public final static int MESSAGE_REPLENISHMENT_DIFF = 22; // 补货单差异接口消息
    public final static int MESSAGE_INVENTORY = 23; // 盘点记录接口消息
    public final static int MESSAGE_STOCKTRANSACTION = 24; // 库存交易记录接口消息
    public final static int MESSAGE_VENDINGPASSWORD = 25; // 售货机强制密码记录接口消息

    public String vendingCode = "";
    // public String vendingId = "112ab3ee-c1c8-41f3-b056-1c743608f263";
    public String vendingId = "";
    public boolean initFlag = false; // 初始化标识，1为初始化，0不初始化
    public static boolean synDataFlag = false; // 同步数据的flag

    private Timer configTimer; // 系统配置定时器
    private int configTimerLength = 1000 * 60 * 2 ; // 系统配置定时间隔时间

    private Timer vendingTimer; // 售货机定时器，有多少个请求任务，增加多少个定时器
    private int vendingTimerLength = 1000 * 60 * 2;

    private Timer vendingPictureTimer; // 待机图片定时器，
    private int vendingPictureTimerLength = 1000 * 60 * 2; // 待机图片定时更新间隔时间

    private Timer vendingChnTimer; // 售货机货道定时器，
    private int vendingChnTimerLength = 1000 * 60 * 2; // 售货机货道定时更新间隔时间

    private Timer vendingProLinkTimer; // 售货机产品定时器，
    private int vendingProLinkTimerLength = 1000 * 60 * 2; // 售货机产品定时更新间隔时间
    private Timer productTimer; // 产品定时器，
    private int productTimerLength = 1000 * 60 * 2; // 产品道定时更新间隔时间
    private int productRowCount = 10;

    private Timer productPictureTimer; // 产品图片定时器，
    private int productPictureTimerLength = 1000 * 60 * 2; // 产品图片道定时更新间隔时间
    private int productPictureRowCount = 10;
    private Timer supplierTimer; // 货主定时器，
    private int supplierTimerLength = 1000 * 60 * 2; // 货主道定时更新间隔时间
    private int supplierRowCount = 10;
    private Timer stationTimer; // 站点图片定时器，
    private int stationTimerLength = 1000 * 60 * 2; // 站点图片道定时更新间隔时间
    private int stationRowCount = 10;
    private Timer vendingCardPowerTimer; // 售货机卡/密码权限定时器，
    private int vendingCardPowerTimerLength = 1000 * 60 * 2; // 售货机卡/密码权限定时更新间隔时间

    private Timer productMaterialPowerTimer; // 产品领料权限定时器，
    private int productMaterialPowerTimerLength = 1000 * 60 * 2; // 产品领料权限定时更新间隔时间

    private Timer cardTimer; // 卡/密码定时器，
    private int cardTimerLength = 1000 * 60 * 2; // 卡/密码定时更新间隔时间

    private Timer cusEmpCardPowerTimer; // 客户员工卡/密码权限定时器，
    private int cusEmpCardPowerTimerLength = 1000 * 60 * 2; // 客户员工卡/密码权限定时更新间隔时间

    private Timer customerEmpLinkTimer; // 客户员工定时器，
    private int customerEmpLinkTimerLength = 1000 * 60 * 2; // 客户员工定时更新间隔时间

    private Timer customerTimer; // 客户定时器，
    private int customerTimerLength = 1000 * 60 * 2; // 客户定时更新间隔时间

    private Timer productGroupTimer; // 产品组合定时器，
    private int productGroupTimerLength = 1000 * 60 * 2; // 产品组合定时更新间隔时间

    private Timer productGroupPowerTimer; // 产品组合权限定时器，
    private int productGroupPowerTimerLength = 1000 * 60 * 2; // 产品组合权限定时更新间隔时间

    private Timer replenishmentTimer; // 补货单定时器，
    private int replenishmentTimerLength = 1000 * 60 * 2; // 补货单定时更新间隔时间
    private int replenishmentRowCount = 10;
    private Timer vendingStatusTimer; // 售货机状态定时器，
    private int vendingStatusTimerLength = 1000 * 60 * 2; // 售货机状态定时更新间隔时间

    private Timer replenishmentDiffTimer; // 补货差异定时器，
    private int replenishmentDiffTimerLength = 1000 * 60 * 2; // 补货差异定时更新间隔时间

    private Timer replenishmentStatusTimer; // 补货状态定时器，
    private int replenishmentStatusTimerLength = 1000 * 60 * 2; // 补货状态定时更新间隔时间

    private Timer inventoryTimer; // 盘点定时器，
    private int inventoryTimerLength = 1000 * 60 * 2; // 盘点定时更新间隔时间

    private Timer stockTransactionTimer; // 库存交易定时器，
    private int stockTransactionTimerLength = 1000 * 60 * 2; // 库存交易定时更新间隔时间

    private Timer vendingPasswordTimer; // 售货机强制密码定时器，
    private int vendingPasswordTimerLength = 1000 * 60 * 2; // 售货机强制密码更新间隔时间

    private Map<String, InterfaceData> configMap = new HashMap<String, InterfaceData>();
    private TimerTask configTask = null;
    private TimerTask vendingTask = null;
    private TimerTask vendingPictureTask = null;
    private TimerTask vendingChnTask = null;
    private TimerTask vendingProLinkTask = null;
    private TimerTask productTask = null;
    private TimerTask productPictureTask = null;
    private TimerTask supplierTask = null;
    private TimerTask stationTask = null;
    private TimerTask vendingCardPowerTask = null;
    private TimerTask productMaterialPowerTask = null;
    private TimerTask cardTask = null;
    private TimerTask cusEmpCardPowerTask = null;
    private TimerTask customerEmpLinkTask = null;
    private TimerTask customerTask = null;
    private TimerTask productGroupTask = null;
    private TimerTask productGroupPowerTask = null;
    private TimerTask replenishmentTask = null;
    private TimerTask vendingStatusTask = null;
    private TimerTask replenishmentStatusTask = null;
    private TimerTask replenishmentDiffTask = null;
    private TimerTask inventoryTask = null;
    private TimerTask stockTransactionTask = null;
    private TimerTask vendingPasswordTask = null;
    private Map<String, String> taskMap;

    private String sql201 = "ALTER TABLE "
            + "Vending add COLUMN VD1_CardType varchar(10) not null DEFAULT '1';";
    private String sql210 = "ALTER TABLE "
            + "Card add COLUMN CD1_ProductPower varchar(1) not null DEFAULT '0';";
    private String sql2101 = "CREATE TABLE if not exists "
            + "ProductCardPower ( "
            + "PC1_ID  varchar(100) NOT NULL, "
            + "PC1_VD1_ID varchar(100),"
            + "PC1_M02_ID varchar(100),"
            + "PC1_CU1_ID varchar(100),"
            + "PC1_CD1_ID varchar(100),"
            + "PC1_VP1_ID varchar(100),"
            + "PC1_Power  varchar(100),"
            + "PC1_OnceQty varchar(100),"
            + "PC1_Period varchar(100),"
            + "PC1_IntervalStart  varchar(100),"
            + "PC1_IntervalFinish varchar(100),"
            + "PC1_StartDate  varchar(100),"
            + "PC1_PeriodQty  varchar(100), "
            + "CreateUser  varchar(100), "
            + "CreateTime  varchar(100), "
            + "ModifyUser  varchar(100), "
            + "ModifyTime  varchar(100), "
            + "RowVersion  varchar(100), "
            + "PRIMARY KEY (PC1_ID) );";
    
//    private String sql2101 = "drop table ProductCardPower;";
    private String sql2102 = "CREATE TABLE if not exists "
            + "UsedRecord ( UR1_ID varchar(100) NOT NULL ,"
            + "UR1_M02_ID varchar(100),"
            + "UR1_CD1_ID varchar(100),"
            + "UR1_VD1_ID varchar(100),"
            + "UR1_PD1_ID varchar(100),"
            + "UR1_Quantity   varchar(100),"
            + "uploadStatus varchar(1) not null DEFAULT '0', "
            + "CreateUser  varchar(100), "
            + "CreateTime  varchar(100), "
            + "ModifyUser  varchar(100), "
            + "ModifyTime  varchar(100), "
            + "RowVersion  varchar(100), "
            + "PRIMARY KEY (UR1_ID) );";
    

//    private String sql202 = "CREATE TABLE if not exists ReturnForward ( id  varchar(100) NOT NULL, RT1_M02_ID  varchar(100), RT1_RTCode  varchar(100), RT1_Type  varchar(10), RT1_CU1_ID  varchar(100), RT1_VD1_ID  varchar(100), RT1_CE1_ID  varchar(100), RT1_Status  varchar(10), CreateUser  varchar(100), CreateTime  varchar(100), ModifyUser  varchar(100), ModifyTime  varchar(100), RowVersion  varchar(100), PRIMARY KEY (id) );";

    @Override
    public void onCreate() {

        if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 201) {
            VersionDbOper.exec(sql201);
        }
        if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")) >= 210) {
            VersionDbOper.exec(sql210);
            VersionDbOper.exec(sql2101);
            VersionDbOper.exec(sql2102);
        }

        taskMap = new HashMap<String, String>();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        IBinder result = null;
        if (null == result) {
            result = new ServiceBinder();
        }
        requestConfig();
        initParam();
        return result;
    }

    /**
     * 初始化启动Timer
     */
    private void initParam() {
        List<InterfaceData> configList = new InterfaceDbOper().findAll();
        for (InterfaceData config : configList) {
            String wsid = config.getM03Target().trim();
            String opType = config.getM03Optype().trim();
            this.configMap.put(wsid + "_" + opType, config);
        }
        resetTimerLength();
        startUploadTimer();
        startDownloadTimer();
    }

    /**
     * 设置售货机编号－－－外部调用
     * 
     * @param vcCode
     */
    public void resetVending(String vcCode) {
        if (StringHelper.isEmpty(vcCode, true)) {
            return;
        }
        this.vendingCode = vcCode;
        this.initFlag = true;
        // synDataFlag = false;
        // 请求售货机接口
        VendingDataParse parse = new VendingDataParse();
        parse.setListener(this);
        parse.requestVendingData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING,
                vendingCode, initFlag);
    }

    /**
     * 启动配置文件定时任务，定时为每天00:00:00分执行
     */
    private void startConfigTimer() {
        cancelConfigTask();
        removeConfigTimer();
        initConfigTask();
        initConfigTimer();
        configTimer.schedule(configTask, DateHelper.truncateTime(new Date(), 1), configTimerLength);
//        configTimer.schedule(configTask, configTimerLength, configTimerLength);
        // configTimer.schedule(configTask, 1, configTimerLength);
    }

    /**
     * 初始化config定时器
     */
    private void initConfigTimer() {
        configTimer = new Timer();
    }

    /**
     * 启动上传定时器
     */
    private void startUploadTimer() {
        
        cancelUploadTask();
        removeUploadTimer();
        initUploadTask();
        initUploadTimer();

        /******************************************************************** 数据上传接口 start ****************************************************/
        // 售货机联机状态 定时任务 全表
        if (vendingStatusTimerLength>0) {
            vendingStatusTimer.schedule(vendingStatusTask, vendingStatusTimerLength, vendingStatusTimerLength);
        }
        // 更新补货 单状态定时任务 上传数据
        if (replenishmentStatusTimerLength > 0) {

            replenishmentStatusTimer.schedule(replenishmentStatusTask, replenishmentStatusTimerLength, replenishmentStatusTimerLength);
        }
        // 补货差异 定时任务 全表 上传数据
        if (replenishmentDiffTimerLength > 0) {

            replenishmentDiffTimer.schedule(replenishmentDiffTask, replenishmentDiffTimerLength, replenishmentDiffTimerLength);
        }
        // 盘点记录 定时任务 全表 上传数据
        if (inventoryTimerLength > 0) {

            inventoryTimer.schedule(inventoryTask, inventoryTimerLength, inventoryTimerLength);
        }
        // 库存交易记录 定时任务 全表 上传数据
        if (stockTransactionTimerLength > 0) {

            stockTransactionTimer.schedule(stockTransactionTask, stockTransactionTimerLength, stockTransactionTimerLength);
        }
        /******************************************************************** 数据上传接口 end ****************************************************/
    }

    /**
     * 启动下载定时器
     */

    private void startDownloadTimer() {

        cancelDownLoadTask();
        removeDownTimer();
//        initDownloadTask();
//        initDownloadTimer();
        synDataFlag = true;

        vendingTimer = new Timer(); // 初始化定时器
        vendingTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "vendingTask start...");
                InterfaceData vendingConfig = configMap.get(Constant.METHOD_WSID_VENDING + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
//                boolean flag = DataServices.isTaskStart(vendingConfig);
                boolean flag = true;
//                ZillionLog.i("isTaskStart", flag);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_VENDING;
                    handler.sendMessage(message);
                }
            }
        };
        if (vendingTimerLength > 0) {
            vendingTimer.schedule(vendingTask,vendingTimerLength, vendingTimerLength);
            
        }
    }

    /**
     * 设置Timer时间间隔
     */
    private void resetTimerLength() {

        int standardInterval = 1000;//间隔标准-秒
//        int standardInterval = 60 * 1000;//间隔标准-分钟
        InterfaceData vendingConfig = configMap.get(Constant.METHOD_WSID_VENDING + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingTimerLength = vendingConfig != null ? vendingConfig.getM03ExeInterval() * standardInterval
                : vendingTimerLength;
        
        ZillionLog.i(this.getClass().getName(),"resetTimerLength");

//        InterfaceData configConfig = configMap.get(Constant.METHOD_WSID_CONFIG + "_"
//                + Constant.HTTP_OPERATE_TYPE_GETDATA);
//        configTimerLength = configConfig != null ? configConfig.getM03ExeInterval() * standardInterval
//                : configTimerLength;

        InterfaceData vendingPictureConfig = configMap.get(Constant.METHOD_WSID_VENDINGPICTURE + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingPictureTimerLength = vendingPictureConfig != null ? vendingPictureConfig.getM03ExeInterval() * standardInterval
                : vendingPictureTimerLength;

        InterfaceData vendingChnConfig = configMap.get(Constant.METHOD_WSID_VENDINGCHN + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingChnTimerLength = vendingChnConfig != null ? vendingChnConfig.getM03ExeInterval() * standardInterval
                : vendingChnTimerLength;

        InterfaceData vendingProLinkConfig = configMap.get(Constant.METHOD_WSID_VENDINGPROLINK + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingProLinkTimerLength = vendingProLinkConfig != null ? vendingProLinkConfig.getM03ExeInterval() * standardInterval
                : vendingProLinkTimerLength;

        InterfaceData vendingCardPowerConfig = configMap.get(Constant.METHOD_WSID_VENDINGCARDPOWER + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingCardPowerTimerLength = vendingCardPowerConfig != null ? vendingCardPowerConfig
                .getM03ExeInterval() * standardInterval : vendingCardPowerTimerLength;

        InterfaceData productMaterialPowerConfig = configMap.get(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER
                + "_" + Constant.HTTP_OPERATE_TYPE_GETDATA);
        productMaterialPowerTimerLength = productMaterialPowerConfig != null ? productMaterialPowerConfig
                .getM03ExeInterval() * standardInterval : productMaterialPowerTimerLength;

        InterfaceData cardConfig = configMap.get(Constant.METHOD_WSID_CARD + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        cardTimerLength = cardConfig != null ? cardConfig.getM03ExeInterval() * standardInterval : cardTimerLength;

        InterfaceData cusEmpCardPowerConfig = configMap.get(Constant.METHOD_WSID_CUSEMPCARDPOWER + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        cusEmpCardPowerTimerLength = cusEmpCardPowerConfig != null ? cusEmpCardPowerConfig
                .getM03ExeInterval() * standardInterval : cusEmpCardPowerTimerLength;

        InterfaceData customerEmpLinkConfig = configMap.get(Constant.METHOD_WSID_CUSTOMEREMPLINK + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        customerEmpLinkTimerLength = customerEmpLinkConfig != null ? customerEmpLinkConfig
                .getM03ExeInterval() * standardInterval : customerEmpLinkTimerLength;

        InterfaceData customerConfig = configMap.get(Constant.METHOD_WSID_CUSTOMER + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        customerTimerLength = customerConfig != null ? customerConfig.getM03ExeInterval() * standardInterval
                : customerTimerLength;

        InterfaceData productGroupConfig = configMap.get(Constant.METHOD_WSID_PRODUCTGROUP + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        productGroupTimerLength = productGroupConfig != null ? productGroupConfig.getM03ExeInterval() * standardInterval
                : productGroupTimerLength;

        InterfaceData productGroupPowerConfig = configMap.get(Constant.METHOD_WSID_PRODUCTGROUPPOWER + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        productGroupPowerTimerLength = productGroupPowerConfig != null ? productGroupPowerConfig
                .getM03ExeInterval() * standardInterval : productGroupPowerTimerLength;

        InterfaceData productConfig = configMap.get(Constant.METHOD_WSID_PRODUCT + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        productTimerLength = productConfig != null ? productConfig.getM03ExeInterval() * standardInterval
                : productTimerLength;
        productRowCount = productConfig != null ? productConfig.getM03RowCount() : productRowCount;

        InterfaceData productPictureConfig = configMap.get(Constant.METHOD_WSID_PRODUCTPICTURE + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        productPictureTimerLength = productPictureConfig != null ? productPictureConfig.getM03ExeInterval() * standardInterval
                : productPictureTimerLength;
        productPictureRowCount = productPictureConfig != null ? productPictureConfig.getM03RowCount()
                : productPictureRowCount;

        InterfaceData supplierConfig = configMap.get(Constant.METHOD_WSID_SUPPLIER + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        supplierTimerLength = supplierConfig != null ? supplierConfig.getM03ExeInterval() * standardInterval
                : supplierTimerLength;
        supplierRowCount = supplierConfig != null ? supplierConfig.getM03RowCount() : supplierRowCount;

        InterfaceData stationConfig = configMap.get(Constant.METHOD_WSID_STATION + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        stationTimerLength = stationConfig != null ? stationConfig.getM03ExeInterval() * standardInterval
                : stationTimerLength;
        stationRowCount = stationConfig != null ? stationConfig.getM03RowCount() : stationRowCount;

        InterfaceData replenishmentConfig = configMap.get(Constant.METHOD_WSID_REPLENISHMENT + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        replenishmentTimerLength = replenishmentConfig != null ? replenishmentConfig.getM03ExeInterval() * standardInterval
                : replenishmentTimerLength;
        replenishmentRowCount = replenishmentConfig != null ? replenishmentConfig.getM03RowCount()
                : replenishmentRowCount;

        InterfaceData vendingStatusConfig = configMap.get(Constant.METHOD_WSID_VENDING_STSATUS + "_"
                + Constant.HTTP_OPERATE_TYPE_UPDATESTATUS);
        vendingStatusTimerLength = vendingStatusConfig != null ? vendingStatusConfig.getM03ExeInterval() * standardInterval
                : vendingStatusTimerLength;

        InterfaceData replenishmentStatusConfig = configMap.get(Constant.METHOD_WSID_REPLENISHMENT_STATUS
                + "_" + Constant.HTTP_OPERATE_TYPE_UPDATESTATUS);
        replenishmentStatusTimerLength = replenishmentStatusConfig != null ? replenishmentStatusConfig
                .getM03ExeInterval() * standardInterval : replenishmentStatusTimerLength;

        InterfaceData replenishmentDiffConfig = configMap.get(Constant.METHOD_WSID_REPLENISHMENT_DIFF + "_"
                + Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY);
        replenishmentDiffTimerLength = replenishmentDiffConfig != null ? replenishmentDiffConfig
                .getM03ExeInterval() * standardInterval : replenishmentDiffTimerLength;

        InterfaceData inventoryConfig = configMap.get(Constant.METHOD_WSID_INVENTORY + "_"
                + Constant.HTTP_OPERATE_TYPE_INSERT);
        inventoryTimerLength = inventoryConfig != null ? inventoryConfig.getM03ExeInterval() * standardInterval
                : inventoryTimerLength;

        InterfaceData stockTransactionConfig = configMap.get(Constant.METHOD_WSID_STOCKTRANSACTION + "_"
                + Constant.HTTP_OPERATE_TYPE_INSERT);
        stockTransactionTimerLength = stockTransactionConfig != null ? stockTransactionConfig
                .getM03ExeInterval() * standardInterval : stockTransactionTimerLength;

        InterfaceData vendingPasswordConfig = configMap.get(Constant.METHOD_WSID_PASSWORD + "_"
                + Constant.HTTP_OPERATE_TYPE_GETDATA);
        vendingPasswordTimerLength = vendingPasswordConfig != null ? vendingPasswordConfig
                .getM03ExeInterval() * standardInterval : vendingPasswordTimerLength;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_NOT_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        ZillionLog.i(this.getClass().getName(),"销毁DataService");
        cancelConfigTask();
        removeConfigTimer();
        cancelDownLoadTask();
        removeDownTimer();
        cancelUploadTask();
        removeUploadTimer();
        super.onDestroy();
    }

    /**
     * 此方法是为了可以在Acitity中获得服务的实例
     * 
     * @author apple
     *
     */
    public class ServiceBinder extends Binder {
        public DataServices getService() {
            return DataServices.this;
        }
    };

    private void initUploadTimer() {
        vendingStatusTimer = new Timer();
        replenishmentStatusTimer = new Timer();
        replenishmentDiffTimer = new Timer();
        inventoryTimer = new Timer();
        stockTransactionTimer = new Timer();
    }

    private void initDownloadTimer() {
        vendingTimer = new Timer(); // 初始化定时器
    }

    /**
     * 移除上传timer
     */
    private void removeUploadTimer() {

        if (vendingStatusTimer != null) {
            vendingStatusTimer.cancel();
            vendingStatusTimer = null;
        }

        if (replenishmentDiffTimer != null) {
            replenishmentDiffTimer.cancel();
            replenishmentDiffTimer = null;
        }

        if (replenishmentStatusTimer != null) {
            replenishmentStatusTimer.cancel();
            replenishmentStatusTimer = null;
        }

        if (inventoryTimer != null) {
            inventoryTimer.cancel();
            inventoryTimer = null;
        }

        if (stockTransactionTimer != null) {
            stockTransactionTimer.cancel();
            stockTransactionTimer = null;
        }
    }

    private void removeConfigTimer() {
        if (configTimer != null) {
            configTimer.cancel();
            configTimer = null;
        }
    }

    /**
     * 移除下载定时器
     */
    private void removeDownTimer() {
        
        if (vendingTimer != null) {
            vendingTimer.cancel();
            vendingTimer = null;
        }

        if (vendingPictureTimer != null) {
            vendingPictureTimer.cancel();
            vendingPictureTimer = null;
        }

        if (vendingChnTimer != null) {
            vendingChnTimer.cancel();
            vendingChnTimer = null;
        }

        if (vendingProLinkTimer != null) {
            vendingProLinkTimer.cancel();
            vendingProLinkTimer = null;
        }

        if (productTimer != null) {
            productTimer.cancel();
            productTimer = null;
        }

        if (productPictureTimer != null) {
            productPictureTimer.cancel();
            productPictureTimer = null;
        }

        if (supplierTimer != null) {
            supplierTimer.cancel();
            supplierTimer = null;
        }

        if (stationTimer != null) {
            stationTimer.cancel();
            stationTimer = null;
        }

        if (vendingCardPowerTimer != null) {
            vendingCardPowerTimer.cancel();
            vendingCardPowerTimer = null;
        }

        if (productMaterialPowerTimer != null) {
            productMaterialPowerTimer.cancel();
            productMaterialPowerTimer = null;
        }

        if (cardTimer != null) {
            cardTimer.cancel();
            cardTimer = null;
        }

        if (cusEmpCardPowerTimer != null) {
            cusEmpCardPowerTimer.cancel();
            cusEmpCardPowerTimer = null;
        }

        if (customerEmpLinkTimer != null) {
            customerEmpLinkTimer.cancel();
            customerEmpLinkTimer = null;
        }

        if (customerTimer != null) {
            customerTimer.cancel();
            customerTimer = null;
        }

        if (productGroupTimer != null) {
            productGroupTimer.cancel();
            productGroupTimer = null;
        }

        if (productGroupPowerTimer != null) {
            productGroupPowerTimer.cancel();
            productGroupPowerTimer = null;
        }

        if (replenishmentTimer != null) {
            replenishmentTimer.cancel();
            replenishmentTimer = null;
        }

        if (vendingPasswordTimer != null) {
            vendingPasswordTimer.cancel();
            vendingPasswordTimer = null;
        }
    }

    /**
     * 移除configtask
     */
    private void cancelConfigTask() {
        if (configTask != null) {
            configTask.cancel();
            configTask = null;
        }

    }

    /**
     * 取消上传任务
     */
    private void cancelUploadTask() {

        if (vendingStatusTask != null) {
            vendingStatusTask.cancel();
            vendingStatusTask = null;
        }
        if (replenishmentStatusTask != null) {
            replenishmentStatusTask.cancel();
            replenishmentStatusTask = null;
        }
        if (replenishmentDiffTask != null) {
            replenishmentDiffTask.cancel();
            replenishmentDiffTask = null;
        }
        if (inventoryTask != null) {
            inventoryTask.cancel();
            inventoryTask = null;
        }
        if (stockTransactionTask != null) {
            stockTransactionTask.cancel();
            stockTransactionTask = null;
        }
    }

    /**
     * 取消下载任务
     */
    private void cancelDownLoadTask() {
        
        if (vendingTask != null) {
            vendingTask.cancel();
            vendingTask = null;
        }
        if (vendingPictureTask != null) {
            vendingPictureTask.cancel();
            vendingPictureTask = null;
        }
        if (vendingChnTask != null) {
            vendingChnTask.cancel();
            vendingChnTask = null;
        }
        if (vendingProLinkTask != null) {
            vendingProLinkTask.cancel();
            vendingProLinkTask = null;
        }
        if (productTask != null) {
            productTask.cancel();
            productTask = null;
        }
        if (productPictureTask != null) {
            productPictureTask.cancel();
            productPictureTask = null;
        }
        if (supplierTask != null) {
            supplierTask.cancel();
            supplierTask = null;
        }

        if (stationTask != null) {
            stationTask.cancel();
            stationTask = null;
        }
        if (vendingCardPowerTask != null) {
            vendingCardPowerTask.cancel();
            vendingCardPowerTask = null;
        }
        if (productMaterialPowerTask != null) {
            productMaterialPowerTask.cancel();
            productMaterialPowerTask = null;
        }
        if (cardTask != null) {
            cardTask.cancel();
            cardTask = null;
        }

        if (cusEmpCardPowerTask != null) {
            cusEmpCardPowerTask.cancel();
            cusEmpCardPowerTask = null;
        }

        if (customerEmpLinkTask != null) {
            customerEmpLinkTask.cancel();
            customerEmpLinkTask = null;
        }
        if (customerTask != null) {
            customerTask.cancel();
            customerTask = null;
        }
        if (productGroupTask != null) {
            productGroupTask.cancel();
            productGroupTask = null;
        }

        if (productGroupPowerTask != null) {
            productGroupPowerTask.cancel();
            productGroupPowerTask = null;
        }
        if (replenishmentTask != null) {
            replenishmentTask.cancel();
            replenishmentTask = null;
        }
        if (vendingPasswordTask != null) {
            vendingPasswordTask.cancel();
            vendingPasswordTask = null;
        }
    }

    /**
     * 初始化配置task
     */
    private void initConfigTask() {
        configTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "initConfigTask start..");
                InterfaceData configConfig = configMap.get(Constant.METHOD_WSID_CONFIG + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                boolean flag = DataServices.isTaskStart(configConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_CONFIG;
                    handler.sendMessage(message);
                }
            }
        };
    }

    /**
     * 初始化上传task
     */
    private void initUploadTask() {

        /**
         * 售货机在线状态定时任务
         */
        vendingStatusTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "vendingStatusTask start..");
                InterfaceData vendingStatusConfig = configMap.get(Constant.METHOD_WSID_VENDING_STSATUS + "_"
                        + Constant.HTTP_OPERATE_TYPE_UPDATESTATUS);
                boolean flag = DataServices.isTaskStart(vendingStatusConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_VENDING_STSATUS;
                    handler.sendMessage(message);
                }
            }
        };

        /**
         * 补货单状态定时任务
         */
        replenishmentStatusTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "replenishmentStatusTask start..");
                InterfaceData replenishmentStatusConfig = configMap
                        .get(Constant.METHOD_WSID_REPLENISHMENT_STATUS + "_"
                                + Constant.HTTP_OPERATE_TYPE_UPDATESTATUS);
                boolean flag = DataServices.isTaskStart(replenishmentStatusConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_REPLENISHMENT_STATUS;
                    handler.sendMessage(message);
                }
            }
        };

        /**
         * 补货单差异定时任务
         */
        replenishmentDiffTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "replenishmentDiffTask start..");
                InterfaceData replenishmentDiffConfig = configMap.get(Constant.METHOD_WSID_REPLENISHMENT_DIFF
                        + "_" + Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY);
                boolean flag = DataServices.isTaskStart(replenishmentDiffConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_REPLENISHMENT_DIFF;
                    handler.sendMessage(message);
                }
            }
        };
        /**
         * 盘点记录定时任务
         */
        inventoryTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "inventoryTask start..");
                InterfaceData inventoryConfig = configMap.get(Constant.METHOD_WSID_INVENTORY + "_"
                        + Constant.HTTP_OPERATE_TYPE_INSERT);
                boolean flag = DataServices.isTaskStart(inventoryConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_INVENTORY;
                    handler.sendMessage(message);
                }
            }
        };
        /**
         * 库存交易记录定时任务
         */
        stockTransactionTask = new TimerTask() {
            @Override
            public void run() {
                ZillionLog.i("vendingTask", "stockTransactionTask start..");
                InterfaceData stockTransactionConfig = configMap.get(Constant.METHOD_WSID_STOCKTRANSACTION
                        + "_" + Constant.HTTP_OPERATE_TYPE_INSERT);
                boolean flag = DataServices.isTaskStart(stockTransactionConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_STOCKTRANSACTION;
                    handler.sendMessage(message);
                }
            }
        };
    }

    /**
     * 初始化定时任务
     */
    private void initDownloadTask() {
        /**
         * 售货机定时任务
         */
        vendingTask = new TimerTask() {
            @Override
            public void run() {
                InterfaceData vendingConfig = configMap.get(Constant.METHOD_WSID_VENDING + "_"
                        + Constant.HTTP_OPERATE_TYPE_GETDATA);
                boolean flag = DataServices.isTaskStart(vendingConfig);
                if (flag) {
                    Message message = new Message();
                    message.what = MESSAGE_VENDING;
                    handler.sendMessage(message);
                }
            }
        };
    }

    /**
     * 请求接口配置Parse
     */
    public void requestConfigParse() {

        ConfigDataParse configParse = new ConfigDataParse();
        configParse.setListener(this);
        configParse.requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG,
                vendingId);
    }

    /**
     * 请求权限认证接口
     */
    public void requestAutherParse() {
        AutherDataParse parse = new AutherDataParse();
        parse.setListener(this);
        parse.requestAutherData(Constant.HTTP_OPERATE_TYPE_DESGET, Constant.METHOD_WSID_AUTHER,
                Constant.BODY_VALUE_UDID);
        taskMap.put(Constant.METHOD_WSID_AUTHER, "0");
    }

    /**
     * 请求售货机数据
     */
    public void requestVendingParse() {
        VendingDataParse parse = new VendingDataParse();
        parse.setListener(this);
        parse.requestVendingData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING,
                vendingCode, initFlag);
        taskMap.put(Constant.METHOD_WSID_VENDING, "0");
    }

    /**
     * 请求售货机图片数据
     */
    public void requestVendingPictureParse() {
        VendingPictureDataParse parse = new VendingPictureDataParse();
        parse.setListener(this);
        parse.requestVendingPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_VENDINGPICTURE, vendingId);
        taskMap.put(Constant.METHOD_WSID_VENDINGPICTURE, "0");
    }

    /**
     * 请求售货机货道
     */
    public void requestVendingChnParse() {
        VendingChnDataParse parse = new VendingChnDataParse();
        parse.setListener(this);
        parse.requestVendingChnData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGCHN,
                vendingId);
        taskMap.put(Constant.METHOD_WSID_VENDINGCHN, "0");
    }

    /**
     * 请求售货机产品
     */
    public void requestVendingProLinkParse() {
        VendingProLinkDataParse parse = new VendingProLinkDataParse();
        parse.setListener(this);
        parse.requestVendingProLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_VENDINGPROLINK, vendingId);
        taskMap.put(Constant.METHOD_WSID_VENDINGPROLINK, "0");
    }

    /**
     * 请求产品
     */
    public void requestProductParse() {
        ProductDataParse parse = new ProductDataParse();
        parse.setListener(this);
        parse.requestProductData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCT, vendingId,
                productRowCount);
        taskMap.put(Constant.METHOD_WSID_PRODUCT, "0");
    }

    /**
     * 请求产品图片
     */
    public void requestProductPictureParse() {
        ProductPictureDataParse parse = new ProductPictureDataParse();
        parse.setListener(this);
        parse.requestProductPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_PRODUCTPICTURE, vendingId, productPictureRowCount);
        taskMap.put(Constant.METHOD_WSID_PRODUCTPICTURE, "0");
    }

    /**
     * 请求供应商
     */
    public void requestSupplierParse() {
        SupplierDataParse parse = new SupplierDataParse();
        parse.setListener(this);
        parse.requestSupplierData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SUPPLIER,
                vendingId, supplierRowCount);
        taskMap.put(Constant.METHOD_WSID_SUPPLIER, "0");
    }

    /**
     * 请求站点
     */
    public void requestStationParse() {
        StationDataParse parse = new StationDataParse();
        parse.setListener(this);
        parse.requestStationData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_STATION, vendingId,
                stationRowCount);
        taskMap.put(Constant.METHOD_WSID_STATION, "0");
    }

    /**
     * 请求售货杨卡密码权限
     */
    public void requestVendingCardPowerParse() {
        VendingCardPowerDataParse parse = new VendingCardPowerDataParse();
        parse.setListener(this);
        parse.requestVendingCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_VENDINGCARDPOWER, vendingId);
        taskMap.put(Constant.METHOD_WSID_VENDINGCARDPOWER, "0");
    }

    /**
     * 请求产品领料权限
     */
    public void requestProductMaterialPowerParse() {
        ProductMaterialPowerDataParse parse = new ProductMaterialPowerDataParse();
        parse.setListener(this);
        parse.requestProductMaterialPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, vendingId);
        taskMap.put(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, "0");
    }

    /**
     * 请求卡密码
     */
    public void requestCardParse() {
        CardDataParse parse = new CardDataParse();
        parse.setListener(this);
        parse.requestCardData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CARD, vendingId);
        taskMap.put(Constant.METHOD_WSID_CARD, "0");
    }

    /**
     * 请求客户员工卡密码权限
     */
    public void requestCusEmpCardPowerParse() {
        CusEmpCardPowerDataParse parse = new CusEmpCardPowerDataParse();
        parse.setListener(this);
        parse.requestCusEmpCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_CUSEMPCARDPOWER, vendingId);
        taskMap.put(Constant.METHOD_WSID_CUSEMPCARDPOWER, "0");
    }

    /**
     * 请求客户员工
     */
    public void requestCustomerEmpLinkParse() {
        CustomerEmpLinkDataParse parse = new CustomerEmpLinkDataParse();
        parse.setListener(this);
        parse.requestCustomerEmpLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_CUSTOMEREMPLINK, vendingId);
        taskMap.put(Constant.METHOD_WSID_CUSTOMEREMPLINK, "0");
    }

    /**
     * 请求客户
     */
    public void requestCustomerParse() {
        CustomerDataParse parse = new CustomerDataParse();
        parse.setListener(this);
        parse.requestCustomerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMER,
                vendingId);
        taskMap.put(Constant.METHOD_WSID_CUSTOMER, "0");
    }

    /**
     * 请求产品组合
     */
    public void requestProductGroupParse() {
        ProductGroupDataParse parse = new ProductGroupDataParse();
        parse.setListener(this);
        parse.requestProductGroupData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTGROUP,
                vendingId);
        taskMap.put(Constant.METHOD_WSID_PRODUCTGROUP, "0");
    }

    /**
     * 请求产品组合权限
     */
    public void requestProductGroupPowerParse() {
        ProductGroupPowerDataParse parse = new ProductGroupPowerDataParse();
        parse.setListener(this);
        parse.requestProductGroupPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_PRODUCTGROUPPOWER, vendingId);
        taskMap.put(Constant.METHOD_WSID_PRODUCTGROUPPOWER, "0");
    }

    /**
     * 请求补货单
     */
    public void requestReplenishmentParse() {
        ReplenishmentDataParse parse = new ReplenishmentDataParse();
        parse.setListener(this);
        parse.requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_GETDATA,
                Constant.METHOD_WSID_REPLENISHMENT, vendingId, replenishmentRowCount);
        taskMap.put(Constant.METHOD_WSID_REPLENISHMENT, "0");
    }

    /**
     * 请求售货机强制密码
     */
    public void requestVendingPasswordParse() {
        VendingPasswordDataParse parse = new VendingPasswordDataParse();
        parse.setListener(this);
        parse.requestVendingPasswordData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PASSWORD,
                vendingId);
        taskMap.put(Constant.METHOD_WSID_PASSWORD, "0");
    }

    /**
     * task 异步回调通知
     * 
     */
    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {

                            switch (msg.what) {
                            case MESSAGE_AUTHER:
                // 请求接口权限key
                                requestAutherParse();
                                break;
                            case MESSAGE_CONFIG:
                // 同步下载接口配置数据
//                              ZillionLog.i(this.getClass().getSimpleName(), "MESSAGE_CONFIG");
                                requestConfigParse();
                                break;
                            case MESSAGE_VENDING:
                // 同步下载售货机数据
                                requestVendingParse();
                                break;
                            case MESSAGE_VENDINGPICTURE:
                // 同步下载待机界面图片数据
                                requestVendingPictureParse();
                                break;
                            case MESSAGE_VENDINGCHN:
                // 同步下载售货机货道数据
                                requestVendingChnParse();
                                break;
                            case MESSAGE_VENDINGPROLINK:
                // 同步下载售货机产品数据
                                requestVendingProLinkParse();
                                break;
                            case MESSAGE_PRODUCT:
                // 同步下载产品数据
                                requestProductParse();
                                break;
                            case MESSAGE_PRODUCTPICTURE:
                // 同步下载售货机图片数据
                                requestProductPictureParse();
                                break;

                            case MESSAGE_SUPPLIER:
                // 同步下载货主数据
                                requestSupplierParse();
                                break;
                            case MESSAGE_STATION:
                // 同步下载站点数据
                                requestStationParse();
                                break;
                            case MESSAGE_VENDINGCARDPOWER:
                // 同步下载售货机卡/密码权限数据
                                requestVendingCardPowerParse();
                                break;
                            case MESSAGE_PRODUCTMATERIAKPOWER:
                // 同步下载产品领料权限数据
                                requestProductMaterialPowerParse();
                                break;
                            case MESSAGE_CARD:
                // 同步下载卡/密码数据
                                requestCardParse();
                                break;
                            case MESSAGE_CUSEMPCARDPOWER:
                // 同步下载客户员工卡/密码权限数据
                                requestCusEmpCardPowerParse();
                                break;
                            case MESSAGE_CUSTOMEREMPLINK:
                // 同步下载客户员工数据
                                requestCustomerEmpLinkParse();
                                break;
                            case MESSAGE_CUSTOMER:
                // 同步下载客户数据
                                requestCustomerParse();
                                break;
                            case MESSAGE_PRODUCTGROUP:
                // 同步下载产品组合数据
                                requestProductGroupParse();
                                break;
                            case MESSAGE_PRODUCTGROUPPOWER:
                // 同步下载产品组合权限数据
                                requestProductGroupPowerParse();
                                break;
                            case MESSAGE_REPLENISHMENT:
                // 同步下载补货单数据
                                requestReplenishmentParse();
                                break;
                            case MESSAGE_VENDINGPASSWORD:
                // 同步下载售货机强制密码
                                requestVendingPasswordParse();
                                break;
                            case MESSAGE_VENDING_STSATUS:
                // 售货机在线状态更新
                                VendingStatusDataParse.getInstance().requestVendingData(
                                        Constant.HTTP_OPERATE_TYPE_UPDATESTATUS,
                                        Constant.METHOD_WSID_VENDING_STSATUS, vendingId);
                                break;
                            case MESSAGE_REPLENISHMENT_STATUS:
                // 补货单状态更新
                                ReplenishmentDataParse.getInstance().requestReplenishmentData(
                                        Constant.HTTP_OPERATE_TYPE_UPDATESTATUS,
                                        Constant.METHOD_WSID_REPLENISHMENT_STATUS, vendingId,
                                        replenishmentRowCount);
                                break;
                            case MESSAGE_REPLENISHMENT_DIFF:
                // 上传补货单差异
                                ReplenishmentDataParse.getInstance().requestReplenishmentData(
                                        Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY,
                                        Constant.METHOD_WSID_REPLENISHMENT_DIFF, vendingId,
                                        replenishmentRowCount);
                                break;
                            case MESSAGE_INVENTORY:
                // 上传盘点记录数据
                                InventoryHistoryDataParse.getInstance().requestInventoryHistoryData(
                                        Constant.HTTP_OPERATE_TYPE_INSERT, Constant.METHOD_WSID_INVENTORY,
                                        vendingId);
                                break;
                            case MESSAGE_STOCKTRANSACTION:
                // 上传库存交易记录
                                ZillionLog.i("上传交易记录--自动任务："+StockTransactionDataParse.getInstance().isSync);
                                if (!StockTransactionDataParse.getInstance().isSync) { //没有上传
                                    
                                    StockTransactionDataParse.getInstance().requestStockTransactionData(
                                            Constant.HTTP_OPERATE_TYPE_INSERT,
                                            Constant.METHOD_WSID_STOCKTRANSACTION, vendingId);
                                }
                                break;
                            default:
                                break;
                            }
                            super.handleMessage(msg);
                        }
                    };

    /**
     * 是否启动定时任务
     * 
     * @param config
     * @return
     */
    public static boolean isTaskStart(InterfaceData config) {
        boolean flag = false;
        if (config == null)
            return flag;
        String startTime = config.getM03StartTime();
        String endTime = config.getM03EndTime();
        Date currentDate = new Date();
        String currentYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
        long current_time = currentDate.getTime();
        long start_time = DateHelper.parse(currentYMD + " " + startTime, "yyyy-MM-dd HH:mm:ss").getTime();
        long end_time = DateHelper.parse(currentYMD + " " + endTime, "yyyy-MM-dd HH:mm:ss").getTime();
        if (current_time >= start_time && current_time <= end_time) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void parseRequestFinised(BaseData baseData) {
        if (baseData.isSuccess()) {
            if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDING)) {
                if (!synDataFlag) {
                    requestConfig();
                    this.initFlag = false;
                } else {
                    if (taskMap.containsKey(Constant.METHOD_WSID_VENDING)) {
                        taskMap.remove(Constant.METHOD_WSID_VENDING);
                    }
                    reCountMap();
                }
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CONFIG)) {

                ZillionLog.i(this.getClass().getName(),"parseRequestFinised:");
                startConfigTimer();
                initParam();

            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_AUTHER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_AUTHER)) {
                    taskMap.remove(Constant.METHOD_WSID_AUTHER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGPICTURE)) {
                    taskMap.remove(Constant.METHOD_WSID_VENDINGPICTURE);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCHN)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGCHN)) {
                    taskMap.remove(Constant.METHOD_WSID_VENDINGCHN);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGPROLINK)) {
                    taskMap.remove(Constant.METHOD_WSID_VENDINGPROLINK);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCT)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCT)) {
                    taskMap.remove(Constant.METHOD_WSID_PRODUCT);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                    taskMap.remove(Constant.METHOD_WSID_PRODUCTPICTURE);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SUPPLIER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_SUPPLIER)) {
                    taskMap.remove(Constant.METHOD_WSID_SUPPLIER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STATION)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_STATION)) {
                    taskMap.remove(Constant.METHOD_WSID_STATION);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                    taskMap.remove(Constant.METHOD_WSID_VENDINGCARDPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                    taskMap.remove(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CARD)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_CARD)) {
                    taskMap.remove(Constant.METHOD_WSID_CARD);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                    taskMap.remove(Constant.METHOD_WSID_CUSEMPCARDPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                    taskMap.remove(Constant.METHOD_WSID_CUSTOMEREMPLINK);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_CUSTOMER)) {
                    taskMap.remove(Constant.METHOD_WSID_CUSTOMER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUP)) {
                    taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUP);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_REPLENISHMENT)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_REPLENISHMENT)) {
                    taskMap.remove(Constant.METHOD_WSID_REPLENISHMENT);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                    taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUPPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PASSWORD)) {
                if (taskMap.containsKey(Constant.METHOD_WSID_PASSWORD)) {
                    taskMap.remove(Constant.METHOD_WSID_PASSWORD);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SYNDATA)) {
                startDownloadTimer();
            }
        }
        baseData = null;
    }

    @Override
    public void parseRequestFailure(BaseData baseData) {
        if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CONFIG)) {

            ZillionLog.i(this.getClass().getName(),"parseRequestFailure:");
            VendingData vending = new VendingDbOper().getVending();
            this.initFlag = false;
            if (vending != null) {
                // 正常启动售货机，售货机表有数据
                vendingId = vending.getVd1Id();
                vendingCode = vending.getVd1Code();
                // 同步接口配置后查询数据库
                startConfigTimer();
//                initParam();
            }
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDING)) {
            if (!synDataFlag) {
                if (taskMap.containsKey(Constant.METHOD_WSID_VENDING)) {
                    taskMap.remove(Constant.METHOD_WSID_VENDING);
                }
                reCountMap();
            }
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_AUTHER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_AUTHER)) {
                taskMap.remove(Constant.METHOD_WSID_AUTHER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGPICTURE)) {
                taskMap.remove(Constant.METHOD_WSID_VENDINGPICTURE);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCHN)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGCHN)) {
                taskMap.remove(Constant.METHOD_WSID_VENDINGCHN);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGPROLINK)) {
                taskMap.remove(Constant.METHOD_WSID_VENDINGPROLINK);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCT)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCT)) {
                taskMap.remove(Constant.METHOD_WSID_PRODUCT);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                taskMap.remove(Constant.METHOD_WSID_PRODUCTPICTURE);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SUPPLIER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_SUPPLIER)) {
                taskMap.remove(Constant.METHOD_WSID_SUPPLIER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STATION)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_STATION)) {
                taskMap.remove(Constant.METHOD_WSID_STATION);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                taskMap.remove(Constant.METHOD_WSID_VENDINGCARDPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                taskMap.remove(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CARD)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_CARD)) {
                taskMap.remove(Constant.METHOD_WSID_CARD);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                taskMap.remove(Constant.METHOD_WSID_CUSEMPCARDPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                taskMap.remove(Constant.METHOD_WSID_CUSTOMEREMPLINK);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_CUSTOMER)) {
                taskMap.remove(Constant.METHOD_WSID_CUSTOMER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUP)) {
                taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUP);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_REPLENISHMENT)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_REPLENISHMENT)) {
                taskMap.remove(Constant.METHOD_WSID_REPLENISHMENT);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUPPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PASSWORD)) {
            if (taskMap.containsKey(Constant.METHOD_WSID_PASSWORD)) {
                taskMap.remove(Constant.METHOD_WSID_PASSWORD);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SYNDATA)) {
            // TODO:同步预请求失败处理
            if (settingListener != null) {
                settingListener.requestFailure();
            }
            if (normalListener != null) {
                normalListener.requestFailure();
            }
            synDataFlag = false;
        }
        baseData = null;

    }

    private void reCountMap() {
        // System.out.println(taskMap.size());
        if (taskMap != null && taskMap.size() == 0) {
            if (settingListener != null) {
                settingListener.requestFinished();
            }
            if (normalListener != null) {
                normalListener.requestFinished();
            }
            synDataFlag = false;
        }
    }

    /**
     * 请求网络配置信息
     */
    public void requestConfig() {
        VendingData vending = new VendingDbOper().getVending();
        if (vending != null && !StringHelper.isEmpty(vending.getVd1Id(), true)) {
            ZillionLog.i(this.getClass().getName(),"requestConfig");
            vendingId = vending.getVd1Id();// 请求接口配置接口
            vendingCode = vending.getVd1Code();
            ConfigDataParse parse = new ConfigDataParse();
            parse.setListener(this);
            parse.requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG,
                    vendingId);
        }

    }

    /**
     * 设置页面同步下载数据
     */
    public void synData() {
        SynDataParse parse = new SynDataParse();
        parse.setListener(this);
        parse.requestSynData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYNDATA);
    }

}
