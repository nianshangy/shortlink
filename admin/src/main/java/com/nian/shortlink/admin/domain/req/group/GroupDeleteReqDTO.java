package com.nian.shortlink.admin.domain.req.group;

import lombok.Data;

/**
 * 删除短链接分组参数
 */
@Data
public class GroupDeleteReqDTO {
    /**
     * 分组标识
     */
    private String gid;
}
