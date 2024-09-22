package ru.skillbox.rest_news_service.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.rest_news_service.entity.Role;
import ru.skillbox.rest_news_service.entity.RoleType;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorResponse {
    private long id;
    private String name;
    private Long newsList;
    private Long comments;
    private List<String> roleTypeList = new ArrayList<>();
}
