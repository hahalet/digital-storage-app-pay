package com.zhongqijia.pay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhongqijia.pay.bean.paytongtong.LogTongtongPayCallBack;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 统统支付回调记录表 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-12-05
 */
@Repository
@Mapper
public interface LogTongtongPayCallBackMapper extends BaseMapper<LogTongtongPayCallBack> {

}
