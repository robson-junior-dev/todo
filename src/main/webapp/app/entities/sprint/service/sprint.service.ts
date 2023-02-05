import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISprint, NewSprint } from '../sprint.model';

export type PartialUpdateSprint = Partial<ISprint> & Pick<ISprint, 'id'>;

export type EntityResponseType = HttpResponse<ISprint>;
export type EntityArrayResponseType = HttpResponse<ISprint[]>;

@Injectable({ providedIn: 'root' })
export class SprintService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sprints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sprint: NewSprint): Observable<EntityResponseType> {
    return this.http.post<ISprint>(this.resourceUrl, sprint, { observe: 'response' });
  }

  update(sprint: ISprint): Observable<EntityResponseType> {
    return this.http.put<ISprint>(`${this.resourceUrl}/${this.getSprintIdentifier(sprint)}`, sprint, { observe: 'response' });
  }

  partialUpdate(sprint: PartialUpdateSprint): Observable<EntityResponseType> {
    return this.http.patch<ISprint>(`${this.resourceUrl}/${this.getSprintIdentifier(sprint)}`, sprint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISprint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISprint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSprintIdentifier(sprint: Pick<ISprint, 'id'>): number {
    return sprint.id;
  }

  compareSprint(o1: Pick<ISprint, 'id'> | null, o2: Pick<ISprint, 'id'> | null): boolean {
    return o1 && o2 ? this.getSprintIdentifier(o1) === this.getSprintIdentifier(o2) : o1 === o2;
  }

  addSprintToCollectionIfMissing<Type extends Pick<ISprint, 'id'>>(
    sprintCollection: Type[],
    ...sprintsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sprints: Type[] = sprintsToCheck.filter(isPresent);
    if (sprints.length > 0) {
      const sprintCollectionIdentifiers = sprintCollection.map(sprintItem => this.getSprintIdentifier(sprintItem)!);
      const sprintsToAdd = sprints.filter(sprintItem => {
        const sprintIdentifier = this.getSprintIdentifier(sprintItem);
        if (sprintCollectionIdentifiers.includes(sprintIdentifier)) {
          return false;
        }
        sprintCollectionIdentifiers.push(sprintIdentifier);
        return true;
      });
      return [...sprintsToAdd, ...sprintCollection];
    }
    return sprintCollection;
  }
}
