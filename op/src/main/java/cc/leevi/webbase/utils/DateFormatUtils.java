package cc.leevi.webbase.utils;

import java.util.Date;

public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {

    private static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 解析：推荐方式
     */
    public static Date parseDate(String time) {
        return DateUtils.parseDate(time, DEFAULT_PATTERN);
    }

    public static Date parseDate(String time, String format) {
        return DateUtils.parseDate(time, format);
    }

    /**
     * 格式化：推荐方式
     * String date = DateFormatUtils.Defaulattern(new Date(), Defaulattern);
     */
    public static String format(Date date) { 
        return DateFormatUtils.format(date, DEFAULT_PATTERN);
    }




    }
