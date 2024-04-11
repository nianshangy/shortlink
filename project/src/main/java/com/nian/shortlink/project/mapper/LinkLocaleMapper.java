package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkLocaleStats;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 短链接监控地区统计访问持久层
 */
@Mapper
public interface LinkLocaleMapper extends BaseMapper<LinkLocaleStats> {

    /**
     * 记录地区访问监控数据
     */
    @Insert("INSERT INTO t_link_locale_stats (full_short_url, gid, date, cnt, country, province, city, adcode, create_time, update_time, del_flag) " +
            "VALUES( #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.gid}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.country}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkLocaleStats.cnt};")
    void shortLinkLocaleState(@Param("linkLocaleStats") LinkLocaleStats linkLocaleStatsDO);

    /**
     * 根据短链接获取指定日期内地区监控数据
     */
    @Select("SELECT " +
            "   province, " +
            "   SUM(cnt) as cnt " +
            "FROM " +
            "   t_link_locale_stats " +
            "where " +
            "   full_short_url = #{param.fullShortUrl}" +
            "   AND gid = #{param.gid}" +
            "   AND date BETWEEN #{param.startDate} AND #{param.endDate}" +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "    full_short_url, gid, province;"
    )
    List<LinkLocaleStats> listLocaleStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内分组地区监控数据
     */
    @Select("SELECT " +
            "   province, " +
            "   SUM(cnt) as cnt " +
            "FROM " +
            "   t_link_locale_stats " +
            "where " +
            "   gid = #{param.gid}" +
            "   AND date BETWEEN #{param.startDate} AND #{param.endDate}" +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "    gid, province;"
    )
    List<LinkLocaleStats> listLocaleStatsByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);
}
