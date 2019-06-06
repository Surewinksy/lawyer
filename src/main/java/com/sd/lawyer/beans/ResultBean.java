package com.sd.lawyer.beans;

import java.io.Serializable;

/**
 * Created by zjhuang on 2018/3/29 21:34.
 *
 * @author 肖文杰 https://github.com/xwjie/    (感谢以下作者的技术分享)
 */
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    // 用户未登录
    public static final int NO_LOGIN = -1;
    // 请求成功
    public static final int SUCCESS = 0;
    // 校验失败
    public static final int CHECK_FAIL = 1;
    // 没有权限
    public static final int NO_PERMISSION = 2;
    // 未知错误
    public static final int UNKNOWN_EXCEPTION = -99;


    /**
     * 返回的信息(主要出错的时候使用)
     */
    private String msg = "success";

    /**
     * 接口返回码, 0表示成功, 其他看对应的定义
     * 晓风轻推荐的做法是:
     * 0   : 成功
     * >0 : 表示已知的异常(例如提示错误等, 需要调用地方单独处理)
     * <0 : 表示未知的异常(不需要单独处理, 调用方统一处理)
     */
    private int code = SUCCESS;

    /**
     * 返回的数据
     */
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = UNKNOWN_EXCEPTION;
    }

    public ResultBean failure(int code, String errmsg) {
        this.code = code;
        this.msg = errmsg;
        return this;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
