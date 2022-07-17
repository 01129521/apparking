package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CameraReadingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CameraReading.class);
        CameraReading cameraReading1 = new CameraReading();
        cameraReading1.setId(1L);
        CameraReading cameraReading2 = new CameraReading();
        cameraReading2.setId(cameraReading1.getId());
        assertThat(cameraReading1).isEqualTo(cameraReading2);
        cameraReading2.setId(2L);
        assertThat(cameraReading1).isNotEqualTo(cameraReading2);
        cameraReading1.setId(null);
        assertThat(cameraReading1).isNotEqualTo(cameraReading2);
    }
}
