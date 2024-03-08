package ru.clevertec.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.CommentResponseDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.dto.response.NewsWithPageCommentsResponseDto;
import ru.clevertec.news.model.Comment;
import ru.clevertec.news.model.News;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NewsMapper {

    News toNews(NewsRequestDto newsRequestDto);

    @Mapping(target = "text", source = "newData.text")
    @Mapping(target = "title", source = "newData.title")
    News mergeNewsRequestDto(News news, NewsRequestDto newData);

    NewsResponseDto toNewsResponseDto(News news);

    NewsRequestDto toNewsRequestDto(News news);

    List<NewsResponseDto> listNewsToListNewsResponseDto(List<News> listNews);

    @Mapping(target = "news", source = "news")
    NewsPageResponseDto toNewsPageResponseDto(List<NewsResponseDto> news, Pageable pageable);

    @Mapping(target = "news", source = "news")
    NewsPageResponseDto toNewsPageResponseDto(Pageable pageable, List<News> news);

    @Mapping(target = "comments", source = "news.comments")
    NewsWithPageCommentsResponseDto toNewsWithCommentsResponseDto(News news, Pageable pageable);

    @Mapping(target = "newsId", source = "comment.news.id")
    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "text", source = "comment.text")
    CommentResponseDto commentToCommentResponseDto(Comment comment);
}
