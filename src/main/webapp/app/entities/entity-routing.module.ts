import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'collaborator',
        data: { pageTitle: 'todoApp.collaborator.home.title' },
        loadChildren: () => import('./collaborator/collaborator.module').then(m => m.CollaboratorModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'todoApp.project.home.title' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'sprint',
        data: { pageTitle: 'todoApp.sprint.home.title' },
        loadChildren: () => import('./sprint/sprint.module').then(m => m.SprintModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'todoApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
