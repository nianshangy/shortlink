package com.nian.shortlink.admin.remote.req.linkStats;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 访问单个短链接的访问记录监控数据请求参数
 */
@Data
public class ShortLinkAccessRecordReqDTO extends Page {
    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
