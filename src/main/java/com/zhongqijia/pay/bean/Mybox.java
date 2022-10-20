package com.zhongqijia.pay.bean;

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
 * 我的盲盒
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mybox")
public class Mybox extends Model<Mybox> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盲盒编号
     */
    private String no;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 盲盒id
     */
    private Integer boxid;

    /**
     * 碎片id
     */
    private Integer spid;

    /**
     * 0未开启1.已开启
     */
    private Integer type;

    private Integer orderid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
