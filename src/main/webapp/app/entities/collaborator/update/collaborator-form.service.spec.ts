import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../collaborator.test-samples';

import { CollaboratorFormService } from './collaborator-form.service';

describe('Collaborator Form Service', () => {
  let service: CollaboratorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CollaboratorFormService);
  });

  describe('Service methods', () => {
    describe('createCollaboratorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCollaboratorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            photo: expect.any(Object),
            document: expect.any(Object),
            cpf: expect.any(Object),
            isLeader: expect.any(Object),
            user: expect.any(Object),
            leader: expect.any(Object),
            projects: expect.any(Object),
          })
        );
      });

      it('passing ICollaborator should create a new form with FormGroup', () => {
        const formGroup = service.createCollaboratorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            dateOfBirth: expect.any(Object),
            photo: expect.any(Object),
            document: expect.any(Object),
            cpf: expect.any(Object),
            isLeader: expect.any(Object),
            user: expect.any(Object),
            leader: expect.any(Object),
            projects: expect.any(Object),
          })
        );
      });
    });

    describe('getCollaborator', () => {
      it('should return NewCollaborator for default Collaborator initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCollaboratorFormGroup(sampleWithNewData);

        const collaborator = service.getCollaborator(formGroup) as any;

        expect(collaborator).toMatchObject(sampleWithNewData);
      });

      it('should return NewCollaborator for empty Collaborator initial value', () => {
        const formGroup = service.createCollaboratorFormGroup();

        const collaborator = service.getCollaborator(formGroup) as any;

        expect(collaborator).toMatchObject({});
      });

      it('should return ICollaborator', () => {
        const formGroup = service.createCollaboratorFormGroup(sampleWithRequiredData);

        const collaborator = service.getCollaborator(formGroup) as any;

        expect(collaborator).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICollaborator should not enable id FormControl', () => {
        const formGroup = service.createCollaboratorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCollaborator should disable id FormControl', () => {
        const formGroup = service.createCollaboratorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
