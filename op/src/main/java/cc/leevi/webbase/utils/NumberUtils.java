package cc.leevi.webbase.utils;

import java.math.BigDecimal;

public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {


    /**
     * 获取BigDecimal
     * @param obj
     * @return
     */
    public static BigDecimal getBigDecimal(Object obj) {
        BigDecimal amt = new BigDecimal("0");
        if (obj != null && !"".equals(obj)) {
            amt = new BigDecimal(obj.toString());
        }
        return amt;
    }

    public static Short createShort(String str) {
        return str == null ? null : Short.valueOf(str);
    }


}
