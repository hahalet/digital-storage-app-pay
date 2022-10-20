package com.zhongqijia.pay.bean;

import java.math.BigDecimal;
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
 * 盲盒
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blindbox")
public class Blindbox extends Model<Blindbox> {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盲盒图片
     */
    private String img;

    /**
     * 盲盒名称
     */
    private String name;

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
     * 发行者
     */
    private String publisher;

    /**
     * 详情
     */
    private String details;

    /**
     * 可能获得
     */
    private String probably;

    /**
     * 头像
     */
    private String creatorimg;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
