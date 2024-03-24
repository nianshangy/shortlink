package com.nian.shortlink.project.service;

/**
 * 网站标题接口
 */
public interface IUrlTitleService {

    /**
     * 根据url获取网站标题
     * @param url 网站 url
     * @return 网站标题
     */
    String getTitleByUrl(String url);
}
