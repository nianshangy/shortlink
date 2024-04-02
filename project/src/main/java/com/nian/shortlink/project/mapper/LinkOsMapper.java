package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkOsStats;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接监控操作系统持久层
 */
@Mapper
public interface LinkOsMapper extends BaseMapper<LinkOsStats> {

    /**
     * 记录操作系统访问监控数据
     */
    @Insert("INSERT INTO t_link_os_stats (full_short_url, gid, date, cnt, os, create_time, update_time, del_flag) " +
            "VALUES( #{linkOseStats.fullShortUrl}, #{linkOseStats.gid}, #{linkOseStats.date}, #{linkOseStats.cnt}, #{linkOseStats.os}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkOseStats.cnt};")
    void shortLinkOsState(@Param("linkOseStats") LinkOsStats linkOsStats);

}
