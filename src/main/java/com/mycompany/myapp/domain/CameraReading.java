package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CameraReading.
 */
@Entity
@Table(name = "camera_reading")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CameraReading implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "camera_reading_date")
    private String cameraReadingDate;

    @Column(name = "license_plate_numbers")
    private String licensePlateNumbers;

    @Lob
    @Column(name = "license_plate_photo")
    private byte[] licensePlatePhoto;

    @Column(name = "license_plate_photo_content_type")
    private String licensePlatePhotoContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CameraReading id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCameraReadingDate() {
        return this.cameraReadingDate;
    }

    public CameraReading cameraReadingDate(String cameraReadingDate) {
        this.setCameraReadingDate(cameraReadingDate);
        return this;
    }

    public void setCameraReadingDate(String cameraReadingDate) {
        this.cameraReadingDate = cameraReadingDate;
    }

    public String getLicensePlateNumbers() {
        return this.licensePlateNumbers;
    }

    public CameraReading licensePlateNumbers(String licensePlateNumbers) {
        this.setLicensePlateNumbers(licensePlateNumbers);
        return this;
    }

    public void setLicensePlateNumbers(String licensePlateNumbers) {
        this.licensePlateNumbers = licensePlateNumbers;
    }

    public byte[] getLicensePlatePhoto() {
        return this.licensePlatePhoto;
    }

    public CameraReading licensePlatePhoto(byte[] licensePlatePhoto) {
        this.setLicensePlatePhoto(licensePlatePhoto);
        return this;
    }

    public void setLicensePlatePhoto(byte[] licensePlatePhoto) {
        this.licensePlatePhoto = licensePlatePhoto;
    }

    public String getLicensePlatePhotoContentType() {
        return this.licensePlatePhotoContentType;
    }

    public CameraReading licensePlatePhotoContentType(String licensePlatePhotoContentType) {
        this.licensePlatePhotoContentType = licensePlatePhotoContentType;
        return this;
    }

    public void setLicensePlatePhotoContentType(String licensePlatePhotoContentType) {
        this.licensePlatePhotoContentType = licensePlatePhotoContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CameraReading)) {
            return false;
        }
        return id != null && id.equals(((CameraReading) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CameraReading{" +
            "id=" + getId() +
            ", cameraReadingDate='" + getCameraReadingDate() + "'" +
            ", licensePlateNumbers='" + getLicensePlateNumbers() + "'" +
            ", licensePlatePhoto='" + getLicensePlatePhoto() + "'" +
            ", licensePlatePhotoContentType='" + getLicensePlatePhotoContentType() + "'" +
            "}";
    }
}
