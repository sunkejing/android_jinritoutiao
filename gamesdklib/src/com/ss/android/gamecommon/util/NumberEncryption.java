package com.ss.android.gamecommon.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberEncryption {
    /**
     * 异或操作
     *
     * @param sourceData --原始数据
     * @param xorB       --异或数据
     * @return
     */
    public static String xorEncryption(String sourceData, String xorB) {
        if (TextUtils.isEmpty(sourceData)) {
            return "";
        }
        char[] array = sourceData.toCharArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (array[i] ^ 5);
        }
        return toHexString(new String(array));
    }

    //转化字符串为十六进制编码  
    public static String toHexString(String source) {
        String str = "";
        for (int i = 0; i < source.length(); i++) {
            int ch = source.charAt(i);
            String s = Integer.toHexString(ch);
            str = str + s;
        }
        return str;
    }

    public static String phoneReplaceMiddle(String account) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(account) && account.length() > 6) {
            for (int i = 0; i < account.length(); i++) {
                char c = account.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        } else if (!TextUtils.isEmpty(account) && account.length() < 6) {
            sb.append(account);
        }
        return sb.toString();

    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param content
     * @return
     */
    public static boolean isNumeric(String content) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(content).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Matcher isNum = pattern.matcher(bigStr);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * md5加密
     *
     * @param content
     * @return
     */
    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
