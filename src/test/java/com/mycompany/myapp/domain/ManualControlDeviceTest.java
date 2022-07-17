package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualControlDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualControlDevice.class);
        ManualControlDevice manualControlDevice1 = new ManualControlDevice();
        manualControlDevice1.setId(1L);
        ManualControlDevice manualControlDevice2 = new ManualControlDevice();
        manualControlDevice2.setId(manualControlDevice1.getId());
        assertThat(manualControlDevice1).isEqualTo(manualControlDevice2);
        manualControlDevice2.setId(2L);
        assertThat(manualControlDevice1).isNotEqualTo(manualControlDevice2);
        manualControlDevice1.setId(null);
        assertThat(manualControlDevice1).isNotEqualTo(manualControlDevice2);
    }
}
