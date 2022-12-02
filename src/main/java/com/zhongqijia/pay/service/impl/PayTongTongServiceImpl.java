package com.zhongqijia.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongqijia.pay.bean.Collection;
import com.zhongqijia.pay.bean.MyOrder;
import com.zhongqijia.pay.bean.TestBean;
import com.zhongqijia.pay.bean.Users;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.mapper.CollectionMapper;
import com.zhongqijia.pay.mapper.MyOrderMapper;
import com.zhongqijia.pay.mapper.TestMapper;
import com.zhongqijia.pay.mapper.UsersMapper;
import com.zhongqijia.pay.service.PayTongTongService;
import com.zhongqijia.pay.service.TestService;
import com.zhongqijia.pay.utils.PayTongTongUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class PayTongTongServiceImpl implements PayTongTongService {
    @Value("${tongtongPay.walletLoginNotifyUrl}")
    private String walletLoginNotifyUrl;
    @Value("${tongtongPay.walletLoginReturnUrl}")
    private String walletLoginReturnUrl;
    @Value("${tongtongPay.domain}")
    private String domain;
    @Value("${tongtongPay.tongtongPayRoot}")
    private String tongtongPayRoot;
    @Value("${tongtongPay.payNotifyUrl}")
    private String payNotifyUrl;
    @Value("${tongtongPay.payReturnUrl}")
    private String payReturnUrl;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired(required = false)
    private UsersMapper userMapper;
    @Autowired(required = false)
    private MyOrderMapper myOrderMapper;
    @Autowired(required = false)
    private CollectionMapper collectionMapper;
    @Override
    public String walletLoginByUsers(Integer userId) {
        Users users = userMapper.selectById(userId);
        return PayTongTongUtils.loginWallet(users,walletLoginNotifyUrl,domain,tongtongPayRoot);
    }

    @Override
    public JSONObject walletIsOpen(Integer userId) {
        Users users = userMapper.selectById(userId);
        JSONObject jsonObjectReturn = new JSONObject();
        Map<String, String> res = PayTongTongUtils.getWalletInfo(users,tongtongPayRoot);
        boolean isOpened = false;
        try{
            if(res!=null && res.get("resp_code").equals("000000")){
                String user_status_list = res.get("user_status_list");
                JSONArray jsa = JSONObject.parseArray(user_status_list);
                if(!jsa.isEmpty()){
                    JSONObject js = (JSONObject)jsa.get(0);
                    String user_status = js.getString("user_status");
                    isOpened = user_status.equals("1");
                }
            }
        }catch (Exception e){

        }
        jsonObjectReturn.put("isOpened",isOpened);
        return jsonObjectReturn;
    }

    @Override
    public String payOrderFirst(Integer userId, Integer orderId,String payerClientIp) {
        Users users = userMapper.selectById(userId);
        MyOrder myOrder = myOrderMapper.selectById(orderId);
        Collection collection = collectionMapper.selectById(myOrder.getCollid());
        return PayTongTongUtils.pay(users,tongtongPayRoot,myOrder,collection,
                payReturnUrl,payNotifyUrl,domain,payerClientIp,redisUtil);
    }
}
