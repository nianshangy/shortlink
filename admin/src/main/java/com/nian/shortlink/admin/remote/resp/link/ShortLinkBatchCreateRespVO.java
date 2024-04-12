package com.nian.shortlink.admin.remote.resp.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 短链接批量创建响应参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkBatchCreateRespVO {

    /**
     * 创建总数
     */
    private Integer total;

    /**
     * 批量创建返回参数
     */
    private List<ShortLinkBatchBaseInfoRespVO> baseLinkInfos;
}
