package com.zhongqijia.pay.utils;

import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.esotericsoftware.minlog.Log;
import com.zhongqijia.pay.bean.Users;
import fosun.sumpay.merchant.integration.core.request.MerchantBaseRequest;
import fosun.sumpay.merchant.integration.core.request.Request;
import fosun.sumpay.merchant.integration.core.request.outer.c2c.QueryC2CH5UrlRequest;
import fosun.sumpay.merchant.integration.core.request.outer.c2c.QueryUserStatusRequest;
import fosun.sumpay.merchant.integration.core.service.SumpayService;
import fosun.sumpay.merchant.integration.core.service.SumpayServiceImpl;

public class PayTongTongUtils {


    public static Map<String, String> post(MerchantBaseRequest req, String domain, String tongtongPayRoot){
        Log.info("tongtongPayRoot:{}",tongtongPayRoot);
        Request request2 = new Request();
        request2.setCharset("UTF-8");// 取jsp的请求编码
        request2.setContent(JSON.toJSONString(req)); // 业务参数的json字段
        request2.setPassword("123"); //

        request2.setPrivateKeyPath(tongtongPayRoot+"cert.pfx");
        request2.setPublicKeyPath(tongtongPayRoot+"public.cer");
        request2.setUrl("https://entrance.sumpay.cn/gateway.htm");
        if(domain!=null){
            request2.setDomain(domain);
        }
        request2.setAesEncodedWords(req.getAesEncodedWords());
        request2.setBase64EncodedWords(req.getBase64EncodedWords());
        request2.setCharsetChangeWords(req.getCharsetChangeWords());

        SumpayService ss = new SumpayServiceImpl();
        return ss.execute(request2);
    }

    public static String loginWallet(Users users, String notifyUrl, String domain, String tongtongPayRoot){
        QueryC2CH5UrlRequest req =new QueryC2CH5UrlRequest();
        req.setFormat("JSON");
        req.setTerminal_type("wap");
        req.setService("cn.sumpay.scene.c2c.wap.get.url");
        req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        req.setVersion("1.0");
        req.setApp_id("200102239651");
        req.setUser_id(users.getUserId().toString());
        req.setUser_type("0");
        req.setMer_no("200102239651");
        req.setRequest_id("jtest"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        req.setNotify_url(notifyUrl);
        //姓名
        req.setRealname(users.getRealname());
        //身份证
        req.setId_no(users.getRealno());
        Map<String, String> res = post(req, domain,tongtongPayRoot);
        Log.info(JSON.toJSONString(res));
        if(res!=null && res.get("resp_code").equals("000000")){
            return JSON.toJSONString(res);
        }else{
            return "";
        }
    }

    public static Boolean getWalletInfo(Users users, String tongtongPayRoot){
        QueryUserStatusRequest req =new QueryUserStatusRequest();
        req.setFormat("JSON");
        req.setTerminal_type("wap");
        req.setService("cn.sumpay.scene.c2c.user.query.user.status");
        req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        req.setVersion("1.0");
        req.setApp_id("200102239651");
        req.setUser_id(users.getUserId().toString());
        req.setPartner_id("200102239651");
        req.setPage_index("1");
        Map<String, String> res = post(req,  null, tongtongPayRoot);
        Log.info(JSON.toJSONString(res));
        if(res!=null && res.get("resp_code").equals("000000")){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args){
        Users users = new Users();
        users.setUserId(1);
        getWalletInfo(users, "/data/apps/pay/target/tongtong/");
    }
}
