package com.askia.common.util;

import android.content.Context;
import android.provider.Settings;

import com.apkfuns.logutils.LogUtils;
import com.askia.coremodel.util.ShellUtils;
import com.ys.rkapi.MyManager;

import java.io.IOException;

public class ScreenUtil
{
    public static void hideBottomUIMenu(Context context)
    {
        try {
            MyManager myManager = MyManager.getInstance(context);
            myManager.hideNavBar(true);
            myManager.setSlideShowNavBar(false);
         //   Runtime.getRuntime().exec("settings put global policy_control immersive.full=*");
          //  ShellUtils.CommandResult result =  ShellUtils.execCmd("settings put global policy_control immersive.full=*", false);

         //   LogUtils.d(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ShellUtils.execCmd("settings put global policy_control immersive.full=*", false);
    }

    public static void showBottomUIMenu(Context context)
    {
        try {
            MyManager myManager = MyManager.getInstance(context);
            myManager.hideNavBar(false);
            myManager.setSlideShowNavBar(true);

            //Runtime.getRuntime().exec("settings put global policy_control null");
            //ShellUtils.CommandResult result =  ShellUtils.execCmd("settings put global policy_control null", false);
     //       LogUtils.d(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
