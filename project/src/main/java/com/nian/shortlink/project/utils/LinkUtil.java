package com.nian.shortlink.project.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.Optional;

import static com.nian.shortlink.project.common.constat.ShortLinkConstant.DEFAULT_CACHE_VALID_DATE;

/**
 * 短链接工具类
 */
public class LinkUtil {

    public static long getShortLinkCacheValidDate(Date vaildDate) {

        return Optional.ofNullable(vaildDate)
                .map(each -> DateUtil.between(new Date(),vaildDate, DateUnit.MS))
                .orElse(DEFAULT_CACHE_VALID_DATE);
    }
}
