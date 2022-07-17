package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ParkingClient;
import com.mycompany.myapp.repository.ParkingClientRepository;
import com.mycompany.myapp.service.criteria.ParkingClientCriteria;
import com.mycompany.myapp.service.dto.ParkingClientDTO;
import com.mycompany.myapp.service.mapper.ParkingClientMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ParkingClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkingClientResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_PLATE_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE_NUMBERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parking-clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParkingClientRepository parkingClientRepository;

    @Autowired
    private ParkingClientMapper parkingClientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkingClientMockMvc;

    private ParkingClient parkingClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParkingClient createEntity(EntityManager em) {
        ParkingClient parkingClient = new ParkingClient()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .licensePlateNumbers(DEFAULT_LICENSE_PLATE_NUMBERS);
        return parkingClient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParkingClient createUpdatedEntity(EntityManager em) {
        ParkingClient parkingClient = new ParkingClient()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .licensePlateNumbers(UPDATED_LICENSE_PLATE_NUMBERS);
        return parkingClient;
    }

    @BeforeEach
    public void initTest() {
        parkingClient = createEntity(em);
    }

    @Test
    @Transactional
    void createParkingClient() throws Exception {
        int databaseSizeBeforeCreate = parkingClientRepository.findAll().size();
        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);
        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeCreate + 1);
        ParkingClient testParkingClient = parkingClientList.get(parkingClientList.size() - 1);
        assertThat(testParkingClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testParkingClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testParkingClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testParkingClient.getLicensePlateNumbers()).isEqualTo(DEFAULT_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void createParkingClientWithExistingId() throws Exception {
        // Create the ParkingClient with an existing ID
        parkingClient.setId(1L);
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        int databaseSizeBeforeCreate = parkingClientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingClientRepository.findAll().size();
        // set the field null
        parkingClient.setFirstName(null);

        // Create the ParkingClient, which fails.
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingClientRepository.findAll().size();
        // set the field null
        parkingClient.setLastName(null);

        // Create the ParkingClient, which fails.
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingClientRepository.findAll().size();
        // set the field null
        parkingClient.setPhoneNumber(null);

        // Create the ParkingClient, which fails.
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicensePlateNumbersIsRequired() throws Exception {
        int databaseSizeBeforeTest = parkingClientRepository.findAll().size();
        // set the field null
        parkingClient.setLicensePlateNumbers(null);

        // Create the ParkingClient, which fails.
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        restParkingClientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParkingClients() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parkingClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].licensePlateNumbers").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBERS)));
    }

    @Test
    @Transactional
    void getParkingClient() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get the parkingClient
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL_ID, parkingClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parkingClient.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.licensePlateNumbers").value(DEFAULT_LICENSE_PLATE_NUMBERS));
    }

    @Test
    @Transactional
    void getParkingClientsByIdFiltering() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        Long id = parkingClient.getId();

        defaultParkingClientShouldBeFound("id.equals=" + id);
        defaultParkingClientShouldNotBeFound("id.notEquals=" + id);

        defaultParkingClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParkingClientShouldNotBeFound("id.greaterThan=" + id);

        defaultParkingClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParkingClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName equals to DEFAULT_FIRST_NAME
        defaultParkingClientShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the parkingClientList where firstName equals to UPDATED_FIRST_NAME
        defaultParkingClientShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName not equals to DEFAULT_FIRST_NAME
        defaultParkingClientShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the parkingClientList where firstName not equals to UPDATED_FIRST_NAME
        defaultParkingClientShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultParkingClientShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the parkingClientList where firstName equals to UPDATED_FIRST_NAME
        defaultParkingClientShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName is not null
        defaultParkingClientShouldBeFound("firstName.specified=true");

        // Get all the parkingClientList where firstName is null
        defaultParkingClientShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName contains DEFAULT_FIRST_NAME
        defaultParkingClientShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the parkingClientList where firstName contains UPDATED_FIRST_NAME
        defaultParkingClientShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where firstName does not contain DEFAULT_FIRST_NAME
        defaultParkingClientShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the parkingClientList where firstName does not contain UPDATED_FIRST_NAME
        defaultParkingClientShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName equals to DEFAULT_LAST_NAME
        defaultParkingClientShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the parkingClientList where lastName equals to UPDATED_LAST_NAME
        defaultParkingClientShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName not equals to DEFAULT_LAST_NAME
        defaultParkingClientShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the parkingClientList where lastName not equals to UPDATED_LAST_NAME
        defaultParkingClientShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultParkingClientShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the parkingClientList where lastName equals to UPDATED_LAST_NAME
        defaultParkingClientShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName is not null
        defaultParkingClientShouldBeFound("lastName.specified=true");

        // Get all the parkingClientList where lastName is null
        defaultParkingClientShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName contains DEFAULT_LAST_NAME
        defaultParkingClientShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the parkingClientList where lastName contains UPDATED_LAST_NAME
        defaultParkingClientShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where lastName does not contain DEFAULT_LAST_NAME
        defaultParkingClientShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the parkingClientList where lastName does not contain UPDATED_LAST_NAME
        defaultParkingClientShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultParkingClientShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the parkingClientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultParkingClientShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultParkingClientShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the parkingClientList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultParkingClientShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultParkingClientShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the parkingClientList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultParkingClientShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber is not null
        defaultParkingClientShouldBeFound("phoneNumber.specified=true");

        // Get all the parkingClientList where phoneNumber is null
        defaultParkingClientShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultParkingClientShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the parkingClientList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultParkingClientShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParkingClientsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultParkingClientShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the parkingClientList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultParkingClientShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers equals to DEFAULT_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldBeFound("licensePlateNumbers.equals=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the parkingClientList where licensePlateNumbers equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.equals=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers not equals to DEFAULT_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.notEquals=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the parkingClientList where licensePlateNumbers not equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldBeFound("licensePlateNumbers.notEquals=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers in DEFAULT_LICENSE_PLATE_NUMBERS or UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldBeFound("licensePlateNumbers.in=" + DEFAULT_LICENSE_PLATE_NUMBERS + "," + UPDATED_LICENSE_PLATE_NUMBERS);

        // Get all the parkingClientList where licensePlateNumbers equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.in=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers is not null
        defaultParkingClientShouldBeFound("licensePlateNumbers.specified=true");

        // Get all the parkingClientList where licensePlateNumbers is null
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers contains DEFAULT_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldBeFound("licensePlateNumbers.contains=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the parkingClientList where licensePlateNumbers contains UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.contains=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllParkingClientsByLicensePlateNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        // Get all the parkingClientList where licensePlateNumbers does not contain DEFAULT_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldNotBeFound("licensePlateNumbers.doesNotContain=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the parkingClientList where licensePlateNumbers does not contain UPDATED_LICENSE_PLATE_NUMBERS
        defaultParkingClientShouldBeFound("licensePlateNumbers.doesNotContain=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParkingClientShouldBeFound(String filter) throws Exception {
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parkingClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].licensePlateNumbers").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBERS)));

        // Check, that the count call also returns 1
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParkingClientShouldNotBeFound(String filter) throws Exception {
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParkingClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParkingClient() throws Exception {
        // Get the parkingClient
        restParkingClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParkingClient() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();

        // Update the parkingClient
        ParkingClient updatedParkingClient = parkingClientRepository.findById(parkingClient.getId()).get();
        // Disconnect from session so that the updates on updatedParkingClient are not directly saved in db
        em.detach(updatedParkingClient);
        updatedParkingClient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .licensePlateNumbers(UPDATED_LICENSE_PLATE_NUMBERS);
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(updatedParkingClient);

        restParkingClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkingClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isOk());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
        ParkingClient testParkingClient = parkingClientList.get(parkingClientList.size() - 1);
        assertThat(testParkingClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testParkingClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testParkingClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParkingClient.getLicensePlateNumbers()).isEqualTo(UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void putNonExistingParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkingClientDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkingClientWithPatch() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();

        // Update the parkingClient using partial update
        ParkingClient partialUpdatedParkingClient = new ParkingClient();
        partialUpdatedParkingClient.setId(parkingClient.getId());

        partialUpdatedParkingClient.licensePlateNumbers(UPDATED_LICENSE_PLATE_NUMBERS);

        restParkingClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParkingClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParkingClient))
            )
            .andExpect(status().isOk());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
        ParkingClient testParkingClient = parkingClientList.get(parkingClientList.size() - 1);
        assertThat(testParkingClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testParkingClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testParkingClient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testParkingClient.getLicensePlateNumbers()).isEqualTo(UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void fullUpdateParkingClientWithPatch() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();

        // Update the parkingClient using partial update
        ParkingClient partialUpdatedParkingClient = new ParkingClient();
        partialUpdatedParkingClient.setId(parkingClient.getId());

        partialUpdatedParkingClient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .licensePlateNumbers(UPDATED_LICENSE_PLATE_NUMBERS);

        restParkingClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParkingClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParkingClient))
            )
            .andExpect(status().isOk());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
        ParkingClient testParkingClient = parkingClientList.get(parkingClientList.size() - 1);
        assertThat(testParkingClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testParkingClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testParkingClient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParkingClient.getLicensePlateNumbers()).isEqualTo(UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void patchNonExistingParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parkingClientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParkingClient() throws Exception {
        int databaseSizeBeforeUpdate = parkingClientRepository.findAll().size();
        parkingClient.setId(count.incrementAndGet());

        // Create the ParkingClient
        ParkingClientDTO parkingClientDTO = parkingClientMapper.toDto(parkingClient);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkingClientMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkingClientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParkingClient in the database
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParkingClient() throws Exception {
        // Initialize the database
        parkingClientRepository.saveAndFlush(parkingClient);

        int databaseSizeBeforeDelete = parkingClientRepository.findAll().size();

        // Delete the parkingClient
        restParkingClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, parkingClient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParkingClient> parkingClientList = parkingClientRepository.findAll();
        assertThat(parkingClientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
