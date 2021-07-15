package com.yuanian.component.mq.constant;

public enum HeadersEnum {

    ID("id","id"),
    TIMESTAMP("timestamp","时间");

    HeadersEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    private String code;
    private String title;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
