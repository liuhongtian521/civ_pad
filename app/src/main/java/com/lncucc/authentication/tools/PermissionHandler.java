package com.lncucc.authentication.tools;

import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION_CODES;

import androidx.annotation.Keep;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限申请工具
 */
public abstract class PermissionHandler {

    private Activity mActivity;
    private int mRequestCode;

    /** 记得重写 Activity 的 onRequestPermissionsResult 方法, 把结果传入这个类的 {@link #onRequestPermissionsResult(int, String[], int[])} */
    public PermissionHandler(Activity activity) {
        mActivity = activity;
    }

    /**
     * @throws GetPermissionsException 默认会自动探测需要的权限, 但是自动探测可能失败. 你可以<br>
     * 1. 重写 {@link PermissionHandler#onGetPermissions()} 方法, 手动声明需要的权限, 避免抛出异常<br>
     * 2. catch 异常后调用 {@link PermissionHandler#start(String[])} 方法以继续业务逻辑
     */
    public void start() throws GetPermissionsException {
        String[] permissions = onGetPermissions();
        start(permissions);
    }

    public void start(String[] permissions) {
        String[] no = checkPermissions(permissions);
        if (no == null || no.length == 0) {
            onAllPermissionGranted();
        } else {
            doRequestPermissions(no);
        }
    }

    /** 把 Activity 的回调交到这里处理 */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        List<String> declinedPermissions = new ArrayList<>();
        if (requestCode == mRequestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    declinedPermissions.add(permissions[i]);
                }
            }
            int declinedPermissionsCount = declinedPermissions.size();
            if (declinedPermissionsCount == 0) {
                onAllPermissionGranted();
            } else {
                onPermissionsDecline(declinedPermissions.toArray(new String[declinedPermissionsCount]));
            }
        }
    }

    /**
     * 收集需要申请的权限, 例如 {@link permission#READ_EXTERNAL_STORAGE}
     * @throws GetPermissionsException 默认会自动探测需要的权限, 但是自动探测可能失败
     */
    @Keep
    protected String[] onGetPermissions() throws GetPermissionsException {
        PackageInfo info = null;
        try {
            info = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (NameNotFoundException e) {
            throw new GetPermissionsException("Detect required permissions fail", e);
        }
        if (info == null) {
            throw new GetPermissionsException("Detect required permissions fail, PackageManager.getPackageInfo() == null");
        }
        if (info.requestedPermissions == null) {
            throw new GetPermissionsException("Detect required permissions fail, PackageInfo.requestedPermissions == null");
        }
        return info.requestedPermissions;

    }

    /**
     * 自动探测需要的权限, 但是自动探测可能失败
     */
    public static class GetPermissionsException extends Exception {

        public GetPermissionsException(String message) {
            super(message);
        }

        public GetPermissionsException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /** 通知用户: 被拒绝的权限, 此时可以调用 {@link #start(String[])} 再次尝试 */
    @Keep
    protected  void onPermissionsDecline(String[] permissions){}

    /** 通知用户: 已经获得全部权限 */
    @Keep
    protected abstract void onAllPermissionGranted();

    /** 检查权限, 返回尚未获得的权限 */
    private String[] checkPermissions(String[] permissions) {
        List<String> missingPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (!hasPermission(p)) {
                missingPermissions.add(p);
            }
        }
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            missingPermissions.remove(permission.WRITE_SETTINGS);//API 23 或以上, 无法通过授权对话框获得授权    
        }
        return missingPermissions.toArray(new String[missingPermissions.size()]);
    }

    /** 判断是否具有权限 */
    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /** 发起权限申请 */
    private void doRequestPermissions(String[] permissions) {
        mRequestCode = 1024;//随便定的
        ActivityCompat.requestPermissions(mActivity, permissions, mRequestCode);
    }

}
