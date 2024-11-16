package by.smertex.database.repository;

import by.smertex.database.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>,
        QuerydslPredicateExecutor<Comment> {

}
