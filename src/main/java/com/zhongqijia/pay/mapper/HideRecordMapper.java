package com.zhongqijia.pay.mapper;

import com.zhongqijia.pay.bean.HideRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 藏品记录 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Repository
@Mapper
public interface HideRecordMapper extends BaseMapper<HideRecord> {

}
