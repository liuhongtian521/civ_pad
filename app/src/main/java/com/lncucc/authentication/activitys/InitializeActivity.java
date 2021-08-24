package com.lncucc.authentication.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.http.entities.DounloadZipData;
import com.askia.coremodel.datamodel.http.entities.GetZipData;
import com.askia.coremodel.datamodel.http.entities.SelectpalnbysitecodeData;
import com.askia.coremodel.event.FaceHandleEvent;
import com.askia.coremodel.event.UnZipHandleEvent;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.viewmodel.DataImportViewModel;
import com.askia.coremodel.viewmodel.InitializeViewModel;
import com.askia.coremodel.viewmodel.ZIPDownloadViewModel;
import com.liulishuo.filedownloader.FileDownloader;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActInitializeBinding;
import com.ttsea.jrxbus2.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;


/**
 * 初始化
 */
@Route(path = ARouterPath.INITIALIZE_ACTIVITY)
public class InitializeActivity extends BaseActivity {

    private ActInitializeBinding actInitializeBinding;

    private InitializeViewModel initializeViewModel;
    private DataImportViewModel dataImportViewModel;
    private ZIPDownloadViewModel zipDownloadViewModel;

    private Disposable mDisposable;


    List<SelectpalnbysitecodeData.ResultBean> mList;

    List<GetZipData> mDownList;

    List<GetZipData> mDownUrlList;
    String siteCode;

    int index = 0;

    private CountDownTimer mCountDownTimer;
    int timeHave = 180;
    int downloadFaile = 0;

    @Override
    public void onInit() {
        siteCode = getIntent().getExtras().getString("code");
        mDownList = new ArrayList<>();
        mDownUrlList = new ArrayList<>();
        mCountDownTimer = new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("TagSnake", "gettime" + timeHave);
                if (timeHave > 0) {
                    timeHave--;
                } else {
                    mCountDownTimer.cancel();
                    timeHave = 180;
                    index = 0;
                    getList();
                }
            }

            @Override
            public void onFinish() {
                Log.e("TagSnake", "gettime" + timeHave);

                if (timeHave > 0) {
                    timeHave--;
                    mCountDownTimer.start();
                } else {
                    mCountDownTimer.cancel();
                    timeHave = 180;
                    index = 0;
                    getList();
                }
            }
        };
//                            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
//                            finish();
        //请求接口判断是否有新数据，有数据更新
        //有更新下载数据 解压 建表
        //无更新 判断本地是否有数据 无->管理员设置页 有->判断是否在识别时间范围内 在范围内？人脸识别页面: 进入首页

        actInitializeBinding.tvMsg.setText("获取下载数据中");
        zipDownloadViewModel.getExam(siteCode);
    }

    /**
     * 请求接口 获取更新数据
     */
    private void checkDataByNet() {

    }

    /**
     * 检查本地数据
     */
    private void checkDataByLocal() {
        //检查本地数据
        if (initializeViewModel.hasExaData()) {
            //判断时间范围
            //获取当前时间戳
            long currentTime = System.currentTimeMillis();
            //获取验证开始时间
            //获取验证结束时间


        } else {
            //管理员设置页
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
            this.finish();
        }
    }

    @Override
    public void onInitViewModel() {
        initializeViewModel = ViewModelProviders.of(this).get(InitializeViewModel.class);
        zipDownloadViewModel = ViewModelProviders.of(this).get(ZIPDownloadViewModel.class);
        dataImportViewModel = ViewModelProviders.of(this).get(DataImportViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        actInitializeBinding = DataBindingUtil.setContentView(this, R.layout.act_initialize);
        actInitializeBinding.setHandlers(this);
    }

    @Subscribe
    public void onFaceHandleEvent(FaceHandleEvent event) {
        switch (event.getType()) {
            case 2:
                int per1 = (event.getCurrent() * 100) / event.getTotal();
                actInitializeBinding.qmProcess.setProgress(per1);
                actInitializeBinding.tvProgress.setText(per1 + "%");
                break;
        }
    }


    @Override
    public void onSubscribeViewModel() {
        //获取考场数据
        zipDownloadViewModel.getSelectExma().observe(this, selectpalnbysitecodeData -> {

//            Log.e("TagSnake", selectpalnbysitecodeData.isSuccess() + ":" + selectpalnbysitecodeData.getMessage() + ":" + selectpalnbysitecodeData.getResult().size());
//            for (SelectpalnbysitecodeData.ResultBean resultBean : selectpalnbysitecodeData.getResult()) {
//                Log.e("TagSnake", resultBean.toString());
//            }
            actInitializeBinding.qmProcess.setProgress(Integer.parseInt(10 + ""));
            actInitializeBinding.tvProgress.setText("10%");
            //获取到数据
            if (!selectpalnbysitecodeData.isSuccess()) {
                Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
                finish();
                return;
            }
            mList = selectpalnbysitecodeData.getResult();
            getList();
        });

        //第一次下载，获取下载地址或者code
        zipDownloadViewModel.getZipDownload().observe(this, getZipData -> {
            if (getZipData == null) {
                downldFaile();
                return;
            }
            int showIndex = (index * 100) / mList.size();
            actInitializeBinding.qmProcess.setProgress(Integer.parseInt(showIndex * 10 + ""));
            actInitializeBinding.tvProgress.setText(showIndex + "%");
            //数据判断
            if ("0".equals(getZipData.getResult().getResult())) {//打包完成
                for (GetZipData item : mDownList) {//这里判断等待逻辑有没有
                    if (getZipData.getResult().getExamCode().equals(item.getResult().getExamCode())) {
                        mDownList.remove(item);
                        break;
                    }
                }
                boolean have = false;
                for (GetZipData item : mDownUrlList) {
                    if (getZipData.getResult().getExamCode().equals(item.getResult().getExamCode())) {
                        have = true;
                        break;
                    }
                }
                if (!have)
                    mDownUrlList.add(getZipData);
            } else if ("1".equals(getZipData.getResult().getResult())) {//打包中
                boolean have = false;
                for (GetZipData item : mDownList) {
                    if (getZipData.getResult().getExamCode().equals(item.getResult().getExamCode())) {
                        have = true;
                        break;
                    }
                }
                if (!have)//没有数据才会加载
                    mDownList.add(getZipData);
            } else if ("10".equals(getZipData.getResult().getResult())) {
                Toast.makeText(getApplicationContext(), "没有数据，无法下载", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getZipData.getResult().getMsg(), Toast.LENGTH_SHORT).show();
            }
            zipDownloadViewModel.writeTime();
        });

        //延时操作
        zipDownloadViewModel.getCanGetNext().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("TagSnake", mList.size() + ":" + index);
                if (index < mList.size() - 1) {
                    index++;
                    getList();
                } else {
                    if (mDownList.size() > 0) {
                        write();
                    } else {
                        if (mDownList.size() == 0 && mDownUrlList.size() == 0) {
                            breakthis(null);
                            return;
                        }
                        if (mDownUrlList.size() > 0) {
                            index = 0;
                            download();
                        } else {
                            downldFaile();
                        }
                    }
                }
            }
        });

        zipDownloadViewModel.getDounloadZipData().observe(this, new Observer<DounloadZipData>() {
            @Override
            public void onChanged(DounloadZipData dounloadZipData) {
                if (dounloadZipData.getErrorCode() == 0) {
                    //下载完成 开始下一个压缩包的下载
                    if (index >= mDownUrlList.size() - 1) {
                        //压缩包下载完成
                        downloadFinish();
                    } else {
                        index++;
                        download();
                    }
                } else if (dounloadZipData.getErrorCode() == -1) {
                    //异常
                    downloadFaile();
                }
            }
        });

        dataImportViewModel.getSdCardData().observe(this, new Observer<UnZipHandleEvent>() {
            @Override
            public void onChanged(UnZipHandleEvent s) {
                actInitializeBinding.tvMsg.setText(s.getMessage());
                if (s.getUnZipProcess() < 0) {
                } else if (s.getUnZipProcess() < 100) {
                    //进度刷新
                    actInitializeBinding.qmProcess.setProgress(s.getUnZipProcess());//Integer.parseInt(+ "0"));
                    actInitializeBinding.tvProgress.setText(s.getUnZipProcess() + "%");
                } else {
                    Log.e("TagSnake", "s.getCode() == 100");
                    //验证是否成功
                    if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
                        startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
                        finish();
                    } else {
                        breakthis(null);
                    }
                }
            }
        });
    }

    public void breakthis(View view) {
        if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
            Log.e("TagSnake", "tomain");
            startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
        } else {
            Log.e("TagSnake", "tosetting");
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
        }
        finish();

//
//        Log.e("TagSnake", "tosetting");
//        //跳过流程
//        startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
//        finish();
    }

    public void refuildthis(View view) {
        //重新下载
        actInitializeBinding.lineErr.setVisibility(View.GONE);
        actInitializeBinding.relProgress.setVisibility(View.VISIBLE);
        download();
    }

    public void write() {
        //服务器打包
        actInitializeBinding.tvMsg.setText("服务器正在打包中，稍后继续下载");
        mCountDownTimer.start();
    }

    public void download() {
        //下载压缩包
        zipDownloadViewModel.downloadZip(mDownUrlList.get(index).getResult());
    }

    public void downloadFinish() {
        actInitializeBinding.tvMsg.setText("系统正在初始化数据请稍后...");
        //全部下载完成
        dataImportViewModel.doSdCardImport();
    }

    public void downloadFaile() {
        //出现问题 下载失败
        actInitializeBinding.lineErr.setVisibility(View.VISIBLE);
        actInitializeBinding.relProgress.setVisibility(View.GONE);
    }

    public void getList() {

        if (mList.size() == 0) {
            Toast.makeText(getApplicationContext(), "没有需要下载的数据", Toast.LENGTH_LONG).show();
            zipDownloadViewModel.writeTime();
        } else {
            actInitializeBinding.tvMsg.setText("正在获取下载路径:" + mList.get(index).getExamName());
            zipDownloadViewModel.getDownloadOne(mList.get(index).getExamCode(), siteCode, getVersionCode());
        }
    }


    public void downldFaile() {
        Toast.makeText(getApplicationContext(), "网络异常，获取下载包失败", Toast.LENGTH_SHORT).show();
    }


    public String getVersionCode() {
        DBDataVersion dbDataVersion = DBOperation.getVersion();
        if (dbDataVersion == null) {
            return "";
        } else {
            return dbDataVersion.getDataVersion() == null ? "" : dbDataVersion.getDataVersion();
        }
//        try {
//            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
//            return pi.versionCode + "";
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return "0";
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null)
            mDisposable.dispose();
    }
}
