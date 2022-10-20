package com.zhongqijia.pay.mapper;

import com.zhongqijia.pay.bean.Blindbox;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 盲盒 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Repository
@Mapper
public interface BlindboxMapper extends BaseMapper<Blindbox> {

}
