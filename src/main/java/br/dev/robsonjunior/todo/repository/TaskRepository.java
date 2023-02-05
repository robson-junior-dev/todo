package br.dev.robsonjunior.todo.repository;

import br.dev.robsonjunior.todo.domain.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    default Optional<Task> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Task> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Task> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct task from Task task left join fetch task.sprint left join fetch task.collaborator",
        countQuery = "select count(distinct task) from Task task"
    )
    Page<Task> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct task from Task task left join fetch task.sprint left join fetch task.collaborator")
    List<Task> findAllWithToOneRelationships();

    @Query("select task from Task task left join fetch task.sprint left join fetch task.collaborator where task.id =:id")
    Optional<Task> findOneWithToOneRelationships(@Param("id") Long id);
}
