import { IProject, NewProject } from './project.model';

export const sampleWithRequiredData: IProject = {
  id: 55962,
  name: 'COM dedicated Agent',
};

export const sampleWithPartialData: IProject = {
  id: 8742,
  name: 'redundant Sul',
  description: 'AGP Grass-roots',
};

export const sampleWithFullData: IProject = {
  id: 66545,
  name: 'Saúde capacitor',
  description: 'Investor Loan Avon',
};

export const sampleWithNewData: NewProject = {
  name: 'secondary users invoice',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
