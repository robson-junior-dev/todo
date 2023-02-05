import { SprintStatus } from 'app/entities/enumerations/sprint-status.model';

import { ISprint, NewSprint } from './sprint.model';

export const sampleWithRequiredData: ISprint = {
  id: 87675,
  number: 'EXE stream',
  status: SprintStatus['IN_PROGRESS'],
};

export const sampleWithPartialData: ISprint = {
  id: 49262,
  number: 'moratorium',
  status: SprintStatus['FINISHED'],
};

export const sampleWithFullData: ISprint = {
  id: 10016,
  number: 'website',
  status: SprintStatus['IN_PLANNING'],
};

export const sampleWithNewData: NewSprint = {
  number: 'Loan Sapat',
  status: SprintStatus['FINISHED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
