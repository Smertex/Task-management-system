package by.smertex.database.repository;

import by.smertex.database.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>,
        QuerydslPredicateExecutor<Task> {
}
