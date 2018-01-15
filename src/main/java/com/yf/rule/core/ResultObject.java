package com.yf.rule.core;

public class ResultObject {
    private int code;
    private String msg;
    private Object data;

    public ResultObject(){}

    public ResultObject(int code, String msg, Object data){
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
}
