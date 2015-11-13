package com.mc.vending.tools;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 用来关闭所有的activity
 * 
 * @author HuNan
 *
 */
public class ActivityManagerTool extends Application {

    private List<Activity>             activities       = new LinkedList<Activity>();

    private static ActivityManagerTool manager;

    private boolean                    isExist          = false;                     //activity 存在标志

    private List<Class<?>>             bottomActivities = new LinkedList<Class<?>>(); //底部导航类集合

    /**
     * 获得 activity管理对象
     * 
     * @return
     */
    public static ActivityManagerTool getActivityManager() {
        if (null == manager) {
            manager = new ActivityManagerTool();
        }
        return manager;
    }

    /**
     * 添加新的activity
     * 
     * @param activity
     * @return
     */
    public boolean add(Activity activity) {

        if (activity == null) {
            Log.e("ActivityManagerTool_add", "activity = null");
            return false;
        }

        int position = 0;
        //导航栏activity进栈，删除非导航栏activity
        if (isBottomActivity(activity)) {

            for (int i = 0; i < activities.size(); i++) {

                if (!isBottomActivity(activities.get(i))) {
                    popActivity(activities.get(i));
                    i--;
                } else if (activities.get(i).getClass().equals(activity.getClass())) {//获得重复activity位置
                    isExist = true;
                    position = i;
                }
                //				
                //				//获得重复activity位置
                //				if (i >= 0 && activities.get(i).getClass().equals(activity.getClass())) {
                //					isExist = true;
                //					position = i;
                //				}

            }

        }

        if (!activities.add(activity)) {
            return false;
        }
        //删除重复activity
        if (isExist) {
            isExist = false;
            activities.remove(position);
        }

        return true;
    }

    /**
     * 关闭除参数activity外的所有activity
     * 
     * @param activity
     */
    public void finish(Activity activity) {

        if (activity == null) {
            Log.e("ActivityManagerTool_finish", "activity = null");
            return;
        }

        for (Activity iterable : activities) {
            if (activity != iterable) {
                iterable.finish();
            }
        }
    }

    /**
     * 关闭所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        ZillionLog.i(this.getClass().getName(),"退出系统");
        //System.out.println("退出系统");
        System.exit(0);
    }

    /**
     * 删除指定activity
     * 
     * @param activity
     */
    public void popActivity(Activity activity) {

        if (activity != null) {
            activity.finish();
            activities.remove(activity);
            activity = null;
        }

    }

    /**
     * 获得当前activity
     * 
     * @return
     */
    public Activity currentActivity() {
        if (activities.size() <= 0) {
            Log.e("ActivityManagerTool_currentActivity", "currentActivity = null");
            return null;
        }
        Activity activity = activities.get(activities.size() - 1);

        return activity;
    }

    /**
     * activity是否为底部导航
     * 
     * @return
     */
    public boolean isBottomActivity(Activity activity) {

        for (int i = 0; i < bottomActivities.size(); i++) {
            if (activity.getClass() != bottomActivities.get(i)) {

            } else {
                return true;
            }
        }

        return false;
    }

    /**
     * 如需返回IndexActivity则返回IndexActivity
     * 
     * @param activity
     * @param context
     */
    public void backIndex(Context context, Class<?> indexActivity) {

        if (activities.size() <= 0) {
            Log.e("ActivityManagerTool_backIndex", "activities.size() <= 0");
            return;
        }

        if (isBottomActivity(activities.get(activities.size() - 1))) {
            Intent intent = new Intent();
            intent.setClass(context, indexActivity);
            context.startActivity(intent);
        }
    }

    /**
     * 删除已经finish的activity
     * 
     * @param activity
     */
    public void removeActivity(Activity activity) {

        if (activity != null) {
            activities.remove(activity);
        }
    }

    /**
     * 初始化，存储底部导航类
     * 
     * @param activityClass
     */
    public void setBottomActivities(Class<?> activityClass) {
        bottomActivities.add(activityClass);
    }

    /**
     * 得到当前界面行为统计的界面Id
     * 
     * @param activity
     *            当前界面实例
     * @return 当前界面行为统计id 如果在action_stat.xml的没有配置此界面，返回为 "";
     * @author zhai
     */
    public String getCurrentActivitytId(Context activity) {
        String actIdStr = "";
        if (null == activity) {
            return actIdStr;
        }
        int actResourceId = activity.getResources().getIdentifier(activity.getClass().getName(), "string",
                activity.getPackageName());
        if (actResourceId > 0) {
            actIdStr = activity.getString(actResourceId);
        }
        return actIdStr;
    }

    /**
     * 根据传入的字符串从配置文件中获取ID
     * 
     * @param activity
     *            当前界面实例
     * @param pageFlag
     *            将根据这个标识从配置文件中获取ID
     * @return 当前界面行为统计id 如果在action_stat.xml的没有配置此界面，返回为 "";
     */
    public static String getCurrentActivitytId(Context activity, String pageFlag) {
        String actIdStr = "";
        if (null == activity) {
            return actIdStr;
        }
        int actResourceId = activity.getResources().getIdentifier(pageFlag, "string",
                activity.getPackageName());
        /* 如果没有找到返回0 */
        if (actResourceId > 0) {
            actIdStr = activity.getString(actResourceId);
        }
        return actIdStr;
    }

    /**
     * 删除非底部导航activity
     */
    public void delNotBottomActivity() {
        for (int i = 0; i < activities.size(); i++) {

            if (!isBottomActivity(activities.get(i))) {
                popActivity(activities.get(i));
                if (i >= 1) {
                    i--;
                }
            }
        }
    }

    /**
     * 判断当前activity是否存在，用于tabbar切换
     * 
     * @param activity
     * @return
     */
    public Activity activityExist(Class<?> c) {
        if (c == null) {
            return null;
        }
        for (int i = 0; i < activities.size(); i++) {
            if (c.equals(activities.get(i).getClass())) {
                return activities.get(i);
            }
        }
        return null;
    }
}
