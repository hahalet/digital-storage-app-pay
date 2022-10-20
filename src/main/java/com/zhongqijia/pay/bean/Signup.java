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
 * 报名表
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("signup")
public class Signup extends Model<Signup> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 参与发售id
     */
    private Integer isid;

    /**
     * 发售开始时间
     */
    private LocalDateTime begintime;

    /**
     * 参与时间
     */
    private LocalDateTime createtime;

    /**
     * 0.未公布1.未中签2.已中签
     */
    private Integer type;

    /**
     * 对应id
     */
    private Integer myorderid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
