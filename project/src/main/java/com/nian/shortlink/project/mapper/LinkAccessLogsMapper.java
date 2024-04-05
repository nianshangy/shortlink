package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkAccessLogs;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

/**
 * 短链接监控日志持久层
 */
@Mapper
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogs> {

    /**
     * 根据短链接获取指定日期内高频ip监控数据
     */
    @Select("SELECT " +
            "   ip, " +
            "   COUNT(ip) as count " +
            "FROM " +
            "   t_link_access_logs " +
            "WHERE " +
            "   full_short_url = #{param.fullShortUrl} " +
            "   AND gid = #{param.gid} " +
            "   AND create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "   full_short_url, gid, ip " +
            "ORDER BY " +
            "   count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内访客类型数据
     */
    @Select("SELECT " +
            "   user, " +
            "   COUNT(user) as count " +
            "FROM " +
            "   t_link_access_logs " +
            "WHERE " +
            "   full_short_url = #{param.fullShortUrl} " +
            "   AND gid = #{param.gid} " +
            "   AND create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "   full_short_url, gid, user; ")
    List<HashMap<String, Object>> listUvByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

}
