import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollaboratorComponent } from './list/collaborator.component';
import { CollaboratorDetailComponent } from './detail/collaborator-detail.component';
import { CollaboratorUpdateComponent } from './update/collaborator-update.component';
import { CollaboratorDeleteDialogComponent } from './delete/collaborator-delete-dialog.component';
import { CollaboratorRoutingModule } from './route/collaborator-routing.module';

@NgModule({
  imports: [SharedModule, CollaboratorRoutingModule],
  declarations: [CollaboratorComponent, CollaboratorDetailComponent, CollaboratorUpdateComponent, CollaboratorDeleteDialogComponent],
})
export class CollaboratorModule {}
