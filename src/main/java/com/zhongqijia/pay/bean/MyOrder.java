package com.zhongqijia.pay.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 我的订单
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("my_order")
public class MyOrder extends Model<MyOrder> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String orderno;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 藏品id
     */
    private Integer collid;

    /**
     * 1.藏品2.盲盒
     */
    private Integer istype;

    /**
     * 参与形式(1.出售2.抽签)
     */
    private Integer ginsengtype;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 订单金额
     */
    private BigDecimal price;

    /**
     * 0.待付款1.已完成2.已取消-1.支付未回调
     */
    private Integer ordertype;

    /**
     * -1.未支付1.未发放2.已发放
     */
    private Integer grants;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 付款截止时间
     */
    private LocalDateTime endtime;

    /**
     * 0.暂无1.平台 2.微信 3.支付宝 4.其他
     */
    private Integer paytype;

    /**
     * 参与id
     */
    private Integer cyid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
