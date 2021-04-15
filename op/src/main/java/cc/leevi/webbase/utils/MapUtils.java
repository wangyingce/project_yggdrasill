package cc.leevi.webbase.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.base.Throwables;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUtils extends org.apache.commons.collections.MapUtils {


    /**
     * map转Object
     * key要与VO的属性名一致
     *
     *   注意点：cDptCde 此格式-->不支持
     *          CDptCde 此格式支持
     *          dptCde  此格式支持
     *
     *          推荐使用
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null)
            return null;
        Object obj = null;
        try {
            obj = beanClass.newInstance();
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }
        return obj;
    }

    /**
     * map转Object
     * key要与VO的属性名一致
     *
     *   注意点：cDptCde 此格式支持
     *          CDptCde 此格式支持
     *          dptCde  此格式支持
     *
     *  liuhuan 不推荐使用, 未完善其他嵌套
     *
     *  栗子：
     private String CPkNo;

     private String cClmNo;

     private String cedrNo;

     private TestMapUtilsDatatttChild child;
     */
    public static Object mapToObjectForClm(Map<String, Object> map, Class<?> beanClass) {
        if (map == null)
            return null;
        Object obj = null;
        try {
            obj = beanClass.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            //BeanUtils.populate(obj, map);
            for (Field field : declaredFields) {
                if(Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                String fieldName/*map.key*/ = field.getName();
                Object mapVal = map.get(fieldName);

                if (mapVal instanceof Map){
                    String fieldClazzName = field.getType().getName();
                    Class<?> fieldClazz = Class.forName(fieldClazzName);

                    Object Clazz = mapToObjectForClm((Map) mapVal, fieldClazz);
                    field.set(obj,Clazz);
                }else {
                    field.set(obj,mapVal);
                }

            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }

        return obj;
    }

    /**
     * Object转map
     * key是VO属性的驼峰式
     */
    public static Map objectTomap(Object VO){
        if (VO == null)return null;
        return new ObjectMapper().convertValue(VO, Map.class);
    }


    /**
     * Object转map
     * key是VO属性的名
     *
     * 缺点：不支持递归, 不支持继承
     */
    public static Map objectTomapForClm(Object VO){
        if (VO == null)return null;
        Field[] declaredFields = VO.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap();

        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String VO_name = field.getName();
                Object VO_value = field.get(VO);
                field.set(VO,"");
                map.put(VO_name,VO_value);
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }

        return map;
    }
    /**
     * Object转map
     * key是VO属性的名
     * add by hhr  移除 属性为空    防止页面传入  	"CClmNo":"" 这种情况
     * 缺点：不支持递归, 不支持继承
     */
    public static Map objectToMapRemoveNull(Object VO){
        if (VO == null)return null;
        Field[] declaredFields = VO.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap();

        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String VO_name = field.getName();
                Object VO_value = field.get(VO);

                if(VO_value!=null&&String.class.equals(VO_value.getClass())&&StringUtils.isBlank(VO_value.toString())){
                    VO_value=null;
                }
                map.put(VO_name,VO_value);
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }

        return map;
    }





    public static Object toUpperCamelObject(Map<String,Object> map,Class beanClass) {
        Map<String,Object> resultMap  = convertKeys(map);
       return mapToObject(resultMap,beanClass);
    }

    public static Map convertKeys(Map<String,Object> map){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()){
            Map.Entry<String, Object> entry = entries.next();
            if (entry.getKey() instanceof  String){
                String upperCamelKey = upperUnderscore2UpperCamel((String)entry.getKey());
                resultMap.put(upperCamelKey,entry.getValue());
            }
        }
        return resultMap;
    }

    /**
     * 大写下划线转大写驼峰
     * @param original
     * @return
     */
    private static String upperUnderscore2UpperCamel(String original){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, original);
    }


}