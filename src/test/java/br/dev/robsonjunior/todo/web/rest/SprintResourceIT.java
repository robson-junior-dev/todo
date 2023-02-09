package br.dev.robsonjunior.todo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.dev.robsonjunior.todo.IntegrationTest;
import br.dev.robsonjunior.todo.domain.Project;
import br.dev.robsonjunior.todo.domain.Sprint;
import br.dev.robsonjunior.todo.domain.enumeration.SprintStatus;
import br.dev.robsonjunior.todo.repository.SprintRepository;
import br.dev.robsonjunior.todo.service.SprintService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SprintResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SprintResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final SprintStatus DEFAULT_STATUS = SprintStatus.IN_PLANNING;
    private static final SprintStatus UPDATED_STATUS = SprintStatus.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/sprints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SprintRepository sprintRepository;

    @Mock
    private SprintRepository sprintRepositoryMock;

    @Mock
    private SprintService sprintServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSprintMockMvc;

    private Sprint sprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createEntity(EntityManager em) {
        Sprint sprint = new Sprint().number(DEFAULT_NUMBER).status(DEFAULT_STATUS);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        sprint.setProject(project);
        return sprint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sprint createUpdatedEntity(EntityManager em) {
        Sprint sprint = new Sprint().number(UPDATED_NUMBER).status(UPDATED_STATUS);
        // Add required entity
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            project = ProjectResourceIT.createUpdatedEntity(em);
            em.persist(project);
            em.flush();
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        sprint.setProject(project);
        return sprint;
    }

    @BeforeEach
    public void initTest() {
        sprint = createEntity(em);
    }

    @Test
    @Transactional
    void createSprint() throws Exception {
        int databaseSizeBeforeCreate = sprintRepository.findAll().size();
        // Create the Sprint
        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isCreated());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate + 1);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSprint.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSprintWithExistingId() throws Exception {
        // Create the Sprint with an existing ID
        sprint.setId(1L);

        int databaseSizeBeforeCreate = sprintRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setNumber(null);

        // Create the Sprint, which fails.

        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = sprintRepository.findAll().size();
        // set the field null
        sprint.setStatus(null);

        // Create the Sprint, which fails.

        restSprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isBadRequest());

        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSprints() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSprintsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sprintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSprintMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sprintServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSprintsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sprintServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSprintMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sprintRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get the sprint
        restSprintMockMvc
            .perform(get(ENTITY_API_URL_ID, sprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sprint.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getSprintsByIdFiltering() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        Long id = sprint.getId();

        defaultSprintShouldBeFound("id.equals=" + id);
        defaultSprintShouldNotBeFound("id.notEquals=" + id);

        defaultSprintShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSprintShouldNotBeFound("id.greaterThan=" + id);

        defaultSprintShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSprintShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSprintsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where number equals to DEFAULT_NUMBER
        defaultSprintShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the sprintList where number equals to UPDATED_NUMBER
        defaultSprintShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllSprintsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultSprintShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the sprintList where number equals to UPDATED_NUMBER
        defaultSprintShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllSprintsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where number is not null
        defaultSprintShouldBeFound("number.specified=true");

        // Get all the sprintList where number is null
        defaultSprintShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllSprintsByNumberContainsSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where number contains DEFAULT_NUMBER
        defaultSprintShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the sprintList where number contains UPDATED_NUMBER
        defaultSprintShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllSprintsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where number does not contain DEFAULT_NUMBER
        defaultSprintShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the sprintList where number does not contain UPDATED_NUMBER
        defaultSprintShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllSprintsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where status equals to DEFAULT_STATUS
        defaultSprintShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the sprintList where status equals to UPDATED_STATUS
        defaultSprintShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSprintsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSprintShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the sprintList where status equals to UPDATED_STATUS
        defaultSprintShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSprintsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        // Get all the sprintList where status is not null
        defaultSprintShouldBeFound("status.specified=true");

        // Get all the sprintList where status is null
        defaultSprintShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSprintsByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            sprintRepository.saveAndFlush(sprint);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        sprint.setProject(project);
        sprintRepository.saveAndFlush(sprint);
        Long projectId = project.getId();

        // Get all the sprintList where project equals to projectId
        defaultSprintShouldBeFound("projectId.equals=" + projectId);

        // Get all the sprintList where project equals to (projectId + 1)
        defaultSprintShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSprintShouldBeFound(String filter) throws Exception {
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSprintShouldNotBeFound(String filter) throws Exception {
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSprintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSprint() throws Exception {
        // Get the sprint
        restSprintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint
        Sprint updatedSprint = sprintRepository.findById(sprint.getId()).get();
        // Disconnect from session so that the updates on updatedSprint are not directly
        // saved in db
        em.detach(updatedSprint);
        updatedSprint.number(UPDATED_NUMBER).status(UPDATED_STATUS);

        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSprint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSprint))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sprint.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSprintWithPatch() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint using partial update
        Sprint partialUpdatedSprint = new Sprint();
        partialUpdatedSprint.setId(sprint.getId());

        partialUpdatedSprint.status(UPDATED_STATUS);

        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSprint))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSprintWithPatch() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();

        // Update the sprint using partial update
        Sprint partialUpdatedSprint = new Sprint();
        partialUpdatedSprint.setId(sprint.getId());

        partialUpdatedSprint.number(UPDATED_NUMBER).status(UPDATED_STATUS);

        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSprint))
            )
            .andExpect(status().isOk());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
        Sprint testSprint = sprintList.get(sprintList.size() - 1);
        assertThat(testSprint.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testSprint.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sprint))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSprint() throws Exception {
        int databaseSizeBeforeUpdate = sprintRepository.findAll().size();
        sprint.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSprintMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sprint)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sprint in the database
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSprint() throws Exception {
        // Initialize the database
        sprintRepository.saveAndFlush(sprint);

        int databaseSizeBeforeDelete = sprintRepository.findAll().size();

        // Delete the sprint
        restSprintMockMvc
            .perform(delete(ENTITY_API_URL_ID, sprint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sprint> sprintList = sprintRepository.findAll();
        assertThat(sprintList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
