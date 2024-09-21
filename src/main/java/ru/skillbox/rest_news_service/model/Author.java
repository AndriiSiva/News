package ru.skillbox.rest_news_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name")
    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToStringExclude
    @Builder.Default
    private List<News> newsList = new ArrayList<>();


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ToStringExclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

      public void addNews(News news) {
        newsList.add(news);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}