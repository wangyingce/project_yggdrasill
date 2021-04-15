package cc.leevi.webbase.support;


import cc.leevi.webbase.constants.ResponseCode;

import java.io.Serializable;

/**
 * response 返回
 *
 * Created by jiang on 2017-04-22.
 */
public class Response<T> implements Serializable{
    private T data;
    private String msg;
    private Integer code;

    public Response() {
        this.msg = ResponseCode.SUCCESS.getMsg();
        this.code = ResponseCode.SUCCESS.getCode();
    }

    public Response(T data) {
        this.data = data;
        this.msg = ResponseCode.SUCCESS.getMsg();
        this.code = ResponseCode.SUCCESS.getCode();
    }
    public static Response ok() {
        return new Response();
    }
    public static Response ok(Object data) {
        return new Response(data);
    }
    public static Response error(){
        Response response = new Response();
        response.responseCode(ResponseCode.ERROR);
        return response;
    }

    public static Response error(String msg){
        Response response = new Response();
        response.setMsg(msg);
        response.setCode(ResponseCode.ERROR.getCode());
        return response;
    }
    public static Response error(ResponseCode responseCode){
        Response response = new Response();
        response.setMsg(responseCode.getMsg());
        response.setCode(responseCode.getCode());
        return response;
    }
    public static Response fail(){
        Response response = new Response();
        response.responseCode(ResponseCode.FAIL);
        return response;
    }

    public static Response forbidden() {
        Response response = new Response();
        response.responseCode(ResponseCode.FORBIDDEN);
        return response;
    }

    public static Response forbidden(String msg) {
        Response response = new Response();
        response.responseCode(ResponseCode.FORBIDDEN);
        response.setMsg(msg);
        return response;
    }

    public static Response unauthorized() {
        Response response = new Response();
        response.responseCode(ResponseCode.TOKEN_EXPIRED);
        return response;
    }

    public static Response unauthorized(String msg) {
        Response response = new Response();
        response.responseCode(ResponseCode.TOKEN_EXPIRED);
        response.setMsg(msg);
        return response;
    }

    public void responseCode(ResponseCode responseCode){
        this.setMsg(responseCode.getMsg());
        this.setCode(responseCode.getCode());
    }
    public static Response invalid(String msg) {
        Response response = invalid();
        response.setMsg(msg);
        return response;
    }

    public static Response invalid() {
        Response response = new Response();
        response.responseCode(ResponseCode.PARAMETER_INVALID);
        return response;
    }
    public static Response noUser() {
        Response response = new Response();
        response.responseCode(ResponseCode.NO_USER);
        return response;
    }
    public static Response lockedUser() {
        Response response = new Response();
        response.responseCode(ResponseCode.LOCKED_USER);
        return response;
    }
    public static Response passwordError(String totalError) {
        Response response = new Response();
        response.setData(totalError);
        response.responseCode(ResponseCode.PASSWORD_ERROR);
        return response;
    }
    public static Response passwordInit() {
        Response response = new Response();
        response.responseCode(ResponseCode.PASSWORD_INIT);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
