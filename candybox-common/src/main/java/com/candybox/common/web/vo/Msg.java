package com.candybox.common.web.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/17.
 */
public class Msg implements Serializable {
    private static final String SUCCESS_CODE = "success";
    private static final String SUCCESS_DESC = "操作成功";
    private static final String ERROR_CODE = "error";
    private static final String ERROR_DESC = "操作失败";
    private static final String LOGIN_CODE = "login";
    private static final String LOGIN_DESC = "登陆超时";
    private String flag;
    private String msg;
    private Object data;

    public static Msg success(String msg , Object data){
        Msg message = new Msg();
        message.setFlag(SUCCESS_CODE);
        message.setData(data);
        message.setMsg(msg);
        return message;
    }
    public static Msg success(String msg){
        return success(msg ,null);
    }
    public static Msg success(Object data){
        return success(SUCCESS_DESC , data);
    }
    public static Msg success(){
        return success(SUCCESS_DESC);
    }

    public static Msg error(String msg){
        Msg message = new Msg();
        message.setFlag(ERROR_CODE);
        message.setMsg(msg);
        return message;
    }
    public static Msg login(){
        Msg message = new Msg();
        message.setFlag(LOGIN_CODE);
        message.setMsg(LOGIN_DESC);
        return message;
    }
    public static Msg error(){
        return error(ERROR_DESC);
    }

    private Msg() {
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return SUCCESS_CODE.equals(this.flag);
    }

    @Override
    public String toString() {
        return "Msg{" +
                "flag='" + flag + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
