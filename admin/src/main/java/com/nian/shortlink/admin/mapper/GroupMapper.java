package com.nian.shortlink.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.admin.domain.entity.Group;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短链接分组持久层
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
