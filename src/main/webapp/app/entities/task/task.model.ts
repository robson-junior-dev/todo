import { ISprint } from 'app/entities/sprint/sprint.model';
import { ICollaborator } from 'app/entities/collaborator/collaborator.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';

export interface ITask {
  id: number;
  name?: string | null;
  description?: string | null;
  status?: TaskStatus | null;
  hoursToComplete?: number | null;
  attachment?: string | null;
  attachmentContentType?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  sprint?: Pick<ISprint, 'id' | 'number'> | null;
  collaborator?: Pick<ICollaborator, 'id' | 'name'> | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
