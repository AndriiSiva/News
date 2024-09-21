package ru.skillbox.rest_news_service.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String name;
}