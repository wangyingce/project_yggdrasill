package cc.leevi.webbase.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static final String KEY_MD5 = "MD5";

    /**
     * MD5加密算法
     * @param data
     * @return
     */
    public static String encryptMD5(String data) {
        byte[] inputData = data.getBytes();
        if (data == null){
            return null;
        }else {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance(KEY_MD5);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md5.update(inputData);
            return new BigInteger(1, md5.digest()).toString(16);
        }
    }
    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        System.out.println(MD5Encode("admin", "UTF-8"));
        System.out.println(MD5Encode("admin2", "UTF-8"));
        // 21232f297a57a5a743894a0e4a801fc3
        // c84258e9c39059a89ab77d846ddab909
    }
}
