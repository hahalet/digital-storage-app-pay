package com.zhongqijia.pay.common.enums;

import java.util.*;


public enum SandC2CTransCode {
    /***C2C状态***/
    成功("00", "成功"),
    处理中("01", "处理中"),
    失败("02", "失败"),
    已生成("03", "已生成"),
    已关闭("04", "已关闭"),
    收款中("05", "收款中"),
    已收款("06", "已收款"),
    退回中("07", "退回中"),
    已退回("08", "已退回"),
    ;


    public static List<String> SUCCEED_CODE = new ArrayList<>();
    static{
        SUCCEED_CODE.add(成功.code);
        SUCCEED_CODE.add(收款中.code);
        SUCCEED_CODE.add(已收款.code);
    }
    /***交易-end***/

    SandC2CTransCode(String typeCode, String codeDesc) {
        this.code = typeCode;
        this.desc = codeDesc;
    }

    private String code; // code

    private String desc; // 描述


    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 通过方法code获取枚举信息
     *
     * @param code code
     * @return
     * @date: 2021-03-10 10:11
     */
    public static SandC2CTransCode get(String code) {
        return Arrays.stream(SandC2CTransCode.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }
}
