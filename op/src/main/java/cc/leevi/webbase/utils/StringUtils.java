package cc.leevi.webbase.utils;

import com.google.common.base.Throwables;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static String urlDncode(String source){
        try {
            return URLDecoder.decode(source,DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            Throwables.throwIfUnchecked(e);
        }
        return null;
    }

    public static boolean isNotEmpty(Object obj){
        if(obj != null){
            if(obj instanceof String){
                return isNotEmpty((String)obj);
            }
        }
        return false;
    }

    public static boolean isEmpty(Object obj){
        return !isNotEmpty(obj);
    }

    public static String urlEncode(String source){
        try {
            return URLEncoder.encode(source, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            Throwables.throwIfUnchecked(e);
        }
        return null;
    }

    //校验是否数字类型
    public static boolean isNumber(String str){
        String reg = "^-?[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

    public static void main(String[] args) {
        //System.out.println(isNumber("100.12345"));
        boolean equals = "1".equals(null);
        System.out.println(equals);
    }
    /**
     * 获取字符串
     * @param obj
     * @return
     */
    public static String getStringValue(Object obj)
    {
        if (obj == null)
        {
            return "";
        }
        return String.valueOf(obj);
    }

    /**
     * 判断字符串为空
     * @param data
     * @return
     */
    public static boolean isNull(String data)
    {
        //入参@RquestBody中已经全局处理trim
        //if (null == data || "".equals(data.trim()))
        //{
        //    return true;
        //}
        //return false;
        return org.apache.commons.lang3.StringUtils.isEmpty(data);
    }

    /**
     * 判断字符串是否为空
     * @param data
     * @return
     */
    public static boolean isNotNull(String data)
    {
        return !isNull(data);
    }

    /**
     * 校验手机号
     * @param data
     * @return
     */
    public static boolean isTel(String data){
        if (isNotEmpty(data) && isNumber(data) && data.length() == 11 && "1".equals(data.substring(0, 1))) {
            return true;
        }
        return false;
    }

    /**
     * 从V6Util迁移过来主要用于截取出险经过
     * @param Cde
     * @return
     */
    public static String subStringStr(String Cde) {
        int dd=40;//截取长度
        String op="\n          ";//间隔
        StringBuffer val=new StringBuffer();
        String ss=Cde;
        int len=ss.length();
        int num=len/dd;
        int yu_=len%dd;
        int you=0;
        for(int i=0;i<num;i++){
            val.append(ss.subSequence(you, (i+1)*dd)).append(op);
            you=(i+1)*dd;
        }
        if(yu_>0){
            val.append(ss.subSequence(you, len)).append(op);
        }
        return val.toString();
    }

    /**
     *字符串右补齐方法
     * @param origin
     * @param len
     * @return
     * @throws Exception
     */
    public static String rightPad(String origin, int len){
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        if(origin==null)return "";
        for (int i = 0; i < origin.length(); i++) {
            /* 获取一个字符 */
            String temp = origin.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        if(valueLength >= len){
            return origin;
        }
        for(int i=0;i<len-valueLength;i++){
            origin = origin +" ";
        }
        return origin;
    }
    public static String getDoubleFortMat(java.math.BigDecimal we){
        String ui=null;
        DecimalFormat df=null;
        if(we!=null&&we.doubleValue()>=1.0){
            df=new DecimalFormat("#,###.00");
        }else {
            df=new DecimalFormat("0.00");
        }
        return df.format(we);
    }
    public static String getDoubleFortMatByFloat(float io)  {
        String ui=null;
        DecimalFormat df=null;
        if(io>=1.0){
            df=new DecimalFormat("#,###.00");
        }else {
            df=new DecimalFormat("0.00");
        }
        return df.format(io);
    }

    public static String cleanQuotes(String str){
        return str.replace("\"","").replace("'","");
    }

    /**
     * 去特殊字符处理工具
     * @param mark
     * @return
     */
    public static String toStandStrings(String mark){
        if(mark!=null&&mark.trim().length()!=0){
            // 问题列过滤特殊字符
            mark = mark.replace("\t", " ").replace("\r", " ").replace("\n", " ").replace("\\", "/").trim();
            // 其他列过滤特殊字符后过滤空格
            mark = mark.replace(" ", "").replace("　", "").replace("'", "").replace(";", "");
            return mark;
        }else{
            return null;
        }
    }
}
