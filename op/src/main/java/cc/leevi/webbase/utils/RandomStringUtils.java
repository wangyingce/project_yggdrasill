package cc.leevi.webbase.utils;

public class RandomStringUtils extends org.apache.commons.lang3.RandomStringUtils{

    /**
     * 生成随机数 数字+字母
     * @return
     */
    public static String randomNumericLetter(int count){
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return random(count,chars);
    }
}
