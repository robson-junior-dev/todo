package br.dev.robsonjunior.todo.service;

import br.dev.robsonjunior.todo.domain.*; // for static metamodels
import br.dev.robsonjunior.todo.domain.Sprint;
import br.dev.robsonjunior.todo.repository.SprintRepository;
import br.dev.robsonjunior.todo.service.criteria.SprintCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Sprint} entities in the database.
 * The main input is a {@link SprintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Sprint} or a {@link Page} of {@link Sprint} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SprintQueryService extends QueryService<Sprint> {

    private final Logger log = LoggerFactory.getLogger(SprintQueryService.class);

    private final SprintRepository sprintRepository;

    public SprintQueryService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    /**
     * Return a {@link List} of {@link Sprint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Sprint> findByCriteria(SprintCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Sprint} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Sprint> findByCriteria(SprintCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SprintCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sprint> specification = createSpecification(criteria);
        return sprintRepository.count(specification);
    }

    /**
     * Function to convert {@link SprintCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sprint> createSpecification(SprintCriteria criteria) {
        Specification<Sprint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sprint_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Sprint_.number));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Sprint_.status));
            }
            if (criteria.getProjectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProjectId(), root -> root.join(Sprint_.project, JoinType.LEFT).get(Project_.id))
                    );
            }
        }
        return specification;
    }
}
