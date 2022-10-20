package com.zhongqijia.pay.mapper;

import com.zhongqijia.pay.bean.UserGrant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 收藏 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Repository
@Mapper
public interface UserGrantMapper extends BaseMapper<UserGrant> {

}
