package com.lncucc.authentication.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.receiver.UsbStatusChangeEvent;
import com.askia.common.widget.NetLoadingDialog;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataExportViewModel;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;
import com.github.mjdev.libaums.partition.Partition;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentExportBinding;
import com.lncucc.authentication.widgets.pop.BottomPopUpWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;
import static com.askia.coremodel.rtc.Constants.STU_EXPORT;
import static com.askia.coremodel.rtc.Constants.ZIP_PATH;

/**
 * 数据导出
 */
public class DataExportFragment extends BaseFragment {
    private List<Boolean> list;
    private List<DBExamArrange> sessionList;
    private String seCode = "";
    private UsbMassStorageDevice[] storageDevices;
    private UsbFile cFolder;
    String siteCode = "";

    private FragmentExportBinding exportBinding;
    private DataExportViewModel exportViewModel;
    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private DBExamArrange itemArrange;

    @Override
    public void onInit() {
        exportBinding.rlArrange.setOnClickListener(v -> showPopUp());
        sessionList = DBOperation.getDBExamArrange();
        if (sessionList != null && sessionList.size() > 0) {
            exportBinding.tvSession.setText(sessionList.get(0).getSeName());
            itemArrange = sessionList.get(0);
            seCode = sessionList.get(0).getSeCode();
            siteCode= DBOperation.getSiteCode(itemArrange.getExamCode());
        }
        RxBus2.getInstance().register(this);
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

    @Subscribe
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        if (event.isConnected) {
//            MyToastUtils.error("U盘已连接", Toast.LENGTH_SHORT);
        } else if (event.isGetPermission) {
            UsbDevice usbDevice = event.usbDevice;
            MyToastUtils.error("权限已获取", Toast.LENGTH_SHORT);
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
                if (usbFile.getName().equals("Export")) {
                    writeZip2UDisk(usbFile);
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
            closeLogadingDialog();
            MyToastUtils.error(result, Toast.LENGTH_SHORT);
        });
        //写入U盘
        exportViewModel.write2UDisk().observe(this, result -> {
            closeLogadingDialog();
            MyToastUtils.success(result, Toast.LENGTH_LONG);
        });

        exportViewModel.zip().observe(this, result -> {
            if (result.getUnZipProcess() == 100) {
                //如果选择U盘导出，从本地sd中拷贝到U盘根目录
                if (exportBinding.sbUsb.isChecked()) {
                    redUDiskDevsList();
                    //选择网络导出
                } else if (exportBinding.sbNet.isChecked()) {
                    String examCode = itemArrange.getExamCode();
                    String seCode = itemArrange.getSeCode();
                    exportViewModel.postData(examCode, siteCode, seCode, result.getFilePath());
                } else {
                    closeLogadingDialog();
                    MyToastUtils.error("导出成功", Toast.LENGTH_SHORT);
                }
            }
        });

        exportViewModel.upLoad().observe(this, result -> {
            closeLogadingDialog();
            MyToastUtils.success(result.getMessage(), Toast.LENGTH_SHORT);
        });
    }

    @Subscribe(code = 0)
    public void onGetSessionEvent(String index) {
        int position = Integer.parseInt(index);
        itemArrange = sessionList.get(position);
        exportBinding.tvSession.setText(itemArrange.getSeName());
        seCode = itemArrange.getSeCode();
        siteCode= DBOperation.getSiteCode(itemArrange.getExamCode());
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

    public void export(View view) {
        list = new ArrayList<>();
        list.add(exportBinding.sbNet.isChecked());
        list.add(exportBinding.sbUsb.isChecked());
        list.add(exportBinding.sbSdcard.isChecked());
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)) {
                count++;
            }
        }
        if (count > 1) {
            MyToastUtils.error("只能选择一种导出方式!", Toast.LENGTH_SHORT);
            return;
        }
        if (count == 0) {
            MyToastUtils.error("请选择一种导出方式!", Toast.LENGTH_SHORT);
            return;
        }
        showLogadingDialog();
        exportViewModel.doDataExport(seCode);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (sessionList != null && sessionList.size() > 0) {
                exportBinding.tvSession.setText(sessionList.get(0).getSeName());
                itemArrange = sessionList.get(0);
                seCode = sessionList.get(0).getSeCode();
                siteCode= DBOperation.getSiteCode(itemArrange.getExamCode());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
