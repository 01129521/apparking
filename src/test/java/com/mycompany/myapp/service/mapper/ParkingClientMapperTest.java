package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingClientMapperTest {

    private ParkingClientMapper parkingClientMapper;

    @BeforeEach
    public void setUp() {
        parkingClientMapper = new ParkingClientMapperImpl();
    }
}
