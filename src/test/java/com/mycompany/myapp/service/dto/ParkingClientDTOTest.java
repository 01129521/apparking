package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkingClientDTO.class);
        ParkingClientDTO parkingClientDTO1 = new ParkingClientDTO();
        parkingClientDTO1.setId(1L);
        ParkingClientDTO parkingClientDTO2 = new ParkingClientDTO();
        assertThat(parkingClientDTO1).isNotEqualTo(parkingClientDTO2);
        parkingClientDTO2.setId(parkingClientDTO1.getId());
        assertThat(parkingClientDTO1).isEqualTo(parkingClientDTO2);
        parkingClientDTO2.setId(2L);
        assertThat(parkingClientDTO1).isNotEqualTo(parkingClientDTO2);
        parkingClientDTO1.setId(null);
        assertThat(parkingClientDTO1).isNotEqualTo(parkingClientDTO2);
    }
}
