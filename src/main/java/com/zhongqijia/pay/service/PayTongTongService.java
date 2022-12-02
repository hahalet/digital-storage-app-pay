package com.zhongqijia.pay.service;

import com.alibaba.fastjson.JSONObject;

public interface PayTongTongService {

    String walletLoginByUsers(Integer userId);

    JSONObject walletIsOpen(Integer userId);
}
