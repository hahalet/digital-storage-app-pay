package com.zhongqijia.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhongqijia.pay.bean.TestBean;
import com.zhongqijia.pay.bean.Users;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.mapper.TestMapper;
import com.zhongqijia.pay.mapper.UsersMapper;
import com.zhongqijia.pay.service.PayTongTongService;
import com.zhongqijia.pay.service.TestService;
import com.zhongqijia.pay.utils.PayTongTongUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    @Autowired(required = false)
    UsersMapper userMapper;
    @Override
    public String walletLoginByUsers(Integer userId) {
        Users users = userMapper.selectById(userId);
        return PayTongTongUtils.loginWallet(users,walletLoginNotifyUrl,domain,tongtongPayRoot);
    }

    @Override
    public JSONObject walletIsOpen(Integer userId) {
        Users users = userMapper.selectById(userId);
        JSONObject jsonObjectReturn = new JSONObject();
        jsonObjectReturn.put("isOpened",PayTongTongUtils.getWalletInfo(users,tongtongPayRoot));
        return jsonObjectReturn;
    }
}
