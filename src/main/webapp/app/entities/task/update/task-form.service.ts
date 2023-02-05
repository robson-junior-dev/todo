import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITask, NewTask } from '../task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITask for edit and NewTaskFormGroupInput for create.
 */
type TaskFormGroupInput = ITask | PartialWithRequiredKeyOf<NewTask>;

type TaskFormDefaults = Pick<NewTask, 'id'>;

type TaskFormGroupContent = {
  id: FormControl<ITask['id'] | NewTask['id']>;
  name: FormControl<ITask['name']>;
  description: FormControl<ITask['description']>;
  status: FormControl<ITask['status']>;
  hoursToComplete: FormControl<ITask['hoursToComplete']>;
  attachment: FormControl<ITask['attachment']>;
  attachmentContentType: FormControl<ITask['attachmentContentType']>;
  image: FormControl<ITask['image']>;
  imageContentType: FormControl<ITask['imageContentType']>;
  sprint: FormControl<ITask['sprint']>;
  collaborator: FormControl<ITask['collaborator']>;
};

export type TaskFormGroup = FormGroup<TaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaskFormService {
  createTaskFormGroup(task: TaskFormGroupInput = { id: null }): TaskFormGroup {
    const taskRawValue = {
      ...this.getFormDefaults(),
      ...task,
    };
    return new FormGroup<TaskFormGroupContent>({
      id: new FormControl(
        { value: taskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(taskRawValue.name, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(255)],
      }),
      description: new FormControl(taskRawValue.description, {
        validators: [Validators.minLength(1), Validators.maxLength(2000)],
      }),
      status: new FormControl(taskRawValue.status, {
        validators: [Validators.required],
      }),
      hoursToComplete: new FormControl(taskRawValue.hoursToComplete, {
        validators: [Validators.min(0)],
      }),
      attachment: new FormControl(taskRawValue.attachment),
      attachmentContentType: new FormControl(taskRawValue.attachmentContentType),
      image: new FormControl(taskRawValue.image),
      imageContentType: new FormControl(taskRawValue.imageContentType),
      sprint: new FormControl(taskRawValue.sprint, {
        validators: [Validators.required],
      }),
      collaborator: new FormControl(taskRawValue.collaborator),
    });
  }

  getTask(form: TaskFormGroup): ITask | NewTask {
    return form.getRawValue() as ITask | NewTask;
  }

  resetForm(form: TaskFormGroup, task: TaskFormGroupInput): void {
    const taskRawValue = { ...this.getFormDefaults(), ...task };
    form.reset(
      {
        ...taskRawValue,
        id: { value: taskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TaskFormDefaults {
    return {
      id: null,
    };
  }
}
