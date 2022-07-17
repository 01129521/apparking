package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ParkingClient;
import com.mycompany.myapp.service.dto.ParkingClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ParkingClient} and its DTO {@link ParkingClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParkingClientMapper extends EntityMapper<ParkingClientDTO, ParkingClient> {}
