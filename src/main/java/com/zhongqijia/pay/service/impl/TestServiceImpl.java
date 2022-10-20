package com.zhongqijia.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeepay.yop.sdk.exception.YopClientException;
import com.yeepay.yop.sdk.model.YopRequestConfig;
import com.yeepay.yop.sdk.service.common.YopClient;
import com.yeepay.yop.sdk.service.common.YopClientBuilder;
import com.yeepay.yop.sdk.service.common.request.YopRequest;
import com.yeepay.yop.sdk.service.common.response.YopResponse;
import com.yeepay.yop.sdk.service.common.response.YosUploadResponse;
import com.zhongqijia.pay.bean.TestBean;
import com.zhongqijia.pay.common.util.FileUtils;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.mapper.TestMapper;
import com.zhongqijia.pay.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;

@Service
@Slf4j
public class TestServiceImpl extends ServiceImpl<TestMapper, TestBean> implements TestService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public int getCount() {
        redisUtil.set("RedisTest:test", "111");

        return testMapper.getCount();
    }
}
