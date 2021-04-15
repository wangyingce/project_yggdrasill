package cc.leevi.webbase.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {

    /**
     * 正则提取所有并返回
     * @param str
     * @param regex
     * @param group group
     * @return
     */
    public static List<String> find(String str,String regex,int group){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<String> groups = new ArrayList<>();
        while (matcher.find()){
            groups.add(matcher.group(group));
        }
        return groups;
    }
    /**
     * 正则提取第一个并返回
     * @param str
     * @param regex
     * @param group group
     * @return
     */
    public static String findFirst(String str, String regex, int group){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.group(group);
    }
    /**
     * 正则提取所有并返回
     * @param str
     * @param regex
     * @return
     */
    public static List<String> find(String str,String regex){
        return find(str,regex,0);
    }

}
