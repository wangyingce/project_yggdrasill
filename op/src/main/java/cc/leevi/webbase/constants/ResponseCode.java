package cc.leevi.webbase.constants;

import java.util.*;

/**
 * 返回信息
 * Created by jiang on 2017-04-23.
 */
public enum ResponseCode{
    /**
     * 成功
     */
    SUCCESS(1,"success"),
    /**
     * 失败
     */
    FAIL(-1,"fail"),
    /**
     * 错误
     */
    ERROR(-2,"error"),
    /**
     * 需要登陆
     */
    REQUIRED_LOGIN(1001,"需要重新登陆!"),
    /**
     * 参数非法
     */
    PARAMETER_INVALID(1002,"参数非法!"),
    /**
     * 没有权限
     */
    FORBIDDEN(1003,"没有权限!"),
    /**
     * 令牌过期或不存在
     */
    TOKEN_EXPIRED(1004,"令牌过期或不存在!"),
    /**
     * 无此用户
     */
    NO_USER(1005,"此用户不存在!"),
    /**
     * 用户被锁定
     */
    LOCKED_USER(1006,"用户已被锁定!"),
    /**
     * 密码错误
     */
    PASSWORD_ERROR(1007,"密码不正确!"),
    /**
     * 密码初始
     */
    PASSWORD_INIT(1008,"您的密码为初始密码，请进行修改，修改后方可进行其他操作。点“确认”进入修改用户密码页面，点“取消”回到登录页面"),
    /**
     * 密码过期
     */
    PASSWORD_OVER(1009,"密码已过期"),
    PLY_EMPTY(1010,"保单信息为空"),
    SYSCDE_EMPTY(1011,"该产品号未配置流程大类，请核实！"),
    PROD_ERROR(1013,"该操作员无产品权限"),
    SMS_ERROR(1016,"短信发送异常");
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
