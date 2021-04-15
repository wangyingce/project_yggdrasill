package cc.leevi.webbase.constants;

import java.util.HashMap;

public class CodeConst {

    public static final class ConfigCode {

        public static final String C_PAY_LIABILITY_1 = "死亡";
        public static final String C_PAY_LIABILITY_2 = "伤残";
        public static final String C_PAY_LIABILITY_3 = "医疗费用";
        public static final String C_PAY_LIABILITY_4 = "津贴";
        public static final String C_PAY_TYP_1 = "N9025001";

        //公众账号ID
        public static final String APPID = "appid";
        //公众帐号密钥
        public static final String APPSECRET = "appsecret";
        //商户号
        public static final String MCH_ID = "mch_id";
        //API密匙
        public static final String PARTNERKEY = "partnerkey";
        //weixin
        public static final String WEIXIN = "weixin";
        /** 单号类型 */
        public static final String NumberType = "numberType";
        /** 微信订单号 开头*/
        public static final String yywxorderinfo = "yywxorderinfo";

        //用户关注推送消息
        public static final String SubScribeSendText = "SubScribeSendText";
        //验证码类型
        public static final String Verity = "Verity";
        //商户验证码过期时间
        public static final String VerityMerchantTime = "VerityMerchantTime";
        //阿里大于
        public static final String alidayu = "alidayu";
        //阿里大于短信key
        public static final String alidayuappkey = "appkey";
        //阿里大于短信密匙
        public static final String alidayusecret = "secret";
        //短信前名code
        public static final String smsfreesignname = "smsfreesignname";
        //微信支付成功回调类型
        public static final String weixinpayback = "weixinpayback";
        //微信支付成功商户通知openid
        public static final String wxtemplatesendid = "wxtemplatesendid";

    }

    /**
     * 接口代码
     */
    public static final class ServerCode {
        /** 微信普通消息发送 */
        public static final String WxMessageSendUrl = "WxMessageSendUrl";
        /** 微信模板消息发送*/
        public static final String WxTemplateMessageSendUrl = "WxTemplateMessageSendUrl";
        /** 微信页面授权 */
        public static final String WXauthorize = "WXauthorize";
        /** 微信页面授权access_token */
        public static final String WXauthorizeaccess = "WXauthorizeaccess";
        /** 微信查询订单 */
        public static final String WXorderquery = "WXorderquery";
        /** 微信统一下单接口 */
        public static final String WXunifiedorder = "WXunifiedorder";
        /** 微信拉取用户信息 */
        public static final String WXuserinfo = "WXuserinfo";
        /** 系统域名 */
        public static final String ysyl = "ysyl";
        /**搜索引擎*/
        public static final String searchUrl = "searchUrl";
        /**阿里大于短信发送*/
        public static final String AliDayuMessage = "AliDayuMessage";
    }

    /**
     * 基础代码类型
     */
    public static final class CodeType {
        /** 微信模板 */
        public static final String WxTemplateId = "WxTemplateId";
        /** 消息头信息 */
        public static final String Message = "Message";
        /** 微信支付类型 */
        public static final String BankType = "BankType";
        /** 摄影工作室城市 */
        public static final String PgCity = "PgCity";
        /** 摄影工作室拍照类型 */
        public static final String PgPhotoType = "PgPhotoType";
    }

    /**
     * 消息头信息
     * */
    public static final class Message{
        /** 支付成功头信息 */
        public static final String Message_1 = "1";
        /** 支付成功尾信息 */
        public static final String Message_2 = "2";
    }

    /**
     * 跳转页面入参
     * */
    public static final class UrlType{
        /** 微信小店跳转入参ws-wxstore */
        public static final String ws = "ws";
        /** 微信支付入口**/
        public static final String wp = "wp";
        /** 摄影工作室 pg-wxphotograph */
        public static final String wpg = "wpg";
        /** 摄影工作室 获取底片菜单*/
        public static final String wpgp = "wpgp";
    }

    /**
     * 单号编码，对应utikey表的fieldCode字段
     * */
    public static final class NoCode{
        /** 微店商户id */
        public static final String WaresOwnerNo = "id";
        /** 支付订单号 */
        public static final String payNo = "payNo";
        /** 用户编码 */
        public static final String userCode = "userCode";
    }

    /**
     * tablename表名，单号生成表utikey表tablename使用
     * */
    public static final class TableName{
        /** 微店商户id */
        public static final String yywaresowner = "yywaresowner";
        /** 支付订单号 */
        public static final String yypay = "yypay";
        /** 用户注册 */
        public static final String yyuser = "yyuser";
    }

    /**
     * 系统环境变量
     * */
    public static final class EnvironmenTypeCode{
        /** 开发环境 */
        public static final String dev = "dev";
        /** 测试环境 */
        public static final String test = "test";
    }

    /**
     * 渠道
     * */
    public static final class ChannelType{
        /** 微信 */
        public static final String wx = "wx";
        /** 支付宝 */
        public static final String aliPay = "alipay";
        /** 其它 */
        public static final String none = "none";
    }

    /**
     * 业务来源
     * */
    public static final class SourceType{
        /** 摄影工作室 */
        public static final String pg = "pg";
        /** 微商 */
        public static final String store = "store";
        /** 微信支付 */
        public static final String wxpay = "wxpay";
    }

    /**
     * 支付状态
     * */
    public static final class PayState{
        /** 待支付 */
        public static final String waitPay = "01";
        /** 支付成功 */
        public static final String successPay = "11";
        /** 转入退款  */
        public static final String refundPay = "12";
        /** 已关闭 */
        public static final String closePay = "13";
        /** 已撤销（刷卡支付） */
        public static final String cancelPay = "14";
        /** 用户支付中 */
        public static final String doingPay = "15";
        /** 支付失败(其他原因，如银行返回失败) */
        public static final String failPay = "99";
        /** 自定义状态：已完成 */
        public static final String finishPay = "67";
        /** 自定义状态：已取消（用户手动触发取消按钮） */
        public static final String userCancelPay = "65";
        /** 自定义状态：已取消（超时取消） */
        public static final String sysCancelPay = "66";

    }

    public static final HashMap<String,String> PayStateName_Map = new HashMap<String,String>();
    static{
        PayStateName_Map.put("01","待付款");
        PayStateName_Map.put("99","支付失败");
        PayStateName_Map.put("11","付款成功");
        /** 增加一个单独状态 67代表已完成*/
        PayStateName_Map.put("67","已完成");
        /** 增加一个单独状态 65已取消（用户手动触发取消按钮)*/
        PayStateName_Map.put("65","已取消-用户取消");
        /** 增加一个单独状态 66已取消（超时取消）*/
        PayStateName_Map.put("66","已取消-超过支付时效取消");
    }

    /**
     * 订单检查状态
     * */
    public static final class PayCheckState{
        /** 00|待核对*/
        public static final String waitCheck = "00";
        /** ，01|待支付  */
        public static final String waitPay = "01";
        /** 11|支付成功 */
        public static final String successPay = "11";
    }
    /**
     * 币别
     *  */
    public static final class Currency{
        /** 人民币 */
        public static final String CNY = "CNY";
    }

    /**
     * 有效标志
     * */
    public static final class Valid{
        /** 有效 */
        public static final String valid = "1";
        /** 无效*/
        public static final String noValid = "1";
    }

    public static final class Uwflag{
        /** 待审核 */
        public static final String wait = "3";
        /** 未激活 */
        public static final String check = "2";
        /** 通过 */
        public static final String pass = "1";
    }

    public static final class VerifyType{
        /** 商户注册验证 */
        public static final String merchantRegist = "1";
        /** 摄影工作室注册验证 */
        public static final String photoGraphRegist = "2";
    }

    public static final class VerifyState{
        /** 发送成功 */
        public static final String success = "success";
        /** 发送失败 */
        public static final String fail = "fail";
    }

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
