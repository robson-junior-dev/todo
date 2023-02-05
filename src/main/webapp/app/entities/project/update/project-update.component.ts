import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProjectFormService, ProjectFormGroup } from './project-form.service';
import { IProject } from '../project.model';
import { ProjectService } from '../service/project.service';
import { ICollaborator } from 'app/entities/collaborator/collaborator.model';
import { CollaboratorService } from 'app/entities/collaborator/service/collaborator.service';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html',
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;
  project: IProject | null = null;

  collaboratorsSharedCollection: ICollaborator[] = [];

  editForm: ProjectFormGroup = this.projectFormService.createProjectFormGroup();

  constructor(
    protected projectService: ProjectService,
    protected projectFormService: ProjectFormService,
    protected collaboratorService: CollaboratorService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCollaborator = (o1: ICollaborator | null, o2: ICollaborator | null): boolean =>
    this.collaboratorService.compareCollaborator(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
      if (project) {
        this.updateForm(project);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.projectFormService.getProject(this.editForm);
    if (project.id !== null) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(project: IProject): void {
    this.project = project;
    this.projectFormService.resetForm(this.editForm, project);

    this.collaboratorsSharedCollection = this.collaboratorService.addCollaboratorToCollectionIfMissing<ICollaborator>(
      this.collaboratorsSharedCollection,
      ...(project.collaborators ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.collaboratorService
      .query()
      .pipe(map((res: HttpResponse<ICollaborator[]>) => res.body ?? []))
      .pipe(
        map((collaborators: ICollaborator[]) =>
          this.collaboratorService.addCollaboratorToCollectionIfMissing<ICollaborator>(
            collaborators,
            ...(this.project?.collaborators ?? [])
          )
        )
      )
      .subscribe((collaborators: ICollaborator[]) => (this.collaboratorsSharedCollection = collaborators));
  }
}
