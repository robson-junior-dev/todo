import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICollaborator, NewCollaborator } from '../collaborator.model';

export type PartialUpdateCollaborator = Partial<ICollaborator> & Pick<ICollaborator, 'id'>;

type RestOf<T extends ICollaborator | NewCollaborator> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

export type RestCollaborator = RestOf<ICollaborator>;

export type NewRestCollaborator = RestOf<NewCollaborator>;

export type PartialUpdateRestCollaborator = RestOf<PartialUpdateCollaborator>;

export type EntityResponseType = HttpResponse<ICollaborator>;
export type EntityArrayResponseType = HttpResponse<ICollaborator[]>;

@Injectable({ providedIn: 'root' })
export class CollaboratorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/collaborators');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(collaborator: NewCollaborator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collaborator);
    return this.http
      .post<RestCollaborator>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(collaborator: ICollaborator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collaborator);
    return this.http
      .put<RestCollaborator>(`${this.resourceUrl}/${this.getCollaboratorIdentifier(collaborator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(collaborator: PartialUpdateCollaborator): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(collaborator);
    return this.http
      .patch<RestCollaborator>(`${this.resourceUrl}/${this.getCollaboratorIdentifier(collaborator)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCollaborator>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCollaborator[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCollaboratorIdentifier(collaborator: Pick<ICollaborator, 'id'>): number {
    return collaborator.id;
  }

  compareCollaborator(o1: Pick<ICollaborator, 'id'> | null, o2: Pick<ICollaborator, 'id'> | null): boolean {
    return o1 && o2 ? this.getCollaboratorIdentifier(o1) === this.getCollaboratorIdentifier(o2) : o1 === o2;
  }

  addCollaboratorToCollectionIfMissing<Type extends Pick<ICollaborator, 'id'>>(
    collaboratorCollection: Type[],
    ...collaboratorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const collaborators: Type[] = collaboratorsToCheck.filter(isPresent);
    if (collaborators.length > 0) {
      const collaboratorCollectionIdentifiers = collaboratorCollection.map(
        collaboratorItem => this.getCollaboratorIdentifier(collaboratorItem)!
      );
      const collaboratorsToAdd = collaborators.filter(collaboratorItem => {
        const collaboratorIdentifier = this.getCollaboratorIdentifier(collaboratorItem);
        if (collaboratorCollectionIdentifiers.includes(collaboratorIdentifier)) {
          return false;
        }
        collaboratorCollectionIdentifiers.push(collaboratorIdentifier);
        return true;
      });
      return [...collaboratorsToAdd, ...collaboratorCollection];
    }
    return collaboratorCollection;
  }

  protected convertDateFromClient<T extends ICollaborator | NewCollaborator | PartialUpdateCollaborator>(collaborator: T): RestOf<T> {
    return {
      ...collaborator,
      dateOfBirth: collaborator.dateOfBirth?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCollaborator: RestCollaborator): ICollaborator {
    return {
      ...restCollaborator,
      dateOfBirth: restCollaborator.dateOfBirth ? dayjs(restCollaborator.dateOfBirth) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCollaborator>): HttpResponse<ICollaborator> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCollaborator[]>): HttpResponse<ICollaborator[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
