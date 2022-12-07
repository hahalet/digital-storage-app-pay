package com.zhongqijia.pay.bean.paytongtong;

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
 * 统统支付回调记录表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("log_tongtong_pay_call_back")
public class LogTongtongPayCallBack extends Model<LogTongtongPayCallBack> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 请求操作相应码。操作成功返回000000，返回其它响应码均为失败
     */
    private String resp_code;

    /**
     * 当resp_code不为000000时，该参数不能为空，返回操作失败原因
     */
    private String resp_msg;

    /**
     * 暂只支持CERT；必须大写
     */
    private String sign_type;

    /**
     * 签名, 参照 签名机制
     */
    private String sign;

    /**
     * 唯一确定一个商户的代码，商户在商盟开户时，由商盟告知商户。
     */
    private String mer_no;

    /**
     * 订单号码
     */
    private String order_no;

    private String offset_amount;

    private String paid_amount;

    private String cash_amount;

    private String trade_no;

    /**
     * 订单创建日期yyyymmddhhmmss 例：20140313142521
     */
    private String order_time;

    /**
     * 0：失败 1：成功 2：处理中
     */
    private String status;

    /**
     * 如：20130520125022
     */
    private String success_time;

    /**
     * 订单交易金额，保留两位小数50.01
     */
    private String success_amount;

    private String remark;

    /**
     * 失败的时候可能会返回
     */
    private String ttfReturnCode;

    /**
     * 失败的时候可能会返回
     */
    private String ttfReturnMsg;

    /**
     * T0001：担保交易 T0002：即时交易
     */
    private String trade_code;

    /**
     * 参考5.12.13目录附录五中的支付产品码
     */
    private String pay_product_code;

    /**
     * 00：借记卡 01：贷记卡 02：预付卡 03：余额
     */
    private String payment_type;

    private String offset_detail;

    /**
     * 有则返回
     */
    private String channel_serial_no;

    /**
     * 信用卡分期支付必填，暂只有：3、6、9、12、18、24、36、60
     */
    private String period_num;

    /**
     * 查证和异步通知原样返回
     */
    private String attach;

    /**
     * 创建时间
     */
    private Integer create_time;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
