package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CameraReading;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CameraReading entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CameraReadingRepository extends JpaRepository<CameraReading, Long>, JpaSpecificationExecutor<CameraReading> {}
