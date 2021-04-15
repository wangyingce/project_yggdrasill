package cc.leevi.webbase.utils;

import java.util.Date;
import java.util.Random;

/**
 * @Auther: liujunhui
 * @Date: 2018/8/11 0011 17:41
 * @Description:
 */
public class SerialNumberUtils {
    private static final String salaryNumber="01";//薪资业务编号
    private static final String assessNumber="02";//考核业务编号

    /**
     * 薪资流水号生成规则 年月日时分秒毫秒+业务编号+4位随机数
     * @param
     * @return
     */
    public static String salarySerialNumber(){
        StringBuffer sb=new StringBuffer();
        String dateStr=DateUtils.getStringDate(new Date(),"yyyyMMddHHmmssS");
        sb.append(dateStr);
        sb.append(salaryNumber);
        //sb.append(System.currentTimeMillis());
        sb.append(getRandomNum(4));
        return sb.toString();
    }

    /**
     * 考核流水号生成规则 年月日时分秒毫秒+业务编号+4位随机数
     * @return
     */
    public static String assessSerialNumber(){
        StringBuffer sbu=new StringBuffer();
        String dateStr=DateUtils.getStringDate(new Date(),"yyyyMMddHHmmssS");
        sbu.append(dateStr);
        sbu.append(assessNumber);
        sbu.append(getRandomNum(4));
        return sbu.toString();
    }

    /**
     * 获取count个随机数
     * @param count 随机数个数
     * @return
     */
    public static String getRandomNum(int count){
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<count;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        //System.out.println(System.currentTimeMillis());
        //System.out.println(salarySerialNumber());
        //System.out.println(assessSerialNumber());
    }
}
