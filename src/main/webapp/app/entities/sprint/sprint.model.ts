import { IProject } from 'app/entities/project/project.model';
import { SprintStatus } from 'app/entities/enumerations/sprint-status.model';

export interface ISprint {
  id: number;
  number?: string | null;
  status?: SprintStatus | null;
  project?: Pick<IProject, 'id' | 'name'> | null;
}

export type NewSprint = Omit<ISprint, 'id'> & { id: null };
