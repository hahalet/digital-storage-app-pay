package com.zhongqijia.pay.bean.vo;

import com.zhongqijia.pay.bean.exception.ErrorType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResponseInfo implements Serializable {
    private static final long serialVersionUID = -4417715614021482064L;

    public static ResponseInfo SUCCESS = new ResponseInfo("200", "ok");

    @ApiModelProperty(value = "处理结果code")
    private String code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String message;

    public ResponseInfo() {
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
    }

    public ResponseInfo(ErrorType errorType) {
        this.code = errorType.getCode();
        this.message = errorType.getMesg();
    }

    public ResponseInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
