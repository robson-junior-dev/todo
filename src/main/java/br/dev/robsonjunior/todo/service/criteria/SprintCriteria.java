package br.dev.robsonjunior.todo.service.criteria;

import br.dev.robsonjunior.todo.domain.enumeration.SprintStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.dev.robsonjunior.todo.domain.Sprint} entity. This class is used
 * in {@link br.dev.robsonjunior.todo.web.rest.SprintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sprints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SprintCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SprintStatus
     */
    public static class SprintStatusFilter extends Filter<SprintStatus> {

        public SprintStatusFilter() {}

        public SprintStatusFilter(SprintStatusFilter filter) {
            super(filter);
        }

        @Override
        public SprintStatusFilter copy() {
            return new SprintStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter number;

    private SprintStatusFilter status;

    private LongFilter projectId;

    private Boolean distinct;

    public SprintCriteria() {}

    public SprintCriteria(SprintCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SprintCriteria copy() {
        return new SprintCriteria(this);
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

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public SprintStatusFilter getStatus() {
        return status;
    }

    public SprintStatusFilter status() {
        if (status == null) {
            status = new SprintStatusFilter();
        }
        return status;
    }

    public void setStatus(SprintStatusFilter status) {
        this.status = status;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
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
        final SprintCriteria that = (SprintCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(status, that.status) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, status, projectId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SprintCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
