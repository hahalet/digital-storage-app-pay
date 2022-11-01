package com.zhongqijia.pay.bean.paysand;

import lombok.Data;

@Data
public class SandPayCallBack
{
    private SandPayHead head;

    private LogSandPayCallBack body;
}