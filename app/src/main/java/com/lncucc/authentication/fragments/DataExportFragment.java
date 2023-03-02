package com.lncucc.authentication.fragments;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;
import static com.askia.coremodel.rtc.Constants.FULL_SCREEN_FLAG;
import static com.askia.coremodel.rtc.Constants.STU_EXPORT;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.NetworkUtils;
import com.askia.common.util.receiver.UsbStatusChangeEvent;
import com.askia.coremodel.datamodel.data.DataImportListBean;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.event.ExportDataEvent;
import com.askia.coremodel.viewmodel.DataExportViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.FileUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.partition.Partition;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataImportAdapter;
import com.lncucc.authentication.databinding.FragmentExportBinding;
import com.lncucc.authentication.widgets.DataLoadingDialog;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.ExportByNetDialog;
import com.lncucc.authentication.widgets.pop.BottomPopUpWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据导出
 */
public class DataExportFragment extends BaseFragment {
    private final List<DataImportListBean> dataExportList = new ArrayList<>();
    private List<DBExamArrange> sessionList;
    private String seCode = "";
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;
    String siteCode = "";

    private FragmentExportBinding exportBinding;
    private DataExportViewModel exportViewModel;

    private DBExamArrange itemArrange;
    private DataImportAdapter dataImportAdapter;
    private int defaultIndex = 2;
    private DataLoadingDialog loadingDialog;
    private NumberFormat numberFormat;
    private ExportByNetDialog dialog;

    @Override
    public void onInit() {
        exportBinding.rlArrange.setOnClickListener(v -> showPopUp());
        sessionList = DBOperation.getDBExamArrange();
        loadingDialog = new DataLoadingDialog(getActivity());
        loadingDialog.setCanceledOnTouchOutside(false);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        if (sessionList != null && sessionList.size() > 0) {
            exportBinding.tvSession.setText(sessionList.get(0).getSeName());
            itemArrange = sessionList.get(0);
            //默认选择list中的第一个场次
            seCode = sessionList.get(0).getSeCode();
            //根据seCode获取当前你场次验证数据数量
            int currentCount = getCurrentVerifyCount();
            exportBinding.tvCount.setText(String.format(getResources().getString(R.string.verify_message_count), currentCount + ""));
            siteCode = DBOperation.getSiteCode(itemArrange.getExamCode());
        }
        for (int i = 0; i < 3; i++) {
            DataImportListBean bean = new DataImportListBean();
            switch (i) {
                case 0:
                    bean.setChecked(false);
                    bean.setTitle("网络导出");
                    dataExportList.add(bean);
                    break;
                case 1:
                    bean.setChecked(false);
                    bean.setTitle("U盘导出");
                    dataExportList.add(bean);
                    break;
                case 2:
                    bean.setChecked(true);
                    bean.setTitle("SD卡导出");
                    dataExportList.add(bean);
                    break;

            }
        }
        exportBinding.rcyDataExport.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataImportAdapter = new DataImportAdapter(dataExportList);
        exportBinding.rcyDataExport.setAdapter(dataImportAdapter);
        initEvent();
        RxBus2.getInstance().register(this);
    }

    /**
     * 2022.10.12 达尔哥提的优化需求
     * 在选择网络导出时，先调用此方法查询并展示本地库中已比对未上传的数据数量。
     *
     * @param filePath 压缩包路径
     */
    private void showUnUpLoadDataNumberDialog(String filePath) {
        //未上传验证数据数量
        int num = DBOperation.getDataUpLoadFailedNum(seCode);
        //dialog展示
        dialog = new ExportByNetDialog(getActivity(), new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                closeLoading();
                dialog.dismiss();
            }

            @Override
            public void backType(int type) {
                if (type == 0) {
                    upLoadData(filePath);
                }
                dialog.dismiss();
            }
        }, "有" + num + "条验证数据未实时上传成功，是否通过网络上传？");
        if (num > 0) {
            dialog.show();
        } else {
            upLoadData(filePath);
        }
    }

    private void upLoadData(String filePath) {
        String examCode = itemArrange.getExamCode();
        String seCode = itemArrange.getSeCode();
        if (NetworkUtils.isConnected()) {
            exportViewModel.postData(examCode, siteCode, seCode, filePath);
            LogsUtil.saveOperationLogs("数据导出");
        } else {
            MyToastUtils.error("网络异常，请检查您的网络！", Toast.LENGTH_SHORT);
            closeLoading();
        }
    }

    private void initEvent() {
        dataImportAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (defaultIndex == position) {
                return;
            } else {
                dataExportList.get(defaultIndex).setChecked(false);
                dataExportList.get(position).setChecked(true);
                defaultIndex = position;
            }
            dataImportAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onInitViewModel() {
        exportViewModel = ViewModelProviders.of(getActivity()).get(DataExportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        exportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_export, container, false);
        exportBinding.setHandles(this);
        return exportBinding.getRoot();
    }

    /**
     * @return 获取当前场次验证数据数量
     */
    private int getCurrentVerifyCount() {
        return DBOperation.getVerifyListBySeCode(seCode).size();
    }

    @Subscribe
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        if (event.isGetPermission) {
            UsbDevice usbDevice = event.usbDevice;
            MyToastUtils.error("权限已获取", Toast.LENGTH_SHORT);
            readDevice(getUsbMass(usbDevice));
        }
    }

    /**
     * 数据导出成功后，更新DBExport表的上传状态
     *
     * @param event ExportDataEvent
     */
    @Subscribe(code = 22)
    public void onModifyDataStateEvent(ExportDataEvent event) {
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        Log.e("modify dbExport ->", "场次" + event.getSeCode() + "result->" + event.isSuccess());
    }


    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    /**
     * U盘设备读取
     */
    private void redUDiskDevsList() {
        //设备管理器
        UsbManager usbManager = (UsbManager) Objects.requireNonNull(getActivity()).getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(getActivity());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        for (UsbMassStorageDevice device : storageDevices) {
            //读取设备是否有权限
            if (usbManager.hasPermission(device.getUsbDevice())) {
                readDevice(device);
            } else {
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
            closeLoading();
            MyToastUtils.success("请插入可用的U盘", Toast.LENGTH_SHORT);
        }
    }

    private void readDevice(UsbMassStorageDevice device) {
        try {
            //设备初始化
            device.init();
            //设备分区
            Partition partition = device.getPartitions().get(0);
            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            //可以获取到设备的标识
            currentFs.getVolumeLabel();
            //设置当前文件对象为根目录
            cFolder = currentFs.getRootDirectory();
            readFromUDisk();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * U盘 io遍历
     */
    private void readFromUDisk() {
        UsbFile[] usbFiles = new UsbFile[0];
        try {
            usbFiles = cFolder.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //查找Export文件夹
        if (null != usbFiles && usbFiles.length > 0) {
            boolean hasFolder = false;
            for (UsbFile usbFile : usbFiles) {
                if (usbFile.getName().equals("Export")) {
                    hasFolder = true;
                    //写入数据
                    writeZip2UDisk(usbFile);
                }
            }
            //如果U盘中没有Export文件夹，需先创建文件夹再进行数据包导出
            if (!hasFolder) {
                try {
                    UsbFile file = cFolder.createDirectory("Export");
                    writeZip2UDisk(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeZip2UDisk(UsbFile usbFile) {
        //获取SDCard中的压缩文件
        List<File> files = FileUtils.listFilesInDir(STU_EXPORT);
        for (File file : files) {
            if (file.getName().contains(".zip") && file.getName().contains(seCode)) {
                exportViewModel.write2UDiskByOTG(file, usbFile);
            }
        }
    }

    @Override
    public void onSubscribeViewModel() {
        //导出到sdcard
        exportViewModel.doExport().observe(this, result -> {
            closeLoading();
            MyToastUtils.error(result, Toast.LENGTH_SHORT);
        });
        //写入U盘
        exportViewModel.write2UDisk().observe(this, result -> {
            //压缩包IO操作进度
            float ioPercentDone = (float) result.getCurrent() / (float) result.getTotal() * 100;
            String percentDone = numberFormat.format(ioPercentDone);
            loadingDialog.setLoadingProgress(percentDone, result.getMessage());
            if (result.getCode() == 0) {
                closeLoading();
                DBOperation.modifyExportDataState(seCode);
                MyToastUtils.success(result.getMessage(), Toast.LENGTH_LONG);
            }
            LogsUtil.saveOperationLogs("数据导出");
        });

        exportViewModel.zip().observe(this, result -> {
            int process = result.getUnZipProcess();
            String message = result.getMessage();
            loadingDialog.setLoadingProgress(process + "", message);
            if (process == 100) {
                //如果选择U盘导出，从本地sd中拷贝到U盘根目录
                if (defaultIndex == 1) {
                    redUDiskDevsList();
                    //选择网络导出
                } else if (defaultIndex == 0) {
                    showUnUpLoadDataNumberDialog(result.getFilePath());
                } else {
                    closeLoading();
                    DBOperation.modifyExportDataState(seCode);
                    LogsUtil.saveOperationLogs("数据导出");
                    MyToastUtils.error("导出成功", Toast.LENGTH_SHORT);
                }
            }
        });

        //网络数据导出模式
        exportViewModel.upLoad().observe(this, result -> {
            //文件上传进度实时回调
            if (result != null && result.getInfo() != null) {
                float ioPercentDone = (float) result.getCurrent() / (float) result.getTotal() * 100;
                String percentDone = numberFormat.format(ioPercentDone);
                loadingDialog.setLoadingProgress(percentDone, result.getInfo());
            }
            //上传成功后改变实时上传状态
            if (result != null  && result.isSuccess()){
                //设置实时上传数据状态
                DBOperation.modifyExportDataState(seCode);
            }
            if (result != null && result.getState() == 0) {
                closeLoading();
                if (result.getMessage() != null) {
                    MyToastUtils.success(result.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });
    }

    /**
     * 考试场次接收
     *
     * @param index list position
     */
    @Subscribe(code = 0)
    public void onGetSessionEvent(String index) {
        int position = Integer.parseInt(index);
        itemArrange = sessionList.get(position);
        exportBinding.tvSession.setText(itemArrange.getSeName());
        seCode = itemArrange.getSeCode();
        siteCode = DBOperation.getSiteCode(itemArrange.getExamCode());
        //更新验证数据数量
        int currentCount = getCurrentVerifyCount();
        exportBinding.tvCount.setText(String.format(getResources().getString(R.string.verify_message_count), currentCount + ""));
    }

    private void showPopUp() {
        View parent = exportBinding.llContainer;
        BottomPopUpWindow pop = new BottomPopUpWindow(getActivity(), sessionList);
        pop.setFocusable(false);
        pop.showAtLocation(parent, Gravity.CENTER, 0, 0);
        pop.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
        pop.setFocusable(true);
        pop.update();
    }

    /**
     * 数据导出
     *
     * @param view view
     */
    public void export(View view) {
        String orgCode = SharedPreferencesUtils.getString(getActivity(), "code", "");
        //判断导出数据中考点代码是否正确
        if (DBOperation.isAffiliationCurrentSite(orgCode)){
            showLoading();
            //添加orgCode判断，只能导出当前考点数据
            exportViewModel.doDataExport(seCode);
        }else {
            MyToastUtils.error("导出数据中，考点代码异常，请检查后重新尝试！",1);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (sessionList != null && sessionList.size() > 0) {
                exportBinding.tvSession.setText(sessionList.get(0).getSeName());
                itemArrange = sessionList.get(0);
                seCode = sessionList.get(0).getSeCode();
                siteCode = DBOperation.getSiteCode(itemArrange.getExamCode());
            }
        }
    }

    private void closeLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.clearLoadingProgress();
            loadingDialog.dismiss();
        }
    }

    private void showLoading() {
        if (!loadingDialog.isShowing() && loadingDialog != null) {
            loadingDialog.show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
