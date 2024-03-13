package com.nian.shortlink.admin.domain.dto.group;

import lombok.Data;

/**
 * 短链接分组创建参数
 */
@Data
public class GroupSaveReqDTO {
    /**
     * 短链接分组组名
     */
    private String groupName;
}
