package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "commentText", target = "commentText")
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "newsId", target = "news.id")
    Comment requestToComment(UpsertCommentRequest request);

    @Mapping(source = "commentId", target = "id")
    @Mapping(source = "request.authorId", target = "author.id")
    @Mapping(source = "request.newsId", target = "news.id")
    Comment requestToComment(Long commentId, UpsertCommentRequest request);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "news.id", target = "newsId")
    CommentResponse commentToResponse(Comment comment);

    List<CommentResponse> commentListToReponseList(List<Comment> comments);

    default CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
        CommentListResponse response = new CommentListResponse();
        response.setComments(commentListToReponseList(comments));
        return response;
    }
}