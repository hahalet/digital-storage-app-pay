package com.zhongqijia.pay.bean.paysand;
public class CardInfo
{
    private String relatedCardType;

    private String relatedCardNo;

    public void setRelatedCardType(String relatedCardType){
        this.relatedCardType = relatedCardType;
    }
    public String getRelatedCardType(){
        return this.relatedCardType;
    }
    public void setRelatedCardNo(String relatedCardNo){
        this.relatedCardNo = relatedCardNo;
    }
    public String getRelatedCardNo(){
        return this.relatedCardNo;
    }
}
