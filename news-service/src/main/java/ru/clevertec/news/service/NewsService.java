package ru.clevertec.news.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.NewsRequestDto;
import ru.clevertec.news.dto.response.NewsPageResponseDto;
import ru.clevertec.news.dto.response.NewsResponseDto;
import ru.clevertec.news.exception.ValidationException;
import ru.clevertec.news.model.News;

public interface NewsService {

    /**
     * Метод создания объекта {@link News}
     *
     * @param dto объект, содержащий данные для создания новости
     * @return созданный объект с информацией о новости
     */
    NewsResponseDto create(NewsRequestDto dto);

    /**
     * @param id идентификатор для поиска объекта новости
     * @return объект с информацией о найденной новости
     * @throws EntityNotFoundException если объект не найден
     */
    NewsResponseDto findById(Long id) throws EntityNotFoundException;

    /**
     * Метод постраничного поиска объектов списка новостей
     *
     * @param pageable объект для разбивки информации на страницы
     * @return объект с информацией о странице и списком новостей
     */
    NewsPageResponseDto findAll(Pageable pageable);

    /**
     * @param pageable объект для разбивки информации на страницы
     * @param specification
     * @return объект с информацией о странице и списком новостей
     */
    NewsPageResponseDto findAllByFilter(Pageable pageable, Specification<News> specification);

    /**
     * @param filter объект фильтр для поиска новостей
     * @param pageable объект для разбивки информации на страницы
     * @return объект с информацией о странице и списком новостей
     * @throws ValidationException если {@link Filter#fields()} содержит поля, которых нет в {@link News}
     */
    NewsPageResponseDto findAllByFilter(Filter filter, Pageable pageable) throws ValidationException;

    /**
     * @param id идентификатор для поиска объекта новости
     * @param dto объект, содержащий данные для обновления новости
     * @return обновленный объект с информацией о новости
     * @throws EntityNotFoundException если объект не найден
     */
    NewsResponseDto update(Long id, NewsRequestDto dto) throws EntityNotFoundException;

    /**
     * @param id идентификатор для удаления объекта {@link News}
     */
    void deleteById(Long id);

}
