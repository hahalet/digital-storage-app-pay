package com.zhongqijia.pay.bean.paysand;

public class SandOpenWalletCallBack
{
    private String extend;

    private String charset;

    private Data data;

    private String sign;

    private String signType;

    public void setExtend(String extend){
        this.extend = extend;
    }
    public String getExtend(){
        return this.extend;
    }
    public void setCharset(String charset){
        this.charset = charset;
    }
    public String getCharset(){
        return this.charset;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
    public void setSign(String sign){
        this.sign = sign;
    }
    public String getSign(){
        return this.sign;
    }
    public void setSignType(String signType){
        this.signType = signType;
    }
    public String getSignType(){
        return this.signType;
    }
}