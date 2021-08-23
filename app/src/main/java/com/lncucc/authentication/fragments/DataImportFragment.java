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

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.receiver.UsbStatusChangeEvent;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataImportViewModel;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.partition.Partition;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentImportBinding;
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
    private FragmentImportBinding leadInBinding;
    private DataImportViewModel viewModel;
    private ArrayList<Boolean> importList;
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;

    @Override
    public void onInit() {
        RxBus2.getInstance().register(this);
    }

    public void initEvent() {
        leadInBinding.sbNet.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.netImport.set(isChecked));
        leadInBinding.sbUsb.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.usbImport.set(isChecked));
        leadInBinding.sbSdcard.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.sdCardImport.set(isChecked));
    }

    @Override
    public void onInitViewModel() {
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DataImportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        leadInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_import, container, false);
        leadInBinding.setViewmodel(viewModel);
        leadInBinding.setClick(this);
        initEvent();
        return leadInBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.getSdCardData().observe(this, result -> {
            closeLogadingDialog();
            MyToastUtils.error(result, Toast.LENGTH_SHORT);
        });

        viewModel.doZipHandle().observe(this, result -> {
            int progress = result.getProgress();
            closeLogadingDialog();
            if (progress == 100) {
                MyToastUtils.error("导入成功", Toast.LENGTH_SHORT);
            } else {
                MyToastUtils.error(result.getMessage(), Toast.LENGTH_SHORT);
            }
        });

        viewModel.usbWriteObservable().observe(this, result ->{
            if (result.getCode() == 0){
                viewModel.doSdCardImport();
            }else {
                MyToastUtils.error(result.getMessage(),Toast.LENGTH_SHORT);
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
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
                showLogadingDialog();
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
            MyToastUtils.error("USB读取超时，请插拔重试！",Toast.LENGTH_SHORT);
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
        importList = new ArrayList<>();
        importList.add(viewModel.netImport.get());
        importList.add(viewModel.usbImport.get());
        importList.add(viewModel.sdCardImport.get());
        int count = 0;
        for (int i = 0; i < importList.size(); i++) {
            if (importList.get(i)) {
                count++;
            }
        }
        if (count > 1) {
            MyToastUtils.error("只能选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }
        if (count == 0) {
            MyToastUtils.error("请选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        if (viewModel.netImport.get()) {
            MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
        } else if (viewModel.usbImport.get()) {
            redUDiskDevsList();
        } else {
            viewModel.doSdCardImport();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
