package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CameraReading;
import com.mycompany.myapp.repository.CameraReadingRepository;
import com.mycompany.myapp.service.criteria.CameraReadingCriteria;
import com.mycompany.myapp.service.dto.CameraReadingDTO;
import com.mycompany.myapp.service.mapper.CameraReadingMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CameraReadingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CameraReadingResourceIT {

    private static final String DEFAULT_CAMERA_READING_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CAMERA_READING_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_PLATE_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_PLATE_NUMBERS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LICENSE_PLATE_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LICENSE_PLATE_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LICENSE_PLATE_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LICENSE_PLATE_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/camera-readings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CameraReadingRepository cameraReadingRepository;

    @Autowired
    private CameraReadingMapper cameraReadingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCameraReadingMockMvc;

    private CameraReading cameraReading;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CameraReading createEntity(EntityManager em) {
        CameraReading cameraReading = new CameraReading()
            .cameraReadingDate(DEFAULT_CAMERA_READING_DATE)
            .licensePlateNumbers(DEFAULT_LICENSE_PLATE_NUMBERS)
            .licensePlatePhoto(DEFAULT_LICENSE_PLATE_PHOTO)
            .licensePlatePhotoContentType(DEFAULT_LICENSE_PLATE_PHOTO_CONTENT_TYPE);
        return cameraReading;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CameraReading createUpdatedEntity(EntityManager em) {
        CameraReading cameraReading = new CameraReading()
            .cameraReadingDate(UPDATED_CAMERA_READING_DATE)
            .licensePlateNumbers(UPDATED_LICENSE_PLATE_NUMBERS)
            .licensePlatePhoto(UPDATED_LICENSE_PLATE_PHOTO)
            .licensePlatePhotoContentType(UPDATED_LICENSE_PLATE_PHOTO_CONTENT_TYPE);
        return cameraReading;
    }

    @BeforeEach
    public void initTest() {
        cameraReading = createEntity(em);
    }

    @Test
    @Transactional
    void getAllCameraReadings() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cameraReading.getId().intValue())))
            .andExpect(jsonPath("$.[*].cameraReadingDate").value(hasItem(DEFAULT_CAMERA_READING_DATE)))
            .andExpect(jsonPath("$.[*].licensePlateNumbers").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBERS)))
            .andExpect(jsonPath("$.[*].licensePlatePhotoContentType").value(hasItem(DEFAULT_LICENSE_PLATE_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].licensePlatePhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_LICENSE_PLATE_PHOTO))));
    }

    @Test
    @Transactional
    void getCameraReading() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get the cameraReading
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL_ID, cameraReading.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cameraReading.getId().intValue()))
            .andExpect(jsonPath("$.cameraReadingDate").value(DEFAULT_CAMERA_READING_DATE))
            .andExpect(jsonPath("$.licensePlateNumbers").value(DEFAULT_LICENSE_PLATE_NUMBERS))
            .andExpect(jsonPath("$.licensePlatePhotoContentType").value(DEFAULT_LICENSE_PLATE_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.licensePlatePhoto").value(Base64Utils.encodeToString(DEFAULT_LICENSE_PLATE_PHOTO)));
    }

    @Test
    @Transactional
    void getCameraReadingsByIdFiltering() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        Long id = cameraReading.getId();

        defaultCameraReadingShouldBeFound("id.equals=" + id);
        defaultCameraReadingShouldNotBeFound("id.notEquals=" + id);

        defaultCameraReadingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCameraReadingShouldNotBeFound("id.greaterThan=" + id);

        defaultCameraReadingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCameraReadingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate equals to DEFAULT_CAMERA_READING_DATE
        defaultCameraReadingShouldBeFound("cameraReadingDate.equals=" + DEFAULT_CAMERA_READING_DATE);

        // Get all the cameraReadingList where cameraReadingDate equals to UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.equals=" + UPDATED_CAMERA_READING_DATE);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate not equals to DEFAULT_CAMERA_READING_DATE
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.notEquals=" + DEFAULT_CAMERA_READING_DATE);

        // Get all the cameraReadingList where cameraReadingDate not equals to UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldBeFound("cameraReadingDate.notEquals=" + UPDATED_CAMERA_READING_DATE);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateIsInShouldWork() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate in DEFAULT_CAMERA_READING_DATE or UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldBeFound("cameraReadingDate.in=" + DEFAULT_CAMERA_READING_DATE + "," + UPDATED_CAMERA_READING_DATE);

        // Get all the cameraReadingList where cameraReadingDate equals to UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.in=" + UPDATED_CAMERA_READING_DATE);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate is not null
        defaultCameraReadingShouldBeFound("cameraReadingDate.specified=true");

        // Get all the cameraReadingList where cameraReadingDate is null
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateContainsSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate contains DEFAULT_CAMERA_READING_DATE
        defaultCameraReadingShouldBeFound("cameraReadingDate.contains=" + DEFAULT_CAMERA_READING_DATE);

        // Get all the cameraReadingList where cameraReadingDate contains UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.contains=" + UPDATED_CAMERA_READING_DATE);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByCameraReadingDateNotContainsSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where cameraReadingDate does not contain DEFAULT_CAMERA_READING_DATE
        defaultCameraReadingShouldNotBeFound("cameraReadingDate.doesNotContain=" + DEFAULT_CAMERA_READING_DATE);

        // Get all the cameraReadingList where cameraReadingDate does not contain UPDATED_CAMERA_READING_DATE
        defaultCameraReadingShouldBeFound("cameraReadingDate.doesNotContain=" + UPDATED_CAMERA_READING_DATE);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers equals to DEFAULT_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldBeFound("licensePlateNumbers.equals=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the cameraReadingList where licensePlateNumbers equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.equals=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers not equals to DEFAULT_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.notEquals=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the cameraReadingList where licensePlateNumbers not equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldBeFound("licensePlateNumbers.notEquals=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers in DEFAULT_LICENSE_PLATE_NUMBERS or UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldBeFound("licensePlateNumbers.in=" + DEFAULT_LICENSE_PLATE_NUMBERS + "," + UPDATED_LICENSE_PLATE_NUMBERS);

        // Get all the cameraReadingList where licensePlateNumbers equals to UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.in=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers is not null
        defaultCameraReadingShouldBeFound("licensePlateNumbers.specified=true");

        // Get all the cameraReadingList where licensePlateNumbers is null
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersContainsSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers contains DEFAULT_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldBeFound("licensePlateNumbers.contains=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the cameraReadingList where licensePlateNumbers contains UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.contains=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    @Test
    @Transactional
    void getAllCameraReadingsByLicensePlateNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        cameraReadingRepository.saveAndFlush(cameraReading);

        // Get all the cameraReadingList where licensePlateNumbers does not contain DEFAULT_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldNotBeFound("licensePlateNumbers.doesNotContain=" + DEFAULT_LICENSE_PLATE_NUMBERS);

        // Get all the cameraReadingList where licensePlateNumbers does not contain UPDATED_LICENSE_PLATE_NUMBERS
        defaultCameraReadingShouldBeFound("licensePlateNumbers.doesNotContain=" + UPDATED_LICENSE_PLATE_NUMBERS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCameraReadingShouldBeFound(String filter) throws Exception {
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cameraReading.getId().intValue())))
            .andExpect(jsonPath("$.[*].cameraReadingDate").value(hasItem(DEFAULT_CAMERA_READING_DATE)))
            .andExpect(jsonPath("$.[*].licensePlateNumbers").value(hasItem(DEFAULT_LICENSE_PLATE_NUMBERS)))
            .andExpect(jsonPath("$.[*].licensePlatePhotoContentType").value(hasItem(DEFAULT_LICENSE_PLATE_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].licensePlatePhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_LICENSE_PLATE_PHOTO))));

        // Check, that the count call also returns 1
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCameraReadingShouldNotBeFound(String filter) throws Exception {
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCameraReadingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCameraReading() throws Exception {
        // Get the cameraReading
        restCameraReadingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
