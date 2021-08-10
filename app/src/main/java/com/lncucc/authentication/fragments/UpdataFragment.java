package com.lncucc.authentication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.viewmodel.CheckUpdateViewModel;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentUpdataBinding;

import java.io.File;

import static com.askia.coremodel.rtc.Constants.apkPath;

/**
 * Create bt she:
 * 升级页面
 *
 * @date 2021/8/10
 */
public class UpdataFragment extends BaseFragment {
    private FragmentUpdataBinding mBinding;
    private CheckUpdateViewModel mViewModel;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {
        mViewModel = ViewModelProviders.of(this).get(CheckUpdateViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_updata, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        mViewModel.getmCheckVersionData().observe(this, new Observer<CheckVersionData>() {
            @Override
            public void onChanged(CheckVersionData checkVersionData) {
                if (checkVersionData.isSuccess()) {
                    //下载安装包并且安装
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(checkVersionData.getResult()));
                    startActivity(intent);
//                    mViewModel.DownApk(checkVersionData.getResult());
                } else {
                    Toast.makeText(getContext(), "没有新的更新包", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewModel.getSdCardData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")) {
                    installAPK();
                }
            }
        });

    }
    private void installAPK() {
        File apkFile = new File(apkPath + "/download.apk");
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = getContext().getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (getContext().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    getContext().startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }

    public void getUpData(View view) {
        mViewModel.checkVersion(getVersion(getContext()));
    }


    public String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "null";
        }
    }
}
