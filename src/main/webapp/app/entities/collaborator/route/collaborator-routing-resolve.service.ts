import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICollaborator } from '../collaborator.model';
import { CollaboratorService } from '../service/collaborator.service';

@Injectable({ providedIn: 'root' })
export class CollaboratorRoutingResolveService implements Resolve<ICollaborator | null> {
  constructor(protected service: CollaboratorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICollaborator | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((collaborator: HttpResponse<ICollaborator>) => {
          if (collaborator.body) {
            return of(collaborator.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
