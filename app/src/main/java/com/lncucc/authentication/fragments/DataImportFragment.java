package com.lncucc.authentication.fragments;

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
import com.askia.coremodel.datamodel.data.DataImportListBean;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataImportViewModel;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.partition.Partition;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataImportAdapter;
import com.lncucc.authentication.databinding.FragmentImportBinding;
import com.lncucc.authentication.widgets.DataLoadingDialog;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;
import static com.askia.coremodel.rtc.Constants.ZIP_PATH;

/**
 * 数据导入
 */
public class DataImportFragment extends BaseFragment {
    private FragmentImportBinding importBinding;
    private DataImportViewModel viewModel;
    private ArrayList<DataImportListBean> importList = new ArrayList<>();
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;
    private DataLoadingDialog loadingDialog;
    private DataImportAdapter dataImportAdapter;
    private int defaultIndex = 2;

    @Override
    public void onInit() {
        RxBus2.getInstance().register(this);
        loadingDialog = new DataLoadingDialog(getActivity());
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
            if (progress == 100) {
                closeLogadingDialog();
                LogsUtil.saveOperationLogs("数据导入成功");
                //解析
                viewModel.getExDataFromLocal(result.getFilePath());
                MyToastUtils.error("导入成功", Toast.LENGTH_SHORT);
            }
            if (result.getCode() == -1) {
                closeLogadingDialog();
//                loadingDialog.dismiss();
                MyToastUtils.error(result.getMessage(), Toast.LENGTH_SHORT);
            }
        });

        viewModel.doFaceDBHandle().observe(this, result -> {
            closeLogadingDialog();
//            MyToastUtils.error(result.getMessage(),Toast.LENGTH_SHORT);
        });

        viewModel.usbWriteObservable().observe(this, result -> {
            closeLogadingDialog();
            if (result.getCode() == 0) {
                viewModel.doUnzip(this);
            } else {
                MyToastUtils.error(result.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    @Subscribe
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        if (event.isConnected) {
//            MyToastUtils.error("U盘已连接", Toast.LENGTH_SHORT);
        } else if (event.isGetPermission) {
            UsbDevice usbDevice = event.usbDevice;
//            MyToastUtils.error("权限已获取", Toast.LENGTH_SHORT);
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
                closeLogadingDialog();
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
            closeLogadingDialog();
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
                    viewModel.readZipFromUDisk(usbFile);
                }
            }
        }
    }

    public void importData(View view) {
        showLogadingDialog();
        switch (defaultIndex) {
            case 0:
                MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
                closeLogadingDialog();
                break;
            case 1:
                redUDiskDevsList();
                break;
            case 2:
                viewModel.doUnzip(this);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
