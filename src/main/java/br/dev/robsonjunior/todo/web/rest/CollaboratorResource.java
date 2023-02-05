package br.dev.robsonjunior.todo.web.rest;

import br.dev.robsonjunior.todo.domain.Collaborator;
import br.dev.robsonjunior.todo.repository.CollaboratorRepository;
import br.dev.robsonjunior.todo.service.CollaboratorQueryService;
import br.dev.robsonjunior.todo.service.CollaboratorService;
import br.dev.robsonjunior.todo.service.criteria.CollaboratorCriteria;
import br.dev.robsonjunior.todo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.dev.robsonjunior.todo.domain.Collaborator}.
 */
@RestController
@RequestMapping("/api")
public class CollaboratorResource {

    private final Logger log = LoggerFactory.getLogger(CollaboratorResource.class);

    private static final String ENTITY_NAME = "collaborator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollaboratorService collaboratorService;

    private final CollaboratorRepository collaboratorRepository;

    private final CollaboratorQueryService collaboratorQueryService;

    public CollaboratorResource(
        CollaboratorService collaboratorService,
        CollaboratorRepository collaboratorRepository,
        CollaboratorQueryService collaboratorQueryService
    ) {
        this.collaboratorService = collaboratorService;
        this.collaboratorRepository = collaboratorRepository;
        this.collaboratorQueryService = collaboratorQueryService;
    }

    /**
     * {@code POST  /collaborators} : Create a new collaborator.
     *
     * @param collaborator the collaborator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collaborator, or with status {@code 400 (Bad Request)} if the collaborator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collaborators")
    public ResponseEntity<Collaborator> createCollaborator(@Valid @RequestBody Collaborator collaborator) throws URISyntaxException {
        log.debug("REST request to save Collaborator : {}", collaborator);
        if (collaborator.getId() != null) {
            throw new BadRequestAlertException("A new collaborator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Collaborator result = collaboratorService.save(collaborator);
        return ResponseEntity
            .created(new URI("/api/collaborators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collaborators/:id} : Updates an existing collaborator.
     *
     * @param id the id of the collaborator to save.
     * @param collaborator the collaborator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collaborator,
     * or with status {@code 400 (Bad Request)} if the collaborator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collaborator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collaborators/{id}")
    public ResponseEntity<Collaborator> updateCollaborator(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Collaborator collaborator
    ) throws URISyntaxException {
        log.debug("REST request to update Collaborator : {}, {}", id, collaborator);
        if (collaborator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collaborator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collaboratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Collaborator result = collaboratorService.update(collaborator);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collaborator.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collaborators/:id} : Partial updates given fields of an existing collaborator, field will ignore if it is null
     *
     * @param id the id of the collaborator to save.
     * @param collaborator the collaborator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collaborator,
     * or with status {@code 400 (Bad Request)} if the collaborator is not valid,
     * or with status {@code 404 (Not Found)} if the collaborator is not found,
     * or with status {@code 500 (Internal Server Error)} if the collaborator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collaborators/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Collaborator> partialUpdateCollaborator(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Collaborator collaborator
    ) throws URISyntaxException {
        log.debug("REST request to partial update Collaborator partially : {}, {}", id, collaborator);
        if (collaborator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collaborator.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collaboratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Collaborator> result = collaboratorService.partialUpdate(collaborator);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collaborator.getId().toString())
        );
    }

    /**
     * {@code GET  /collaborators} : get all the collaborators.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collaborators in body.
     */
    @GetMapping("/collaborators")
    public ResponseEntity<List<Collaborator>> getAllCollaborators(
        CollaboratorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Collaborators by criteria: {}", criteria);
        Page<Collaborator> page = collaboratorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collaborators/count} : count all the collaborators.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collaborators/count")
    public ResponseEntity<Long> countCollaborators(CollaboratorCriteria criteria) {
        log.debug("REST request to count Collaborators by criteria: {}", criteria);
        return ResponseEntity.ok().body(collaboratorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collaborators/:id} : get the "id" collaborator.
     *
     * @param id the id of the collaborator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collaborator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collaborators/{id}")
    public ResponseEntity<Collaborator> getCollaborator(@PathVariable Long id) {
        log.debug("REST request to get Collaborator : {}", id);
        Optional<Collaborator> collaborator = collaboratorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collaborator);
    }

    /**
     * {@code DELETE  /collaborators/:id} : delete the "id" collaborator.
     *
     * @param id the id of the collaborator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collaborators/{id}")
    public ResponseEntity<Void> deleteCollaborator(@PathVariable Long id) {
        log.debug("REST request to delete Collaborator : {}", id);
        collaboratorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
