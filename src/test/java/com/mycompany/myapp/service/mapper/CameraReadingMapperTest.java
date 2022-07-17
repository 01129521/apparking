package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CameraReadingMapperTest {

    private CameraReadingMapper cameraReadingMapper;

    @BeforeEach
    public void setUp() {
        cameraReadingMapper = new CameraReadingMapperImpl();
    }
}
