package com.zhongqijia.pay.bean.paysand;

import lombok.Data;

@Data
public class SandPayHead
{
    private String version;

    private String respTime;

    private String respCode;

    private String respMsg;

    public void setVersion(String version){
        this.version = version;
    }
    public String getVersion(){
        return this.version;
    }
    public void setRespTime(String respTime){
        this.respTime = respTime;
    }
    public String getRespTime(){
        return this.respTime;
    }
    public void setRespCode(String respCode){
        this.respCode = respCode;
    }
    public String getRespCode(){
        return this.respCode;
    }
    public void setRespMsg(String respMsg){
        this.respMsg = respMsg;
    }
    public String getRespMsg(){
        return this.respMsg;
    }
}