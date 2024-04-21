package com.nian.shortlink.project.domain.req.linkStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取分组用户信息是否新老访客
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeGroupUvTypeReqDTO {

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
