package com.zhongqijia.pay.controller;

import com.zhongqijia.pay.bean.vo.Response;
import com.zhongqijia.pay.service.TestService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay/test")
@Api(tags = "测试")
@Slf4j
public class ControllerTest {
    @Autowired
    private TestService testService;

    @PostMapping("测试")
    @ApiOperation("测试")
    public Response<Integer> findFacebookStatus(){
        Response response = new Response<>();
        response.setBo(testService.getCount());
        log.info("测试:{}","is ok!");
        return response;
    }


}
