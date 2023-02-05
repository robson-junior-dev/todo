import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISprint, NewSprint } from '../sprint.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISprint for edit and NewSprintFormGroupInput for create.
 */
type SprintFormGroupInput = ISprint | PartialWithRequiredKeyOf<NewSprint>;

type SprintFormDefaults = Pick<NewSprint, 'id'>;

type SprintFormGroupContent = {
  id: FormControl<ISprint['id'] | NewSprint['id']>;
  number: FormControl<ISprint['number']>;
  status: FormControl<ISprint['status']>;
  project: FormControl<ISprint['project']>;
};

export type SprintFormGroup = FormGroup<SprintFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SprintFormService {
  createSprintFormGroup(sprint: SprintFormGroupInput = { id: null }): SprintFormGroup {
    const sprintRawValue = {
      ...this.getFormDefaults(),
      ...sprint,
    };
    return new FormGroup<SprintFormGroupContent>({
      id: new FormControl(
        { value: sprintRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      number: new FormControl(sprintRawValue.number, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(10)],
      }),
      status: new FormControl(sprintRawValue.status, {
        validators: [Validators.required],
      }),
      project: new FormControl(sprintRawValue.project, {
        validators: [Validators.required],
      }),
    });
  }

  getSprint(form: SprintFormGroup): ISprint | NewSprint {
    return form.getRawValue() as ISprint | NewSprint;
  }

  resetForm(form: SprintFormGroup, sprint: SprintFormGroupInput): void {
    const sprintRawValue = { ...this.getFormDefaults(), ...sprint };
    form.reset(
      {
        ...sprintRawValue,
        id: { value: sprintRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SprintFormDefaults {
    return {
      id: null,
    };
  }
}
