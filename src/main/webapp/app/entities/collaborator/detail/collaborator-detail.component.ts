import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollaborator } from '../collaborator.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-collaborator-detail',
  templateUrl: './collaborator-detail.component.html',
})
export class CollaboratorDetailComponent implements OnInit {
  collaborator: ICollaborator | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ collaborator }) => {
      this.collaborator = collaborator;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
