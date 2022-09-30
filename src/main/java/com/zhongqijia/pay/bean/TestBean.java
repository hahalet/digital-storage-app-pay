package com.zhongqijia.pay.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author nicolasli
 * @since 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TestBean extends BasePo {

    private static final long serialVersionUID = 1L;

    /**
     * 电话
     */
    private String phone;

    /**
     * 身份证
     */
    private String identityCard;

    /**
     * 设备号
     */
    private String macCode;
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 修改时间
     */
    private Integer updateTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作原因
     */
    private String reason;


}
