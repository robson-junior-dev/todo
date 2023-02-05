import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICollaborator } from '../collaborator.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../collaborator.test-samples';

import { CollaboratorService, RestCollaborator } from './collaborator.service';

const requireRestSample: RestCollaborator = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
};

describe('Collaborator Service', () => {
  let service: CollaboratorService;
  let httpMock: HttpTestingController;
  let expectedResult: ICollaborator | ICollaborator[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CollaboratorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Collaborator', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const collaborator = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(collaborator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Collaborator', () => {
      const collaborator = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(collaborator).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Collaborator', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Collaborator', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Collaborator', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCollaboratorToCollectionIfMissing', () => {
      it('should add a Collaborator to an empty array', () => {
        const collaborator: ICollaborator = sampleWithRequiredData;
        expectedResult = service.addCollaboratorToCollectionIfMissing([], collaborator);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collaborator);
      });

      it('should not add a Collaborator to an array that contains it', () => {
        const collaborator: ICollaborator = sampleWithRequiredData;
        const collaboratorCollection: ICollaborator[] = [
          {
            ...collaborator,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCollaboratorToCollectionIfMissing(collaboratorCollection, collaborator);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Collaborator to an array that doesn't contain it", () => {
        const collaborator: ICollaborator = sampleWithRequiredData;
        const collaboratorCollection: ICollaborator[] = [sampleWithPartialData];
        expectedResult = service.addCollaboratorToCollectionIfMissing(collaboratorCollection, collaborator);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collaborator);
      });

      it('should add only unique Collaborator to an array', () => {
        const collaboratorArray: ICollaborator[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const collaboratorCollection: ICollaborator[] = [sampleWithRequiredData];
        expectedResult = service.addCollaboratorToCollectionIfMissing(collaboratorCollection, ...collaboratorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const collaborator: ICollaborator = sampleWithRequiredData;
        const collaborator2: ICollaborator = sampleWithPartialData;
        expectedResult = service.addCollaboratorToCollectionIfMissing([], collaborator, collaborator2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(collaborator);
        expect(expectedResult).toContain(collaborator2);
      });

      it('should accept null and undefined values', () => {
        const collaborator: ICollaborator = sampleWithRequiredData;
        expectedResult = service.addCollaboratorToCollectionIfMissing([], null, collaborator, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(collaborator);
      });

      it('should return initial array if no Collaborator is added', () => {
        const collaboratorCollection: ICollaborator[] = [sampleWithRequiredData];
        expectedResult = service.addCollaboratorToCollectionIfMissing(collaboratorCollection, undefined, null);
        expect(expectedResult).toEqual(collaboratorCollection);
      });
    });

    describe('compareCollaborator', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCollaborator(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCollaborator(entity1, entity2);
        const compareResult2 = service.compareCollaborator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCollaborator(entity1, entity2);
        const compareResult2 = service.compareCollaborator(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCollaborator(entity1, entity2);
        const compareResult2 = service.compareCollaborator(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
