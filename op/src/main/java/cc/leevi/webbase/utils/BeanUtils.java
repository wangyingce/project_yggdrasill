package cc.leevi.webbase.utils;

import com.google.common.base.Throwables;

public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    public static void copyProperties(Object dest, Object orig) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            //非业务类型抛RuntimeExcetion , 业务类型抛AppException
            Throwables.throwIfUnchecked(e);
        }
    }
}
