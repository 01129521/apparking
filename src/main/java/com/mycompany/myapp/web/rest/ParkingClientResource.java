package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ParkingClientRepository;
import com.mycompany.myapp.service.ParkingClientQueryService;
import com.mycompany.myapp.service.ParkingClientService;
import com.mycompany.myapp.service.criteria.ParkingClientCriteria;
import com.mycompany.myapp.service.dto.ParkingClientDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ParkingClient}.
 */
@RestController
@RequestMapping("/api")
public class ParkingClientResource {

    private final Logger log = LoggerFactory.getLogger(ParkingClientResource.class);

    private static final String ENTITY_NAME = "parkingClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParkingClientService parkingClientService;

    private final ParkingClientRepository parkingClientRepository;

    private final ParkingClientQueryService parkingClientQueryService;

    public ParkingClientResource(
        ParkingClientService parkingClientService,
        ParkingClientRepository parkingClientRepository,
        ParkingClientQueryService parkingClientQueryService
    ) {
        this.parkingClientService = parkingClientService;
        this.parkingClientRepository = parkingClientRepository;
        this.parkingClientQueryService = parkingClientQueryService;
    }

    /**
     * {@code POST  /parking-clients} : Create a new parkingClient.
     *
     * @param parkingClientDTO the parkingClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parkingClientDTO, or with status {@code 400 (Bad Request)} if the parkingClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parking-clients")
    public ResponseEntity<ParkingClientDTO> createParkingClient(@Valid @RequestBody ParkingClientDTO parkingClientDTO)
        throws URISyntaxException {
        log.debug("REST request to save ParkingClient : {}", parkingClientDTO);
        if (parkingClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new parkingClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParkingClientDTO result = parkingClientService.save(parkingClientDTO);
        return ResponseEntity
            .created(new URI("/api/parking-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parking-clients/:id} : Updates an existing parkingClient.
     *
     * @param id the id of the parkingClientDTO to save.
     * @param parkingClientDTO the parkingClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkingClientDTO,
     * or with status {@code 400 (Bad Request)} if the parkingClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parkingClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parking-clients/{id}")
    public ResponseEntity<ParkingClientDTO> updateParkingClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParkingClientDTO parkingClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParkingClient : {}, {}", id, parkingClientDTO);
        if (parkingClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkingClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkingClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParkingClientDTO result = parkingClientService.update(parkingClientDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parkingClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parking-clients/:id} : Partial updates given fields of an existing parkingClient, field will ignore if it is null
     *
     * @param id the id of the parkingClientDTO to save.
     * @param parkingClientDTO the parkingClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parkingClientDTO,
     * or with status {@code 400 (Bad Request)} if the parkingClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parkingClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parkingClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parking-clients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParkingClientDTO> partialUpdateParkingClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParkingClientDTO parkingClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParkingClient partially : {}, {}", id, parkingClientDTO);
        if (parkingClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parkingClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parkingClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParkingClientDTO> result = parkingClientService.partialUpdate(parkingClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parkingClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /parking-clients} : get all the parkingClients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parkingClients in body.
     */
    @GetMapping("/parking-clients")
    public ResponseEntity<List<ParkingClientDTO>> getAllParkingClients(
        ParkingClientCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ParkingClients by criteria: {}", criteria);
        Page<ParkingClientDTO> page = parkingClientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parking-clients/count} : count all the parkingClients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parking-clients/count")
    public ResponseEntity<Long> countParkingClients(ParkingClientCriteria criteria) {
        log.debug("REST request to count ParkingClients by criteria: {}", criteria);
        return ResponseEntity.ok().body(parkingClientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parking-clients/:id} : get the "id" parkingClient.
     *
     * @param id the id of the parkingClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parkingClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parking-clients/{id}")
    public ResponseEntity<ParkingClientDTO> getParkingClient(@PathVariable Long id) {
        log.debug("REST request to get ParkingClient : {}", id);
        Optional<ParkingClientDTO> parkingClientDTO = parkingClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parkingClientDTO);
    }

    /**
     * {@code DELETE  /parking-clients/:id} : delete the "id" parkingClient.
     *
     * @param id the id of the parkingClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parking-clients/{id}")
    public ResponseEntity<Void> deleteParkingClient(@PathVariable Long id) {
        log.debug("REST request to delete ParkingClient : {}", id);
        parkingClientService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
