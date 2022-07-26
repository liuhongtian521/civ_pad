package com.askia.coremodel.rtc;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUtil {
    private static final String LOG_FOLDER_NAME = "log";
    private static final String LOG_FILE_NAME = "agora-rtc.log";

    /**
     * Initialize the log folder
     * @param context Context to find the accessible file folder
     * @return the absolute path of the log file
     */
    public static String initializeLogFile(Context context) {
        File folder;
        if (Build.VERSION.SDK_INT >=  29) {
            folder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), LOG_FOLDER_NAME);
        } else {
            String path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator +
                    context.getPackageName() + File.separator +
                    LOG_FOLDER_NAME;
            folder = new File(path);
            if (!folder.exists() && !folder.mkdir()) folder = null;
        }

        if (folder != null && !folder.exists() && !folder.mkdir()) return "";
        else return new File(folder, LOG_FILE_NAME).getAbsolutePath();
    }

    /**
     * file to byte[]
     * @param file
     * @return
     */
    public static byte[] readFile(File file){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] getBytesByFile(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
            fis.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
