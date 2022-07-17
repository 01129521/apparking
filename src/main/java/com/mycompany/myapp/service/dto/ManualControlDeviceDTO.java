package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ManualControlDevice} entity.
 */
public class ManualControlDeviceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String device;

    private Boolean state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualControlDeviceDTO)) {
            return false;
        }

        ManualControlDeviceDTO manualControlDeviceDTO = (ManualControlDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, manualControlDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualControlDeviceDTO{" +
            "id=" + getId() +
            ", device='" + getDevice() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
