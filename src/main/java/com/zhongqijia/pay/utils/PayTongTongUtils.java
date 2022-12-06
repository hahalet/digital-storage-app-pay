package com.zhongqijia.pay.utils;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.zhongqijia.pay.bean.Collection;
import com.zhongqijia.pay.bean.MyOrder;
import com.zhongqijia.pay.bean.UserGrant;
import com.zhongqijia.pay.bean.Users;
import com.zhongqijia.pay.common.util.RedisUtil;
import fosun.sumpay.merchant.integration.core.request.MerchantBaseRequest;
import fosun.sumpay.merchant.integration.core.request.Request;
import fosun.sumpay.merchant.integration.core.request.outer.c2c.QueryC2CH5UrlRequest;
import fosun.sumpay.merchant.integration.core.request.outer.c2c.QueryUserStatusRequest;
import fosun.sumpay.merchant.integration.core.request.outer.cashier.TradeOrderApplyRequest;
import fosun.sumpay.merchant.integration.core.request.outer.perfectbill.QueryOrderStatusRequest;
import fosun.sumpay.merchant.integration.core.service.SumpayService;
import fosun.sumpay.merchant.integration.core.service.SumpayServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayTongTongUtils {
    public static Map<String, String> post(MerchantBaseRequest req, String domain, String tongtongPayRoot){
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
        log.info("post start============");
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

    public static Map<String, String> getWalletInfo(Integer userId, String tongtongPayRoot){
        try{
            QueryUserStatusRequest req =new QueryUserStatusRequest();
            req.setFormat("JSON");
            req.setTerminal_type("wap");
            req.setService("cn.sumpay.scene.c2c.user.query.user.status");
            req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            req.setVersion("1.0");
            req.setApp_id("200102239651");
            req.setOut_user_id(userId.toString());
            req.setPartner_id("200102239651");
            req.setPage_index("1");
            Map<String, String> res = post(req,  null, tongtongPayRoot);
            log.info(JSON.toJSONString(res));
            return res;
        }catch (Exception e){
            log.info("getWalletInfo error:{}",e.getMessage());
        }
        return null;
    }

    public static JSONObject getOrderInfo(String orderNo, String domain, String tongtongPayRoot){
        QueryOrderStatusRequest req =new QueryOrderStatusRequest();
        req.setFormat("JSON");
        req.setTerminal_type("wap");
        req.setService("fosun.sumpay.api.trade.order.search.merchant");
        req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        req.setVersion("1.0");
        req.setApp_id("200102239651");
        req.setOrder_no(orderNo);
        Map<String, String> res = post(req, domain,tongtongPayRoot);
        Log.info(JSON.toJSONString(res));
        if(res!=null && res.get("resp_code").equals("000000")){
            return JSONObject.parseObject(JSON.toJSONString(res));
        }else{
            return null;
        }
    }

    public static String payFirst(Users users, String tongtongPayRoot,
                                  MyOrder myOrder, Collection collection,
                                  String return_url, String notify_url, String domain, String payerClientIp, RedisUtil redisUtil){
        String jsonString = null;
        try{
            Map<String, String> resUserInfo = PayTongTongUtils.getWalletInfo(users.getUserId(),tongtongPayRoot);
            String user_id = null;
            if(resUserInfo!=null && resUserInfo.get("resp_code").equals("000000")){
                String user_status_list = resUserInfo.get("user_status_list");
                JSONArray jsa = JSONObject.parseArray(user_status_list);
                if(!jsa.isEmpty()){
                    JSONObject js = (JSONObject)jsa.get(0);
                    user_id = js.getString("user_id");
                }
            }
            if(user_id==null){
                return null;
            }
            String needNotify = "1";
            String needReturn = "1";
            String sub_merid = "";
            String mer_id = "200102239651";
            String dtLong  = "yyyyMMddHHmmss";
            DateFormat df = new SimpleDateFormat(dtLong);
            Date date = new Date();
            String order_time = df.format(date);

            BigDecimal decimal = myOrder.getPrice();
            BigDecimal setScale = decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            String order_amt = setScale.toPlainString();
            String terminal_type = "wap";
            String goods_name = collection.getName();
            String goods_num = myOrder.getNumber().toString();
            String goods_type = "2";
            String attach = "123";
            String img_url = collection.getImg();
            String pay_channels = "";
            TradeOrderApplyRequest req = new TradeOrderApplyRequest();
            req.setMer_no(mer_id);
            req.setApp_id(mer_id);
            req.setSub_mer_no(sub_merid);
            req.setNeed_notify(needNotify);
            req.setNeed_return(needReturn);
            req.setNotify_url(notify_url);
            req.setReturn_url(return_url);
            req.setUser_id(user_id);
            String mer_order_no = PayHelp.FIRST_ORDER+myOrder.getOrderno();
            if(mer_order_no.length()<=30){
                Object indexObj = redisUtil.get(RedisHelp.TT_PAY_ORDER_KEY+mer_order_no);
                Integer index = 0;
                if(indexObj == null){
                    index = 0;
                }else{
                    index = (Integer)indexObj;
                }
                index++;
                redisUtil.set(RedisHelp.TT_PAY_ORDER_KEY+mer_order_no,index);
                mer_order_no = mer_order_no+"_"+index;
            }
            req.setOrder_no(mer_order_no);
            req.setOrder_time(order_time);
            req.setOrder_amount(order_amt);
            req.setGoods_name(goods_name);
            req.setGoods_num(goods_num);
            req.setGoods_type(goods_type);
            req.setAttach(attach);
            req.setUser_id_type("1");
            req.setUser_ip_addr(payerClientIp);
            req.setPay_channels(pay_channels);
            req.setMer_logo_url(img_url);
            req.setTerminal_type(terminal_type);
            req.setService(String.format("fosun.sumpay.cashier.wap.trade.order.apply"));
            req.setVersion("1.0");
            req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            Map<String, String> res = post(req,  domain, tongtongPayRoot);
            log.info(JSON.toJSONString(res));
            //{"resp_code":"000000","redirect_url":"https://pay.sumpay.cn/cashier-fed/pages/wap/index.html#/?key=dc207e68-b08f-4316-ae05-f105887af7d8&pre=0"}
            //JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(res));
            jsonString = JSON.toJSONString(res);
        }catch (Exception e){
            log.info("getWalletInfo error:{}",e.getMessage());
        }
        return jsonString;
    }

    public static void main(String[] args){
        Users users = new Users();
        users.setUserId(119414);
        //getWalletInfo(users, "C:\\Users\\llg\\qyy\\server\\digital-storage-pay\\src\\main\\resources\\tongtong\\");
        MyOrder myOrder = new MyOrder();
        myOrder.setOrderno("test2234567");
        myOrder.setPrice(new BigDecimal(1));
        myOrder.setNumber(2);
        myOrder.setEndtime(LocalDateTime.now());
        Collection collection = new Collection();
        collection.setName("测试");
        collection.setImg("http://120.77.148.40/taurus/profile/upload/2022/11/22/036ae2a48819792f883d8cedeff40d6e_20221122140928A064.jpeg");
        String return_url = "http://120.77.148.40/h5/#/pages/good/detail/paystatus?paytype=3";
        String notify_url = "http://120.77.148.40/aries2/tongtong/payCallback";
        String domain = "http://120.77.148.40";
        myOrder.getPrice().setScale(2,BigDecimal.ROUND_HALF_DOWN);
        String ip_address = "112.74.161.59";
        payFirst(users,"C:\\Users\\llg\\qyy\\server\\digital-storage-pay\\src\\main\\resources\\tongtong\\",
                myOrder,collection,return_url,notify_url,domain,ip_address,
                null);
    }

    public static String paySecond(Users buyUser,UserGrant userGrant, Collection collection, String payerClientIp, String tongtongPayRoot, String return_url, String notify_url, String domain, RedisUtil redisUtil, String walletLoginNotifyUrl) {
        String jsonString = null;
        try{
            Map<String, String> resUserInInfo = PayTongTongUtils.getWalletInfo(userGrant.getUserid(),tongtongPayRoot);
            String user_in_id = null;
            if(resUserInInfo!=null && resUserInInfo.get("resp_code").equals("000000")){
                String user_status_list = resUserInInfo.get("user_status_list");
                JSONArray jsa = JSONObject.parseArray(user_status_list);
                if(!jsa.isEmpty()){
                    JSONObject js = (JSONObject)jsa.get(0);
                    user_in_id = js.getString("user_id");
                }
            }
            Map<String, String> resUserInfo = PayTongTongUtils.getWalletInfo(userGrant.getOppositeuser(),tongtongPayRoot);
            String user_id = null;
            if(resUserInfo!=null && resUserInfo.get("resp_code").equals("000000")){
                String user_status_list = resUserInfo.get("user_status_list");
                JSONArray jsa = JSONObject.parseArray(user_status_list);
                if(!jsa.isEmpty()){
                    JSONObject js = (JSONObject)jsa.get(0);
                    user_id = js.getString("user_id");
                }
            }
            if(user_id==null){
                return loginWallet(buyUser,walletLoginNotifyUrl,domain,tongtongPayRoot);
            }
            log.info("user_in_id:{},user_id:{}",user_in_id,user_id);
            if(user_in_id==null){
                return null;
            }
            String needNotify = "1";
            String needReturn = "1";
            String sub_merid = "";
            String mer_id = "200102239651";
            String dtLong  = "yyyyMMddHHmmss";
            DateFormat df = new SimpleDateFormat(dtLong);
            Date date = new Date();
            String order_time = df.format(date);

            BigDecimal decimal = userGrant.getSellprice();
            BigDecimal setScale = decimal.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            String order_amt = setScale.toPlainString();
            String terminal_type = "wap";
            String goods_name = collection.getName();
            String goods_num = "1";
            String goods_type = "2";
            String attach = "123";
            String img_url = collection.getImg();
            String pay_channels = "";
            TradeOrderApplyRequest req = new TradeOrderApplyRequest();
            req.setMer_no(mer_id);
            req.setApp_id(mer_id);
            req.setSub_mer_no(sub_merid);
            req.setNeed_notify(needNotify);
            req.setNeed_return(needReturn);
            req.setNotify_url(notify_url);
            req.setReturn_url(return_url);
            req.setUser_id(user_id);
            //分润
            req.setShare_benefit_flag("1");
            BigDecimal second = decimal.multiply(PayHelp.SECOND_ORDER_PERCENT).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    		req.setShare_benefit_exp("{\"share_type\":\"1\",\"prior\":\"1\",\"benefit_bean_list\":[{\"mer_no\":\""+user_in_id
                                    + "\",\"share_type\":\"1\",\"prior\":\"1\",\"amount\":\""
                                    + second.toPlainString() + "\"}]}");
            String mer_order_no = PayHelp.SECOND_ORDER + userGrant.getId()+"_"+userGrant.getBuytime().getTime();
            if(mer_order_no.length()<=30){
                Object indexObj = redisUtil.get(RedisHelp.TT_PAY_ORDER_KEY+mer_order_no);
                Integer index = 0;
                if(indexObj == null){
                    index = 0;
                }else{
                    index = (Integer)indexObj;
                }
                index++;
                redisUtil.set(RedisHelp.TT_PAY_ORDER_KEY+mer_order_no,index);
                mer_order_no = mer_order_no+"_"+index;
            }
            req.setOrder_no(mer_order_no);
            req.setOrder_time(order_time);
            req.setOrder_amount(order_amt);
            req.setGoods_name(goods_name);
            req.setGoods_num(goods_num);
            req.setGoods_type(goods_type);
            req.setAttach(attach);
            req.setUser_id_type("1");
            req.setUser_ip_addr(payerClientIp);
            req.setPay_channels(pay_channels);
            req.setMer_logo_url(img_url);
            req.setTerminal_type(terminal_type);
            req.setService(String.format("fosun.sumpay.cashier.wap.trade.order.apply"));
            req.setVersion("1.0");
            req.setTimestamp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            log.info("request:{}",JSON.toJSONString(req));
            Map<String, String> res = post(req,  domain, tongtongPayRoot);
            log.info(JSON.toJSONString(res));
            //{"resp_code":"000000","redirect_url":"https://pay.sumpay.cn/cashier-fed/pages/wap/index.html#/?key=dc207e68-b08f-4316-ae05-f105887af7d8&pre=0"}
            //JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(res));
            jsonString = JSON.toJSONString(res);
        }catch (Exception e){
            log.info("getWalletInfo error:{}",e.getMessage());
        }
        return jsonString;
    }
}
