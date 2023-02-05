package br.dev.robsonjunior.todo.web.rest;

import br.dev.robsonjunior.todo.domain.Project;
import br.dev.robsonjunior.todo.repository.ProjectRepository;
import br.dev.robsonjunior.todo.service.ProjectQueryService;
import br.dev.robsonjunior.todo.service.ProjectService;
import br.dev.robsonjunior.todo.service.criteria.ProjectCriteria;
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
 * REST controller for managing {@link br.dev.robsonjunior.todo.domain.Project}.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final ProjectQueryService projectQueryService;

    public ProjectResource(ProjectService projectService, ProjectRepository projectRepository, ProjectQueryService projectQueryService) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
        this.projectQueryService = projectQueryService;
    }

    /**
     * {@code POST  /projects} : Create a new project.
     *
     * @param project the project to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new project, or with status {@code 400 (Bad Request)} if the project has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Project result = projectService.save(project);
        return ResponseEntity
            .created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /projects/:id} : Updates an existing project.
     *
     * @param id the id of the project to save.
     * @param project the project to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated project,
     * or with status {@code 400 (Bad Request)} if the project is not valid,
     * or with status {@code 500 (Internal Server Error)} if the project couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Project project
    ) throws URISyntaxException {
        log.debug("REST request to update Project : {}, {}", id, project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, project.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Project result = projectService.update(project);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, project.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /projects/:id} : Partial updates given fields of an existing project, field will ignore if it is null
     *
     * @param id the id of the project to save.
     * @param project the project to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated project,
     * or with status {@code 400 (Bad Request)} if the project is not valid,
     * or with status {@code 404 (Not Found)} if the project is not found,
     * or with status {@code 500 (Internal Server Error)} if the project couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/projects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Project> partialUpdateProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Project project
    ) throws URISyntaxException {
        log.debug("REST request to partial update Project partially : {}, {}", id, project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, project.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Project> result = projectService.partialUpdate(project);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, project.getId().toString())
        );
    }

    /**
     * {@code GET  /projects} : get all the projects.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projects in body.
     */
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects(
        ProjectCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Projects by criteria: {}", criteria);
        Page<Project> page = projectQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /projects/count} : count all the projects.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/projects/count")
    public ResponseEntity<Long> countProjects(ProjectCriteria criteria) {
        log.debug("REST request to count Projects by criteria: {}", criteria);
        return ResponseEntity.ok().body(projectQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /projects/:id} : get the "id" project.
     *
     * @param id the id of the project to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the project, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Optional<Project> project = projectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(project);
    }

    /**
     * {@code DELETE  /projects/:id} : delete the "id" project.
     *
     * @param id the id of the project to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
