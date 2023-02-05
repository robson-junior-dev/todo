import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CollaboratorFormService, CollaboratorFormGroup } from './collaborator-form.service';
import { ICollaborator } from '../collaborator.model';
import { CollaboratorService } from '../service/collaborator.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-collaborator-update',
  templateUrl: './collaborator-update.component.html',
})
export class CollaboratorUpdateComponent implements OnInit {
  isSaving = false;
  collaborator: ICollaborator | null = null;

  usersSharedCollection: IUser[] = [];
  collaboratorsSharedCollection: ICollaborator[] = [];

  editForm: CollaboratorFormGroup = this.collaboratorFormService.createCollaboratorFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected collaboratorService: CollaboratorService,
    protected collaboratorFormService: CollaboratorFormService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareCollaborator = (o1: ICollaborator | null, o2: ICollaborator | null): boolean =>
    this.collaboratorService.compareCollaborator(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collaborator }) => {
      this.collaborator = collaborator;
      if (collaborator) {
        this.updateForm(collaborator);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('todoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const collaborator = this.collaboratorFormService.getCollaborator(this.editForm);
    if (collaborator.id !== null) {
      this.subscribeToSaveResponse(this.collaboratorService.update(collaborator));
    } else {
      this.subscribeToSaveResponse(this.collaboratorService.create(collaborator));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollaborator>>): void {
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

  protected updateForm(collaborator: ICollaborator): void {
    this.collaborator = collaborator;
    this.collaboratorFormService.resetForm(this.editForm, collaborator);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, collaborator.user);
    this.collaboratorsSharedCollection = this.collaboratorService.addCollaboratorToCollectionIfMissing<ICollaborator>(
      this.collaboratorsSharedCollection,
      collaborator.leader
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.collaborator?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.collaboratorService
      .query()
      .pipe(map((res: HttpResponse<ICollaborator[]>) => res.body ?? []))
      .pipe(
        map((collaborators: ICollaborator[]) =>
          this.collaboratorService.addCollaboratorToCollectionIfMissing<ICollaborator>(collaborators, this.collaborator?.leader)
        )
      )
      .subscribe((collaborators: ICollaborator[]) => (this.collaboratorsSharedCollection = collaborators));
  }
}
