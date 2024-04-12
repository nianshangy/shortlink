package com.nian.shortlink.admin.remote.req.link;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 短链接分页查寻请求参数
 */
@Data
public class ShortLinkPageDTO extends Page {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序标识
     */
    private String orderTag;
}
