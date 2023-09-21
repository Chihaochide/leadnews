package com.liuxuanhe.wemedia.service;

import com.liuxuanhe.model.wemedia.pojos.WmNews;

public interface WmNewsAutoScanService {

    public void autoScanWmNews(Integer id);
    public void publishApArticle(WmNews wmNews);
}
