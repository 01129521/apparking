package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ManualControlDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ManualControlDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualControlDeviceRepository
    extends JpaRepository<ManualControlDevice, Long>, JpaSpecificationExecutor<ManualControlDevice> {}
