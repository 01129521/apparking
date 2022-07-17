package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CameraReading;
import com.mycompany.myapp.repository.CameraReadingRepository;
import com.mycompany.myapp.service.criteria.CameraReadingCriteria;
import com.mycompany.myapp.service.dto.CameraReadingDTO;
import com.mycompany.myapp.service.mapper.CameraReadingMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CameraReading} entities in the database.
 * The main input is a {@link CameraReadingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CameraReadingDTO} or a {@link Page} of {@link CameraReadingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CameraReadingQueryService extends QueryService<CameraReading> {

    private final Logger log = LoggerFactory.getLogger(CameraReadingQueryService.class);

    private final CameraReadingRepository cameraReadingRepository;

    private final CameraReadingMapper cameraReadingMapper;

    public CameraReadingQueryService(CameraReadingRepository cameraReadingRepository, CameraReadingMapper cameraReadingMapper) {
        this.cameraReadingRepository = cameraReadingRepository;
        this.cameraReadingMapper = cameraReadingMapper;
    }

    /**
     * Return a {@link List} of {@link CameraReadingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CameraReadingDTO> findByCriteria(CameraReadingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CameraReading> specification = createSpecification(criteria);
        return cameraReadingMapper.toDto(cameraReadingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CameraReadingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CameraReadingDTO> findByCriteria(CameraReadingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CameraReading> specification = createSpecification(criteria);
        return cameraReadingRepository.findAll(specification, page).map(cameraReadingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CameraReadingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CameraReading> specification = createSpecification(criteria);
        return cameraReadingRepository.count(specification);
    }

    /**
     * Function to convert {@link CameraReadingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CameraReading> createSpecification(CameraReadingCriteria criteria) {
        Specification<CameraReading> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CameraReading_.id));
            }
            if (criteria.getCameraReadingDate() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCameraReadingDate(), CameraReading_.cameraReadingDate));
            }
            if (criteria.getLicensePlateNumbers() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLicensePlateNumbers(), CameraReading_.licensePlateNumbers));
            }
        }
        return specification;
    }
}
