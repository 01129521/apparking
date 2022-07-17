package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CameraReading;
import com.mycompany.myapp.repository.CameraReadingRepository;
import com.mycompany.myapp.service.dto.CameraReadingDTO;
import com.mycompany.myapp.service.mapper.CameraReadingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CameraReading}.
 */
@Service
@Transactional
public class CameraReadingService {

    private final Logger log = LoggerFactory.getLogger(CameraReadingService.class);

    private final CameraReadingRepository cameraReadingRepository;

    private final CameraReadingMapper cameraReadingMapper;

    public CameraReadingService(CameraReadingRepository cameraReadingRepository, CameraReadingMapper cameraReadingMapper) {
        this.cameraReadingRepository = cameraReadingRepository;
        this.cameraReadingMapper = cameraReadingMapper;
    }

    /**
     * Save a cameraReading.
     *
     * @param cameraReadingDTO the entity to save.
     * @return the persisted entity.
     */
    public CameraReadingDTO save(CameraReadingDTO cameraReadingDTO) {
        log.debug("Request to save CameraReading : {}", cameraReadingDTO);
        CameraReading cameraReading = cameraReadingMapper.toEntity(cameraReadingDTO);
        cameraReading = cameraReadingRepository.save(cameraReading);
        return cameraReadingMapper.toDto(cameraReading);
    }

    /**
     * Update a cameraReading.
     *
     * @param cameraReadingDTO the entity to save.
     * @return the persisted entity.
     */
    public CameraReadingDTO update(CameraReadingDTO cameraReadingDTO) {
        log.debug("Request to save CameraReading : {}", cameraReadingDTO);
        CameraReading cameraReading = cameraReadingMapper.toEntity(cameraReadingDTO);
        cameraReading = cameraReadingRepository.save(cameraReading);
        return cameraReadingMapper.toDto(cameraReading);
    }

    /**
     * Partially update a cameraReading.
     *
     * @param cameraReadingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CameraReadingDTO> partialUpdate(CameraReadingDTO cameraReadingDTO) {
        log.debug("Request to partially update CameraReading : {}", cameraReadingDTO);

        return cameraReadingRepository
            .findById(cameraReadingDTO.getId())
            .map(existingCameraReading -> {
                cameraReadingMapper.partialUpdate(existingCameraReading, cameraReadingDTO);

                return existingCameraReading;
            })
            .map(cameraReadingRepository::save)
            .map(cameraReadingMapper::toDto);
    }

    /**
     * Get all the cameraReadings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CameraReadingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CameraReadings");
        return cameraReadingRepository.findAll(pageable).map(cameraReadingMapper::toDto);
    }

    /**
     * Get one cameraReading by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CameraReadingDTO> findOne(Long id) {
        log.debug("Request to get CameraReading : {}", id);
        return cameraReadingRepository.findById(id).map(cameraReadingMapper::toDto);
    }

    /**
     * Delete the cameraReading by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CameraReading : {}", id);
        cameraReadingRepository.deleteById(id);
    }
}
