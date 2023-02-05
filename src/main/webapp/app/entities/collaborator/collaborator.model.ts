import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProject } from 'app/entities/project/project.model';

export interface ICollaborator {
  id: number;
  name?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  photo?: string | null;
  photoContentType?: string | null;
  document?: string | null;
  documentContentType?: string | null;
  cpf?: string | null;
  isLeader?: boolean | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  leader?: Pick<ICollaborator, 'id' | 'name'> | null;
  projects?: Pick<IProject, 'id' | 'name'>[] | null;
}

export type NewCollaborator = Omit<ICollaborator, 'id'> & { id: null };
