package br.dev.robsonjunior.todo.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link br.dev.robsonjunior.todo.domain.Collaborator} entity. This class is used
 * in {@link br.dev.robsonjunior.todo.web.rest.CollaboratorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /collaborators?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CollaboratorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter dateOfBirth;

    private StringFilter cpf;

    private BooleanFilter isLeader;

    private LongFilter userId;

    private LongFilter leaderId;

    private LongFilter projectId;

    private Boolean distinct;

    public CollaboratorCriteria() {}

    public CollaboratorCriteria(CollaboratorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.dateOfBirth = other.dateOfBirth == null ? null : other.dateOfBirth.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.isLeader = other.isLeader == null ? null : other.isLeader.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.leaderId = other.leaderId == null ? null : other.leaderId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CollaboratorCriteria copy() {
        return new CollaboratorCriteria(this);
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

    public LocalDateFilter getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateFilter dateOfBirth() {
        if (dateOfBirth == null) {
            dateOfBirth = new LocalDateFilter();
        }
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateFilter dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public StringFilter cpf() {
        if (cpf == null) {
            cpf = new StringFilter();
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public BooleanFilter getIsLeader() {
        return isLeader;
    }

    public BooleanFilter isLeader() {
        if (isLeader == null) {
            isLeader = new BooleanFilter();
        }
        return isLeader;
    }

    public void setIsLeader(BooleanFilter isLeader) {
        this.isLeader = isLeader;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getLeaderId() {
        return leaderId;
    }

    public LongFilter leaderId() {
        if (leaderId == null) {
            leaderId = new LongFilter();
        }
        return leaderId;
    }

    public void setLeaderId(LongFilter leaderId) {
        this.leaderId = leaderId;
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
        final CollaboratorCriteria that = (CollaboratorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(dateOfBirth, that.dateOfBirth) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(isLeader, that.isLeader) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(leaderId, that.leaderId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, cpf, isLeader, userId, leaderId, projectId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollaboratorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth + ", " : "") +
            (cpf != null ? "cpf=" + cpf + ", " : "") +
            (isLeader != null ? "isLeader=" + isLeader + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (leaderId != null ? "leaderId=" + leaderId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
