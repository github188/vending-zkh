package com.mc.vending.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;

/**
 * 日期时间类型的工具类
 * 
 * @Filename: DateUtil.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu@mightcloud.com
 *
 */
public class DateHelper {
    private static long MS_IN_DAY = 24 * 60 * 60 * 1000;

    /**
     * 获取当前日期时间，包括年、月、日，也包括时、分、秒、毫秒。
     * 
     * @return
     */
    public static Date currentDateTime() {
        return new Date();
    }

    /**
     * 获取当前日期，包括年、月、日（时、分、秒、毫秒均为0）。
     * 
     * @return
     */
    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天的零点零分 例如：2015-03-04 22:43:09 返回 2015-03-04 00:00:00. 如果正好是零点，不处理
     */
    public static Date getDateZero(Date date) {
        return parse(format(date, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间，包括时、分、秒、毫秒，日期为0001-01-01（无法用{@link Date}表示0000-00-00）。
     * 
     * @return
     */
    public static Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() % MS_IN_DAY);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 用指定的年、月、日创建{@link Date}对象。
     * 
     * @param year
     *            年份
     * @param month
     *            月份，1为1月，2为2月，以此类推（与JDK的{@link Calendar}不同，{@link Calendar}
     *            以0为起始索引）。
     * @param dayOfMonth
     * @return
     * @throws ArgumentException
     *             年月日参数错误时抛出该异常。
     */
    public static Date getDate(int year, int month, int dayOfMonth) {
        if (month <= 0 || month > 12)
            throw new RuntimeException("有效月份值必须从1开始，小于等于12，1表示1月，2表示2月，以此类推");
        if (year < 0)
            throw new RuntimeException("年份必须大于等于0");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 格式化日期、时间。
     * 
     * @param date
     *            要格式化的日期对象。
     * @param pattern
     *            格式化字符串。
     * @return 格式化后的字符串。
     *         <p>
     *         date为null时，返回空字符串""；pattern为null或者空字符串""时，返回
     *         {@link Date#toString()}。
     *         <p>
     *         格式化过程发生异常，将记录详细异常信息，并返回{@link Date#toString()}。
     * @throws BusinessException
     *             格式化异常时。
     */
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        pattern = pattern == null ? "" : pattern.trim();
        if ("".equals(pattern))
            return date.toString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Throwable e) {
            throw new RuntimeException("无法格式化日期时间：" + date.toString() + ", 格式：" + pattern);
        }
    }

    /**
     * 将字符串表示的日期时间转化为{@link Date}。
     * 
     * @param dateString
     *            日期时间字符串。
     * @param pattern
     *            格式。
     * @return dateString、pattern无效时返回null；转换过程发生异常时返回null，异常信息记录在日志中。
     */
    public static Date parse(String dateString, String pattern) {
        dateString = dateString == null ? "" : dateString.trim();
        pattern = pattern == null ? "" : pattern.trim();
        if ("".equals(dateString) || "".equals(pattern))
            return null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String dateString) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        dateString = dateString == null ? "" : dateString.trim();
        pattern = pattern == null ? "" : pattern.trim();
        if ("".equals(dateString) || "".equals(pattern))
            return null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对日期、时间进行加、减操作。
     * 
     * <pre>
     * DateUtil.add(date, Calendar.YEAR, -1); // date减一年
     * DateUtil.add(date, Calendar.HOUR, -4); // date减4个小时
     * DateUtil.add(date, Calendar.MONTH, 3); // date加3个月
     * </pre>
     * 
     * @param date
     *            日期时间。
     * @param field
     *            执行加减操作的属性，参考{@link Calendar#YEAR}、{@link Calendar#MONTH}、
     *            {@link Calendar#HOUR}等。
     * @param amount
     *            加减数量。
     * @return 执行加减操作后的日期、时间。
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 去除时间（小时分秒）部分，保留日期（年月）部分＋－dateIndex。
     * 
     * @param date
     *            要操作的日期时间对象。
     * @return 去除时间部分后的日期时间对象。
     */
    public static Date truncateTime(Date date, int dateIndex) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateIndex);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 去除时间（时分秒）部分，保留日期（年月日）部分。
     * 
     * @param date
     *            要操作的日期时间对象。
     * @return 去除时间部分后的日期时间对象。
     */
    public static Date truncateTime(Date date) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 去除日期（年月日）部分，保留时间（时分秒）部分。
     * 
     * @param date
     *            要操作的日期时间对象。
     * @return 去除日期部分后的日期时间对象（返回的日期部分为0001-01-01，因为无法用{@link Date}表示0000-00-00）。
     */
    public static Date truncateDate(Date date) {
        if (date == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }

    public static long cha(Date startTime, Date endTime, int type) {
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        //获得两个时间的毫秒时间差异

        long cha = 0;

        long diff = endTime.getTime() - startTime.getTime();
        switch (type) {
        case Calendar.YEAR:
            cha = endTime.getYear() - startTime.getYear();
            break;
        case Calendar.MONTH:
            cha = (endTime.getYear() - startTime.getYear()) * 12
                    + (endTime.getMonth() - startTime.getMonth());//计算差多少天
            break;
        case Calendar.DATE:
            cha = diff / nd;//计算差多少天
            break;
        case Calendar.HOUR:
            diff = endTime.getTime() - getDateZero(endTime).getTime();
            cha = (diff % nd / nh);//计算差多少小时
            break;
        default:
            break;
        }
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");

        return cha;
    }

    public static void main(String[] args) {
        Date cDate = new Date();
        System.out.println(cDate);
        long a = 0;
        a = cha(parse("2015-09-14 10:10:22"), cDate, Calendar.DATE);
        System.out.println(a);
        a = cha(parse("2015-09-18 10:10:22"), cDate, Calendar.HOUR);
        System.out.println(a);
        a = cha(parse("2013-09-18 10:10:22"), cDate, Calendar.YEAR);
        System.out.println(a);
        a = cha(parse("2015-09-18 10:10:22"), cDate, Calendar.MONTH);
        System.out.println(a);
    }

}