package com.zhongqijia.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhongqijia.pay.bean.paysand.LogSandPayC2cCallBack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 杉德支付C2C回调记录表 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-11-02
 */
@Repository
@Mapper
public interface LogSandPayC2cCallBackMapper extends BaseMapper<LogSandPayC2cCallBack> {

}
