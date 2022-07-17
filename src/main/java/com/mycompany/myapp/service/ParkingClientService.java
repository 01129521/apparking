package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ParkingClient;
import com.mycompany.myapp.repository.ParkingClientRepository;
import com.mycompany.myapp.service.dto.ParkingClientDTO;
import com.mycompany.myapp.service.mapper.ParkingClientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParkingClient}.
 */
@Service
@Transactional
public class ParkingClientService {

    private final Logger log = LoggerFactory.getLogger(ParkingClientService.class);

    private final ParkingClientRepository parkingClientRepository;

    private final ParkingClientMapper parkingClientMapper;

    public ParkingClientService(ParkingClientRepository parkingClientRepository, ParkingClientMapper parkingClientMapper) {
        this.parkingClientRepository = parkingClientRepository;
        this.parkingClientMapper = parkingClientMapper;
    }

    /**
     * Save a parkingClient.
     *
     * @param parkingClientDTO the entity to save.
     * @return the persisted entity.
     */
    public ParkingClientDTO save(ParkingClientDTO parkingClientDTO) {
        log.debug("Request to save ParkingClient : {}", parkingClientDTO);
        ParkingClient parkingClient = parkingClientMapper.toEntity(parkingClientDTO);
        parkingClient = parkingClientRepository.save(parkingClient);
        return parkingClientMapper.toDto(parkingClient);
    }

    /**
     * Update a parkingClient.
     *
     * @param parkingClientDTO the entity to save.
     * @return the persisted entity.
     */
    public ParkingClientDTO update(ParkingClientDTO parkingClientDTO) {
        log.debug("Request to save ParkingClient : {}", parkingClientDTO);
        ParkingClient parkingClient = parkingClientMapper.toEntity(parkingClientDTO);
        parkingClient = parkingClientRepository.save(parkingClient);
        return parkingClientMapper.toDto(parkingClient);
    }

    /**
     * Partially update a parkingClient.
     *
     * @param parkingClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParkingClientDTO> partialUpdate(ParkingClientDTO parkingClientDTO) {
        log.debug("Request to partially update ParkingClient : {}", parkingClientDTO);

        return parkingClientRepository
            .findById(parkingClientDTO.getId())
            .map(existingParkingClient -> {
                parkingClientMapper.partialUpdate(existingParkingClient, parkingClientDTO);

                return existingParkingClient;
            })
            .map(parkingClientRepository::save)
            .map(parkingClientMapper::toDto);
    }

    /**
     * Get all the parkingClients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParkingClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ParkingClients");
        return parkingClientRepository.findAll(pageable).map(parkingClientMapper::toDto);
    }

    /**
     * Get one parkingClient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParkingClientDTO> findOne(Long id) {
        log.debug("Request to get ParkingClient : {}", id);
        return parkingClientRepository.findById(id).map(parkingClientMapper::toDto);
    }

    /**
     * Delete the parkingClient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParkingClient : {}", id);
        parkingClientRepository.deleteById(id);
    }
}
