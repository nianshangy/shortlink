package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nian.shortlink.project.domain.entity.LinkDeviceStats;
import com.nian.shortlink.project.domain.req.ShortLinkGroupStatsReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 短链接监控设备持久层
 */
@Mapper
public interface LinkDeviceMapper extends BaseMapper<LinkDeviceStats> {

    /**
     * 记录设备访问监控数据
     */
    @Insert("INSERT INTO t_link_device_stats (full_short_url, gid, date, cnt, device, create_time, update_time, del_flag) " +
            "VALUES( #{linkDeviceStats.fullShortUrl}, #{linkDeviceStats.gid}, #{linkDeviceStats.date}, #{linkDeviceStats.cnt}, #{linkDeviceStats.device}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkDeviceStats.cnt};")
    void shortLinkDeviceState(@Param("linkDeviceStats") LinkDeviceStats linkDeviceStats);

    /**
     * 根据短链接获取指定日期内设备监控数据
     */
    @Select("SELECT " +
            "   device, " +
            "   SUM(cnt) as cnt " +
            "FROM " +
            "   t_link_device_stats " +
            "WHERE " +
            "   full_short_url = #{param.fullShortUrl}" +
            "   AND gid = #{param.gid}" +
            "   AND date BETWEEN #{param.startDate} AND #{param.endDate}" +
            "   AND del_flag = 0 " +
            "GROUP BY " +
            "    full_short_url, gid, device;")
    List<LinkDeviceStats> listDeviceStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据分组获取指定日期内访问设备监控数据
     */
    @Select("SELECT " +
            "    device, " +
            "    SUM(cnt) AS cnt " +
            "FROM " +
            "    t_link_device_stats " +
            "WHERE " +
            "    gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "    AND del_flag = 0 " +
            "GROUP BY " +
            "    gid, device;")
    List<LinkDeviceStats> listDeviceStatsByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);
}
