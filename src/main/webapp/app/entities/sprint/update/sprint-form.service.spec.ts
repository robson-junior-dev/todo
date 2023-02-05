import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sprint.test-samples';

import { SprintFormService } from './sprint-form.service';

describe('Sprint Form Service', () => {
  let service: SprintFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SprintFormService);
  });

  describe('Service methods', () => {
    describe('createSprintFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSprintFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            status: expect.any(Object),
            project: expect.any(Object),
          })
        );
      });

      it('passing ISprint should create a new form with FormGroup', () => {
        const formGroup = service.createSprintFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            number: expect.any(Object),
            status: expect.any(Object),
            project: expect.any(Object),
          })
        );
      });
    });

    describe('getSprint', () => {
      it('should return NewSprint for default Sprint initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSprintFormGroup(sampleWithNewData);

        const sprint = service.getSprint(formGroup) as any;

        expect(sprint).toMatchObject(sampleWithNewData);
      });

      it('should return NewSprint for empty Sprint initial value', () => {
        const formGroup = service.createSprintFormGroup();

        const sprint = service.getSprint(formGroup) as any;

        expect(sprint).toMatchObject({});
      });

      it('should return ISprint', () => {
        const formGroup = service.createSprintFormGroup(sampleWithRequiredData);

        const sprint = service.getSprint(formGroup) as any;

        expect(sprint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISprint should not enable id FormControl', () => {
        const formGroup = service.createSprintFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSprint should disable id FormControl', () => {
        const formGroup = service.createSprintFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
