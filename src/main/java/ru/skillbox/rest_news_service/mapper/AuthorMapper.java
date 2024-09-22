package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class, CommentMapper.class})
public interface AuthorMapper {

    @Mapping(source = "password", target = "password")
    Author requestToAuthor(UpsertAuthorRequest request);

    @Mapping(source = "authorId", target = "id")
    Author requestToAuthor(Long authorId, UpdateAuthorRequest request);


    @Mapping(target = "roleTypeList", expression = "java(mapRolesToRoleTypeList(author))")
    AuthorResponse authorToResponse(Author author);

    default AuthorListResponse authorListToAuthorResponseList(Page<Author> authorsPage) {
        AuthorListResponse response = new AuthorListResponse();
        response.setAuthors(authorsPage.getContent().stream()
                .map(this::authorToResponse)
                .toList());

        response.setTotalElements(authorsPage.getTotalElements());
        response.setTotalPages(authorsPage.getTotalPages());
        response.setCurrentPage(authorsPage.getNumber());
        response.setPageSize(authorsPage.getSize());
        return response;
    }

    default List<String> mapRolesToRoleTypeList(Author author) {
        return author.getRoles().stream()
                .filter(role -> role.getAuthority() != null)
                .map(role -> role.getAuthority().name())
                .collect(Collectors.toList());
    }

    default Long countNewsList(List<News> newsList) {
        return (long) newsList.size();
    }

    @Mapping(source = "password", target = "password")
    @Mapping(source = "newsList", target = "newsList")
    Author requestToAuthorWithNews(UpsertAuthorRequestWithNews request);
}
