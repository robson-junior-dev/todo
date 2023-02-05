import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISprint } from '../sprint.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sprint.test-samples';

import { SprintService } from './sprint.service';

const requireRestSample: ISprint = {
  ...sampleWithRequiredData,
};

describe('Sprint Service', () => {
  let service: SprintService;
  let httpMock: HttpTestingController;
  let expectedResult: ISprint | ISprint[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SprintService);
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

    it('should create a Sprint', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sprint = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sprint', () => {
      const sprint = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sprint).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sprint', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sprint', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sprint', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSprintToCollectionIfMissing', () => {
      it('should add a Sprint to an empty array', () => {
        const sprint: ISprint = sampleWithRequiredData;
        expectedResult = service.addSprintToCollectionIfMissing([], sprint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sprint);
      });

      it('should not add a Sprint to an array that contains it', () => {
        const sprint: ISprint = sampleWithRequiredData;
        const sprintCollection: ISprint[] = [
          {
            ...sprint,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSprintToCollectionIfMissing(sprintCollection, sprint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sprint to an array that doesn't contain it", () => {
        const sprint: ISprint = sampleWithRequiredData;
        const sprintCollection: ISprint[] = [sampleWithPartialData];
        expectedResult = service.addSprintToCollectionIfMissing(sprintCollection, sprint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sprint);
      });

      it('should add only unique Sprint to an array', () => {
        const sprintArray: ISprint[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sprintCollection: ISprint[] = [sampleWithRequiredData];
        expectedResult = service.addSprintToCollectionIfMissing(sprintCollection, ...sprintArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sprint: ISprint = sampleWithRequiredData;
        const sprint2: ISprint = sampleWithPartialData;
        expectedResult = service.addSprintToCollectionIfMissing([], sprint, sprint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sprint);
        expect(expectedResult).toContain(sprint2);
      });

      it('should accept null and undefined values', () => {
        const sprint: ISprint = sampleWithRequiredData;
        expectedResult = service.addSprintToCollectionIfMissing([], null, sprint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sprint);
      });

      it('should return initial array if no Sprint is added', () => {
        const sprintCollection: ISprint[] = [sampleWithRequiredData];
        expectedResult = service.addSprintToCollectionIfMissing(sprintCollection, undefined, null);
        expect(expectedResult).toEqual(sprintCollection);
      });
    });

    describe('compareSprint', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSprint(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSprint(entity1, entity2);
        const compareResult2 = service.compareSprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSprint(entity1, entity2);
        const compareResult2 = service.compareSprint(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSprint(entity1, entity2);
        const compareResult2 = service.compareSprint(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
