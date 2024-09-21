package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.AuthorListResponse;
import ru.skillbox.rest_news_service.web.model.AuthorResponse;
import ru.skillbox.rest_news_service.web.model.UpsertAuthorRequest;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapper.class, CommentMapper.class})
public interface AuthorMapper {
    Author requestToAuthor(UpsertAuthorRequest request);

    @Mapping(source = "authorId", target = "id")
    Author requestToAuthor(Long authorId, UpsertAuthorRequest request);

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

    default Long countNewsList(List<News> newsList) {
        return (long) newsList.size();
    }
}
