package com.zhongqijia.pay.bean.paysand;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 杉德支付回调记录表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_sand_pay_call_back")
public class C2BLogSandPayCallBack extends Model<C2BLogSandPayCallBack> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String mid;

    private String orderCode;

    private String tradeNo;

    private String clearDate;

    private String totalAmount;

    private String orderStatus;

    private String payTime;

    private String settleAmount;

    private String buyerPayAmount;

    private String discAmount;

    private String txnCompleteTime;

    private String payOrderCode;

    private String accLogonNo;

    private String accNo;

    private String midFee;

    private String extraFee;

    private String specialFee;

    private String plMidFee;

    private String bankserial;

    private String externalProductCode;

    private String cardNo;

    private String creditFlag;

    private String bid;

    private String benefitAmount;

    private String remittanceCode;

    private String extend;

    private String accountAmt;

    private String masterAccount;

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
