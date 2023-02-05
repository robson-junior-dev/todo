package br.dev.robsonjunior.todo.service;

import br.dev.robsonjunior.todo.domain.Sprint;
import br.dev.robsonjunior.todo.repository.SprintRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sprint}.
 */
@Service
@Transactional
public class SprintService {

    private final Logger log = LoggerFactory.getLogger(SprintService.class);

    private final SprintRepository sprintRepository;

    public SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    /**
     * Save a sprint.
     *
     * @param sprint the entity to save.
     * @return the persisted entity.
     */
    public Sprint save(Sprint sprint) {
        log.debug("Request to save Sprint : {}", sprint);
        return sprintRepository.save(sprint);
    }

    /**
     * Update a sprint.
     *
     * @param sprint the entity to save.
     * @return the persisted entity.
     */
    public Sprint update(Sprint sprint) {
        log.debug("Request to update Sprint : {}", sprint);
        return sprintRepository.save(sprint);
    }

    /**
     * Partially update a sprint.
     *
     * @param sprint the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Sprint> partialUpdate(Sprint sprint) {
        log.debug("Request to partially update Sprint : {}", sprint);

        return sprintRepository
            .findById(sprint.getId())
            .map(existingSprint -> {
                if (sprint.getNumber() != null) {
                    existingSprint.setNumber(sprint.getNumber());
                }
                if (sprint.getStatus() != null) {
                    existingSprint.setStatus(sprint.getStatus());
                }

                return existingSprint;
            })
            .map(sprintRepository::save);
    }

    /**
     * Get all the sprints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Sprint> findAll(Pageable pageable) {
        log.debug("Request to get all Sprints");
        return sprintRepository.findAll(pageable);
    }

    /**
     * Get all the sprints with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Sprint> findAllWithEagerRelationships(Pageable pageable) {
        return sprintRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one sprint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Sprint> findOne(Long id) {
        log.debug("Request to get Sprint : {}", id);
        return sprintRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the sprint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Sprint : {}", id);
        sprintRepository.deleteById(id);
    }
}
