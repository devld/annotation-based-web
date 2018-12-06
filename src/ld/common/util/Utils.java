package ld.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private final static char[] HEX = "0123456789abcdef".toCharArray();

    public static String byte2hex(byte[] data) {
        char[] hexChars = new char[data.length * 2];
        for (int j = 0; j < data.length; j++) {
            int v = data[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String md5(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(data.getBytes());
            byte[] res = digest.digest();
            return byte2hex(res);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String randomStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = HEX[(int) (Math.random() * HEX.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static int countMatches(String src, String c) {
        int n = 0;
        int last = 0;
        while (true) {
            int index = src.indexOf(c, last);
            if (index < 0) {
                break;
            }
            n++;
            last = index + c.length();
        }
        return n;
    }

    public static boolean isEmptyStr(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmptyTrimStr(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String calcPassword(String password, String salt) {
        return md5(salt + password + salt);
    }

}
