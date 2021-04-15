package cc.leevi.webbase.utils;

import com.google.common.base.Throwables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyong
 *
 * @Date: 2018/12/11.
 * @Description: OriginList，TargetList
 */
public class PropertyCopyUtil {

    /**
     * 属性复制工具类
     * @param o1 属性源集合
     * @param clazz 目标类
     * @return
     */
    public static <T, Y> List<Y> sourceToTargetList(List<T> o1, Class<Y> clazz) {
        List<Y> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(o1)) {
            for (T o : o1) {
                Y y = null;
                try {
                    y = clazz.newInstance();
                } catch (InstantiationException e) {
                    Throwables.throwIfUnchecked(e);
                } catch (IllegalAccessException e) {
                    Throwables.throwIfUnchecked(e);
                }
                BeanUtils.copyProperties(y, o);
                list.add(y);
            }
        }
        return list;
    }
}
