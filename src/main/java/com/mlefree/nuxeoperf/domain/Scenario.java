package com.mlefree.nuxeoperf.domain;



import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.mlefree.nuxeoperf.domain.enumeration.ScenarioType;

import com.mlefree.nuxeoperf.domain.enumeration.Approach;

/**
 * A Scenario.
 */
@Entity
@Table(name = "scenario")
public class Scenario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "thread_count")
    private Long threadCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ScenarioType type;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approach")
    private Approach approach;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Scenario name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public Scenario detail(String detail) {
        this.detail = detail;
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getThreadCount() {
        return threadCount;
    }

    public Scenario threadCount(Long threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public void setThreadCount(Long threadCount) {
        this.threadCount = threadCount;
    }

    public ScenarioType getType() {
        return type;
    }

    public Scenario type(ScenarioType type) {
        this.type = type;
        return this;
    }

    public void setType(ScenarioType type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Scenario startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Scenario endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Approach getApproach() {
        return approach;
    }

    public Scenario approach(Approach approach) {
        this.approach = approach;
        return this;
    }

    public void setApproach(Approach approach) {
        this.approach = approach;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scenario scenario = (Scenario) o;
        if (scenario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scenario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scenario{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", detail='" + getDetail() + "'" +
            ", threadCount=" + getThreadCount() +
            ", type='" + getType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", approach='" + getApproach() + "'" +
            "}";
    }
}
