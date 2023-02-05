import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICollaborator } from '../collaborator.model';
import { CollaboratorService } from '../service/collaborator.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './collaborator-delete-dialog.component.html',
})
export class CollaboratorDeleteDialogComponent {
  collaborator?: ICollaborator;

  constructor(protected collaboratorService: CollaboratorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.collaboratorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
