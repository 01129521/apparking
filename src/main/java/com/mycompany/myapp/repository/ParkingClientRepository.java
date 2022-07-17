package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ParkingClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParkingClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingClientRepository extends JpaRepository<ParkingClient, Long>, JpaSpecificationExecutor<ParkingClient> {}
