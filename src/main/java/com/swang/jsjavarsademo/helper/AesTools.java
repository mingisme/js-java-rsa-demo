package com.swang.jsjavarsademo.helper;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class AesTools {

    public static final String ENCODING = "UTF-8";
    private static String ALGO = "AES";
    private static String ALGO_MODE_PATTERN = "AES/CBC/PKCS5Padding";
    private static String DEF_KEY = "sfsafasdfsafasff";

    private static String DEF_IV = "phjlknslfdhksldf";
    
    private static int validLength = 16;
    
    public static void setDefKey(String key) {
        DEF_KEY = key;
    }

    public static void setDefIv(String iv) {
        DEF_IV = iv;
    }

    private AesTools(){}

    public String encrypt(String sSrc) throws Exception {
        return encrypt(sSrc, DEF_KEY);
    }

    public String encrypt(String sSrc, String sKey) throws Exception {
        return encrypt(sSrc, sKey, DEF_IV);
    }

    public String encrypt(String sSrc, String sKey, String sIv) throws Exception {
        if (sKey == null) {
            return null;
        }
        if (sKey.length() != validLength) {
            return null;
        }
        byte[] raw = sKey.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGO);
        Cipher cipher = Cipher.getInstance(ALGO_MODE_PATTERN);
        IvParameterSpec iv = new IvParameterSpec(sIv.getBytes(ENCODING));
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(ENCODING));
        return Base64.encodeBase64String(encrypted);
    }

    public String decrypt(String sSrc) throws Exception {
        return decrypt(sSrc, DEF_KEY);
    }

    public String decrypt(String sSrc, String sKey) throws Exception {
        return decrypt(sSrc, sKey, DEF_IV);
    }

    public String decrypt(String sSrc, String sKey, String sIv) throws Exception {
        if (sKey == null) {
            return null;
        }

        if (sKey.length() != validLength) {
            return null;
        }
        byte[] raw = sKey.getBytes("ASCII");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGO);
        Cipher cipher = Cipher.getInstance(ALGO_MODE_PATTERN);
        IvParameterSpec iv = new IvParameterSpec(sIv.getBytes(ENCODING));
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decodeBase64(sSrc);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, ENCODING);
    }

    public static void main(String[] args) throws Exception {
        AesTools aesTools = new AesTools();
        System.out.println(aesTools.encrypt("123456"));
        System.out.println(aesTools.decrypt(aesTools.encrypt("123456")));
    }

}
