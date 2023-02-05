import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CollaboratorFormService } from './collaborator-form.service';
import { CollaboratorService } from '../service/collaborator.service';
import { ICollaborator } from '../collaborator.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { CollaboratorUpdateComponent } from './collaborator-update.component';

describe('Collaborator Management Update Component', () => {
  let comp: CollaboratorUpdateComponent;
  let fixture: ComponentFixture<CollaboratorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let collaboratorFormService: CollaboratorFormService;
  let collaboratorService: CollaboratorService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CollaboratorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CollaboratorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CollaboratorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    collaboratorFormService = TestBed.inject(CollaboratorFormService);
    collaboratorService = TestBed.inject(CollaboratorService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const collaborator: ICollaborator = { id: 456 };
      const user: IUser = { id: 64390 };
      collaborator.user = user;

      const userCollection: IUser[] = [{ id: 44605 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collaborator });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Collaborator query and add missing value', () => {
      const collaborator: ICollaborator = { id: 456 };
      const leader: ICollaborator = { id: 76194 };
      collaborator.leader = leader;

      const collaboratorCollection: ICollaborator[] = [{ id: 93904 }];
      jest.spyOn(collaboratorService, 'query').mockReturnValue(of(new HttpResponse({ body: collaboratorCollection })));
      const additionalCollaborators = [leader];
      const expectedCollection: ICollaborator[] = [...additionalCollaborators, ...collaboratorCollection];
      jest.spyOn(collaboratorService, 'addCollaboratorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ collaborator });
      comp.ngOnInit();

      expect(collaboratorService.query).toHaveBeenCalled();
      expect(collaboratorService.addCollaboratorToCollectionIfMissing).toHaveBeenCalledWith(
        collaboratorCollection,
        ...additionalCollaborators.map(expect.objectContaining)
      );
      expect(comp.collaboratorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const collaborator: ICollaborator = { id: 456 };
      const user: IUser = { id: 33607 };
      collaborator.user = user;
      const leader: ICollaborator = { id: 46807 };
      collaborator.leader = leader;

      activatedRoute.data = of({ collaborator });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.collaboratorsSharedCollection).toContain(leader);
      expect(comp.collaborator).toEqual(collaborator);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollaborator>>();
      const collaborator = { id: 123 };
      jest.spyOn(collaboratorFormService, 'getCollaborator').mockReturnValue(collaborator);
      jest.spyOn(collaboratorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collaborator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collaborator }));
      saveSubject.complete();

      // THEN
      expect(collaboratorFormService.getCollaborator).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(collaboratorService.update).toHaveBeenCalledWith(expect.objectContaining(collaborator));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollaborator>>();
      const collaborator = { id: 123 };
      jest.spyOn(collaboratorFormService, 'getCollaborator').mockReturnValue({ id: null });
      jest.spyOn(collaboratorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collaborator: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: collaborator }));
      saveSubject.complete();

      // THEN
      expect(collaboratorFormService.getCollaborator).toHaveBeenCalled();
      expect(collaboratorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICollaborator>>();
      const collaborator = { id: 123 };
      jest.spyOn(collaboratorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ collaborator });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(collaboratorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCollaborator', () => {
      it('Should forward to collaboratorService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(collaboratorService, 'compareCollaborator');
        comp.compareCollaborator(entity, entity2);
        expect(collaboratorService.compareCollaborator).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
