package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ManualControlDevice;
import com.mycompany.myapp.repository.ManualControlDeviceRepository;
import com.mycompany.myapp.service.criteria.ManualControlDeviceCriteria;
import com.mycompany.myapp.service.dto.ManualControlDeviceDTO;
import com.mycompany.myapp.service.mapper.ManualControlDeviceMapper;
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
 * Integration tests for the {@link ManualControlDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManualControlDeviceResourceIT {

    private static final String DEFAULT_DEVICE = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATE = false;
    private static final Boolean UPDATED_STATE = true;

    private static final String ENTITY_API_URL = "/api/manual-control-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManualControlDeviceRepository manualControlDeviceRepository;

    @Autowired
    private ManualControlDeviceMapper manualControlDeviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManualControlDeviceMockMvc;

    private ManualControlDevice manualControlDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualControlDevice createEntity(EntityManager em) {
        ManualControlDevice manualControlDevice = new ManualControlDevice().device(DEFAULT_DEVICE).state(DEFAULT_STATE);
        return manualControlDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualControlDevice createUpdatedEntity(EntityManager em) {
        ManualControlDevice manualControlDevice = new ManualControlDevice().device(UPDATED_DEVICE).state(UPDATED_STATE);
        return manualControlDevice;
    }

    @BeforeEach
    public void initTest() {
        manualControlDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createManualControlDevice() throws Exception {
        int databaseSizeBeforeCreate = manualControlDeviceRepository.findAll().size();
        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);
        restManualControlDeviceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        ManualControlDevice testManualControlDevice = manualControlDeviceList.get(manualControlDeviceList.size() - 1);
        assertThat(testManualControlDevice.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testManualControlDevice.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createManualControlDeviceWithExistingId() throws Exception {
        // Create the ManualControlDevice with an existing ID
        manualControlDevice.setId(1L);
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        int databaseSizeBeforeCreate = manualControlDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualControlDeviceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeviceIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualControlDeviceRepository.findAll().size();
        // set the field null
        manualControlDevice.setDevice(null);

        // Create the ManualControlDevice, which fails.
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        restManualControlDeviceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManualControlDevices() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualControlDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));
    }

    @Test
    @Transactional
    void getManualControlDevice() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get the manualControlDevice
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, manualControlDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manualControlDevice.getId().intValue()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.booleanValue()));
    }

    @Test
    @Transactional
    void getManualControlDevicesByIdFiltering() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        Long id = manualControlDevice.getId();

        defaultManualControlDeviceShouldBeFound("id.equals=" + id);
        defaultManualControlDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultManualControlDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultManualControlDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultManualControlDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultManualControlDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device equals to DEFAULT_DEVICE
        defaultManualControlDeviceShouldBeFound("device.equals=" + DEFAULT_DEVICE);

        // Get all the manualControlDeviceList where device equals to UPDATED_DEVICE
        defaultManualControlDeviceShouldNotBeFound("device.equals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device not equals to DEFAULT_DEVICE
        defaultManualControlDeviceShouldNotBeFound("device.notEquals=" + DEFAULT_DEVICE);

        // Get all the manualControlDeviceList where device not equals to UPDATED_DEVICE
        defaultManualControlDeviceShouldBeFound("device.notEquals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device in DEFAULT_DEVICE or UPDATED_DEVICE
        defaultManualControlDeviceShouldBeFound("device.in=" + DEFAULT_DEVICE + "," + UPDATED_DEVICE);

        // Get all the manualControlDeviceList where device equals to UPDATED_DEVICE
        defaultManualControlDeviceShouldNotBeFound("device.in=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device is not null
        defaultManualControlDeviceShouldBeFound("device.specified=true");

        // Get all the manualControlDeviceList where device is null
        defaultManualControlDeviceShouldNotBeFound("device.specified=false");
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceContainsSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device contains DEFAULT_DEVICE
        defaultManualControlDeviceShouldBeFound("device.contains=" + DEFAULT_DEVICE);

        // Get all the manualControlDeviceList where device contains UPDATED_DEVICE
        defaultManualControlDeviceShouldNotBeFound("device.contains=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByDeviceNotContainsSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where device does not contain DEFAULT_DEVICE
        defaultManualControlDeviceShouldNotBeFound("device.doesNotContain=" + DEFAULT_DEVICE);

        // Get all the manualControlDeviceList where device does not contain UPDATED_DEVICE
        defaultManualControlDeviceShouldBeFound("device.doesNotContain=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where state equals to DEFAULT_STATE
        defaultManualControlDeviceShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the manualControlDeviceList where state equals to UPDATED_STATE
        defaultManualControlDeviceShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where state not equals to DEFAULT_STATE
        defaultManualControlDeviceShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the manualControlDeviceList where state not equals to UPDATED_STATE
        defaultManualControlDeviceShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where state in DEFAULT_STATE or UPDATED_STATE
        defaultManualControlDeviceShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the manualControlDeviceList where state equals to UPDATED_STATE
        defaultManualControlDeviceShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllManualControlDevicesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        // Get all the manualControlDeviceList where state is not null
        defaultManualControlDeviceShouldBeFound("state.specified=true");

        // Get all the manualControlDeviceList where state is null
        defaultManualControlDeviceShouldNotBeFound("state.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManualControlDeviceShouldBeFound(String filter) throws Exception {
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualControlDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.booleanValue())));

        // Check, that the count call also returns 1
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManualControlDeviceShouldNotBeFound(String filter) throws Exception {
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManualControlDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingManualControlDevice() throws Exception {
        // Get the manualControlDevice
        restManualControlDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManualControlDevice() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();

        // Update the manualControlDevice
        ManualControlDevice updatedManualControlDevice = manualControlDeviceRepository.findById(manualControlDevice.getId()).get();
        // Disconnect from session so that the updates on updatedManualControlDevice are not directly saved in db
        em.detach(updatedManualControlDevice);
        updatedManualControlDevice.device(UPDATED_DEVICE).state(UPDATED_STATE);
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(updatedManualControlDevice);

        restManualControlDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualControlDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
        ManualControlDevice testManualControlDevice = manualControlDeviceList.get(manualControlDeviceList.size() - 1);
        assertThat(testManualControlDevice.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testManualControlDevice.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualControlDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManualControlDeviceWithPatch() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();

        // Update the manualControlDevice using partial update
        ManualControlDevice partialUpdatedManualControlDevice = new ManualControlDevice();
        partialUpdatedManualControlDevice.setId(manualControlDevice.getId());

        partialUpdatedManualControlDevice.device(UPDATED_DEVICE).state(UPDATED_STATE);

        restManualControlDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualControlDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualControlDevice))
            )
            .andExpect(status().isOk());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
        ManualControlDevice testManualControlDevice = manualControlDeviceList.get(manualControlDeviceList.size() - 1);
        assertThat(testManualControlDevice.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testManualControlDevice.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void fullUpdateManualControlDeviceWithPatch() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();

        // Update the manualControlDevice using partial update
        ManualControlDevice partialUpdatedManualControlDevice = new ManualControlDevice();
        partialUpdatedManualControlDevice.setId(manualControlDevice.getId());

        partialUpdatedManualControlDevice.device(UPDATED_DEVICE).state(UPDATED_STATE);

        restManualControlDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualControlDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualControlDevice))
            )
            .andExpect(status().isOk());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
        ManualControlDevice testManualControlDevice = manualControlDeviceList.get(manualControlDeviceList.size() - 1);
        assertThat(testManualControlDevice.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testManualControlDevice.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualControlDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManualControlDevice() throws Exception {
        int databaseSizeBeforeUpdate = manualControlDeviceRepository.findAll().size();
        manualControlDevice.setId(count.incrementAndGet());

        // Create the ManualControlDevice
        ManualControlDeviceDTO manualControlDeviceDTO = manualControlDeviceMapper.toDto(manualControlDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualControlDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualControlDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualControlDevice in the database
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManualControlDevice() throws Exception {
        // Initialize the database
        manualControlDeviceRepository.saveAndFlush(manualControlDevice);

        int databaseSizeBeforeDelete = manualControlDeviceRepository.findAll().size();

        // Delete the manualControlDevice
        restManualControlDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, manualControlDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManualControlDevice> manualControlDeviceList = manualControlDeviceRepository.findAll();
        assertThat(manualControlDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
