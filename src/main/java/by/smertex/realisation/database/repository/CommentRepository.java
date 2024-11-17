package by.smertex.realisation.database.repository;

import by.smertex.realisation.database.entity.Comment;
import by.smertex.realisation.dto.filter.CommentFilter;
import by.smertex.realisation.dto.security.SecurityUserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
        @Query("""
           select c, u from Comment c
           join Task t on t.id = c.task.id
           join User u on u.id = t.performer.id
           where t.id = :taskId
           and (:#{#commentFilter.createdBy.email} is null or :#{#commentFilter.createdBy.email} = c.createdBy.email) 
           and (:#{#commentFilter.createdBy.role} is null or :#{#commentFilter.createdBy.role} = c.createdBy.role)
           and (:#{#securityUserDto.email} = t.performer.email or :#{#securityUserDto.isAdmin} = true)
           """)
    List<Comment> findAllByFilter(UUID taskId, CommentFilter commentFilter, SecurityUserDto securityUserDto, Pageable pageable);
}
