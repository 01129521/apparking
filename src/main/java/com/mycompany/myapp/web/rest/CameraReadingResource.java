package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CameraReadingRepository;
import com.mycompany.myapp.service.CameraReadingQueryService;
import com.mycompany.myapp.service.CameraReadingService;
import com.mycompany.myapp.service.criteria.CameraReadingCriteria;
import com.mycompany.myapp.service.dto.CameraReadingDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CameraReading}.
 */
@RestController
@RequestMapping("/api")
public class CameraReadingResource {

    private final Logger log = LoggerFactory.getLogger(CameraReadingResource.class);

    private final CameraReadingService cameraReadingService;

    private final CameraReadingRepository cameraReadingRepository;

    private final CameraReadingQueryService cameraReadingQueryService;

    public CameraReadingResource(
        CameraReadingService cameraReadingService,
        CameraReadingRepository cameraReadingRepository,
        CameraReadingQueryService cameraReadingQueryService
    ) {
        this.cameraReadingService = cameraReadingService;
        this.cameraReadingRepository = cameraReadingRepository;
        this.cameraReadingQueryService = cameraReadingQueryService;
    }

    /**
     * {@code GET  /camera-readings} : get all the cameraReadings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cameraReadings in body.
     */
    @GetMapping("/camera-readings")
    public ResponseEntity<List<CameraReadingDTO>> getAllCameraReadings(
        CameraReadingCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CameraReadings by criteria: {}", criteria);
        Page<CameraReadingDTO> page = cameraReadingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /camera-readings/count} : count all the cameraReadings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/camera-readings/count")
    public ResponseEntity<Long> countCameraReadings(CameraReadingCriteria criteria) {
        log.debug("REST request to count CameraReadings by criteria: {}", criteria);
        return ResponseEntity.ok().body(cameraReadingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /camera-readings/:id} : get the "id" cameraReading.
     *
     * @param id the id of the cameraReadingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cameraReadingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/camera-readings/{id}")
    public ResponseEntity<CameraReadingDTO> getCameraReading(@PathVariable Long id) {
        log.debug("REST request to get CameraReading : {}", id);
        Optional<CameraReadingDTO> cameraReadingDTO = cameraReadingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cameraReadingDTO);
    }
}
