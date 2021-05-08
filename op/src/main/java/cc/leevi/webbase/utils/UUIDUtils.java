package cc.leevi.webbase.utils;

import java.util.UUID;

/**
 * 生成随机字符串的工具类 uuid
 */
public class UUIDUtils {

    public static final String usercId = "ygg1412";
    /**
     * 格式前的UUID ： 68dcb13d-02ec-4233-b65a-6698830e0b51
     */
    public static String getUUIDOrg() {
        return UUID.randomUUID().toString();
    }

    /**
     * 格式化后的UUID ：d39b37b98606404cad9077ae29884330
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}

