package com.nian.shortlink.project.domain.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.project.domain.entity.ShortLink;
import lombok.Data;

/**
 * 短链接分页查寻请求参数
 */
@Data
public class ShortLinkPageDTO extends Page<ShortLink> {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序标识
     */
    private String orderTag;
}
