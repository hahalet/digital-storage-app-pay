package com.zhongqijia.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.common.enums.SandMethodEnum;
import com.zhongqijia.pay.service.SandService;
import com.zhongqijia.pay.utils.CeasHttpUtil;
import com.zhongqijia.pay.utils.SandBase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @desc 示例
 * @date 2021/03/12
 * @author: sandpay
 */
@Service
@Slf4j
public class SandServiceImpl implements SandService {

    private static Logger logger = LoggerFactory.getLogger(SandServiceImpl.class);

    @Override
    public JSONObject invoke(JSONObject param, SandMethodEnum sandMethodEnum) {
        JSONObject resp = CeasHttpUtil.doPost(param, sandMethodEnum);
        String jsonResp = JSONObject.toJSONString(resp, true);
        //logger.info("响应报文：\n"+jsonResp);
        return resp;
    }

    @Override
    public JSONObject walletIsOpen(Integer userId){
        JSONObject jsonObjectReturn = new JSONObject();
        try{
            JSONObject param = new JSONObject();
            param.put("customerOrderNo", SandBase.getCustomerOrderNo()); //商户订单号
            param.put("bizUserNo", userId.toString()); //会员编号
            JSONObject jsonObject = invoke(param, SandMethodEnum.CEAS_ELEC_MEMBER_INFO);
            String masterAccount = jsonObject.getString("masterAccount");
            String faceStatus = jsonObject.getString("faceStatus");
            if(masterAccount!=null && masterAccount.length()>0){
                jsonObjectReturn.put("isOpened",true);
                jsonObjectReturn.put("faceStatus",faceStatus);
            }else{
                jsonObjectReturn.put("isOpened",false);
            }
        }catch (Exception e){

        }
        return jsonObjectReturn;
    }
}
