package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkStatsToday;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接今日统计量持久层
 */
@Mapper
public interface LinkStatsTodayMapper extends BaseMapper<LinkStatsToday> {
    /**
     * 记录今日统计监控数据
     */
    @Insert("INSERT INTO t_link_stats_today (full_short_url, gid, date,  today_uv, today_pv, today_uip, create_time, update_time, del_flag) " +
            "VALUES( #{linkTodayStats.fullShortUrl}, #{linkTodayStats.gid}, #{linkTodayStats.date}, #{linkTodayStats.todayUv}, #{linkTodayStats.todayPv}, #{linkTodayStats.todayUip}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE today_uv = today_uv +  #{linkTodayStats.todayUv}, today_pv = today_pv +  #{linkTodayStats.todayPv}, today_uip = today_uip +  #{linkTodayStats.todayUip};")
    void shortLinkTodayState(@Param("linkTodayStats") LinkStatsToday linkStatsTodayDO);
}
