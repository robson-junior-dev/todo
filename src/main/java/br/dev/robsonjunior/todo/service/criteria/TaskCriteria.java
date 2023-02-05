package br.dev.robsonjunior.todo.service.criteria;

import br.dev.robsonjunior.todo.domain.enumeration.TaskStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.dev.robsonjunior.todo.domain.Task} entity. This class is used
 * in {@link br.dev.robsonjunior.todo.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TaskStatus
     */
    public static class TaskStatusFilter extends Filter<TaskStatus> {

        public TaskStatusFilter() {}

        public TaskStatusFilter(TaskStatusFilter filter) {
            super(filter);
        }

        @Override
        public TaskStatusFilter copy() {
            return new TaskStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private TaskStatusFilter status;

    private LongFilter hoursToComplete;

    private LongFilter sprintId;

    private LongFilter collaboratorId;

    private Boolean distinct;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.hoursToComplete = other.hoursToComplete == null ? null : other.hoursToComplete.copy();
        this.sprintId = other.sprintId == null ? null : other.sprintId.copy();
        this.collaboratorId = other.collaboratorId == null ? null : other.collaboratorId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public TaskStatusFilter getStatus() {
        return status;
    }

    public TaskStatusFilter status() {
        if (status == null) {
            status = new TaskStatusFilter();
        }
        return status;
    }

    public void setStatus(TaskStatusFilter status) {
        this.status = status;
    }

    public LongFilter getHoursToComplete() {
        return hoursToComplete;
    }

    public LongFilter hoursToComplete() {
        if (hoursToComplete == null) {
            hoursToComplete = new LongFilter();
        }
        return hoursToComplete;
    }

    public void setHoursToComplete(LongFilter hoursToComplete) {
        this.hoursToComplete = hoursToComplete;
    }

    public LongFilter getSprintId() {
        return sprintId;
    }

    public LongFilter sprintId() {
        if (sprintId == null) {
            sprintId = new LongFilter();
        }
        return sprintId;
    }

    public void setSprintId(LongFilter sprintId) {
        this.sprintId = sprintId;
    }

    public LongFilter getCollaboratorId() {
        return collaboratorId;
    }

    public LongFilter collaboratorId() {
        if (collaboratorId == null) {
            collaboratorId = new LongFilter();
        }
        return collaboratorId;
    }

    public void setCollaboratorId(LongFilter collaboratorId) {
        this.collaboratorId = collaboratorId;
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
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(hoursToComplete, that.hoursToComplete) &&
            Objects.equals(sprintId, that.sprintId) &&
            Objects.equals(collaboratorId, that.collaboratorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, hoursToComplete, sprintId, collaboratorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (hoursToComplete != null ? "hoursToComplete=" + hoursToComplete + ", " : "") +
            (sprintId != null ? "sprintId=" + sprintId + ", " : "") +
            (collaboratorId != null ? "collaboratorId=" + collaboratorId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
