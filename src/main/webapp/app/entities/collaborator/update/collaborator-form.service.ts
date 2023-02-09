import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICollaborator, NewCollaborator } from '../collaborator.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICollaborator for edit and NewCollaboratorFormGroupInput for create.
 */
type CollaboratorFormGroupInput = ICollaborator | PartialWithRequiredKeyOf<NewCollaborator>;

type CollaboratorFormDefaults = Pick<NewCollaborator, 'id' | 'isLeader' | 'projects'>;

type CollaboratorFormGroupContent = {
  id: FormControl<ICollaborator['id'] | NewCollaborator['id']>;
  name: FormControl<ICollaborator['name']>;
  dateOfBirth: FormControl<ICollaborator['dateOfBirth']>;
  photo: FormControl<ICollaborator['photo']>;
  photoContentType: FormControl<ICollaborator['photoContentType']>;
  document: FormControl<ICollaborator['document']>;
  documentContentType: FormControl<ICollaborator['documentContentType']>;
  cpf: FormControl<ICollaborator['cpf']>;
  isLeader: FormControl<ICollaborator['isLeader']>;
  user: FormControl<ICollaborator['user']>;
  leader: FormControl<ICollaborator['leader']>;
  projects: FormControl<ICollaborator['projects']>;
};

export type CollaboratorFormGroup = FormGroup<CollaboratorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CollaboratorFormService {
  createCollaboratorFormGroup(collaborator: CollaboratorFormGroupInput = { id: null }): CollaboratorFormGroup {
    const collaboratorRawValue = {
      ...this.getFormDefaults(),
      ...collaborator,
    };
    return new FormGroup<CollaboratorFormGroupContent>({
      id: new FormControl(
        { value: collaboratorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(collaboratorRawValue.name, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(255), Validators.pattern('(\\D+)')],
      }),
      dateOfBirth: new FormControl(collaboratorRawValue.dateOfBirth),
      photo: new FormControl(collaboratorRawValue.photo),
      photoContentType: new FormControl(collaboratorRawValue.photoContentType),
      document: new FormControl(collaboratorRawValue.document),
      documentContentType: new FormControl(collaboratorRawValue.documentContentType),
      cpf: new FormControl(collaboratorRawValue.cpf, {
        validators: [Validators.required, Validators.pattern('([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})')],
      }),
      isLeader: new FormControl(collaboratorRawValue.isLeader),
      user: new FormControl(collaboratorRawValue.user, {
        validators: [Validators.required],
      }),
      leader: new FormControl(collaboratorRawValue.leader),
      projects: new FormControl(collaboratorRawValue.projects ?? []),
    });
  }

  getCollaborator(form: CollaboratorFormGroup): ICollaborator | NewCollaborator {
    return form.getRawValue() as ICollaborator | NewCollaborator;
  }

  resetForm(form: CollaboratorFormGroup, collaborator: CollaboratorFormGroupInput): void {
    const collaboratorRawValue = { ...this.getFormDefaults(), ...collaborator };
    form.reset(
      {
        ...collaboratorRawValue,
        id: { value: collaboratorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CollaboratorFormDefaults {
    return {
      id: null,
      isLeader: false,
      projects: [],
    };
  }
}
