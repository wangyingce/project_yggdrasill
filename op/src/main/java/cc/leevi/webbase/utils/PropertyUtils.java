package cc.leevi.webbase.utils;

import com.google.common.base.Throwables;

public class PropertyUtils extends org.apache.commons.beanutils.PropertyUtils {

    public static Object getSimpleProperty(Object bean,String name){
        try {
            return org.apache.commons.beanutils.PropertyUtils.getProperty(bean,name);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }
        return null;
    }

    public static void setProperty(Object bean,String name,Object value){
        try {
            org.apache.commons.beanutils.PropertyUtils.setProperty(bean,name,value);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
        }
    }

    public static Class getPropertyType(final Object bean, final String name){
        try {
            return org.apache.commons.beanutils.PropertyUtils.getPropertyType(bean,name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
