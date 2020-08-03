import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInfoResolver } from '../../resolvers/user.info.resolver';
import { OnboardingRootComponent } from './onboarding.root.component';
import { WorkflowsListComponent } from './workflows-list/workflows-list.component';
import { WorkflowProfileComponent } from './workflow-profile/workflow-profile.component';
import { WorkflowOverviewComponent } from './workflow-profile/workflow-overview/workflow-overview.component';
import { InvitationComponent } from './workflow-profile/invitations/InvitationForm/invitation.component';
import { InvitationPreviewComponent } from './workflow-profile/invitations/invitationPreview/invitation-preview.component';
import { InvitationsListComponent } from './workflow-profile/invitations/invitations-list/invitations-list.component';
import { InvitationOverviewComponent } from './workflow-profile/invitations/invitation-overview/invitation-overview.component';
import { TaskListComponent } from './workflow-profile/tasks/tasks-list/tasks-list.component';
import { TaskOverviewComponent } from './workflow-profile/tasks/task-overview/task-overview.component';
import { TaskComponent } from './workflow-profile/tasks/task-edit/task.component';

const onboardingRoutes: Routes = [
  {
    path: '', component: OnboardingRootComponent, resolve: { userLocale: UserInfoResolver },
    children: [

      { path: '', component: WorkflowsListComponent, data: { 'breadcrumb-label': 'onboarding' } },
      {
        path: 'workflow/:id', component: WorkflowProfileComponent,
        children: [
          { path: '', redirectTo: 'overview', pathMatch: 'full' },
          { path: 'overview', component: WorkflowOverviewComponent, data: { editMode: false } },

          { path: 'overview/edit', component: WorkflowOverviewComponent, data: { editMode: true } },

          { path: 'invitations', component: InvitationsListComponent },
          {
              path: 'invitations/create', component: InvitationComponent,data:{ editMode: false }
          },
          {
            path: 'invitations/preview', component: InvitationPreviewComponent
          },
      //    { path: 'invitations/edit/:name', component: InvitationComponent },
	   { path: 'invitations/overview/:id', component: InvitationOverviewComponent},
          { path: 'invitations/edit/:id', component: InvitationComponent,data: { editMode: true }},

          { path: 'tasks', component: TaskListComponent, data: { editMode: false } },
          { path: 'tasks/create', component: TaskComponent, data: { editMode: false } },
          { path: 'tasks/overview/:id', component: TaskOverviewComponent, data: { editMode: false } },
          { path: 'tasks/edit/:id', component: TaskComponent, data: { editMode: true } },


          
        ]
      }
    ]

  }
];
@NgModule({
  imports: [
    RouterModule.forChild(
      onboardingRoutes
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class OnboardingRoutingModule { }
