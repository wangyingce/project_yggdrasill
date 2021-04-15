package cc.leevi.webbase.utils;

import java.util.List;
import java.util.Optional;

public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils{

    /**
     * 根据异常类型获取根异常
     * @param throwable
     * @param throwableClass
     * @return
     */
    public static Throwable getRootCause(Throwable throwable,Class<? extends Throwable> throwableClass){
        List<Throwable> throwableList = getThrowableList(throwable);
        Optional<Throwable> first = throwableList.stream().filter(t -> t.getClass().isAssignableFrom(throwableClass)).findFirst();
        if(first.isPresent()){
            return first.get();
        }
        return throwable;
    }
}
