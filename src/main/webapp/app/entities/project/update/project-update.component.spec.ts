import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProjectFormService } from './project-form.service';
import { ProjectService } from '../service/project.service';
import { IProject } from '../project.model';
import { ICollaborator } from 'app/entities/collaborator/collaborator.model';
import { CollaboratorService } from 'app/entities/collaborator/service/collaborator.service';

import { ProjectUpdateComponent } from './project-update.component';

describe('Project Management Update Component', () => {
  let comp: ProjectUpdateComponent;
  let fixture: ComponentFixture<ProjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectFormService: ProjectFormService;
  let projectService: ProjectService;
  let collaboratorService: CollaboratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProjectUpdateComponent],
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
      .overrideTemplate(ProjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectFormService = TestBed.inject(ProjectFormService);
    projectService = TestBed.inject(ProjectService);
    collaboratorService = TestBed.inject(CollaboratorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Collaborator query and add missing value', () => {
      const project: IProject = { id: 456 };
      const collaborators: ICollaborator[] = [{ id: 19112 }];
      project.collaborators = collaborators;

      const collaboratorCollection: ICollaborator[] = [{ id: 69779 }];
      jest.spyOn(collaboratorService, 'query').mockReturnValue(of(new HttpResponse({ body: collaboratorCollection })));
      const additionalCollaborators = [...collaborators];
      const expectedCollection: ICollaborator[] = [...additionalCollaborators, ...collaboratorCollection];
      jest.spyOn(collaboratorService, 'addCollaboratorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(collaboratorService.query).toHaveBeenCalled();
      expect(collaboratorService.addCollaboratorToCollectionIfMissing).toHaveBeenCalledWith(
        collaboratorCollection,
        ...additionalCollaborators.map(expect.objectContaining)
      );
      expect(comp.collaboratorsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const project: IProject = { id: 456 };
      const collaborator: ICollaborator = { id: 38382 };
      project.collaborators = [collaborator];

      activatedRoute.data = of({ project });
      comp.ngOnInit();

      expect(comp.collaboratorsSharedCollection).toContain(collaborator);
      expect(comp.project).toEqual(project);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue(project);
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectService.update).toHaveBeenCalledWith(expect.objectContaining(project));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectFormService, 'getProject').mockReturnValue({ id: null });
      jest.spyOn(projectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: project }));
      saveSubject.complete();

      // THEN
      expect(projectFormService.getProject).toHaveBeenCalled();
      expect(projectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProject>>();
      const project = { id: 123 };
      jest.spyOn(projectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ project });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
