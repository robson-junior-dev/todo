import { TaskStatus } from 'app/entities/enumerations/task-status.model';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 37978,
  name: 'CSS Account Up-sized',
  status: TaskStatus['TO_DO'],
};

export const sampleWithPartialData: ITask = {
  id: 98903,
  name: 'Granito TCP Mesa',
  status: TaskStatus['FINISHED'],
  hoursToComplete: 57163,
  attachment: '../fake-data/blob/hipster.png',
  attachmentContentType: 'unknown',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithFullData: ITask = {
  id: 91271,
  name: 'Dynamic attitude Industrial',
  description: 'Cambridgeshire invoice Concreto',
  status: TaskStatus['FINISHED'],
  hoursToComplete: 22300,
  attachment: '../fake-data/blob/hipster.png',
  attachmentContentType: 'unknown',
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewTask = {
  name: 'Alameda',
  status: TaskStatus['FINISHED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
