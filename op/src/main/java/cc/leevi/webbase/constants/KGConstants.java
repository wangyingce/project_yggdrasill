package cc.leevi.webbase.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class KGConstants {

    public static final String TRACE_ID_KEY = "rid";

    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final int HTTP_STATUS_OK = 200;
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8; //Charset.forName(DEFAULT_CHARSET_NAME);

    /** 各种分隔符 https://www.computerhope.com/keys.htm */
    public static final class Separator {
        public static final String  Enter      = System.getProperty("line.separator");
        public static final String  Tilde      = "~";
        public static final String  At         = "@";
        public static final String  Underscore = "_";
        public static final String  Hyphen     = "-";
        public static final String  Plus       = "+";
        public static final String  Equal      = "=";
        public static final String  Octothorpe = "#";
        public static final String  Dollar     = "$";
        public static final String  Percent    = "%";
        public static final String  And        = "&";
        public static final String  Pipe       = "|";
        public static final String  Star       = "*";
        public static final String  Point      = ".";
        public static final String  Semicolon  = ";";
        public static final String  Colon      = ":";
        public static final String  Comma      = ",";
        public static final String  Question   = "?";
        public static final String  ForwardSlash= "/";
        public static final String  BackSlash   = "\\";

    }

    /**处理程序标识：成功失败：0失败1成功*/
    public static final class Handle {
        public static final String  SUCCESS = "1";   //1成功
        public static final String  FAIL    = "0";   //0失败
        public static final String  ERROR   = "9";   //9未知错误
        public static final Boolean TRUE    = true;  //成功
        public static final Boolean FALSE   = false; //失败
    }


    public static final String fileDir = System.getProperty("user.dir") + "/export/";

    /**处理程序标识：成功失败：0失败1成功*/
    public static final class PrefixFileName {
        public static final String  OnlyHealth  = "OnlyHealth";   //07健康险
        public static final String  RangeHealth = "RangeHealth";   //07健康险
        public static final String  accident    = "accident";   //06意外险
        public static final String  property    = "property";   //非06 07 财险
        public static final String automobile   = "automobile";  //车险
        public static final String DataStream_Json2CSV = "Json2CSV";  //数据处理
    }

    public static final HashMap <Integer,String> NodeColorMap = new HashMap<Integer,String>();
    static{
        NodeColorMap.put(1,"#FFCF3C");
        NodeColorMap.put(2,"#C990C0");
        NodeColorMap.put(3,"#F79767");
        NodeColorMap.put(4,"#57C7E3");
        NodeColorMap.put(5,"#F16667");
        NodeColorMap.put(6,"#D9C8AE");
        NodeColorMap.put(7,"#8DCC93");
        NodeColorMap.put(8,"#ECB5C9");
        NodeColorMap.put(9,"#4C8EDA");
        NodeColorMap.put(10,"#FFC454");
        NodeColorMap.put(11,"#DA7194");
        NodeColorMap.put(12,"#569480");
    }

}
