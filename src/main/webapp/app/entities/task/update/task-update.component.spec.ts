import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaskFormService } from './task-form.service';
import { TaskService } from '../service/task.service';
import { ITask } from '../task.model';
import { ISprint } from 'app/entities/sprint/sprint.model';
import { SprintService } from 'app/entities/sprint/service/sprint.service';
import { ICollaborator } from 'app/entities/collaborator/collaborator.model';
import { CollaboratorService } from 'app/entities/collaborator/service/collaborator.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Task Management Update Component', () => {
  let comp: TaskUpdateComponent;
  let fixture: ComponentFixture<TaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskFormService: TaskFormService;
  let taskService: TaskService;
  let sprintService: SprintService;
  let collaboratorService: CollaboratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaskUpdateComponent],
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
      .overrideTemplate(TaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskFormService = TestBed.inject(TaskFormService);
    taskService = TestBed.inject(TaskService);
    sprintService = TestBed.inject(SprintService);
    collaboratorService = TestBed.inject(CollaboratorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sprint query and add missing value', () => {
      const task: ITask = { id: 456 };
      const sprint: ISprint = { id: 52464 };
      task.sprint = sprint;

      const sprintCollection: ISprint[] = [{ id: 10346 }];
      jest.spyOn(sprintService, 'query').mockReturnValue(of(new HttpResponse({ body: sprintCollection })));
      const additionalSprints = [sprint];
      const expectedCollection: ISprint[] = [...additionalSprints, ...sprintCollection];
      jest.spyOn(sprintService, 'addSprintToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(sprintService.query).toHaveBeenCalled();
      expect(sprintService.addSprintToCollectionIfMissing).toHaveBeenCalledWith(
        sprintCollection,
        ...additionalSprints.map(expect.objectContaining)
      );
      expect(comp.sprintsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Collaborator query and add missing value', () => {
      const task: ITask = { id: 456 };
      const collaborator: ICollaborator = { id: 44484 };
      task.collaborator = collaborator;

      const collaboratorCollection: ICollaborator[] = [{ id: 95840 }];
      jest.spyOn(collaboratorService, 'query').mockReturnValue(of(new HttpResponse({ body: collaboratorCollection })));
      const additionalCollaborators = [collaborator];
      const expectedCollection: ICollaborator[] = [...additionalCollaborators, ...collaboratorCollection];
      jest.spyOn(collaboratorService, 'addCollaboratorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(collaboratorService.query).toHaveBeenCalled();
      expect(collaboratorService.addCollaboratorToCollectionIfMissing).toHaveBeenCalledWith(
        collaboratorCollection,
        ...additionalCollaborators.map(expect.objectContaining)
      );
      expect(comp.collaboratorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const task: ITask = { id: 456 };
      const sprint: ISprint = { id: 33296 };
      task.sprint = sprint;
      const collaborator: ICollaborator = { id: 812 };
      task.collaborator = collaborator;

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(comp.sprintsSharedCollection).toContain(sprint);
      expect(comp.collaboratorsSharedCollection).toContain(collaborator);
      expect(comp.task).toEqual(task);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 123 };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue(task);
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskService.update).toHaveBeenCalledWith(expect.objectContaining(task));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 123 };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue({ id: null });
      jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(taskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 123 };
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSprint', () => {
      it('Should forward to sprintService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sprintService, 'compareSprint');
        comp.compareSprint(entity, entity2);
        expect(sprintService.compareSprint).toHaveBeenCalledWith(entity, entity2);
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
