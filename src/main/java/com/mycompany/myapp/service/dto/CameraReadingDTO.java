package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CameraReading} entity.
 */
public class CameraReadingDTO implements Serializable {

    private Long id;

    private String cameraReadingDate;

    private String licensePlateNumbers;

    @Lob
    private byte[] licensePlatePhoto;

    private String licensePlatePhotoContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCameraReadingDate() {
        return cameraReadingDate;
    }

    public void setCameraReadingDate(String cameraReadingDate) {
        this.cameraReadingDate = cameraReadingDate;
    }

    public String getLicensePlateNumbers() {
        return licensePlateNumbers;
    }

    public void setLicensePlateNumbers(String licensePlateNumbers) {
        this.licensePlateNumbers = licensePlateNumbers;
    }

    public byte[] getLicensePlatePhoto() {
        return licensePlatePhoto;
    }

    public void setLicensePlatePhoto(byte[] licensePlatePhoto) {
        this.licensePlatePhoto = licensePlatePhoto;
    }

    public String getLicensePlatePhotoContentType() {
        return licensePlatePhotoContentType;
    }

    public void setLicensePlatePhotoContentType(String licensePlatePhotoContentType) {
        this.licensePlatePhotoContentType = licensePlatePhotoContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CameraReadingDTO)) {
            return false;
        }

        CameraReadingDTO cameraReadingDTO = (CameraReadingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cameraReadingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CameraReadingDTO{" +
            "id=" + getId() +
            ", cameraReadingDate='" + getCameraReadingDate() + "'" +
            ", licensePlateNumbers='" + getLicensePlateNumbers() + "'" +
            ", licensePlatePhoto='" + getLicensePlatePhoto() + "'" +
            "}";
    }
}
