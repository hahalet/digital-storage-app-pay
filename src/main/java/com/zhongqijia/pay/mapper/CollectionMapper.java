package com.zhongqijia.pay.mapper;

import com.zhongqijia.pay.bean.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数字藏品 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Repository
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

}
