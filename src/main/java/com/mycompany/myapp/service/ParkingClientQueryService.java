package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.ParkingClient;
import com.mycompany.myapp.repository.ParkingClientRepository;
import com.mycompany.myapp.service.criteria.ParkingClientCriteria;
import com.mycompany.myapp.service.dto.ParkingClientDTO;
import com.mycompany.myapp.service.mapper.ParkingClientMapper;
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
 * Service for executing complex queries for {@link ParkingClient} entities in the database.
 * The main input is a {@link ParkingClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParkingClientDTO} or a {@link Page} of {@link ParkingClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParkingClientQueryService extends QueryService<ParkingClient> {

    private final Logger log = LoggerFactory.getLogger(ParkingClientQueryService.class);

    private final ParkingClientRepository parkingClientRepository;

    private final ParkingClientMapper parkingClientMapper;

    public ParkingClientQueryService(ParkingClientRepository parkingClientRepository, ParkingClientMapper parkingClientMapper) {
        this.parkingClientRepository = parkingClientRepository;
        this.parkingClientMapper = parkingClientMapper;
    }

    /**
     * Return a {@link List} of {@link ParkingClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParkingClientDTO> findByCriteria(ParkingClientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParkingClient> specification = createSpecification(criteria);
        return parkingClientMapper.toDto(parkingClientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParkingClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParkingClientDTO> findByCriteria(ParkingClientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParkingClient> specification = createSpecification(criteria);
        return parkingClientRepository.findAll(specification, page).map(parkingClientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParkingClientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParkingClient> specification = createSpecification(criteria);
        return parkingClientRepository.count(specification);
    }

    /**
     * Function to convert {@link ParkingClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParkingClient> createSpecification(ParkingClientCriteria criteria) {
        Specification<ParkingClient> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParkingClient_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), ParkingClient_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), ParkingClient_.lastName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), ParkingClient_.phoneNumber));
            }
            if (criteria.getLicensePlateNumbers() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLicensePlateNumbers(), ParkingClient_.licensePlateNumbers));
            }
        }
        return specification;
    }
}
