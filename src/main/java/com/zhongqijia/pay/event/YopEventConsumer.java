package com.zhongqijia.pay.event;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhongqijia.pay.bean.*;
import com.zhongqijia.pay.bean.payyop.LogYopCreatAccount;
import com.zhongqijia.pay.bean.payyop.LogYopPayCallBack;
import com.zhongqijia.pay.common.util.RedisUtil;
import com.zhongqijia.pay.config.BusConfig;
import com.zhongqijia.pay.mapper.*;
import com.zhongqijia.pay.utils.RedisHelp;
import com.zhongqijia.pay.utils.TiChainPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class YopEventConsumer {
    @Autowired(required = false)
    UsersMapper userMapper;
    @Autowired(required = false)
    MyOrderMapper myOrderMapper;
    @Autowired(required = false)
    CollectionMapper collectionMapper;
    @Autowired(required = false)
    HideRecordMapper hideRecordMapper;
    @Autowired(required = false)
    BlindboxMapper blindboxMapper;
    @Autowired(required = false)
    IssueMapper issueMapper;
    @Autowired(required = false)
    SignupMapper signupMapper;
    @Autowired(required = false)
    MyboxMapper myboxMapper;
    @Autowired(required = false)
    UserGrantMapper userGrantMapper;
    @Autowired
    private RedisUtil redisUtil;

    @RabbitListener(queues = BusConfig.YOP_WALLET_CALLBACK_QUEUE)
    public void walletCallback(JSONObject message) {
        log.info("YopEventConsumer message = {}", message);
        try {
            LogYopCreatAccount logYopCreatAccount = JSONObject.parseObject(message.toJSONString(), LogYopCreatAccount.class);
            String userId = logYopCreatAccount.getMerchantUserNo();
            Users users = userMapper.selectById(Integer.parseInt(userId));
            users.setYopMemberNo(logYopCreatAccount.getWalletUserNo());
            userMapper.updateById(users);
        } catch (Exception e) {
            log.info("walletCallback error:{}", e.getMessage());
        }
    }

    @RabbitListener(queues = BusConfig.YOP_PAY_CALLBACK_QUEUE)
    public void payCallback(JSONObject message) {
        //锁一分钟,检查订单状态不允许回调修改订单
        redisUtil.getKey(RedisHelp.CHECK_ORDER_STATUS_LOCK_KEY,RedisHelp.CHECK_ORDER_STATUS_LOCK_VALUE);
        try {
            LogYopPayCallBack logYopPayCallBack = JSONObject.parseObject(message.toJSONString(), LogYopPayCallBack.class);
            String orderNo = logYopPayCallBack.getOrderId();
            if (orderNo == null || orderNo.length() == 0) {
                redisUtil.delete(RedisHelp.CHECK_ORDER_STATUS_LOCK_KEY);
                return;
            }
            boolean firstOrder = orderNo.startsWith(TiChainPayUtil.FIRST_ORDER);
            if (firstOrder && logYopPayCallBack.getStatus().equals(TiChainPayUtil.SUCCESS)) {
                orderNo = orderNo.substring(TiChainPayUtil.FIRST_ORDER.length());
                QueryWrapper<MyOrder> queryWrapper=new QueryWrapper();
                queryWrapper.eq("orderno",orderNo);
                queryWrapper.ne("ordertype",2);

                List<MyOrder> myOrders = myOrderMapper.selectList(queryWrapper);
                if(myOrders==null || myOrders.size()==0){
                    redisUtil.delete(RedisHelp.CHECK_ORDER_STATUS_LOCK_KEY);
                    return;
                }
                if (myOrders.size() > 0) {
                    MyOrder myOrder = myOrders.get(0);
                    if(myOrder.getGrants()==2){//已发放不重复发放
                        redisUtil.delete(RedisHelp.CHECK_ORDER_STATUS_LOCK_KEY);
                        return;
                    }
                    myOrder.setGrants(1);
                    myOrder.setPaytype(5);
                    myOrder.setOrdertype(1);
                    myOrderMapper.updateById(myOrder);
                    RedisHelp.refreshMyOrder(myOrder,redisUtil);
                    if (myOrder.getIstype() == 1) {//藏品+出售
                        Collection collection = collectionMapper.selectById(myOrder.getCollid());
                        HideRecord hideRecord = new HideRecord();
                        hideRecord.setUserid(myOrder.getUserid());
                        hideRecord.setImg(collection.getImg());
                        hideRecord.setName(collection.getName());
                        hideRecord.setPrice(collection.getPrice());
                        hideRecord.setNo(myOrder.getOrderno());
                        hideRecord.setCreatetime(LocalDateTime.now());
                        hideRecord.setMs("购买成功");
                        hideRecord.setType(2);//0.黄的1.绿2.红
                        hideRecordMapper.insert(hideRecord);
                    } else {
                        Blindbox collection = blindboxMapper.selectById(myOrder.getCollid());
                        HideRecord hideRecord = new HideRecord();
                        hideRecord.setUserid(myOrder.getUserid());
                        hideRecord.setImg(collection.getImg());
                        hideRecord.setName(collection.getName());
                        hideRecord.setPrice(collection.getPrice());
                        hideRecord.setNo(myOrder.getOrderno());
                        hideRecord.setCreatetime(LocalDateTime.now());
                        hideRecord.setMs("购买成功");
                        hideRecord.setType(2);//0.黄的1.绿2.红
                        hideRecordMapper.insert(hideRecord);
                    }
                    if (myOrder.getGinsengtype() == 2) {
                        Issue issue = issueMapper.selectById(myOrder.getCyid());
                        QueryWrapper<Signup> queryWrapperSignup = new QueryWrapper();
                        queryWrapperSignup.eq("userid",myOrder.getCyid());
                        queryWrapperSignup.eq("isid",myOrder.getUserid());
                        queryWrapperSignup.eq("begintime",issue.getReleasetime());
                        if (signupMapper.selectList(queryWrapperSignup).size() == 0) {
                            Signup signup = new Signup();
                            signup.setUserid(myOrder.getUserid());
                            signup.setIsid(myOrder.getCyid());
                            signup.setBegintime(issue.getReleasetime());
                            signup.setCreatetime(LocalDateTime.now());
                            signup.setMyorderid(myOrder.getId());
                            signupMapper.insert(signup);
                        }
                    } else {
                        QueryWrapper<Mybox> queryWrapperMybox = new QueryWrapper();
                        queryWrapperMybox.eq("userid",myOrder.getUserid());
                        queryWrapperMybox.eq("orderid",myOrder.getId());
                        if (myboxMapper.selectList(queryWrapperMybox).size() == 0) {
                            Mybox mybox = new Mybox();
                            mybox.setUserid(myOrder.getUserid());
                            mybox.setNo(myOrder.getOrderno());
                            mybox.setBoxid(myOrder.getCollid());
                            mybox.setOrderid(myOrder.getId());
                            myboxMapper.insert(mybox);
                        }
                    }
                }
            } else {//二级市场
                orderNo = orderNo.substring(TiChainPayUtil.SECOND_ORDER.length());
                String idTime[] = orderNo.split("_");
                orderNo = idTime[0];
                UserGrant userGrant = userGrantMapper.selectById(Integer.parseInt(orderNo));
                if (userGrant != null) {
                    if (userGrant.getType() == 2) {
                        userGrant.setType(5);
                        userGrant.setPaytype(5);
                        userGrant.setType(3);
                        userGrantMapper.updateById(userGrant);
                    }
                }
            }
        } catch (Exception e) {
            log.info("支付回调执行失败error:{}", e.getMessage());
        }
        redisUtil.delete(RedisHelp.CHECK_ORDER_STATUS_LOCK_KEY);
    }
}
