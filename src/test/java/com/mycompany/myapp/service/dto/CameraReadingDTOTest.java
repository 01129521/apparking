package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CameraReadingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CameraReadingDTO.class);
        CameraReadingDTO cameraReadingDTO1 = new CameraReadingDTO();
        cameraReadingDTO1.setId(1L);
        CameraReadingDTO cameraReadingDTO2 = new CameraReadingDTO();
        assertThat(cameraReadingDTO1).isNotEqualTo(cameraReadingDTO2);
        cameraReadingDTO2.setId(cameraReadingDTO1.getId());
        assertThat(cameraReadingDTO1).isEqualTo(cameraReadingDTO2);
        cameraReadingDTO2.setId(2L);
        assertThat(cameraReadingDTO1).isNotEqualTo(cameraReadingDTO2);
        cameraReadingDTO1.setId(null);
        assertThat(cameraReadingDTO1).isNotEqualTo(cameraReadingDTO2);
    }
}
