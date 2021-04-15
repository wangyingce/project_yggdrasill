package cc.leevi.webbase.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

public class TypeUtils {

    /**
     * 判断是否基础类型
     * @param value
     * @return
     */
    public static boolean isBaseType(Object value){
        Assert.notNull(value,"The value can not be empty!");
        Class clazz = value.getClass();
        if(clazz.equals(String.class) ||
                clazz.equals(Integer.class)||
                clazz.equals(Byte.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(BigDecimal.class) ||
                clazz.equals(BigInteger.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Date.class) ||
                clazz.equals(Timestamp.class) ||
                clazz.isPrimitive()){
            return true;
        }
        return false;
    }

    public static boolean isArray(Object value){
        return value!=null&&value.getClass().isArray();
    }

    public static Object[] toBoxArray(Object value){
        if(isArray(value)){
            Class clazz = value.getClass();
            String typeName = clazz.getTypeName();
            Object[] boxArray = null;
            if("int[]".equals(typeName)){
                int[] arr = (int[]) value;
                boxArray = Arrays.stream( arr ).boxed().toArray( Integer[]::new );
            }else if("double[]".equals(typeName)){
                double[] arr = (double[]) value;
                boxArray = Arrays.stream( arr ).boxed().toArray( Double[]::new );
            }else if("long[]".equals(typeName)){
                double[] arr = (double[]) value;
                boxArray = Arrays.stream( arr ).boxed().toArray( Double[]::new );
            }else{
                boxArray = (Object[]) value;
            }
            return boxArray;
        }
        return new Object[0];
    }

}
