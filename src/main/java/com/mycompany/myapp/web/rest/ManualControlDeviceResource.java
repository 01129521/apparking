package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ManualControlDeviceRepository;
import com.mycompany.myapp.service.ManualControlDeviceQueryService;
import com.mycompany.myapp.service.ManualControlDeviceService;
import com.mycompany.myapp.service.criteria.ManualControlDeviceCriteria;
import com.mycompany.myapp.service.dto.ManualControlDeviceDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ManualControlDevice}.
 */
@RestController
@RequestMapping("/api")
public class ManualControlDeviceResource {

    private final Logger log = LoggerFactory.getLogger(ManualControlDeviceResource.class);

    private static final String ENTITY_NAME = "manualControlDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualControlDeviceService manualControlDeviceService;

    private final ManualControlDeviceRepository manualControlDeviceRepository;

    private final ManualControlDeviceQueryService manualControlDeviceQueryService;

    public ManualControlDeviceResource(
        ManualControlDeviceService manualControlDeviceService,
        ManualControlDeviceRepository manualControlDeviceRepository,
        ManualControlDeviceQueryService manualControlDeviceQueryService
    ) {
        this.manualControlDeviceService = manualControlDeviceService;
        this.manualControlDeviceRepository = manualControlDeviceRepository;
        this.manualControlDeviceQueryService = manualControlDeviceQueryService;
    }

    /**
     * {@code POST  /manual-control-devices} : Create a new manualControlDevice.
     *
     * @param manualControlDeviceDTO the manualControlDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualControlDeviceDTO, or with status {@code 400 (Bad Request)} if the manualControlDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manual-control-devices")
    public ResponseEntity<ManualControlDeviceDTO> createManualControlDevice(
        @Valid @RequestBody ManualControlDeviceDTO manualControlDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ManualControlDevice : {}", manualControlDeviceDTO);
        if (manualControlDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualControlDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualControlDeviceDTO result = manualControlDeviceService.save(manualControlDeviceDTO);
        return ResponseEntity
            .created(new URI("/api/manual-control-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manual-control-devices/:id} : Updates an existing manualControlDevice.
     *
     * @param id the id of the manualControlDeviceDTO to save.
     * @param manualControlDeviceDTO the manualControlDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualControlDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the manualControlDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualControlDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manual-control-devices/{id}")
    public ResponseEntity<ManualControlDeviceDTO> updateManualControlDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManualControlDeviceDTO manualControlDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManualControlDevice : {}, {}", id, manualControlDeviceDTO);
        if (manualControlDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualControlDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualControlDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManualControlDeviceDTO result = manualControlDeviceService.update(manualControlDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualControlDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /manual-control-devices/:id} : Partial updates given fields of an existing manualControlDevice, field will ignore if it is null
     *
     * @param id the id of the manualControlDeviceDTO to save.
     * @param manualControlDeviceDTO the manualControlDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualControlDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the manualControlDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manualControlDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualControlDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manual-control-devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualControlDeviceDTO> partialUpdateManualControlDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManualControlDeviceDTO manualControlDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManualControlDevice partially : {}, {}", id, manualControlDeviceDTO);
        if (manualControlDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualControlDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualControlDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualControlDeviceDTO> result = manualControlDeviceService.partialUpdate(manualControlDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualControlDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /manual-control-devices} : get all the manualControlDevices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualControlDevices in body.
     */
    @GetMapping("/manual-control-devices")
    public ResponseEntity<List<ManualControlDeviceDTO>> getAllManualControlDevices(
        ManualControlDeviceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ManualControlDevices by criteria: {}", criteria);
        Page<ManualControlDeviceDTO> page = manualControlDeviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-control-devices/count} : count all the manualControlDevices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/manual-control-devices/count")
    public ResponseEntity<Long> countManualControlDevices(ManualControlDeviceCriteria criteria) {
        log.debug("REST request to count ManualControlDevices by criteria: {}", criteria);
        return ResponseEntity.ok().body(manualControlDeviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /manual-control-devices/:id} : get the "id" manualControlDevice.
     *
     * @param id the id of the manualControlDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualControlDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manual-control-devices/{id}")
    public ResponseEntity<ManualControlDeviceDTO> getManualControlDevice(@PathVariable Long id) {
        log.debug("REST request to get ManualControlDevice : {}", id);
        Optional<ManualControlDeviceDTO> manualControlDeviceDTO = manualControlDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualControlDeviceDTO);
    }

    /**
     * {@code DELETE  /manual-control-devices/:id} : delete the "id" manualControlDevice.
     *
     * @param id the id of the manualControlDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manual-control-devices/{id}")
    public ResponseEntity<Void> deleteManualControlDevice(@PathVariable Long id) {
        log.debug("REST request to delete ManualControlDevice : {}", id);
        manualControlDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
