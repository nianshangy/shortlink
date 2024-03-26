package com.nian.shortlink.project.domain.req;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nian.shortlink.project.domain.entity.ShortLink;
import lombok.Data;

import java.util.List;

/**
 * 短链接回收站分页查寻请求参数
 */
@Data
public class ShortLinkRecycleBinPageReqDTO extends Page<ShortLink> {

    /**
     * 分组标识集合
     */
    private List<String> gidList;
}
