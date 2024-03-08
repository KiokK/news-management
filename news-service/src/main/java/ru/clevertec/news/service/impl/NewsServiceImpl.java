package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.model.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.NewsService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.clevertec.news.validation.SearchFilterValidation.validate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);
    private final NewsRepository newsRepository;

    private final List<String> newsFields = Arrays.stream(News.class.getDeclaredFields())
            .map(Field::getName).toList();

    @Override
    @Transactional
    @CacheEvict(cacheNames = "newsCache", key = "#result.id")
    public NewsResponseDto create(NewsRequestDto dto) {
        return Optional.of(dto)
                .map(newsMapper::toNews)
                .map(newsRepository::save)
                .map(newsMapper::toNewsResponseDto)
                .orElseThrow();
    }

    @Override
    @Cacheable(cacheNames = "newsCache")
    public NewsResponseDto findById(Long id) throws EntityNotFoundException {
        return newsRepository.findById(id)
                .map(newsMapper::toNewsResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public NewsPageResponseDto findAll(Pageable pageable) {
        List<NewsResponseDto> newsResponseDto = newsRepository.findAll(pageable)
                .map(newsMapper::toNewsResponseDto)
                .toList();

        return newsMapper.toNewsPageResponseDto(newsResponseDto, pageable);
    }

    @Override
    public NewsPageResponseDto findAllByFilter(Pageable pageable, Specification<News> specification) {
        List<News> news = Optional.ofNullable(specification)
                .map(spec -> newsRepository.findAll(spec, pageable))
                .orElseGet(() -> newsRepository.findAll(pageable))
                .toList();

        return newsMapper.toNewsPageResponseDto(pageable, news);
    }

    @Override
    public NewsPageResponseDto findAllByFilter(Filter filter, Pageable pageable) throws ValidationException {
        validate(filter, News.class);

        List<String> searchFields = filter.fields();

        Specification<News> specification = Optional.of(filter.part())
                .map(part -> "%" + part + "%")
                .map(part -> Specification.anyOf(
                        searchFields.stream()
                                .map(field -> (Specification<News>) (root, query, cb) -> cb.like(root.get(field), part))
                                .toList()
                ))
                .orElse(null);

        return findAllByFilter(pageable, specification);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "newsCache", key = "#id")
    public NewsResponseDto update(Long id, NewsRequestDto dto) throws EntityNotFoundException {
        return newsRepository.findById(id)
                .map(news -> newsMapper.mergeNewsRequestDto(news, dto))
                .map(newsRepository::save)
                .map(newsMapper::toNewsResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "newsCache", key = "#id")
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
}
