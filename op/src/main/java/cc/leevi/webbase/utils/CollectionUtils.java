package cc.leevi.webbase.utils;

import java.util.Arrays;
import java.util.List;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {


    /**
     *  将list<String> 转换为 sql in 中的格式  'a','b',....'n'
     */
    public static String listToString(List<String> list){
        StringBuffer sbf = new StringBuffer();
        list.forEach(obj -> {sbf.append("'").append(obj).append("',");});
        String str = sbf.substring(0, sbf.length() - 1);
        return str;
    }

    /**
     *  将list<String> 转换为 sql in 中的格式  'a','b',....'n'
     */
    public static String listToString(String... args){
       return listToString(Arrays.asList(args));
    }

//    public static void main(String[] args) {
//        String s = CollectionUtils.listToString("1", "2", "3", "4", "5");
//        System.out.println(s);//'1','2','3','4','5'
//    }
}
