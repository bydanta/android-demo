package com.template.http;

import java.io.Serializable;
import java.util.Map;

/**
 * 公共属性类
 */
public class HttpResults implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String mesg;
    private Map<String,Object> map;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
