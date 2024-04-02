package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkNetworkStats;
import com.nian.shortlink.project.domain.entity.LinkOsStats;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接监控网络持久层
 */
@Mapper
public interface LinkNetworkMapper extends BaseMapper<LinkOsStats> {

    /**
     * 记录网络访问监控数据
     */
    @Insert("INSERT INTO t_link_network_stats (full_short_url, gid, date, cnt, network, create_time, update_time, del_flag) " +
            "VALUES( #{linkNetworkStats.fullShortUrl}, #{linkNetworkStats.gid}, #{linkNetworkStats.date}, #{linkNetworkStats.cnt}, #{linkNetworkStats.network}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkNetworkStats.cnt};")
    void shortLinkNetworkState(@Param("linkNetworkStats") LinkNetworkStats linkNetworkStats);

}

