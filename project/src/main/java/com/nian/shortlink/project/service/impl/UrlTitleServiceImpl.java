package com.nian.shortlink.project.service.impl;

import com.nian.shortlink.project.service.IUrlTitleService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网站标题接口实现层
 */
@Service
public class UrlTitleServiceImpl implements IUrlTitleService {


    @SneakyThrows
    @Override
    public String getTitleByUrl(String url){
        URL tagetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) tagetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            return document.title();
        }
        return "Error while fetching title";
    }
}
