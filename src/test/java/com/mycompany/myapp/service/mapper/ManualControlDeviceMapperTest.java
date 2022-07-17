package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManualControlDeviceMapperTest {

    private ManualControlDeviceMapper manualControlDeviceMapper;

    @BeforeEach
    public void setUp() {
        manualControlDeviceMapper = new ManualControlDeviceMapperImpl();
    }
}
