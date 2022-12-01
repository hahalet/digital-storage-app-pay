package com.zhongqijia.pay.service;

public interface PayTongTongService {

    String walletLoginByUsers(Integer userId);

    boolean getWalletInfo(Integer userId);
}
