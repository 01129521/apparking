package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ParkingClient} entity.
 */
public class ParkingClientDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @NotNull
    @Size(max = 20)
    private String phoneNumber;

    @NotNull
    @Size(max = 20)
    private String licensePlateNumbers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicensePlateNumbers() {
        return licensePlateNumbers;
    }

    public void setLicensePlateNumbers(String licensePlateNumbers) {
        this.licensePlateNumbers = licensePlateNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParkingClientDTO)) {
            return false;
        }

        ParkingClientDTO parkingClientDTO = (ParkingClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, parkingClientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParkingClientDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", licensePlateNumbers='" + getLicensePlateNumbers() + "'" +
            "}";
    }
}
