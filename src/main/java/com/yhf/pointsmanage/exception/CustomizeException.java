package com.yhf.pointsmanage.exception;

public class CustomizeException extends RuntimeException {
    private String retCd ;  //异常对应的返回码
    private String msgDes;  //异常对应的描述信息

    public CustomizeException() {
        super();
    }

    public CustomizeException(String message) {
        super(message);
        msgDes = message;
    }

    public CustomizeException(String retCd, String msgDes) {
        super();
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return retCd;
    }

    public String getMsgDes() {
        return msgDes;
    }
}
