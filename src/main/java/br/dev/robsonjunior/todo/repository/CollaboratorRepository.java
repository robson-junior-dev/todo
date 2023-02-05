package br.dev.robsonjunior.todo.repository;

import br.dev.robsonjunior.todo.domain.Collaborator;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Collaborator entity.
 */
@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long>, JpaSpecificationExecutor<Collaborator> {
    default Optional<Collaborator> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Collaborator> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Collaborator> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct collaborator from Collaborator collaborator left join fetch collaborator.user left join fetch collaborator.leader",
        countQuery = "select count(distinct collaborator) from Collaborator collaborator"
    )
    Page<Collaborator> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct collaborator from Collaborator collaborator left join fetch collaborator.user left join fetch collaborator.leader"
    )
    List<Collaborator> findAllWithToOneRelationships();

    @Query(
        "select collaborator from Collaborator collaborator left join fetch collaborator.user left join fetch collaborator.leader where collaborator.id =:id"
    )
    Optional<Collaborator> findOneWithToOneRelationships(@Param("id") Long id);
}
