package com.lncucc.authentication.fragments;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
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
import com.askia.common.util.receiver.UsbStatusChangeEvent;
import com.askia.coremodel.datamodel.data.DataImportBean;
import com.askia.coremodel.datamodel.data.DataImportListBean;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataImportViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.partition.Partition;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.InitializeActivity;
import com.lncucc.authentication.adapters.DataImportAdapter;
import com.lncucc.authentication.databinding.FragmentImportBinding;
import com.lncucc.authentication.widgets.DataImportDialog;
import com.lncucc.authentication.widgets.DataImportDialogListener;
import com.lncucc.authentication.widgets.DataLoadingDialog;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.FaceImportErrorDialog;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据导入
 */
public class DataImportFragment extends BaseFragment implements DataImportDialogListener {
    private FragmentImportBinding importBinding;
    private DataImportViewModel viewModel;
    private ArrayList<DataImportListBean> importList = new ArrayList<>();
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;
    private DataLoadingDialog loadingDialog;
    private DataImportAdapter dataImportAdapter;
    private int defaultIndex = 2;
    private NumberFormat numberFormat;
    private String currentIOPercent = "";
    private DataImportDialog importDialog;
    private List<DataImportBean> mList = new ArrayList<>();
    private FaceImportErrorDialog errorDialog;

    @Override
    public void onInit() {
        RxBus2.getInstance().register(this);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        loadingDialog = new DataLoadingDialog(getActivity());
        importDialog = new DataImportDialog(getActivity(), this);
        loadingDialog.setCanceledOnTouchOutside(false);
        for (int i = 0; i < 3; i++) {
            DataImportListBean bean = new DataImportListBean();
            switch (i) {
                case 0:
                    bean.setChecked(false);
                    bean.setTitle("网络导入");
                    importList.add(bean);
                    break;
                case 1:
                    bean.setChecked(false);
                    bean.setTitle("U盘导入");
                    importList.add(bean);
                    break;
                case 2:
                    bean.setChecked(true);
                    bean.setTitle("SD卡导入");
                    importList.add(bean);
                    break;

            }
        }
        importBinding.rcyDataImport.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataImportAdapter = new DataImportAdapter(importList);
        importBinding.rcyDataImport.setAdapter(dataImportAdapter);
        initEvent();
    }

    public void initEvent() {
        dataImportAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (defaultIndex == position) {
                return;
            } else {
                importList.get(defaultIndex).setChecked(false);
                importList.get(position).setChecked(true);
                defaultIndex = position;
            }
            dataImportAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onInitViewModel() {
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DataImportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        importBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_import, container, false);
        importBinding.setViewmodel(viewModel);
        importBinding.setClick(this);
        return importBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.doZipHandle().observe(this, result -> {
            int progress = result.getUnZipProcess();
            loadingDialog.setLoadingProgress(result.getUnZipProcess() + "", result.getMessage());
            if (progress == 100) {
                LogUtils.e("file unzip success ->", result.getUnZipProcess());
                //解析 插入数据/插入人脸库
                viewModel.getExDataFromLocal(result.getFilePath());
            }
            if (result.getCode() == -1) {
                closeLoading();
                MyToastUtils.error(result.getMessage(), Toast.LENGTH_SHORT);
            }
        });

        viewModel.doFaceDBHandle().observe(this, result -> {
            float number = (float) result.getCurrent() / (float) result.getTotal() * 100;
            String percent = numberFormat.format(number);
            loadingDialog.setLoadingProgress(percent, String.format("正在插入第%d张,共%d张", result.getCurrent(), result.getTotal()));
            if (result.getState() == 1) {
                closeLoading();
                MyToastUtils.success("导入成功", Toast.LENGTH_SHORT);
                LogsUtil.saveOperationLogs("数据导入");
            }
            //人脸库导入异常
            if (result.getState() == 2) {
                MyToastUtils.success(result.getMessage(), Toast.LENGTH_SHORT);
                closeLoading();
                errorDialog = new FaceImportErrorDialog(getActivity(), new DialogClickBackListener() {
                    @Override
                    public void dissMiss() {

                    }

                    @Override
                    public void backType(int type) {
                        errorDialog.dismiss();
                    }
                }, result.getErrorList());
                errorDialog.show();
            }
            closeLogadingDialog();
        });

        viewModel.usbWriteObservable().observe(this, result -> {
            showLoading();
            //压缩包IO操作进度
            float ioPercentDone = (float) result.getCurrent() / (float) result.getTotal() * 100;
            String percentDone = numberFormat.format(ioPercentDone);
            if (!currentIOPercent.equals(percentDone)) {
                loadingDialog.setLoadingProgress(percentDone, result.getMessage());
            }
            if (result.getCode() == 0) {
                LogUtils.e("file copy success ->", result.getCode());
                DataImportBean bean = new DataImportBean();
                bean.setFilePath(result.getZipPath());
                bean.setFileName(result.getFileName());
                viewModel.doUnzip(this, bean);
            }
            currentIOPercent = percentDone;
        });
    }


    @Subscribe
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        if (event.isConnected) {
        } else if (event.isGetPermission) {
            UsbDevice usbDevice = event.usbDevice;
            readDevice(getUsbMass(usbDevice));
        }
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
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
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
            MyToastUtils.success("请插入可用的U盘", Toast.LENGTH_SHORT);
        }
    }

    private void readDevice(UsbMassStorageDevice device) {
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);
            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            currentFs.getVolumeLabel();//可以获取到设备的标识
            //设置当前文件对象为根目录
            cFolder = currentFs.getRootDirectory();
            readFromUDisk();

        } catch (Exception e) {
            closeLoading();
            MyToastUtils.error("USB读取超时，请插拔重试！", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    private void readFromUDisk() {
        UsbFile[] usbFiles = new UsbFile[0];
        try {
            usbFiles = cFolder.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != usbFiles && usbFiles.length > 0) {

            for (UsbFile usbFile : usbFiles) {
                if (usbFile.getName().equals("Examination")) {
                    mList.clear();
                    mList.addAll(viewModel.fetchDataFromUsb(usbFile));
                    importDialog.showImportDialog(1, mList);
                }
            }
        }
    }

    /**
     * 数据导入
     * @param view view
     */
    public void importData(View view) {
        switch (defaultIndex) {
            case 0: //网络导入复用initializeActivity,
                Intent intent = new Intent(getActivity(), InitializeActivity.class);
                intent.putExtra("type", -1);
                startActivity(intent);
                break;
            case 1:
                redUDiskDevsList();
                break;
            case 2:
                mList.clear();
                mList.addAll(viewModel.fetchDataFromSdCard());
                importDialog.showImportDialog(2, mList);
                break;
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

    @Override
    public void confirm(int position) {
        importDialog.dismiss();
        //USB导入
        if (defaultIndex == 1) {
            UsbFile file = mList.get(position).getUsbFile();
            if (file != null) {
                showLoading();
                viewModel.readZipFromUDisk(file);
            }
        } else if (defaultIndex == 2) {
            showLoading();
            viewModel.doUnzip(this, mList.get(position));
        }
    }

    @Override
    public void cancel() {
        if (importDialog != null) {
            importDialog.dismiss();
        }
    }
}
