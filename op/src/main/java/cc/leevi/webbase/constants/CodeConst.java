package cc.leevi.webbase.constants;

public class CodeConst {

    //搜索引擎格式类型
    public static final class SearchType{
        /** 错误信息 */
        public static final String errorMessage = "errorMessage";
        /** 接口交互日志 */
        public static final String interfMessage = "interfMessage";
    }

    //接口类型
    public static final class InterfType{
        //统一下单
        public static final String unifiedorder = "unifiedorder";
        //支付
        public static final String weixinpay = "weixinpay";
    }

    //0和1
    public static final class isOrNot{
        //统一下单
        public static final String is_zero = "0";
        //支付
        public static final String is_one = "1";
    }

    /**
     * 短信模板名称
     */
    public static final class ShortMessageTemplate {
        /** 验证码短信 */
        public static final String SmsTemplateCode_1 = "SmsTemplateCode_1";

    }
}
