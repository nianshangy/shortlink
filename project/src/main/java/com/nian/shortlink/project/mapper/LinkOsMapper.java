package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkOsStats;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 根据短链接获取指定日期内操作系统监控数据
     */
    @Select("SELECT " +
            "   os, " +
            "   SUM(cnt) as cnt " +
            "FROM " +
            "   t_link_os_stats " +
            "WHERE " +
            "   full_short_url = #{param.fullShortUrl}" +
            "   AND gid = #{param.gid}" +
            "   AND date BETWEEN #{param.startDate} AND #{param.endDate}" +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "    full_short_url, gid, os;")
    List<LinkOsStats> listOsStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内操作系统监控数据
     */
    @Select("SELECT " +
            "    os, " +
            "    SUM(cnt) AS cnt " +
            "FROM " +
            "    t_link_os_stats " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "    AND del_flag = 0 " +
            "GROUP BY " +
            "    gid, os;")
    List<LinkOsStats> listOsStatsByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);
}
