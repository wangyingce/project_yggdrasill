package cc.leevi.webbase.utils;

import com.google.common.base.Throwables;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 判断对象是否为空或null
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        else if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        else if (obj instanceof Collection) return ((Collection) obj).isEmpty();
        else if (obj instanceof Map) return ((Map) obj).isEmpty();
        else if (obj.getClass().isArray()) return Array.getLength(obj) == 0;

        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 功能类似三目运算符
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getOrDefault(T value,T defaultValue){
        if(value == null){
            return defaultValue;
        }
        return value;
    }

    /**
     * 判断一个对象所有的属性是否为空
     *     返回ture表示所有属性为null  返回false表示不是所有属性都是null
     *
     *     使用场景：查询 ， 至少录入一个查询条件
     */
    public static boolean isAllFieldNull(Object obj , List<String> exclude) {
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        boolean flag = true;
        try {
            for (Field f : fs) {//遍历属性

                //排外属性
                String name = f.getName();
                if(CollectionUtils.isNotEmpty(exclude) && exclude.contains(name)){
                    continue;
                }

                f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                Object val = f.get(obj);// 得到此属性的值
                if(val!=null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                    flag = false;
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            Throwables.throwIfUnchecked(e);
        }
        return flag;
    }
}