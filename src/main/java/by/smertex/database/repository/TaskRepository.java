package by.smertex.database.repository;

import by.smertex.database.entity.Task;
import by.smertex.dto.filter.TaskFilter;
import by.smertex.dto.security.SecurityUserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Query("""
           select t from Task t
           join Metainfo m on t.metainfo.id = m.id
           join User u on t.performer.id = u.id
           where (:#{#filter.createdBy.email} is null or :#{#filter.createdBy.email} = m.createdBy.email)
           and (:#{#filter.createdBy.role} is null or :#{#filter.createdBy.role} = m.createdBy.role)
           and (:#{#filter.createdAt} is null or :#{#filter.createdAt} = m.createdAt)
           and (:#{#filter.closedAt} is null or :#{#filter.closedAt} = m.closedAt)
           and (:#{#filter.status} is null or :#{#filter.status} = t.status)
           and (:#{#filter.priority} is null or :#{#filter.priority} = t.priority)
           and (:#{#filter.performer.email} is null or :#{#filter.performer.email} = t.performer.email)
           and (:#{#filter.performer.role} is null or :#{#filter.performer.role} = t.performer.role)
           and (:#{#filter.name} is null or t.name like %:#{#filter.name}%)
           and (:#{#user.email} = t.performer.email or :#{#user.isAdmin} = true)
           """)
    List<Task> findAllByFilter(TaskFilter filter, SecurityUserDto user, Pageable pageable);
}
