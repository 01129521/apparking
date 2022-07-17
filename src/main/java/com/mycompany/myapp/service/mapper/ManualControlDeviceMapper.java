package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ManualControlDevice;
import com.mycompany.myapp.service.dto.ManualControlDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManualControlDevice} and its DTO {@link ManualControlDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualControlDeviceMapper extends EntityMapper<ManualControlDeviceDTO, ManualControlDevice> {}
