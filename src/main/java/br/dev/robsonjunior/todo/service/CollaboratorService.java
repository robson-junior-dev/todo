package br.dev.robsonjunior.todo.service;

import br.dev.robsonjunior.todo.domain.Collaborator;
import br.dev.robsonjunior.todo.repository.CollaboratorRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Collaborator}.
 */
@Service
@Transactional
public class CollaboratorService {

    private final Logger log = LoggerFactory.getLogger(CollaboratorService.class);

    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    /**
     * Save a collaborator.
     *
     * @param collaborator the entity to save.
     * @return the persisted entity.
     */
    public Collaborator save(Collaborator collaborator) {
        log.debug("Request to save Collaborator : {}", collaborator);
        return collaboratorRepository.save(collaborator);
    }

    /**
     * Update a collaborator.
     *
     * @param collaborator the entity to save.
     * @return the persisted entity.
     */
    public Collaborator update(Collaborator collaborator) {
        log.debug("Request to update Collaborator : {}", collaborator);
        return collaboratorRepository.save(collaborator);
    }

    /**
     * Partially update a collaborator.
     *
     * @param collaborator the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Collaborator> partialUpdate(Collaborator collaborator) {
        log.debug("Request to partially update Collaborator : {}", collaborator);

        return collaboratorRepository
            .findById(collaborator.getId())
            .map(existingCollaborator -> {
                if (collaborator.getName() != null) {
                    existingCollaborator.setName(collaborator.getName());
                }
                if (collaborator.getDateOfBirth() != null) {
                    existingCollaborator.setDateOfBirth(collaborator.getDateOfBirth());
                }
                if (collaborator.getPhoto() != null) {
                    existingCollaborator.setPhoto(collaborator.getPhoto());
                }
                if (collaborator.getPhotoContentType() != null) {
                    existingCollaborator.setPhotoContentType(collaborator.getPhotoContentType());
                }
                if (collaborator.getDocument() != null) {
                    existingCollaborator.setDocument(collaborator.getDocument());
                }
                if (collaborator.getDocumentContentType() != null) {
                    existingCollaborator.setDocumentContentType(collaborator.getDocumentContentType());
                }
                if (collaborator.getCpf() != null) {
                    existingCollaborator.setCpf(collaborator.getCpf());
                }
                if (collaborator.getIsLeader() != null) {
                    existingCollaborator.setIsLeader(collaborator.getIsLeader());
                }

                return existingCollaborator;
            })
            .map(collaboratorRepository::save);
    }

    /**
     * Get all the collaborators.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Collaborator> findAll(Pageable pageable) {
        log.debug("Request to get all Collaborators");
        return collaboratorRepository.findAll(pageable);
    }

    /**
     * Get all the collaborators with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Collaborator> findAllWithEagerRelationships(Pageable pageable) {
        return collaboratorRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one collaborator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Collaborator> findOne(Long id) {
        log.debug("Request to get Collaborator : {}", id);
        return collaboratorRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the collaborator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Collaborator : {}", id);
        collaboratorRepository.deleteById(id);
    }
}
