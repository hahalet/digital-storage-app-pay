package com.zhongqijia.pay.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("users")
public class Users extends Model<Users> {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID从10001自增长
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 头像
     */
    private String headPrtraits;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String passwd;

    /**
     * 操作密码
     */
    private String tradePassWord;

    /**
     * 个性签名
     */
    private String autograph;

    /**
     * 我的资产
     */
    private BigDecimal balance;

    /**
     * 支付宝账号
     */
    private String alipay;

    /**
     * 支付宝姓名
     */
    private String alipayname;

    /**
     * 0.未实名1.待审核2.已通过
     */
    private Integer realnametype;

    /**
     * 账号创建时间
     */
    private LocalDateTime createTime;

    /**
     * 状态：0启用，1禁用
     */
    private String statusId;

    /**
     * 以太坊地址
     */
    private String address;

    /**
     * 以太坊私钥
     */
    @TableField("privateKey")
    private String privateKey;

    /**
     * 白名单0.未开启1.已开启
     */
    private Integer whitelist;

    /**
     * 实名姓名
     */
    private String realname;

    /**
     * 实名证件
     */
    private String realno;

    /**
     * 邀请人数
     */
    private Integer invitationcount;

    /**
     * 修改支付宝次数
     */
    private Integer szcount;

    /**
     * 修改支付宝时间
     */
    private LocalDateTime sztime;

    private Integer invitationId;

    /**
     * 是否更换天河钱包 1:否 2:是
     */
    private Integer tichain;

    /**
     * 是否为创作者 0.否1.是
     */
    private Integer isCreator;

    private String yopMemberNo;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
