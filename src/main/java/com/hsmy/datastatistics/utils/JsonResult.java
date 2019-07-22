package com.hsmy.datastatistics.utils;

import java.util.HashSet;
import java.util.Set;

public class JsonResult {
    private Object obj;
    private String msg;
    private String code;

    public JsonResult() {
    }

    public JsonResult(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
