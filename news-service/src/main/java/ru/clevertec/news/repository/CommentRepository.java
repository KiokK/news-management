package ru.clevertec.news.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.news.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByNews_Id(Long id, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN fetch c.news WHERE c.id = :id")
    Optional<Comment> findById(Long id);

    Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);
}
