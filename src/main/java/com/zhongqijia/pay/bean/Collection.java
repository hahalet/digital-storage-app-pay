package com.zhongqijia.pay.bean;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数字藏品
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("collection")
public class Collection extends Model<Collection> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 藏品图
     */
    private String img;

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品缩写
     */
    private String minname;

    /**
     * 限量
     */
    private Integer limits;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 详情展示图
     */
    private String img1;

    /**
     * 详情
     */
    private String details;

    /**
     * 发行者
     */
    private String publisher;

    /**
     * 创作者id
     */
    private Integer creatorId;

    /**
     * 创作者
     */
    private String creator;

    /**
     * 创作者头像
     */
    private String creatorimg;

    /**
     * 创作者地址
     */
    private String creatorAddress;

    /**
     * 0未部署1已部署
     */
    private Integer isdeploy;

    /**
     * 合约地址
     */
    @TableField("contractAddress")
    private String contractAddress;

    /**
     * 已售
     */
    private Integer sold;

    /**
     * 碎片专属可售库存
     */
    private Integer stockc;

    /**
     * 编号设置
     */
    private String nosetup;

    /**
     * 作品介绍
     */
    private String introduce;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
