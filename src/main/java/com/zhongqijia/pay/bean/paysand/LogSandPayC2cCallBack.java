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
 * 杉德支付C2C回调记录表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_sand_pay_c2c_call_back")
public class LogSandPayC2cCallBack extends Model<LogSandPayC2cCallBack> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Double amount;

    private Double feeAmt;

    private Double userFeeAmt;

    private String mid;

    private String orderNo;

    private String orderStatus;

    private String respCode;

    private String respMsg;

    private String respTime;

    private String sandSerialNo;

    private String transType;

    private String payerAccNo;

    private String payerMemId;

    private String payerAccName;

    private String payeeMemId;

    private String payeeAccNo;

    private String payeeAccName;

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
