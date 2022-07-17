package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.CameraReading} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CameraReadingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /camera-readings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CameraReadingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cameraReadingDate;

    private StringFilter licensePlateNumbers;

    private Boolean distinct;

    public CameraReadingCriteria() {}

    public CameraReadingCriteria(CameraReadingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cameraReadingDate = other.cameraReadingDate == null ? null : other.cameraReadingDate.copy();
        this.licensePlateNumbers = other.licensePlateNumbers == null ? null : other.licensePlateNumbers.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CameraReadingCriteria copy() {
        return new CameraReadingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCameraReadingDate() {
        return cameraReadingDate;
    }

    public StringFilter cameraReadingDate() {
        if (cameraReadingDate == null) {
            cameraReadingDate = new StringFilter();
        }
        return cameraReadingDate;
    }

    public void setCameraReadingDate(StringFilter cameraReadingDate) {
        this.cameraReadingDate = cameraReadingDate;
    }

    public StringFilter getLicensePlateNumbers() {
        return licensePlateNumbers;
    }

    public StringFilter licensePlateNumbers() {
        if (licensePlateNumbers == null) {
            licensePlateNumbers = new StringFilter();
        }
        return licensePlateNumbers;
    }

    public void setLicensePlateNumbers(StringFilter licensePlateNumbers) {
        this.licensePlateNumbers = licensePlateNumbers;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CameraReadingCriteria that = (CameraReadingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cameraReadingDate, that.cameraReadingDate) &&
            Objects.equals(licensePlateNumbers, that.licensePlateNumbers) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cameraReadingDate, licensePlateNumbers, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CameraReadingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cameraReadingDate != null ? "cameraReadingDate=" + cameraReadingDate + ", " : "") +
            (licensePlateNumbers != null ? "licensePlateNumbers=" + licensePlateNumbers + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
