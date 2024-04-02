package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkAccessLogs;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短链接监控日志持久层
 */
@Mapper
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogs> {
}
