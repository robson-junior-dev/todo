import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CollaboratorDeleteDialogComponent } from './delete/collaborator-delete-dialog.component';
import { CollaboratorDetailComponent } from './detail/collaborator-detail.component';
import { CollaboratorComponent } from './list/collaborator.component';
import { CollaboratorRoutingModule } from './route/collaborator-routing.module';
import { CollaboratorUpdateComponent } from './update/collaborator-update.component';
@NgModule({
  imports: [SharedModule, CollaboratorRoutingModule],
  declarations: [CollaboratorComponent, CollaboratorDetailComponent, CollaboratorUpdateComponent, CollaboratorDeleteDialogComponent],
})
export class CollaboratorModule {}
