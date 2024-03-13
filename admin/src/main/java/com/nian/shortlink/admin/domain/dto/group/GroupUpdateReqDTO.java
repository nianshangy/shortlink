package com.nian.shortlink.admin.domain.dto.group;

import lombok.Data;

/**
 * 更新短链接分组名称参数
 */
@Data
public class GroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接分组组名
     */
    private String groupName;
}
