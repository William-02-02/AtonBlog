package com.lyh.AtonBlog.util;

import org.apache.logging.log4j.util.PropertiesUtil;
import sun.security.provider.MD5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }
    
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
    
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)){
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
            }
        } catch (Exception exception) {
        }
        PropertiesUtil.getProperties();
        return resultString;
        
    }
    
    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    
}

// public static String getMD5String(String str) {
//     try {
//         // 生成一个MD5加密计算摘要
//         MessageDigest md = MessageDigest.getInstance("MD5");
//
//         // 计算md5函数
//         md.update(str.getBytes());
//         // digest()最后确定返回md5 hash值，返回值为8位字符串。
//         // 因为md5 hash值是16位的hex值，实际上就是8位的字符
//         // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
//         //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
//         return new BigInteger(1, md.digest()).toString(16);
//     } catch (Exception e) {
//         e.printStackTrace();
//         return null;
//     }
// }
