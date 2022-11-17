package com.zhongqijia.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongqijia.pay.common.enums.SandC2CTransCode;
import com.zhongqijia.pay.common.enums.SandMethodEnum;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.event.AppEventSender;
import com.zhongqijia.pay.service.SandService;
import com.zhongqijia.pay.utils.CeasHttpUtil;
import com.zhongqijia.pay.utils.RedisHelp;
import com.zhongqijia.pay.utils.SandBase;
import com.zhongqijia.pay.utils.TiChainPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AppEventSender appEventSender;

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

    @Override
    public JSONObject getPayInfo(String orderNo) {
        JSONObject jsonObjectReturn = new JSONObject();
        try{
            JSONObject param = new JSONObject();
            param.put("customerOrderNo", SandBase.getCustomerOrderNo()); //商户订单号
            if(orderNo.length()<=30){
                Integer index = (Integer)redisUtil.get(RedisHelp.SAND_PAY_ORDER_KEY+orderNo);
                if(index == null){
                    jsonObjectReturn.put("isPayed",false);
                    return jsonObjectReturn;
                }
                orderNo = orderNo+"_"+index;
            }
            param.put("oriCustomerOrderNo", orderNo);//原交易订单号
            JSONObject jsonObject = invoke(param, SandMethodEnum.CEAS_SERVER_ORDER_QUERY);

            String oriResponseCode = jsonObject.getString("oriResponseCode");
            String oriResponseDesc = jsonObject.getString("oriResponseDesc");
            String orirOderStatus = jsonObject.getString("orirOderStatus");
            String oriCustomerOrderNo = jsonObject.getString("oriCustomerOrderNo");
            if(oriResponseCode!=null && oriResponseCode.equals("00000") &&
                    oriResponseDesc!=null && oriResponseDesc.equals(SandC2CTransCode.成功.getDesc()) &&
                    orirOderStatus!=null && orirOderStatus.equals(SandC2CTransCode.成功.getCode())){
                JSONObject json = new JSONObject();
                json.put("orderNo",oriCustomerOrderNo);
                appEventSender.send(BusConfig.SAND_PAY_CALLBACK_C2C_ROUTING_KEY, json);
            }

            if(oriCustomerOrderNo!=null){
                jsonObjectReturn.put("isPayed",true);
            }else{
                jsonObjectReturn.put("isPayed",false);
            }
        }catch (Exception e){
            log.info("getPayInfo error:{}",e.getMessage());
        }
        return jsonObjectReturn;
    }

    public JSONObject invokeC2B(JSONObject param, SandMethodEnum sandMethodEnum) {
        JSONObject resp = CeasHttpUtil.doPost(param, sandMethodEnum);
        String jsonResp = JSONObject.toJSONString(resp, true);
        //logger.info("响应报文：\n"+jsonResp);
        return resp;
    }

    public static void main(String[] args){
        String jsonString = "{\"responseDesc\":\"订单信息不存在\",\"responseTime\":\"20221117105027\",\"mid\":\"6888801117499\",\"sandSerialNo\":\"CEAS2022111710502746221429\",\"responseStatus\":\"01\",\"version\":\"1.0.0\",\"customerOrderNo\":\"2022111710502795\",\"responseCode\":\"05005\"}";
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        String oriResponseCode = jsonObject.getString("oriResponseCode");
        String oriResponseDesc = jsonObject.getString("oriResponseDesc");
        String orirOderStatus = jsonObject.getString("orirOderStatus");
        String oriCustomerOrderNo = jsonObject.getString("oriCustomerOrderNo");
        JSONObject jsonObjectReturn = new JSONObject();
        if(oriResponseCode!=null && oriResponseCode.equals("00000") &&
                oriResponseDesc!=null && oriResponseDesc.equals(SandC2CTransCode.成功.getDesc()) &&
                orirOderStatus!=null && orirOderStatus.equals(SandC2CTransCode.成功.getCode())){
            JSONObject json = new JSONObject();
            json.put("orderNo",oriCustomerOrderNo);
        }

        if(oriCustomerOrderNo!=null){
            jsonObjectReturn.put("isPayed",true);
        }else{
            jsonObjectReturn.put("isPayed",false);
        }
        log.info("test:{}",jsonObjectReturn);
    }

}
