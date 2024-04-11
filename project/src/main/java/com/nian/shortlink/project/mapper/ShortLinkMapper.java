package com.nian.shortlink.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nian.shortlink.project.domain.entity.ShortLink;
import com.nian.shortlink.project.domain.req.link.ShortLinkPageDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 短链接持久层
 */
public interface ShortLinkMapper extends BaseMapper<ShortLink> {

    /**
     * 增加总访问量
     */
    @Update("update " +
            "   t_link " +
            "set " +
            "   total_pv = total_pv + #{totalPv}," +
            "   total_uv = total_uv + #{totalUv}," +
            "   total_uip = total_uip + #{totalUip} " +
            "where" +
            "   full_short_url = #{fullShortUrl}" +
            "   AND gid = #{gid}")
    void incrementStats(
            @Param("fullShortUrl") String fullShortUrl,
            @Param("gid") String gid,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );

    /**
     * 分页查询短链接
     */
    IPage<ShortLink> pageShortLink(ShortLinkPageDTO shortLinkPageDTO);
}
