package com.askia.coremodel.util;

import android.text.TextUtils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;

/**
 * @authoer:chengming
 * @create:2021-07-22 14:49
 * @note:
 */
public class ZipTool {


    public static void zip(String file_path, String zip_file, String password) throws Exception {

        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(zip_file);

        ZipParameters parameters = new ZipParameters();

        // 压缩方式
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);

        // 压缩级别
        parameters.setCompressionLevel(CompressionLevel.NORMAL);

        // 是否设置加密文件
        parameters.setEncryptFiles(true);

        // 设置加密算法
        parameters.setEncryptionMethod(EncryptionMethod.AES);

        // 设置AES加密密钥的密钥强度
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        // 设置密码
        if(!TextUtils.isEmpty(password)) {
            zipFile.setPassword(password.toCharArray());
        }

        // 要打包的文件夹
        File folder = new File(file_path);
        File[] fs = folder.listFiles();

        // 遍历test文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isDirectory()) {
                zipFile.addFolder(f, parameters);
            } else {
                zipFile.addFile(f, parameters);
            }
        }
    }


    public static void unzip(String zipFilePath, String toPath, String password) throws Exception {
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(zipFilePath);
        // 设置密码
        if(!TextUtils.isEmpty(password)) {
            zipFile.setPassword(password.toCharArray());
        }
        // 解压缩所有文件以及文件夹
        zipFile.extractAll(toPath);
    }

    public static void main(String[] args) throws Exception {
        //zip("D:\\temp\\110204","D:\\temp\\110204.zip","123456");
//        unzip("D:\\temp\\test\\110204.zip","D:\\temp\\test\\110204","123456");
    }






}

















