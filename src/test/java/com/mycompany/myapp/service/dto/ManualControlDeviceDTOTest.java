package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualControlDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualControlDeviceDTO.class);
        ManualControlDeviceDTO manualControlDeviceDTO1 = new ManualControlDeviceDTO();
        manualControlDeviceDTO1.setId(1L);
        ManualControlDeviceDTO manualControlDeviceDTO2 = new ManualControlDeviceDTO();
        assertThat(manualControlDeviceDTO1).isNotEqualTo(manualControlDeviceDTO2);
        manualControlDeviceDTO2.setId(manualControlDeviceDTO1.getId());
        assertThat(manualControlDeviceDTO1).isEqualTo(manualControlDeviceDTO2);
        manualControlDeviceDTO2.setId(2L);
        assertThat(manualControlDeviceDTO1).isNotEqualTo(manualControlDeviceDTO2);
        manualControlDeviceDTO1.setId(null);
        assertThat(manualControlDeviceDTO1).isNotEqualTo(manualControlDeviceDTO2);
    }
}
