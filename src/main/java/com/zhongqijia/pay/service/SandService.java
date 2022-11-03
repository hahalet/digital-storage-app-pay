package com.zhongqijia.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.common.enums.SandMethodEnum;
import com.zhongqijia.pay.utils.SandBase;

/**
 * @desc 示例
 * @date 2021/03/12
 * @author: sandpay
 * */
public interface SandService {

    /**
     * 接口调用
     */
    JSONObject invoke(JSONObject param, SandMethodEnum sandMethodEnum);

    boolean walletIsOpen(Integer userId);
}
