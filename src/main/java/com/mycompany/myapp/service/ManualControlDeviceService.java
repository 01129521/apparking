package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ManualControlDevice;
import com.mycompany.myapp.repository.ManualControlDeviceRepository;
import com.mycompany.myapp.service.dto.ManualControlDeviceDTO;
import com.mycompany.myapp.service.mapper.ManualControlDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ManualControlDevice}.
 */
@Service
@Transactional
public class ManualControlDeviceService {

    private final Logger log = LoggerFactory.getLogger(ManualControlDeviceService.class);

    private final ManualControlDeviceRepository manualControlDeviceRepository;

    private final ManualControlDeviceMapper manualControlDeviceMapper;

    public ManualControlDeviceService(
        ManualControlDeviceRepository manualControlDeviceRepository,
        ManualControlDeviceMapper manualControlDeviceMapper
    ) {
        this.manualControlDeviceRepository = manualControlDeviceRepository;
        this.manualControlDeviceMapper = manualControlDeviceMapper;
    }

    /**
     * Save a manualControlDevice.
     *
     * @param manualControlDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    public ManualControlDeviceDTO save(ManualControlDeviceDTO manualControlDeviceDTO) {
        log.debug("Request to save ManualControlDevice : {}", manualControlDeviceDTO);
        ManualControlDevice manualControlDevice = manualControlDeviceMapper.toEntity(manualControlDeviceDTO);
        manualControlDevice = manualControlDeviceRepository.save(manualControlDevice);
        return manualControlDeviceMapper.toDto(manualControlDevice);
    }

    /**
     * Update a manualControlDevice.
     *
     * @param manualControlDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    public ManualControlDeviceDTO update(ManualControlDeviceDTO manualControlDeviceDTO) {
        log.debug("Request to save ManualControlDevice : {}", manualControlDeviceDTO);
        ManualControlDevice manualControlDevice = manualControlDeviceMapper.toEntity(manualControlDeviceDTO);
        manualControlDevice = manualControlDeviceRepository.save(manualControlDevice);
        return manualControlDeviceMapper.toDto(manualControlDevice);
    }

    /**
     * Partially update a manualControlDevice.
     *
     * @param manualControlDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ManualControlDeviceDTO> partialUpdate(ManualControlDeviceDTO manualControlDeviceDTO) {
        log.debug("Request to partially update ManualControlDevice : {}", manualControlDeviceDTO);

        return manualControlDeviceRepository
            .findById(manualControlDeviceDTO.getId())
            .map(existingManualControlDevice -> {
                manualControlDeviceMapper.partialUpdate(existingManualControlDevice, manualControlDeviceDTO);

                return existingManualControlDevice;
            })
            .map(manualControlDeviceRepository::save)
            .map(manualControlDeviceMapper::toDto);
    }

    /**
     * Get all the manualControlDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ManualControlDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManualControlDevices");
        return manualControlDeviceRepository.findAll(pageable).map(manualControlDeviceMapper::toDto);
    }

    /**
     * Get one manualControlDevice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ManualControlDeviceDTO> findOne(Long id) {
        log.debug("Request to get ManualControlDevice : {}", id);
        return manualControlDeviceRepository.findById(id).map(manualControlDeviceMapper::toDto);
    }

    /**
     * Delete the manualControlDevice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ManualControlDevice : {}", id);
        manualControlDeviceRepository.deleteById(id);
    }
}
