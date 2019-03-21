package com.zhangjian.coolweather.util;

public class CommonUtil {
    /**
     * 将数组转化为16进制
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转化为数组
     *
     * @param data
     * @return
     */
    public static byte[] stringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int intCh;  // 两位16进制数转化后的10进制数
            char hexChar1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int intCh1;
            if (hexChar1 >= '0' && hexChar1 <= '9') {
                intCh1 = (hexChar1 - 48) * 16;   //// 0 的Ascll - 48
            } else if (hexChar1 >= 'A' && hexChar1 <= 'F') {
                intCh1 = (hexChar1 - 55) * 16; //// A 的Ascll - 65
            } else {
                return null;
            }
            i++;
            char hexChar2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int intCh2;
            if (hexChar2 >= '0' && hexChar2 <= '9') {
                intCh2 = (hexChar2 - 48); //// 0 的Ascll - 48
            } else if (hexChar2 >= 'A' && hexChar2 <= 'F') {
                intCh2 = hexChar2 - 55; //// A 的Ascll - 65
            } else {
                return null;
            }
            intCh = intCh1 + intCh2;
            retData[i / 2] = (byte) intCh;//将转化后的数放入Byte里
        }
        return retData;
    }
}
