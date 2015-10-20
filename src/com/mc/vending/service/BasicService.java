package com.mc.vending.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.mc.vending.config.Constant;
import com.mc.vending.data.CardData;
import com.mc.vending.data.CusEmpCardPowerData;
import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.SupplierData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.CusEmpCardPowerDbOper;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.SupplierDbOper;
import com.mc.vending.db.VendingCardPowerDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.SystemException;
import com.zillionstar.tools.L;

public class BasicService {

    static {
        L.logLevel = Constant.LOGLEVEL;
    }

    /**
     * 检查售货机是否可用
     * 
     * @return
     */
    public ServiceResult<VendingData> checkVending() {
        ServiceResult<VendingData> result = new ServiceResult<VendingData>();
        try {
            // 检查售货机与检查货道
            VendingData vending = this.validateVending();

            result.setSuccess(true);
            result.setResult(vending);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            L.e("======>>>>检查售货机是否可用发生异常" + e.getMessage());
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查售货机是否可用发生异常");
        }
        return result;
    }

    /**
     * 售货机货道：售货机货道存在、状态、销售类型
     * 
     * @param vendingChnCode
     *            货道编号
     * @param methodType
     *            一般领料 借还领料区分标识 VendingChnData.VENDINGCHN_METHOD_GENERAL： 一般领料 ，
     *            VendingChnData.VENDINGCHN_METHOD_BORROW： 借还领料
     * @return
     */
    public ServiceResult<VendingChnData> checkVendingChn(String vendingChnCode, String methodType) {
        ServiceResult<VendingChnData> result = new ServiceResult<VendingChnData>();
        try {
            // 检查货道
            VendingChnData vendingChn = this.validateVendingChn(vendingChnCode, methodType);
            result.setSuccess(true);
            result.setResult(vendingChn);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            L.e("======>>>>检查售货机货道发生异常!" + e.getMessage());
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查售货机货道发生异常!");
        }
        return result;
    }

    /**
     * 外部检查卡/密码权限
     * 
     * @param paramType
     *            参数类型 CardData.CARD_SERIALNO_PARAM为卡序列号 CARD_PASSWORD_PARAM为卡密码
     * @param paramValue
     *            参数值
     * @param vendingId
     *            售货机ID
     * @return
     */
    public ServiceResult<VendingCardPowerWrapperData> checkCardPowerOut(String paramType, String paramValue,
            String vendingId) {

        ServiceResult<VendingCardPowerWrapperData> result = new ServiceResult<VendingCardPowerWrapperData>();
        try {
            // 检查卡/密码权限
            VendingCardPowerWrapperData vendingCardPowerWrapper = this.validateCardPower(paramType,
                    paramValue, vendingId);
            result.setSuccess(true);
            result.setResult(vendingCardPowerWrapper);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + e.getMessage());
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查检查卡/密码权限发生异常!");
        }
        return result;
    }

    /**
     * 内部检查卡/密码权限
     * 
     * @param serialNo
     *            卡序列号
     * @param vendingId
     *            售货机ID
     * @return
     */
    public ServiceResult<VendingCardPowerWrapperData> checkCardPowerInner(String serialNo, String vendingId) {

        ServiceResult<VendingCardPowerWrapperData> result = new ServiceResult<VendingCardPowerWrapperData>();
        try {
            // 检查卡/密码权限
            VendingCardPowerWrapperData vendingCardPowerWrapper = this.validateCardPower(serialNo, vendingId);
            result.setSuccess(true);
            result.setResult(vendingCardPowerWrapper);
        } catch (BusinessException be) {
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            Log.i(this.getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + e.getMessage());
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查检查卡/密码权限发生异常!");
        }
        return result;
    }

    /**
     * 检查售货机
     * 
     * @return
     * @throws SystemException
     */
    private VendingData validateVending() throws SystemException {
        VendingData vending = new VendingDbOper().getVending();
        if (vending == null) {
            throw new SystemException("系统数据异常!");
        }

        String vendingStatus = vending.getVd1Status();
        if (!VendingData.VENDING_STATUS_YES.equals(vendingStatus)) {
            throw new BusinessException("本台售货无法使用,请与系统管理员联系!");
        }

        return vending;
    }

    /**
     * 根据售货机货道号检查售货机货道
     * 
     * @param vendingChnCode
     *            货道编号
     * @param methodType
     *            : 一般领料 借还领料区分标识 VendingChnData.VENDINGCHN_METHOD_GENERAL： 一般领料
     *            ， VendingChnData.VENDINGCHN_METHOD_BORROW： 借还领料
     * @return
     */
    private VendingChnData validateVendingChn(String vendingChnCode, String methodType) {
        vendingChnCode = StringHelper.nullSafeString(vendingChnCode).trim();
        if (StringHelper.isEmpty(vendingChnCode, true)) {
            throw new BusinessException("货道号不能为空,请重新输入!");
        }

        VendingChnData vendingChn = new VendingChnDbOper().getVendingChnByCode(vendingChnCode);
        if (vendingChn == null) {
            throw new BusinessException("货道号  " + vendingChnCode + " 不存在,请重新输入!");
        }
        String vendingChnStatus = vendingChn.getVc1Status();
        if (!VendingChnData.VENDINGCHN_STATUS_NORMAL.equals(vendingChnStatus)) {
            throw new BusinessException("货道号 " + vendingChnCode + " 已冻结,请与系统管理员联系!");
        }

        String vendingChnSaleType = vendingChn.getVc1SaleType();
        String skuId = vendingChn.getVc1Pd1Id();
        if (VendingChnData.VENDINGCHN_METHOD_GENERAL.equals(methodType)) {
            if (VendingChnData.VENDINGCHN_SALETYPE_BORROW.equals(vendingChnSaleType)) {
                throw new BusinessException("货道号 " + vendingChnCode + " 只能'借/还',不能领料,请重新输入!");
            }
            // 判断SKUID是否为空，为空结束操作，提示：货道号XXX没有产品信息，不能领料，请重新输入！
            if (StringHelper.isEmpty(skuId, true)) {
                throw new BusinessException("货道号 " + vendingChnCode + " 没有产品信息,不能领料，请重新输入!");
            }
        }

        if (VendingChnData.VENDINGCHN_METHOD_BORROW.equals(methodType)) {
            if (!VendingChnData.VENDINGCHN_SALETYPE_BORROW.equals(vendingChnSaleType)) {
                throw new BusinessException("货道号 " + vendingChnCode + " 不能'借/还',请重新输入!");
            }

            String vendingChnType = vendingChn.getVc1Type();
            if (VendingChnData.VENDINGCHN_TYPE_VENDING.equals(vendingChnType)) {
                throw new BusinessException("货道号 " + vendingChnCode + " 非格子机,不能借/还,请重新输入！");
            }
        }

        return vendingChn;

    }

    /**
     * 外部领料检查卡/密码权限
     * 
     * @param paramType
     *            参数类型 CardData.CARD_SERIALNO_PARAM为卡序列号 CARD_PASSWORD_PARAM为卡密码
     * @param paramValue
     *            参数值
     * @param vendingId
     *            售货机ID
     * @return
     * @throws SystemException
     */
    private VendingCardPowerWrapperData validateCardPower(String paramType, String paramValue,
            String vendingId) throws SystemException {
        CardDbOper cardDb = new CardDbOper();
        CardData card = null;
        // 根据卡序列号查询
        if (CardData.CARD_SERIALNO_PARAM.equals(paramType)) {
            paramValue = StringHelper.nullSafeString(paramValue).trim();
            if (StringHelper.isEmpty(paramValue, true)) {
                throw new BusinessException("卡序列号不能为空,请重新输入!");
            }

            card = cardDb.getCardBySerialNo(paramValue);
        }
        // 根据卡密码查询
        if (CardData.CARD_PASSWORD_PARAM.equals(paramType)) {
            paramValue = StringHelper.nullSafeString(paramValue).trim();
            if (StringHelper.isEmpty(paramValue, true)) {
                throw new BusinessException("卡密码不能为空,请重新输入!");
            }
            card = cardDb.getCardByPassword(paramValue);
        }

        // 判断逻辑
        if (card == null) {
            throw new BusinessException("输入的卡号或密码不存在,请重新输入!");
        }

        //卡产品权限类型
        String cardPuductPowerType = card.getCd1ProductPower();

        String cardType = card.getCd1Type();// 卡类型
        if (!CardData.CARD_TYPE_CUS.equals(cardType)) {
            throw new BusinessException("输入的卡号或密码不能领料,请重新输入!");
        }

        String customerStatus = card.getCd1CustomerStatus();// 客户状态
        String status = card.getCd1Status();// 震坤行状态
        if (!(CardData.CARD_CUS_STATUS_YES.equals(customerStatus) && CardData.CARD_STATUS_YES.equals(status))) {
            throw new BusinessException("输入的卡号或密码不可用,请重新输入!");
        }
        // 获取卡ID
        String cardId = card.getCd1Id();
        CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerDbOper().getCusEmpCardPowerByCardId(cardId);

        if (cusEmpCardPower == null) {
            throw new BusinessException("输入的卡号或密码未分配给员工,请与系统管理员联系!");
        }
        // 获取客户员工ID
        String cusEmpId = cusEmpCardPower.getCe2Ce1Id();

        CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkDbOper().getCustomerEmpLinkByCeId(cusEmpId);
        if (customerEmpLink == null) {
            throw new SystemException("数据异常,客户不存在!");
        }
        // 获取客户ID
        String cusId = customerEmpLink.getCe1Cu1Id();
        String cusStatus = customerEmpLink.getCe1Status();
        if (CustomerEmpLinkData.CUS_EMP_STATUS_DISABLE.equals(cusStatus)) {
            throw new BusinessException("员工状态不可用,请重新输入!");
        }

        VendingCardPowerData vendingCardPower = new VendingCardPowerDbOper().getVendingCardPower(cusId,
                vendingId, cardId);

        if (vendingCardPower == null) {
            throw new BusinessException("输入的卡号或密码未分配给本台售货机,请与系统管理员联系!");
        }
        VendingCardPowerWrapperData vendingCardPowerWrapper = new VendingCardPowerWrapperData();
        vendingCardPowerWrapper.setVendingCardPowerData(vendingCardPower);
        vendingCardPowerWrapper.setCusEmpId(cusEmpId);
        vendingCardPowerWrapper.setCardPuductPowerType(cardPuductPowerType);
        return vendingCardPowerWrapper;
    }

    /**
     * 内部操作检查卡/密码权限
     * 
     * @param serialNo
     *            卡序列号
     * @param vendingId
     *            售货机ID
     * @return
     * @throws SystemException
     */
    private VendingCardPowerWrapperData validateCardPower(String serialNo, String vendingId)
            throws SystemException {
        serialNo = StringHelper.nullSafeString(serialNo).trim();
        if (StringHelper.isEmpty(serialNo, true)) {
            throw new BusinessException("卡序列号不能为空,请重新输入!");
        }
        CardDbOper cardDb = new CardDbOper();
        // 根据卡序列号查询
        CardData card = cardDb.getCardBySerialNo(serialNo);
        // 判断逻辑
        if (card == null) {
            throw new BusinessException("输入的卡号不存在,请重新输入!");
        }

        String cardType = card.getCd1Type();// 卡类型
        if (!CardData.CARD_TYPE_ZKH.equals(cardType)) {
            throw new BusinessException("输入的卡号不是震坤行内部卡，请重新输入！");
        }

        String status = card.getCd1Status();// 震坤行状态
        if (CardData.CARD_STATUS_NO.equals(status)) {
            throw new BusinessException("输入的卡号不可用，请重新输入！");
        }
        // 获取卡ID
        String cardId = card.getCd1Id();
        CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerDbOper().getCusEmpCardPowerByCardId(cardId);

        if (cusEmpCardPower == null) {
            throw new BusinessException("输入的卡号或密码未分配给员工,请与系统管理员联系!");
        }
        // 获取客户员工ID
        String cusEmpId = cusEmpCardPower.getCe2Ce1Id();

        VendingCardPowerData vendingCardPower = new VendingCardPowerDbOper().getVendingCardPower(vendingId,
                cardId);

        if (vendingCardPower == null) {
            throw new BusinessException("输入的卡号未分配给本台售货机，请与系统管理员联系！");
        }
        VendingCardPowerWrapperData vendingCardPowerWrapper = new VendingCardPowerWrapperData();
        vendingCardPowerWrapper.setVendingCardPowerData(vendingCardPower);
        vendingCardPowerWrapper.setCusEmpId(cusEmpId);
        return vendingCardPowerWrapper;
    }

    /**
     * 根据期间设置，间隔开始，间隔结束，起始时间确定查询时间
     * 
     * @param periodStr
     * @param intervalStartStr
     * @param intervalFinishStr
     * @param startDateStr
     * @return
     */
    public Date getDate(String periodStr, String intervalStartStr, String intervalFinishStr,
            String startDateStr) {
        if (StringHelper.isEmpty(startDateStr, true))
            return null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date startDate = DateHelper.parse(startDateStr, pattern);
        int period = ConvertHelper.toInt(periodStr, 0);
        if (period >= Constant.YEAR && period <= Constant.HOUR) {
            Date date = null;
            Date tmpDate = new Date();
            // 期间设置为年时
            int intervalStart = ConvertHelper.toInt(intervalStartStr, 0);
            switch (period) {
            case Constant.YEAR:
                date = DateHelper.add(tmpDate, Calendar.YEAR, -intervalStart); // date减intervalStart
                break;
            case Constant.MONTH:
                // 期间设置为月时
                date = DateHelper.add(tmpDate, Calendar.MONTH, -intervalStart); // month减intervalStart
                break;
            case Constant.DAY:
                // 期间设置为日时
                date = DateHelper.add(tmpDate, Calendar.DAY_OF_MONTH, -intervalStart); // date减intervalStart

                // 更改设置为当天的零点，不再是24小时之前 forever add
                date = DateHelper.getDateZero(DateHelper.add(date, Calendar.DAY_OF_MONTH, 1));

                break;
            case Constant.HOUR:
                // 期间设置为小时时
                date = DateHelper.add(tmpDate, Calendar.HOUR, -intervalStart); // hour减intervalStart
                break;
            default:
                break;
            }
            if (date.before(startDate)) {
                return startDate;
            } else {
                return date;
            }
        } else if (period == Constant.TIME) {
            // 期间设置为时间段时
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                return null;
            }
            String startYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
            Date intervalStart = DateHelper.parse(startYMD + " " + intervalStartStr, "yyyy-MM-dd HH:mm:ss");
            Date intervalFinish = DateHelper.parse(startYMD + " " + intervalFinishStr, "yyyy-MM-dd HH:mm:ss");
            // 如果当前时间在起始时间之前或者在结束时间之后，忽略，否则取起始时间
            if (currentDate.before(intervalStart) || currentDate.after(intervalFinish)) {
                return null;
            } else {
                return intervalStart;
            }
        }
        return null;
    }
    
    /**
     * 根据期间设置，间隔开始，间隔结束，起始时间确定查询时间
     * 查询时间为分段制，例如天，即每n天为一个查询时间段
     * @param periodStr 类型
     * @param intervalStartStr 时间间隔
     * @param intervalFinishStr 
     * @param startDateStr 开始时间
     * @return
     */
    public Date getDate2(String periodStr, String intervalStartStr, String intervalFinishStr,
            String startDateStr) {
        if (StringHelper.isEmpty(startDateStr, true))
            return null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date startDate = DateHelper.parse(startDateStr, pattern);
        int period = ConvertHelper.toInt(periodStr, 0);
        if (period >= Constant.YEAR && period <= Constant.HOUR) {
            Date date = null;
            Date tmpDate = new Date();
            // 期间设置为年时
            int intervalStart = ConvertHelper.toInt(intervalStartStr, 0);
            switch (period) {
            case Constant.YEAR:
                long cha = DateHelper.cha(tmpDate, startDate, Calendar.YEAR);
                int yu = (int) (cha % intervalStart);
                
                date = DateHelper.add(tmpDate, Calendar.YEAR, -yu); // date减intervalStart
                break;
            case Constant.MONTH:

                cha = DateHelper.cha(tmpDate, startDate, Calendar.MONTH);
                yu = (int) (cha % intervalStart);
                
                // 期间设置为月时
                date = DateHelper.add(tmpDate, Calendar.MONTH, -yu); // month减intervalStart
                break;
            case Constant.DAY:

                cha = DateHelper.cha(tmpDate, startDate, Calendar.DATE);
                yu = (int) (cha % intervalStart);
                
                // 期间设置为日时
                date = DateHelper.add(tmpDate, Calendar.DAY_OF_MONTH, -yu); // date减intervalStart
                
                // 更改设置为当天的零点，不再是24小时之前 forever add
                date = DateHelper.getDateZero(date);
                
                break;
            case Constant.HOUR:

                cha = DateHelper.cha(tmpDate, startDate, Calendar.HOUR);
                yu = (int) (cha % intervalStart);
                
                // 期间设置为小时时
                date = DateHelper.add(tmpDate, Calendar.HOUR, -yu); // hour减intervalStart
                break;
            default:
                break;
            }
            if (date.before(startDate)) {
                return startDate;
            } else {
                return date;
            }
        } else if (period == Constant.TIME) {
            // 期间设置为时间段时
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                return null;
            }
            String startYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
            Date intervalStart = DateHelper.parse(startYMD + " " + intervalStartStr, "yyyy-MM-dd HH:mm:ss");
            Date intervalFinish = DateHelper.parse(startYMD + " " + intervalFinishStr, "yyyy-MM-dd HH:mm:ss");
            // 如果当前时间在起始时间之前或者在结束时间之后，忽略，否则取起始时间
            if (currentDate.before(intervalStart) || currentDate.after(intervalFinish)) {
                return null;
            } else {
                return intervalStart;
            }
        }
        return null;
    }

    /**
     * 构建库存交易对象
     * 
     * @param qty
     * @param billType
     * @param billCode
     * @param vendingChn
     * @param vendingCardPower
     * @return
     */
    public StockTransactionData buildStockTransaction(int qty, String billType, String billCode,
            VendingChnData vendingChn, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        VendingCardPowerData vendingCardPower = vendingCardPowerWrapper.getVendingCardPowerData();
        String cardId = vendingCardPower.getVc2Cd1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        String skuId = vendingChn.getVc1Pd1Id();
        String saleType = vendingChn.getVc1SaleType();
        String supplierId = vendingChn.getVc1Sp1Id();
        SupplierData supplier = new SupplierDbOper().getSupplierBySpId(supplierId);
        String spCode = "";
        String spName = "";
        if (supplier != null) {
            spCode = supplier.getSp1Code();
            spName = supplier.getSp1Name();
        }

        StockTransactionData stockTransaction = new StockTransactionData();
        stockTransaction.setTs1Id(UUID.randomUUID().toString());
        stockTransaction.setTs1BillType(billType);
        stockTransaction.setTs1M02Id("");
        stockTransaction.setTs1BillCode(billCode);
        stockTransaction.setTs1Cd1Id(cardId);
        stockTransaction.setTs1Vd1Id(vendingId);
        stockTransaction.setTs1Pd1Id(skuId);
        stockTransaction.setTs1TransQty(qty);
        stockTransaction.setTs1Vc1Code(vendingChnCode);
        stockTransaction.setTs1TransType(saleType);
        stockTransaction.setTs1Sp1Code(spCode);
        stockTransaction.setTs1Sp1Name(spName);
        stockTransaction.setTs1CreateUser(cusEmpId);
        stockTransaction.setTs1UploadStatus(StockTransactionData.UPLOAD_UNLOAD);
        Date currentDate = DateHelper.currentDateTime();
        String dateTime = DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss");
        stockTransaction.setTs1CreateTime(dateTime);
        stockTransaction.setTs1ModifyUser(cusEmpId);
        stockTransaction.setTs1ModifyTime(dateTime);
        stockTransaction.setTs1RowVersion(currentDate.getTime() + "");
        return stockTransaction;

    }

    public StockTransactionData buildStockTransaction(int qty, String billType, String billCode,
            String vendingId, String vendingChnCode, String skuId, String saleType, String supplierId,
            VendingCardPowerWrapperData vendingCardPowerWrapper) {
        VendingCardPowerData vendingCardPower = vendingCardPowerWrapper.getVendingCardPowerData();
        String cardId = vendingCardPower.getVc2Cd1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        SupplierData supplier = new SupplierDbOper().getSupplierBySpId(supplierId);
        String spCode = "";
        String spName = "";
        if (supplier != null) {
            spCode = supplier.getSp1Code();
            spName = supplier.getSp1Name();
        }

        StockTransactionData stockTransaction = new StockTransactionData();
        stockTransaction.setTs1Id(UUID.randomUUID().toString());
        stockTransaction.setTs1BillType(billType);
        stockTransaction.setTs1M02Id("");
        stockTransaction.setTs1BillCode(billCode);
        stockTransaction.setTs1Cd1Id(cardId);
        stockTransaction.setTs1Vd1Id(vendingId);
        stockTransaction.setTs1Pd1Id(skuId);
        stockTransaction.setTs1TransQty(qty);
        stockTransaction.setTs1Vc1Code(vendingChnCode);
        stockTransaction.setTs1TransType(saleType);
        stockTransaction.setTs1Sp1Code(spCode);
        stockTransaction.setTs1Sp1Name(spName);
        stockTransaction.setTs1CreateUser(cusEmpId);
        stockTransaction.setTs1UploadStatus(StockTransactionData.UPLOAD_UNLOAD);
        Date currentDate = DateHelper.currentDateTime();
        stockTransaction.setTs1CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockTransaction.setTs1ModifyUser(cusEmpId);
        stockTransaction.setTs1ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockTransaction.setTs1RowVersion(currentDate.getTime() + "");
        return stockTransaction;
    }

    /**
     * 查询销售类型不等于借还状态的所有货道记录
     * 
     * @return
     */
    public List<VendingChnData> findAllVendingChnBySaleType() {
        List<VendingChnData> vendingChnList = new VendingChnDbOper()
                .findAllVendingChn(VendingChnData.VENDINGCHN_SALETYPE_BORROW);
        return vendingChnList;
    }

    /**
     * 查询“售货机货道”“产品”表（条件：售货机货道.销售类型<>借/还），列出“售货机货道.货道编号“”产品名称“
     * 
     * @return
     */
    public List<VendingChnProductWrapperData> findChnCodeProductName() {
        List<VendingChnData> vendingChnList = new VendingChnDbOper()
                .findAllVendingChn(VendingChnData.VENDINGCHN_SALETYPE_BORROW);

        return this.findChnCodeProductName(vendingChnList);

    }

    /**
     * 查询“售货机货道”“产品”表（条件：售货机货道.销售类型<>借/还），列出“售货机货道.货道编号“”产品名称“
     * 
     * @param vendingChnList
     *            售货机货道列表
     * @return
     */
    public List<VendingChnProductWrapperData> findChnCodeProductName(List<VendingChnData> vendingChnList) {
        List<VendingChnProductWrapperData> returnList = new ArrayList<VendingChnProductWrapperData>();
        Map<String, String> map = new ProductDbOper().findAllProduct();
        for (VendingChnData vendingChn : vendingChnList) {
            VendingChnProductWrapperData wrapper = new VendingChnProductWrapperData();
            String skuId = vendingChn.getVc1Pd1Id();
            if (StringHelper.isEmpty(skuId, true))
                continue;
            if (map.get(skuId) == null)
                continue;
            String productName = map.get(skuId);
            wrapper.setProductName(productName);
            wrapper.setVendingChn(vendingChn);
            returnList.add(wrapper);
        }
        return returnList;
    }

    /**
     * 构建售货机货道库存对象
     * 
     * @param vendingId
     *            售货机ID
     * @param vendingChnCode
     *            售货机货道编号
     * @param skuId
     *            skuID
     * @param qty
     *            货道库存数量
     * @return
     */
    public VendingChnStockData buildVendingChnStock(String vendingId, String vendingChnCode, String skuId,
            int qty) {
        VendingChnStockData stockData = new VendingChnStockData();
        stockData.setVs1Id(UUID.randomUUID().toString());
        stockData.setVs1M02Id("");
        stockData.setVs1Vd1Id(vendingId);
        stockData.setVs1Vc1Code(vendingChnCode);
        stockData.setVs1Pd1Id(skuId);
        stockData.setVs1Quantity(qty);
        Date currentDate = DateHelper.currentDateTime();
        stockData.setVs1CreateUser("");
        stockData.setVs1CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockData.setVs1ModifyUser("");
        stockData.setVs1ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockData.setVs1RowVersion(currentDate.getTime() + "");
        return stockData;
    }

    /**
     * 领料成功，新增库存交易记录,库存交易记录新增成功，更新售货机货道库存
     * 
     * @param inputQty
     *            输入领料数量
     * @param vendingChn
     *            售货机货道对象
     * @param vendingCardPower
     *            售货机卡/密码权限对象
     */
    public void saveStockTransaction(int inputQty, VendingChnData vendingChn,
            VendingCardPowerWrapperData vendingCardPowerWrapper) {
        StockTransactionData stockTransaction = this.buildStockTransaction(inputQty * (-1),
                StockTransactionData.BILL_TYPE_GET, "", vendingChn, vendingCardPowerWrapper);
        boolean flag = new StockTransactionDbOper().addStockTransaction(stockTransaction);

        if (flag) {
            // 更新售货机货道库存.库存数量=售货机货道库存.库存数量+领料数量(注意领料数量为负数)
            new VendingChnStockDbOper().updateStockQuantity(inputQty * (-1), vendingChn);
        }
    }
}
