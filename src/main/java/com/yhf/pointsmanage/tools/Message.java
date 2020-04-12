package com.yhf.pointsmanage.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yhf.pointsmanage.constant.Constant;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求响应Bean
 * 使用JSON包装请求返回，使用jackson库
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {
    private int code ;
    private String message ;
    private Map<String,Object> data = new HashMap<String, Object>();

    /**
     * 自定义返回
     * @param code
     * @param message
     * @return
     */
    public Message setMessage(int code, String message){
        this.code = code;
        this.message = message;
        return this;
    }

    /**
     * 返回成功
     * @return
     */
    public Message setSuccessMessage(){
        this.code = Constant.SUCCESS ;
        this.message = "操作成功" ;
        return this;
    }

    /**
     * 返回成功
     * @param message
     * @return
     */
    public Message setSuccessMessage(String message){
        this.code = Constant.SUCCESS ;
        this.message = message ;
        return this;
    }

    /**
     * 返回错误
     * @param message
     * @return
     */
    public Message setErrorMessage(String message){
        this.code = Constant.ERROR ;
        this.message = message ;
        return this;
    }

    /**
     * 返回警告
     * @param message
     * @return
     */
    public Message setWarnMessage(String message){
        this.code = Constant.WARN ;
        this.message = message ;
        return this;
    }

    /**
     * 返回登录失败
     * @param message
     * @return
     */
    public Message setLoginFailMessage(String message){
        this.code = Constant.LOGIN_FAILED ;
        this.message = message ;
        return this;
    }

    /**
     * 返回没有权限
     * @param message
     * @return
     */
    public Message setPermissionDeniedMessage(String message){
        this.code = Constant.PERMISSION_DENIED ;
        this.message = message ;
        return this;
    }
}
