package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParkingClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkingClient.class);
        ParkingClient parkingClient1 = new ParkingClient();
        parkingClient1.setId(1L);
        ParkingClient parkingClient2 = new ParkingClient();
        parkingClient2.setId(parkingClient1.getId());
        assertThat(parkingClient1).isEqualTo(parkingClient2);
        parkingClient2.setId(2L);
        assertThat(parkingClient1).isNotEqualTo(parkingClient2);
        parkingClient1.setId(null);
        assertThat(parkingClient1).isNotEqualTo(parkingClient2);
    }
}
