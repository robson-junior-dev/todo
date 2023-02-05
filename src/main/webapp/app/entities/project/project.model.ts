import { ICollaborator } from 'app/entities/collaborator/collaborator.model';

export interface IProject {
  id: number;
  name?: string | null;
  description?: string | null;
  collaborators?: Pick<ICollaborator, 'id' | 'name'>[] | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
