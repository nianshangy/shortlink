package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkAccessLogs;
import com.nian.shortlink.project.domain.req.linkStats.JudgeGroupUvTypeReqDTO;
import com.nian.shortlink.project.domain.req.linkStats.JudgeUvTypeReqDTO;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.project.domain.req.linkStats.ShortLinkStatsReqDTO;
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
     * 根据分组获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "    ip, " +
            "    COUNT(ip) AS count " +
            "FROM " +
            "    t_link_access_logs " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    gid, ip " +
            "ORDER BY " +
            "    count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);



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

    /**
     * 根据短链接获取指定日期内分组访客类型数据
     */
    @Select("SELECT " +
            "   user, " +
            "   COUNT(user) as count " +
            "FROM " +
            "   t_link_access_logs " +
            "WHERE " +
            "   gid = #{param.gid} " +
            "   AND create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "   gid, user; ")
    List<HashMap<String, Object>> listUvByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);



    /**
     * 获取用户信息是否新老访客
     */
    //TODO 新老访客判断
    @Select("<script> " +
            "SELECT " +
            "    user, " +
            "    CASE " +
            "        WHEN COUNT(*) = 1 THEN '新访客' " +
            "        WHEN create_time = MIN(create_time) THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    t_link_access_logs " +
            "WHERE " +
            "    full_short_url = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "    AND user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    user,create_time;" +
            "    </script>"
    )
    List<HashMap<String, Object>> listUvTypesByShortLink(@Param("param") JudgeUvTypeReqDTO requestParam,@Param("userAccessLogsList") List<String> userAccessLogsList);

    /**
     * 获取用户信息是否新老访客
     */
    //TODO 新老访客判断
    @Select("<script> " +
            "SELECT " +
            "    user, " +
            "    CASE " +
            "        WHEN COUNT(*) = 1 THEN '新访客' " +
            "        WHEN create_time = MIN(create_time) THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    t_link_access_logs " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "    AND user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    user,create_time;" +
            "    </script>"
    )
    List<HashMap<String, Object>> listGroupUvTypesByShortLink(@Param("param") JudgeGroupUvTypeReqDTO requestParam, @Param("userAccessLogsList") List<String> userAccessLogsList);
}
