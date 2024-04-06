package com.nian.shortlink.project.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取用户信息是否新老访客
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeUvTypeReqDTO {
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

    /**
     * 用户列表
     */
    private List<String> userList;
}
