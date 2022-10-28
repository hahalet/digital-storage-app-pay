package com.zhongqijia.pay.bean.paysand;

import java.util.ArrayList;
import java.util.List;
public class Data
{
    private String bizType;

    private List<CardInfo> cardInfo;

    private String orderNo;

    private String masterAccount;

    private String respTime;

    private String bizUserNo;

    private String mid;

    private String respMsg;

    private String respCode;

    public void setBizType(String bizType){
        this.bizType = bizType;
    }
    public String getBizType(){
        return this.bizType;
    }
    public void setCardInfo(List<CardInfo> cardInfo){
        this.cardInfo = cardInfo;
    }
    public List<CardInfo> getCardInfo(){
        return this.cardInfo;
    }
    public void setOrderNo(String orderNo){
        this.orderNo = orderNo;
    }
    public String getOrderNo(){
        return this.orderNo;
    }
    public void setMasterAccount(String masterAccount){
        this.masterAccount = masterAccount;
    }
    public String getMasterAccount(){
        return this.masterAccount;
    }
    public void setRespTime(String respTime){
        this.respTime = respTime;
    }
    public String getRespTime(){
        return this.respTime;
    }
    public void setBizUserNo(String bizUserNo){
        this.bizUserNo = bizUserNo;
    }
    public String getBizUserNo(){
        return this.bizUserNo;
    }
    public void setMid(String mid){
        this.mid = mid;
    }
    public String getMid(){
        return this.mid;
    }
    public void setRespMsg(String respMsg){
        this.respMsg = respMsg;
    }
    public String getRespMsg(){
        return this.respMsg;
    }
    public void setRespCode(String respCode){
        this.respCode = respCode;
    }
    public String getRespCode(){
        return this.respCode;
    }
}