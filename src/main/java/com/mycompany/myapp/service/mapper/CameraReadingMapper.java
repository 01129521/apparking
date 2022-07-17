package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CameraReading;
import com.mycompany.myapp.service.dto.CameraReadingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CameraReading} and its DTO {@link CameraReadingDTO}.
 */
@Mapper(componentModel = "spring")
public interface CameraReadingMapper extends EntityMapper<CameraReadingDTO, CameraReading> {}
