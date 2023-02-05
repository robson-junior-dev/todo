import dayjs from 'dayjs/esm';

import { ICollaborator, NewCollaborator } from './collaborator.model';

export const sampleWithRequiredData: ICollaborator = {
  id: 51360,
  name: '^r',
};

export const sampleWithPartialData: ICollaborator = {
  id: 3997,
  name: 'om|',
  cpf: 'Phased Applications Director',
};

export const sampleWithFullData: ICollaborator = {
  id: 42686,
  name: 'e+C',
  dateOfBirth: dayjs('2023-02-05'),
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  cpf: 'Prático panel dynamic',
  isLeader: true,
};

export const sampleWithNewData: NewCollaborator = {
  name: 'k)Bb',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
