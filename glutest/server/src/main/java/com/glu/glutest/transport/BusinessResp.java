package com.glu.glutest.transport;

import javax.ws.rs.core.MediaType;

public class BusinessResp {
    public static final String JSON_CONTENT_UTF8 = MediaType.APPLICATION_JSON + "; charset=UTF-8";

    public static final int OK = 0;
    public static final int INVALID_REQUEST = 1;
    // ...
    public static final int SERVER_ERROR = 500;

    private int code;
    private String message;
    private Object payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
