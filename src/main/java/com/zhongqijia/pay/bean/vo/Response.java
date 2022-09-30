package com.zhongqijia.pay.bean.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhongqijia.pay.bean.exception.BaseException;
import com.zhongqijia.pay.bean.exception.ErrorType;
import com.zhongqijia.pay.bean.exception.SystemErrorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 * @author nicolasli
 */
@ApiModel(description = "rest请求的返回模型，所有rest正常都返回该类的对象")
@Getter
public class Response<T> {
    @ApiModelProperty(value = "返回状态", required = true)
    private ResponseInfo status;

    @ApiModelProperty(value = "返回对象")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T bo;

    @ApiModelProperty(value = "当前时间戳")
    private Long timestamps;

    public Response() {
        this.status = ResponseInfo.SUCCESS;
    }

    /**
     * @param status
     */
    public Response(HttpStatus status) {
        this.status = new ResponseInfo(status.value() + "",status.getReasonPhrase());
    }
    /**
     * @param errorType
     */
    public Response(ErrorType errorType) {
        this.status = new ResponseInfo(errorType);
        this.timestamps = System.currentTimeMillis()/1000;
    }

    /**
     * @param errorType
     * @param bo
     */
    public Response(ErrorType errorType, T bo) {
        this(errorType);
        this.bo = bo;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param mesg
     */
    private Response(String code, String mesg) {
        this.status = new ResponseInfo(code, mesg);
        this.timestamps = System.currentTimeMillis()/1000;
        this.timestamps = System.currentTimeMillis()/1000;;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param mesg
     * @param bo
     */
    private Response(String code, String mesg, T bo) {
        this.status = new ResponseInfo(code, mesg);
        this.bo = bo;
        this.timestamps = System.currentTimeMillis()/1000;
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param bo
     * @return Result
     */
    public static Response success(Object bo) {
        return new Response<>(ResponseInfo.SUCCESS.getCode(), ResponseInfo.SUCCESS.getMessage(), bo);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Response success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static Response fail() {
        return new Response(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException
     * @return Result
     */
    public static Response fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param bo
     * @return Result
     */
    public static Response fail(BaseException baseException, Object bo) {
        return new Response<>(baseException.getErrorType(), bo);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @param bo
     * @return Result
     */
    public static Response fail(ErrorType errorType, Object bo) {
        return new Response<>(errorType, bo);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @return Result
     */
    public static Response fail(ErrorType errorType) {
        return Response.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param bo
     * @return Result
     */
    public static Response fail(Object bo) {
        return new Response<>(SystemErrorType.SYSTEM_ERROR, bo);
    }

    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return ResponseInfo.SUCCESS.getCode().equals(this.status.getCode());
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    public static Response throwException(String code, String msg) {
        Response<Object> objectResponse = new Response<>();
        objectResponse.status = new ResponseInfo(code, msg);
        return objectResponse;
    }

    public void setBo(T bo) {
        this.bo = bo;
    }

    public void setStatus(ResponseInfo status) {
        this.status = status;
    }
}
