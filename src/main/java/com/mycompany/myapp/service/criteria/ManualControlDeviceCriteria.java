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
 * Criteria class for the {@link com.mycompany.myapp.domain.ManualControlDevice} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ManualControlDeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /manual-control-devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ManualControlDeviceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter device;

    private BooleanFilter state;

    private Boolean distinct;

    public ManualControlDeviceCriteria() {}

    public ManualControlDeviceCriteria(ManualControlDeviceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.device = other.device == null ? null : other.device.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ManualControlDeviceCriteria copy() {
        return new ManualControlDeviceCriteria(this);
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

    public StringFilter getDevice() {
        return device;
    }

    public StringFilter device() {
        if (device == null) {
            device = new StringFilter();
        }
        return device;
    }

    public void setDevice(StringFilter device) {
        this.device = device;
    }

    public BooleanFilter getState() {
        return state;
    }

    public BooleanFilter state() {
        if (state == null) {
            state = new BooleanFilter();
        }
        return state;
    }

    public void setState(BooleanFilter state) {
        this.state = state;
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
        final ManualControlDeviceCriteria that = (ManualControlDeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(device, that.device) &&
            Objects.equals(state, that.state) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, device, state, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualControlDeviceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (device != null ? "device=" + device + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
