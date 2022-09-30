package com.zhongqijia.pay.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nicolasli
 * @version 1.0
 * @className BasePo
 * @date 2020/3/28 下午3:54
 **/

@Data
public class BasePo implements Serializable {
    public final static String DEFAULT_USERNAME = "system";
    @TableId(type = IdType.AUTO)
    private Integer id;

}
