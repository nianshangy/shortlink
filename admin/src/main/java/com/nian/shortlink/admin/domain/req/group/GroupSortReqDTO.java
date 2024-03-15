package com.nian.shortlink.admin.domain.req.group;

import lombok.Data;

/**
 * 短链接分组排序参数
 */
@Data
public class GroupSortReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序id
     */
    private Integer sortOrder;
}
