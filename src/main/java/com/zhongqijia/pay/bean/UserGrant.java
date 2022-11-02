package com.zhongqijia.pay.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 收藏
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_grant")
public class UserGrant extends Model<UserGrant> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer orderId;

    /**
     * 用户id(0则为官方)
     */
    private Integer userid;

    /**
     * 藏品id
     */
    private Integer collid;

    /**
     * 发行编号
     */
    private String truenumber;

    /**
     * 藏品hash值
     */
    private String hashs;

    /**
     * 生成时间
     */
    private LocalDateTime createtime;

    /**
     * 购买价格
     */
    private BigDecimal buyprice;

    /**
     * 购买时间
     */
    private Date buytime;

    /**
     * 付款截止时间
     */
    private Date endtime;

    /**
     * 出售价格
     */
    private BigDecimal sellprice;

    /**
     * 0.待上架1.已上架2.交易中3.已完成4.已消耗
     */
    private Integer type;

    /**
     * 对方用户
     */
    private Integer oppositeuser;

    /**
     * 上架时间
     */
    private LocalDateTime sjtime;

    /**
     * nft币id
     */
    @TableField("tokenId")
    private Integer tokenId;

    /**
     * 1.平台 2.微信 3.支付宝 4.其他
     */
    private Integer paytype;

    /**
     * 天河链铸造编号 因有老数据存在 新赠字段
     */
    private Integer castingNumber;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
