import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SprintFormService, SprintFormGroup } from './sprint-form.service';
import { ISprint } from '../sprint.model';
import { SprintService } from '../service/sprint.service';
import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { SprintStatus } from 'app/entities/enumerations/sprint-status.model';

@Component({
  selector: 'jhi-sprint-update',
  templateUrl: './sprint-update.component.html',
})
export class SprintUpdateComponent implements OnInit {
  isSaving = false;
  sprint: ISprint | null = null;
  sprintStatusValues = Object.keys(SprintStatus);

  projectsSharedCollection: IProject[] = [];

  editForm: SprintFormGroup = this.sprintFormService.createSprintFormGroup();

  constructor(
    protected sprintService: SprintService,
    protected sprintFormService: SprintFormService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProject = (o1: IProject | null, o2: IProject | null): boolean => this.projectService.compareProject(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sprint }) => {
      this.sprint = sprint;
      if (sprint) {
        this.updateForm(sprint);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sprint = this.sprintFormService.getSprint(this.editForm);
    if (sprint.id !== null) {
      this.subscribeToSaveResponse(this.sprintService.update(sprint));
    } else {
      this.subscribeToSaveResponse(this.sprintService.create(sprint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISprint>>): void {
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

  protected updateForm(sprint: ISprint): void {
    this.sprint = sprint;
    this.sprintFormService.resetForm(this.editForm, sprint);

    this.projectsSharedCollection = this.projectService.addProjectToCollectionIfMissing<IProject>(
      this.projectsSharedCollection,
      sprint.project
    );
  }

  protected loadRelationshipsOptions(): void {
    this.projectService
      .query()
      .pipe(map((res: HttpResponse<IProject[]>) => res.body ?? []))
      .pipe(map((projects: IProject[]) => this.projectService.addProjectToCollectionIfMissing<IProject>(projects, this.sprint?.project)))
      .subscribe((projects: IProject[]) => (this.projectsSharedCollection = projects));
  }
}
