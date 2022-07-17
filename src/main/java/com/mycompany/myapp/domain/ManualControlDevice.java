package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualControlDevice.
 */
@Entity
@Table(name = "manual_control_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ManualControlDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "device", length = 100, nullable = false)
    private String device;

    @Column(name = "state")
    private Boolean state;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ManualControlDevice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevice() {
        return this.device;
    }

    public ManualControlDevice device(String device) {
        this.setDevice(device);
        return this;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Boolean getState() {
        return this.state;
    }

    public ManualControlDevice state(Boolean state) {
        this.setState(state);
        return this;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManualControlDevice)) {
            return false;
        }
        return id != null && id.equals(((ManualControlDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManualControlDevice{" +
            "id=" + getId() +
            ", device='" + getDevice() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
