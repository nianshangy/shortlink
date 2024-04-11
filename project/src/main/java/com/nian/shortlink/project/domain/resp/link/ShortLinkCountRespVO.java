package com.nian.shortlink.project.domain.resp.link;

import lombok.Data;

/**
 * 查询短链接分组内数量返回参数
 */
@Data
public class ShortLinkCountRespVO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接数量
     */
    private Integer shortLinkCount;
}
