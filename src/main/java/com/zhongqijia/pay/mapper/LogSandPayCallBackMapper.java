package com.zhongqijia.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhongqijia.pay.bean.paysand.LogSandPayCallBack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 杉德支付回调记录表 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-11-01
 */
@Repository
@Mapper
public interface LogSandPayCallBackMapper extends BaseMapper<LogSandPayCallBack> {

}
