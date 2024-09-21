package ru.skillbox.rest_news_service.web.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsListResponse {
    private List<NewsResponse> news = new ArrayList<>();

    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
