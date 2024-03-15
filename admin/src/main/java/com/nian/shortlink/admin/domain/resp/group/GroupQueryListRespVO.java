package com.nian.shortlink.admin.domain.resp.group;

import lombok.Data;

/**
 * 查询短链接分组集合返回参数
 */
@Data
public class GroupQueryListRespVO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;
    /**
     * 排序id
     */
    private Integer sortOrder;
}
