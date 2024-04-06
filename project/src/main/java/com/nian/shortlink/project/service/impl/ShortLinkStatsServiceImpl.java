package com.nian.shortlink.project.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nian.shortlink.project.domain.entity.*;
import com.nian.shortlink.project.domain.req.JudgeUvTypeReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkAccessRecordReqDTO;
import com.nian.shortlink.project.domain.req.ShortLinkStatsReqDTO;
import com.nian.shortlink.project.domain.resp.*;
import com.nian.shortlink.project.mapper.*;
import com.nian.shortlink.project.service.IShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 短链接监控统计接口实现层
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShortLinkStatsServiceImpl implements IShortLinkStatsService {

    private final LinkAccessMapper linkAccessMapper;
    private final LinkLocaleMapper linkLocaleMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkBrowserMapper linkBrowserMapper;
    private final LinkOsMapper linkOsMapper;
    private final LinkDeviceMapper linkDeviceMapper;
    private final LinkNetworkMapper linkNetworkMapper;

    @Override
    public ShortLinkStatsRespVO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        //查询指定日期内的全部访问数
        LinkAccessStats linkSumAccessStats = linkAccessMapper.SumStatsByShortLink(requestParam);
        //1.查询每日的访问数
        List<LinkAccessStats> linkAccessStats = linkAccessMapper.listStatsByShortLink(requestParam);
        //判断数据库中是否有链接基础访问短的数据
        if (ObjectUtil.isEmpty(linkAccessStats)) {
            return null;
        }
        List<ShortLinkStatsAccessDailyRespVO> daily = new ArrayList<>();
        //将开始时间与结束时间之前的时间保存为一个List集合
        List<String> rangeDates = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .toList();
        //遍历Date表将相同日期的pv，uv，uip存进daily中
        rangeDates.forEach(each -> linkAccessStats.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    ShortLinkStatsAccessDailyRespVO shortLinkStatsAccessDailyRespVO = ShortLinkStatsAccessDailyRespVO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    daily.add(shortLinkStatsAccessDailyRespVO);
                }, () -> {
                    ShortLinkStatsAccessDailyRespVO shortLinkStatsAccessDailyRespVO = ShortLinkStatsAccessDailyRespVO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    daily.add(shortLinkStatsAccessDailyRespVO);
                })
        );

        //2.查询监控地区数
        List<ShortLinkStatsLocaleCNRespVO> localeCnStats = new ArrayList<>();
        List<LinkLocaleStats> linkLocaleStats = linkLocaleMapper.listLocaleStatsByShortLink(requestParam);
        int localeSum = linkLocaleStats.stream().mapToInt(LinkLocaleStats::getCnt).sum();

        linkLocaleStats.forEach(each -> {
            double ratio = (double) each.getCnt() / localeSum;
            double actualRatio = (double) Math.round(ratio * 100) / 100; //将6位小数转成 类似 25.10% 形式
            ShortLinkStatsLocaleCNRespVO linkStatsLocaleCNRespVO = ShortLinkStatsLocaleCNRespVO.builder()
                    .ratio(actualRatio)
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .build();
            localeCnStats.add(linkStatsLocaleCNRespVO);
        });

        //3.查询小时访问详情
        List<Integer> hourStats = new ArrayList<>();
        List<LinkAccessStats> linkHourAccessStats = linkAccessMapper.listHourStatsByShortLink(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger(i);
            linkHourAccessStats.stream()
                    .filter(each -> ObjectUtil.equals(each.getHour(), hour.get()))
                    .findFirst()
                    .ifPresentOrElse(item -> hourStats.add(item.getPv()), () -> hourStats.add(0));
        }

        //4.查询高频访问IP详情
        List<ShortLinkStatsTopIpRespVO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> linkAccessLogs = linkAccessLogsMapper.listTopIpByShortLink(requestParam);
        linkAccessLogs.forEach(each -> {
            ShortLinkStatsTopIpRespVO shortLinkStatsTopIpRespVO = ShortLinkStatsTopIpRespVO.builder()
                    .ip(each.get("ip").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .build();
            topIpStats.add(shortLinkStatsTopIpRespVO);
        });

        //5.查询一周访问详情
        List<Integer> weekdayStats = new ArrayList<>();
        List<LinkAccessStats> listWeekdayStatsByShortLink = linkAccessMapper.listWeekdayStatsByShortLink(requestParam);
        for (int i = 1; i < 8; i++) {
            AtomicInteger weekday = new AtomicInteger(i);
            listWeekdayStatsByShortLink.stream()
                    .filter(each -> ObjectUtil.equals(each.getWeekday(), weekday.get()))
                    .findFirst()
                    .ifPresentOrElse(item -> weekdayStats.add(item.getPv()),
                            () -> weekdayStats.add(0));
        }

        //6.查询浏览器访问详情
        List<ShortLinkStatsBrowserRespVO> browserStats = new ArrayList<>();
        List<LinkBrowserStats> linkBrowserStats = linkBrowserMapper.listBrowserStatsByShortLink(requestParam);
        int browserSum = linkBrowserStats.stream().mapToInt(LinkBrowserStats::getCnt).sum();

        linkBrowserStats.forEach(each -> {
            double ratio = (double) each.getCnt() / browserSum;
            double actual = (double) Math.round(ratio * 100) / 100;
            ShortLinkStatsBrowserRespVO shortLinkStatsBrowserRespVO = ShortLinkStatsBrowserRespVO.builder()
                    .browser(each.getBrowser())
                    .cnt(each.getCnt())
                    .ratio(actual)
                    .build();
            browserStats.add(shortLinkStatsBrowserRespVO);
        });

        //7.查询操作系统访问详情
        List<ShortLinkStatsOsRespVO> osStats = new ArrayList<>();
        List<LinkOsStats> linkOsStats = linkOsMapper.listOsStatsByShortLink(requestParam);
        int osSum = linkOsStats.stream().mapToInt(LinkOsStats::getCnt).sum();

        linkOsStats.forEach(each -> {
            double ratio = (double) each.getCnt() / osSum;
            double actual = (double) Math.round(ratio * 100) / 100;
            ShortLinkStatsOsRespVO shortLinkStatsOsRespVO = ShortLinkStatsOsRespVO.builder()
                    .os(each.getOs())
                    .cnt(each.getCnt())
                    .ratio(actual)
                    .build();
            osStats.add(shortLinkStatsOsRespVO);
        });

        //8.查询访客访问类型详情
        List<ShortLinkStatsUvRespVO> uvTypeStats = new ArrayList<>();
        List<HashMap<String, Object>> listUvByShortLink = linkAccessLogsMapper.listUvByShortLink(requestParam);
        AtomicInteger newUserCnt = new AtomicInteger();
        AtomicInteger oldUserCnt = new AtomicInteger();
        listUvByShortLink.forEach(each -> {
            int userCount = Integer.parseInt(each.get("count").toString());
            if(userCount > 1){
                oldUserCnt.getAndIncrement();
            }else {
                newUserCnt.getAndIncrement();
            }
        });
        int allUserCnt = newUserCnt.get() + oldUserCnt.get();
        double newUserRatio = (double) newUserCnt.get() / allUserCnt;
        double actualNewUserRatio = (double) Math.round(newUserRatio * 100) / 100;
        double oldUserRatio = (double) oldUserCnt.get() / allUserCnt;
        double actualOleUserRatio = (double) Math.round(oldUserRatio * 100) / 100;

        ShortLinkStatsUvRespVO newUserShortLinkStatsUvRespVO = ShortLinkStatsUvRespVO.builder()
                .uvType("newUser")
                .cnt(newUserCnt.get())
                .ratio(actualNewUserRatio)
                .build();
        uvTypeStats.add(newUserShortLinkStatsUvRespVO);
        ShortLinkStatsUvRespVO oldUserShortLinkStatsUvRespVO = ShortLinkStatsUvRespVO.builder()
                .uvType("oldUser")
                .cnt(oldUserCnt.get())
                .ratio(actualOleUserRatio)
                .build();
        uvTypeStats.add(oldUserShortLinkStatsUvRespVO);

        //9.查询设备访问详情
        List<ShortLinkStatsDeviceRespVO> deviceStats = new ArrayList<>();
        List<LinkDeviceStats> linkDeviceStats = linkDeviceMapper.listDeviceStatsByShortLink(requestParam);
        int deviceSum = linkDeviceStats.stream().mapToInt(LinkDeviceStats::getCnt).sum();

        linkDeviceStats.forEach(each -> {
            double ratio = (double) each.getCnt() / deviceSum;
            double actual = (double) Math.round(ratio * 100) / 100;
            ShortLinkStatsDeviceRespVO shortLinkStatsDeviceRespVO = ShortLinkStatsDeviceRespVO.builder()
                    .device(each.getDevice())
                    .cnt(each.getCnt())
                    .ratio(actual)
                    .build();
            deviceStats.add(shortLinkStatsDeviceRespVO);
        });

        //10.查询网络访问详情
        List<ShortLinkStatsNetworkRespVO> networkStats = new ArrayList<>();
        List<LinkNetworkStats> linkNetworkStats = linkNetworkMapper.listNetworkStatsByShortLink(requestParam);
        int networkSum = linkNetworkStats.stream().mapToInt(LinkNetworkStats::getCnt).sum();

        linkNetworkStats.forEach(each -> {
            double ratio = (double) each.getCnt() / networkSum;
            double actual = (double) Math.round(ratio * 100) / 100;
            ShortLinkStatsNetworkRespVO shortLinkStatsNetworkRespVO = ShortLinkStatsNetworkRespVO.builder()
                    .network(each.getNetwork())
                    .cnt(each.getCnt())
                    .ratio(actual)
                    .build();
            networkStats.add(shortLinkStatsNetworkRespVO);
        });

        return ShortLinkStatsRespVO.builder()
                .pv(linkSumAccessStats.getPv())
                .uv(linkSumAccessStats.getUv())
                .uip(linkSumAccessStats.getUip())
                .daily(daily)
                .localeCnStats(localeCnStats)
                .hourStats(hourStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdayStats)
                .browserStats(browserStats)
                .osStats(osStats)
                .uvTypeStats(uvTypeStats)
                .deviceStats(deviceStats)
                .networkStats(networkStats)
                .build();
    }

    @Override
    public IPage<ShortLinkAccessRecordRespVO> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam) {
        //查询该短链接下的所有日志记录
        LambdaQueryWrapper<LinkAccessLogs> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogs.class)
                .eq(LinkAccessLogs::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkAccessLogs::getGid, requestParam.getGid())
                .eq(LinkAccessLogs::getDelFlag, 0)
                .between(LinkAccessLogs::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .orderByDesc(LinkAccessLogs::getCreateTime);
        IPage<LinkAccessLogs> shortLinkAccessRecordIPage = linkAccessLogsMapper.selectPage(requestParam, queryWrapper);
        IPage<ShortLinkAccessRecordRespVO> actualResult = shortLinkAccessRecordIPage.convert(each -> BeanUtil.toBean(each, ShortLinkAccessRecordRespVO.class));
        List<String> userList = actualResult.getRecords().stream()
                .map(ShortLinkAccessRecordRespVO::getUser)
                .toList();
        JudgeUvTypeReqDTO judgeUvTypeReqDTO = JudgeUvTypeReqDTO.builder()
                .fullShortUrl(requestParam.getFullShortUrl())
                .gid(requestParam.getGid())
                .startDate(requestParam.getStartDate())
                .endDate(requestParam.getEndDate())
                .userList(userList)
                .build();
        List<HashMap<String, Object>> uvTypes = linkAccessLogsMapper.listUvTypesByShortLink(judgeUvTypeReqDTO);
        //根据user去判断访客类型（为新老访客）
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypes.stream()
                    .filter(item -> Objects.equals(item.get("user"), each.getUser()))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("老访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }
}
