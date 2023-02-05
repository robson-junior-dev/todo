import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CollaboratorComponent } from '../list/collaborator.component';
import { CollaboratorDetailComponent } from '../detail/collaborator-detail.component';
import { CollaboratorUpdateComponent } from '../update/collaborator-update.component';
import { CollaboratorRoutingResolveService } from './collaborator-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const collaboratorRoute: Routes = [
  {
    path: '',
    component: CollaboratorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CollaboratorDetailComponent,
    resolve: {
      collaborator: CollaboratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CollaboratorUpdateComponent,
    resolve: {
      collaborator: CollaboratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CollaboratorUpdateComponent,
    resolve: {
      collaborator: CollaboratorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(collaboratorRoute)],
  exports: [RouterModule],
})
export class CollaboratorRoutingModule {}
