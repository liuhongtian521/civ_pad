package com.lncucc.authentication.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.viewmodel.CheckUpdateViewModel;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActUpdateBinding;

import java.io.File;

import static com.askia.coremodel.rtc.Constants.apkPath;

/**
 * Create bt she:
 * 系统设置
 *
 * @date 2021/8/10
 */
@Route(path = ARouterPath.SYSTEM_INFO)
public class UpdateActivity extends BaseActivity {
    private ActUpdateBinding mBinding;
    private CheckUpdateViewModel mViewModel;

    @Override
    public void onInit() {
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        ((TextView)findViewById(R.id.tv_title)).setText("系统信息");

        initView();
    }

    @Override
    public void onInitViewModel() {
        mViewModel = ViewModelProviders.of(this).get(CheckUpdateViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_update);
    }

    private void initView() {
        File datapath = Environment.getDataDirectory();
        StatFs dataFs = new StatFs(datapath.getPath());

        long sizes = (long) dataFs.getFreeBlocks() * (long) dataFs.getBlockSize();
        long available = sizes / ((1024 * 1024));

        //身份信息总数
        mBinding.tvViewOne.setText(DBOperation.getCount());
        //数据存储量
        mBinding.tvViewTwo.setText("29902kb");
        //设备ID
        mBinding.tvViewThree.setText(DeviceUtils.getAndroidID());
        //软件版本
        mBinding.tvViewFour.setText(AppUtils.getAppVersionName());
        //出厂日期
        mBinding.tvViewFive.setText("20190803");
        //品牌
        mBinding.tvViewSix.setText(DeviceUtils.getManufacturer());
        //验证记录总数
        mBinding.tvViewSeven.setText(DBOperation.getAuthCount());
        //剩余存储量
        mBinding.tvViewEight.setText(available + "kb");
        //设备型号
        mBinding.tvViewNine.setText(DeviceUtils.getModel());
        //算法版本
        mBinding.tvViewTeen.setText(DeviceUtils.getMacAddress());
        //固件版本
        mBinding.tvViewElen.setText(com.askia.coremodel.util.DeviceUtils.getDeviceSN());
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
                    Toast.makeText(UpdateActivity.this, "没有新的更新包", Toast.LENGTH_SHORT).show();
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
                    Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    startActivity(intent);
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
        startActivity(intent);
    }

    public void getUpData(View view) {
        mViewModel.checkVersion(getVersion(this));
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
