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
 * 藏品记录
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("hide_record")
public class HideRecord extends Model<HideRecord> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userid;

    private String img;

    private String name;

    private BigDecimal price;

    private String no;

    private String ms;

    /**
     * 0.黄的1.绿2.红
     */
    private Integer type;

    private LocalDateTime createtime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
