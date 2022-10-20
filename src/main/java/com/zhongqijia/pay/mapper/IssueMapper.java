package com.zhongqijia.pay.mapper;

import com.zhongqijia.pay.bean.Issue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数字藏品发售 Mapper 接口
 * </p>
 *
 * @author Luolaigang
 * @since 2022-10-20
 */
@Repository
@Mapper
public interface IssueMapper extends BaseMapper<Issue> {

}
