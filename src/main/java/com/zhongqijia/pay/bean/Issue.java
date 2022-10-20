package com.zhongqijia.pay.bean;

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
 * 数字藏品发售
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("issue")
public class Issue extends Model<Issue> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 1.藏品2.盲盒
     */
    private Integer istype;

    /**
     * 绑定id
     */
    private Integer collid;

    /**
     * 参与形式(1.出售2.抽签)
     */
    private Integer ginsengtype;

    /**
     * 开售时间
     */
    private LocalDateTime releasetime;

    /**
     * 结束时间
     */
    private LocalDateTime endtime;

    /**
     * 预售数量
     */
    private Integer presale;

    /**
     * 已售数量
     */
    private Integer sold;

    /**
     * 每人限购数量
     */
    private Integer limitcount;

    /**
     * 抽奖抽取中奖人数
     */
    private Integer ginscount;

    /**
     * 0.未上架1.已上架
     */
    private Integer type;

    /**
     * (抽签专用0.未抽签1.已抽签)
     */
    private Integer checkout;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
