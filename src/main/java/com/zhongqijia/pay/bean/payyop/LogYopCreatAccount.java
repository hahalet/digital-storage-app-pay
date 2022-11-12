package com.zhongqijia.pay.bean.payyop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 易宝开户返回记录表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_yop_creat_account")
public class LogYopCreatAccount extends Model<LogYopCreatAccount> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 易宝唯一订单号
     */
    private String businessNo;

    /**
     * 商户编号，易宝支付分配的商户唯一标识 示例值：1001234567
     */
    private String merchantNo;

    /**
     * 商户用户标识，用户在商户侧的用户id 示例值：User89849
     */
    private String merchantUserNo;

    /**
     * 钱包账户等级，ONE_CATEGORY:一类 TWO_CATEGORY:二类 THREE_CATEGORY:三类
     */
    private String walletCategory;

    /**
     * 钱包账户ID，已开立成功的会员则返回同一钱包账户ID。用户在易宝钱包生成的唯一编号
     */
    private String walletUserNo;

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
