package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.ShortLinkGoto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短链接跳转持久层
 */
@Mapper
public interface ShortLinkGotoMapper extends BaseMapper<ShortLinkGoto> {
}
