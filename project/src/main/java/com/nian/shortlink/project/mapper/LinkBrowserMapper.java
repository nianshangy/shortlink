package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkBrowserStats;
import com.nian.shortlink.project.domain.entity.LinkOsStats;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 短链接监控浏览器持久层
 */
@Mapper
public interface LinkBrowserMapper extends BaseMapper<LinkOsStats> {

    /**
     * 记录浏览器访问监控数据
     */
    @Insert("INSERT INTO t_link_browser_stats (full_short_url, gid, date, cnt, browser, create_time, update_time, del_flag) " +
            "VALUES( #{linkBrowserStats.fullShortUrl}, #{linkBrowserStats.gid}, #{linkBrowserStats.date}, #{linkBrowserStats.cnt}, #{linkBrowserStats.browser}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkBrowserStats.cnt};")
    void shortLinkBrowserStats(@Param("linkBrowserStats") LinkBrowserStats linkBrowserStats);

    /**
     * 根据短链接获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "   browser, " +
            "   SUM(cnt) as cnt " +
            "FROM " +
            "   t_link_browser_stats " +
            "WHERE " +
            "   full_short_url = #{param.fullShortUrl}" +
            "   AND gid = #{param.gid}" +
            "   AND date BETWEEN #{param.startDate} AND #{param.endDate}" +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "    full_short_url, gid, browser;")
    List<LinkBrowserStats> listBrowserStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

}
