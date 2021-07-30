package com.askia.common.base;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Activity和Fragment管理
 *
 * @name ViewManager
 */
public class ViewManager {

    private static Stack<Activity> activityStack;
    private static List<BaseFragment> fragmentList;
    private static Activity sOperatorActivity;

    public static Activity getOperatorActivity() {
        return sOperatorActivity;
    }

    public static void setOperatorActivity(Activity operatorActivity) {
        sOperatorActivity = operatorActivity;
    }

    public static ViewManager getInstance() {
        return ViewManagerHolder.sInstance;
    }

    private static class ViewManagerHolder {
        private static final ViewManager sInstance = new ViewManager();
    }

    private ViewManager() {
    }

    public void addFragment(int index, BaseFragment fragment) {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(index, fragment);
    }


    public BaseFragment getFragment(int index) {
        if (fragmentList != null) {
            return fragmentList.get(index);
        }
        return null;
    }


    public List<BaseFragment> getAllFragment() {
        if (fragmentList != null) {
            return fragmentList;
        }
        return null;
    }


    /**
     * 添加指定Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     *  activity onresume时调用  确保当前顶层的activity总在栈顶
     * @param activity
     */
    public void onResumeActivity(Activity activity)
    {
        if (activityStack != null && activityStack.contains(activity) && activity != activityStack.lastElement())
        {
            activityStack.remove(activity);
            activityStack.add(activity);
        }
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
           // activity.finish();
            activity = null;
        }
    }


    /**
     * 结束指定Class的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }


    /**
     * 结束全部的Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束其他全部的Activity
     */
    public void finishAllOtherActivitys(Class<?> cls) {
        int index = activityStack.size() - 1;
        for (int i = index; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                if (activityStack.get(i).getClass().equals(cls)) {
                    continue;
                }
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
        LogUtils.d("currentActivity:" + currentActivity() + "activityStack Size:" + activityStack.size());
    }

    public void finishAllOtherActivitys(Activity activity) {
        if(activityStack == null)
            return;
        int index = activityStack.size() - 1;
        for (int i = index; i >= 0; i--) {
            if (null != activityStack.get(i)) {
                if (activity.getClass().getName().equals(activityStack.get(i).getClass().getName())) {
                    continue;
                }
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {
        try {
            finishAllActivity();
            //杀死后台进程需要在AndroidManifest中声明android.permission.KILL_BACKGROUND_PROCESSES；
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
        } catch (Exception e) {
            Log.e("ActivityManager", "app exit" + e.getMessage());
        }
    }
}
