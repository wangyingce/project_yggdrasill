package cc.leevi.webbase.utils;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * vo转map工具类
 */
public class ObjTranMapUtils {
    public static HashMap<String, Object> convertToMap(Object obj)  {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            boolean accessFlag = fields[i].isAccessible();
            fields[i].setAccessible(true);
            Object o = null;
            try {
                o = fields[i].get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o != null)
                map.put(varName, o.toString());
            fields[i].setAccessible(accessFlag);
        }
        return map;
    }
}
