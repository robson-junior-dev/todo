package br.dev.robsonjunior.todo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.dev.robsonjunior.todo.IntegrationTest;
import br.dev.robsonjunior.todo.domain.Collaborator;
import br.dev.robsonjunior.todo.domain.Collaborator;
import br.dev.robsonjunior.todo.domain.Project;
import br.dev.robsonjunior.todo.domain.User;
import br.dev.robsonjunior.todo.repository.CollaboratorRepository;
import br.dev.robsonjunior.todo.service.CollaboratorService;
import br.dev.robsonjunior.todo.service.criteria.CollaboratorCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CollaboratorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CollaboratorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LEADER = false;
    private static final Boolean UPDATED_IS_LEADER = true;

    private static final String ENTITY_API_URL = "/api/collaborators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private CollaboratorRepository collaboratorRepositoryMock;

    @Mock
    private CollaboratorService collaboratorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollaboratorMockMvc;

    private Collaborator collaborator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collaborator createEntity(EntityManager em) {
        Collaborator collaborator = new Collaborator()
            .name(DEFAULT_NAME)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE)
            .cpf(DEFAULT_CPF)
            .isLeader(DEFAULT_IS_LEADER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        collaborator.setUser(user);
        return collaborator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collaborator createUpdatedEntity(EntityManager em) {
        Collaborator collaborator = new Collaborator()
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .cpf(UPDATED_CPF)
            .isLeader(UPDATED_IS_LEADER);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        collaborator.setUser(user);
        return collaborator;
    }

    @BeforeEach
    public void initTest() {
        collaborator = createEntity(em);
    }

    @Test
    @Transactional
    void createCollaborator() throws Exception {
        int databaseSizeBeforeCreate = collaboratorRepository.findAll().size();
        // Create the Collaborator
        restCollaboratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collaborator)))
            .andExpect(status().isCreated());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeCreate + 1);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollaborator.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testCollaborator.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testCollaborator.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testCollaborator.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testCollaborator.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
        assertThat(testCollaborator.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testCollaborator.getIsLeader()).isEqualTo(DEFAULT_IS_LEADER);
    }

    @Test
    @Transactional
    void createCollaboratorWithExistingId() throws Exception {
        // Create the Collaborator with an existing ID
        collaborator.setId(1L);

        int databaseSizeBeforeCreate = collaboratorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollaboratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collaborator)))
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaboratorRepository.findAll().size();
        // set the field null
        collaborator.setName(null);

        // Create the Collaborator, which fails.

        restCollaboratorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collaborator)))
            .andExpect(status().isBadRequest());

        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollaborators() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].isLeader").value(hasItem(DEFAULT_IS_LEADER.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollaboratorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(collaboratorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollaboratorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(collaboratorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollaboratorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(collaboratorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollaboratorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(collaboratorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get the collaborator
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL_ID, collaborator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collaborator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.isLeader").value(DEFAULT_IS_LEADER.booleanValue()));
    }

    @Test
    @Transactional
    void getCollaboratorsByIdFiltering() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        Long id = collaborator.getId();

        defaultCollaboratorShouldBeFound("id.equals=" + id);
        defaultCollaboratorShouldNotBeFound("id.notEquals=" + id);

        defaultCollaboratorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCollaboratorShouldNotBeFound("id.greaterThan=" + id);

        defaultCollaboratorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCollaboratorShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where name equals to DEFAULT_NAME
        defaultCollaboratorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the collaboratorList where name equals to UPDATED_NAME
        defaultCollaboratorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCollaboratorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the collaboratorList where name equals to UPDATED_NAME
        defaultCollaboratorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where name is not null
        defaultCollaboratorShouldBeFound("name.specified=true");

        // Get all the collaboratorList where name is null
        defaultCollaboratorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCollaboratorsByNameContainsSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where name contains DEFAULT_NAME
        defaultCollaboratorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the collaboratorList where name contains UPDATED_NAME
        defaultCollaboratorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where name does not contain DEFAULT_NAME
        defaultCollaboratorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the collaboratorList where name does not contain UPDATED_NAME
        defaultCollaboratorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth is not null
        defaultCollaboratorShouldBeFound("dateOfBirth.specified=true");

        // Get all the collaboratorList where dateOfBirth is null
        defaultCollaboratorShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultCollaboratorShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the collaboratorList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultCollaboratorShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where cpf equals to DEFAULT_CPF
        defaultCollaboratorShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the collaboratorList where cpf equals to UPDATED_CPF
        defaultCollaboratorShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultCollaboratorShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the collaboratorList where cpf equals to UPDATED_CPF
        defaultCollaboratorShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where cpf is not null
        defaultCollaboratorShouldBeFound("cpf.specified=true");

        // Get all the collaboratorList where cpf is null
        defaultCollaboratorShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllCollaboratorsByCpfContainsSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where cpf contains DEFAULT_CPF
        defaultCollaboratorShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the collaboratorList where cpf contains UPDATED_CPF
        defaultCollaboratorShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where cpf does not contain DEFAULT_CPF
        defaultCollaboratorShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the collaboratorList where cpf does not contain UPDATED_CPF
        defaultCollaboratorShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByIsLeaderIsEqualToSomething() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where isLeader equals to DEFAULT_IS_LEADER
        defaultCollaboratorShouldBeFound("isLeader.equals=" + DEFAULT_IS_LEADER);

        // Get all the collaboratorList where isLeader equals to UPDATED_IS_LEADER
        defaultCollaboratorShouldNotBeFound("isLeader.equals=" + UPDATED_IS_LEADER);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByIsLeaderIsInShouldWork() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where isLeader in DEFAULT_IS_LEADER or UPDATED_IS_LEADER
        defaultCollaboratorShouldBeFound("isLeader.in=" + DEFAULT_IS_LEADER + "," + UPDATED_IS_LEADER);

        // Get all the collaboratorList where isLeader equals to UPDATED_IS_LEADER
        defaultCollaboratorShouldNotBeFound("isLeader.in=" + UPDATED_IS_LEADER);
    }

    @Test
    @Transactional
    void getAllCollaboratorsByIsLeaderIsNullOrNotNull() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        // Get all the collaboratorList where isLeader is not null
        defaultCollaboratorShouldBeFound("isLeader.specified=true");

        // Get all the collaboratorList where isLeader is null
        defaultCollaboratorShouldNotBeFound("isLeader.specified=false");
    }

    @Test
    @Transactional
    void getAllCollaboratorsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = collaborator.getUser();
        collaboratorRepository.saveAndFlush(collaborator);
        Long userId = user.getId();

        // Get all the collaboratorList where user equals to userId
        defaultCollaboratorShouldBeFound("userId.equals=" + userId);

        // Get all the collaboratorList where user equals to (userId + 1)
        defaultCollaboratorShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllCollaboratorsByLeaderIsEqualToSomething() throws Exception {
        Collaborator leader;
        if (TestUtil.findAll(em, Collaborator.class).isEmpty()) {
            collaboratorRepository.saveAndFlush(collaborator);
            leader = CollaboratorResourceIT.createEntity(em);
        } else {
            leader = TestUtil.findAll(em, Collaborator.class).get(0);
        }
        em.persist(leader);
        em.flush();
        collaborator.setLeader(leader);
        collaboratorRepository.saveAndFlush(collaborator);
        Long leaderId = leader.getId();

        // Get all the collaboratorList where leader equals to leaderId
        defaultCollaboratorShouldBeFound("leaderId.equals=" + leaderId);

        // Get all the collaboratorList where leader equals to (leaderId + 1)
        defaultCollaboratorShouldNotBeFound("leaderId.equals=" + (leaderId + 1));
    }

    @Test
    @Transactional
    void getAllCollaboratorsByProjectIsEqualToSomething() throws Exception {
        Project project;
        if (TestUtil.findAll(em, Project.class).isEmpty()) {
            collaboratorRepository.saveAndFlush(collaborator);
            project = ProjectResourceIT.createEntity(em);
        } else {
            project = TestUtil.findAll(em, Project.class).get(0);
        }
        em.persist(project);
        em.flush();
        collaborator.addProject(project);
        collaboratorRepository.saveAndFlush(collaborator);
        Long projectId = project.getId();

        // Get all the collaboratorList where project equals to projectId
        defaultCollaboratorShouldBeFound("projectId.equals=" + projectId);

        // Get all the collaboratorList where project equals to (projectId + 1)
        defaultCollaboratorShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCollaboratorShouldBeFound(String filter) throws Exception {
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaborator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].isLeader").value(hasItem(DEFAULT_IS_LEADER.booleanValue())));

        // Check, that the count call also returns 1
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCollaboratorShouldNotBeFound(String filter) throws Exception {
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCollaboratorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCollaborator() throws Exception {
        // Get the collaborator
        restCollaboratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Update the collaborator
        Collaborator updatedCollaborator = collaboratorRepository.findById(collaborator.getId()).get();
        // Disconnect from session so that the updates on updatedCollaborator are not directly saved in db
        em.detach(updatedCollaborator);
        updatedCollaborator
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .cpf(UPDATED_CPF)
            .isLeader(UPDATED_IS_LEADER);

        restCollaboratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCollaborator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCollaborator))
            )
            .andExpect(status().isOk());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollaborator.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testCollaborator.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCollaborator.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCollaborator.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testCollaborator.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
        assertThat(testCollaborator.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testCollaborator.getIsLeader()).isEqualTo(UPDATED_IS_LEADER);
    }

    @Test
    @Transactional
    void putNonExistingCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collaborator.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collaborator))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collaborator))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collaborator)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollaboratorWithPatch() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Update the collaborator using partial update
        Collaborator partialUpdatedCollaborator = new Collaborator();
        partialUpdatedCollaborator.setId(collaborator.getId());

        partialUpdatedCollaborator
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .cpf(UPDATED_CPF)
            .isLeader(UPDATED_IS_LEADER);

        restCollaboratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollaborator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollaborator))
            )
            .andExpect(status().isOk());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollaborator.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testCollaborator.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCollaborator.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCollaborator.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testCollaborator.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
        assertThat(testCollaborator.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testCollaborator.getIsLeader()).isEqualTo(UPDATED_IS_LEADER);
    }

    @Test
    @Transactional
    void fullUpdateCollaboratorWithPatch() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();

        // Update the collaborator using partial update
        Collaborator partialUpdatedCollaborator = new Collaborator();
        partialUpdatedCollaborator.setId(collaborator.getId());

        partialUpdatedCollaborator
            .name(UPDATED_NAME)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE)
            .cpf(UPDATED_CPF)
            .isLeader(UPDATED_IS_LEADER);

        restCollaboratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollaborator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollaborator))
            )
            .andExpect(status().isOk());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
        Collaborator testCollaborator = collaboratorList.get(collaboratorList.size() - 1);
        assertThat(testCollaborator.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollaborator.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testCollaborator.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCollaborator.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testCollaborator.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testCollaborator.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
        assertThat(testCollaborator.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testCollaborator.getIsLeader()).isEqualTo(UPDATED_IS_LEADER);
    }

    @Test
    @Transactional
    void patchNonExistingCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collaborator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collaborator))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collaborator))
            )
            .andExpect(status().isBadRequest());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollaborator() throws Exception {
        int databaseSizeBeforeUpdate = collaboratorRepository.findAll().size();
        collaborator.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollaboratorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(collaborator))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Collaborator in the database
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollaborator() throws Exception {
        // Initialize the database
        collaboratorRepository.saveAndFlush(collaborator);

        int databaseSizeBeforeDelete = collaboratorRepository.findAll().size();

        // Delete the collaborator
        restCollaboratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, collaborator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();
        assertThat(collaboratorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
