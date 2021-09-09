package com.askia.coremodel.util;

import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {

    public static void saveSDFile2OTG(final File f, final UsbFile usbFile, IOProcess listener) {
        UsbFile uFile = null;
        FileInputStream fis = null;
        try {//开始写入
            fis = new FileInputStream(f);//读取选择的文件的
            if (usbFile.isDirectory()) {//如果选择是个文件夹
                UsbFile[] usbFiles = usbFile.listFiles();
                if (usbFiles != null && usbFiles.length > 0) {
                    for (UsbFile file : usbFiles) {
                        if (file.getName().equals(f.getName())) {
                            file.delete();
                        }
                    }
                }
                uFile = usbFile.createFile(f.getName());
                UsbFileOutputStream uos = new UsbFileOutputStream(uFile);
                try {
                    redFileStream(uos, fis,listener,f.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void redFileStream(OutputStream os, InputStream is,IOProcess listener,long len) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
        long sumBytes = 0l;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
            sumBytes += bytesRead;
            listener.onProcessDoneListener(sumBytes, len);
        }
        os.flush();
        os.close();
        is.close();
    }

    public interface IOProcess{
        void onProcessDoneListener(long current,long total);
    }
}
