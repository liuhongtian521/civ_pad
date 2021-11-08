package com.askia.coremodel.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by hcc on 2016/11/8.
 */
public class SignUtils {
    private static final String ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+lPAao8SRhomkqFbNZRcLW+TNIoAflw3iMh+V9bSX3/Dl7YsqHr+yS4QUVTFM/WRNC2cxm5PRoi1H93rezWe/6HuaDeo7xpZxNG09jIirGvSzW9Z/bmwBw4rm3Af88fJJk5zSf1NMIxOuRGA4WtzZQluVvmThofgEMR5AUEL8RQIDAQAB";
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL6U8BqjxJGGiaSoVs1lFwtb5M0igB+XDeIyH5X1tJff8OXtiyoev7JLhBRVMUz9ZE0LZzGbk9GiLUf3et7NZ7/oe5oN6jvGlnE0bT2MiKsa9LNb1n9ubAHDiubcB/zx8kmTnNJ/U0wjE65EYDha3NlCW5W+ZOGh+AQxHkBQQvxFAgMBAAECgYBJAiFg8y/QV+vOyjZGhN1pGpDyDK9sap+E8ZKsDe5a6A0O6AsR3amuEcPNTj2OstuDsESd+m2MN0aFdUb6p1GAx8CmpxhjqDzWvtFnN1Wklcg3GAaIUm84dEXQwAUscYoZWI319VhvbyNTHGzF22gzOxGY62pC71BaseDcme10WQJBAN2m/+gyeYXljw2bq1hlBETmdcZ43EuecOGXl+/2KbtBEElGPUL4sYLIaQ7EH2pCw+g9tJ1EqiBEivRMHb226OsCQQDcHVy0R0KqFGTRBYNZgwh3QiIXLKYAZ11qHYMxRAJBFmMXKFM/1lLYY453LRexqrfQeOqPsAIPPUsAyWwQDGOPAkEA0g77RXXYX0G/wdi+mOYbFqUGBtLxi6SbO+BruJkk0XaG1bqAQmDn8Za2oazSTOT7PrSD3+t8A1qSCZW9NdcLgwJAOOpWuD7c3AGd9/ZG+nRJUh0Fl5xx6BTSMMgkzi5Zrt7NDIXe0NjoXw+PHP3J7KTKFtvNOw412h7tL+zlRlkNvQJAZxDhrnDB4vJWmsMQLKNrb0b57gigKBeUhXYr3EtSdU278imGNdfIFA9uMy/3jY7TpkkT7wwP0RjMch7VC7aNRg==";

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }


    /**
     * 从字符串中加载私钥
     *
     * @param privateKeyStr 私钥
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 公钥加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        try {
            // 必须使用RSA/ECB/PKCS1Padding而不是RSA，否则每次加密结果一样
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return Base64Utils.encode(output);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }


    /**
     * 私钥解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空,请设置");
        }
        try {
            // 必须使用RSA/ECB/PKCS1Padding而不是RSA，否则解密会乱码
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(cipherData);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 生成公私钥对
     *
     */
    public static Map<String, String> generateKey() throws NoSuchAlgorithmException {
        Map<String, String> keyMap = new HashMap<>();
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom random = new SecureRandom();
        // 初始化加密
        keygen.initialize(1024, random);
        // 取得密钥对
        KeyPair kp = keygen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        String privateKeyString = Base64Utils.encode(privateKey.getEncoded());
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        String publicKeyString = Base64Utils.encode(publicKey.getEncoded());
        keyMap.put("publicKey", publicKeyString);
        keyMap.put("privateKey", privateKeyString);
        return keyMap;
    }

    // 公钥加密
    public static String encryptByPublic(String num) {
        String encrypt = null;
        try {
            encrypt = SignUtils.encrypt(loadPublicKeyByStr(PUBLIC_KEY), num.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    //私钥解密
    public static String decryptByPrivate(String encryptStr) {
        byte[] decrypt = null;
        try {
            decrypt = SignUtils.decrypt(loadPrivateKeyByStr(PRIVATE_KEY), Base64Utils.decode(encryptStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decrypt);
    }
}


