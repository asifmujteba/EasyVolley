package com.github.asifmujteba.easyvolley;

import java.io.UnsupportedEncodingException;

/**
 * Created by asifmujteba on 11/08/15.
 */
public class ASFUtils {

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array;
            try {
                array = md.digest(md5.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                array = md.digest(md5.getBytes());
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
