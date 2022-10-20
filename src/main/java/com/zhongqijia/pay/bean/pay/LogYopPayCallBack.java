package com.zhongqijia.pay.bean.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 易宝支付回调记录表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_yop_pay_call_back")
public class LogYopPayCallBack extends Model<LogYopPayCallBack> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * WECHAT：微信ALIPAY：支付宝UNIONPAY：银联APPLEPAY：苹果支付仅聚合支付返回该参数
     */
    private String channel;

    /**
     * 该笔订单在微信、支付宝或银行侧系统生成的单号
     */
    private String channelOrderId;

    /**
     * 收款商户编号
     */
    private String merchantNo;

    /**
     * 元
     */
    private String orderAmount;

    /**
     * 交易下单传入的商户收款请求号
     */
    private String orderId;

    /**
     * 单位:元商户承担手续费:支付金额=订单金额用户承担手续费:支付金额= 订单金额 手续费金额
     */
    private String payAmount;

    private String payerInfo;

    /**
     * 支付成功时间
     */
    private LocalDateTime paySuccessDate;

    private String payWay;

    private String realPayAmount;

    /**
     * SUCCESS（订单支付成功）
     */
    private String status;

    private String uniqueOrderNo;

    /**
     * 返回结果
     */
    private String response;

    /**
     * 创建时间
     */
    private Integer createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
