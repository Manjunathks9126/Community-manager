import { NgModule } from "@angular/core";
import { CommonModule } from '@angular/common';
import { OnboardingRoutingModule } from "./onboarding-routing.module";
import { TranslateModule } from "@ngx-translate/core";
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface } from "ngx-perfect-scrollbar";
import { FormsModule } from "@angular/forms";
import { CalendarModule, OtDatePipeModule, CheckboxModule, RadioButtonModule, AccordionModule, SelectDropdownModule, AccordionPanelModule, TooltipModule, DataTableModule, OtDateTimePipeModule, TrimModule,MultiSelectModule, DialogueboxModule } from "tgocp-ng/dist";
import { WorkflowOverviewComponent } from "./workflow-profile/workflow-overview/workflow-overview.component";
import { OnboardingRootComponent } from "./onboarding.root.component";
import { WorkflowsListComponent } from "./workflows-list/workflows-list.component";
import { WorkflowProfileComponent } from "./workflow-profile/workflow-profile.component";
import { InvitationComponent } from "./workflow-profile/invitations/InvitationForm/invitation.component";
import { InvitationsListComponent } from "./workflow-profile/invitations/invitations-list/invitations-list.component";
import { InvitationPreviewComponent } from "./workflow-profile/invitations/invitationPreview/invitation-preview.component";
import { InvitationOverviewComponent } from "./workflow-profile/invitations/invitation-overview/invitation-overview.component";
import { TaskListComponent } from "./workflow-profile/tasks/tasks-list/tasks-list.component";
import { TaskOverviewComponent } from './workflow-profile/tasks/task-overview/task-overview.component';
import { TaskComponent } from './workflow-profile/tasks/task-edit/task.component';
import { CustomFieldsService } from '../../services/custom-fields.service';
const PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };
@NgModule({
    imports: [ PerfectScrollbarModule, CommonModule, 
        FormsModule ,  OnboardingRoutingModule, PerfectScrollbarModule, OtDateTimePipeModule, OtDatePipeModule,
        TranslateModule, CalendarModule, OtDatePipeModule, CheckboxModule, RadioButtonModule,
        TranslateModule, AccordionModule, SelectDropdownModule, AccordionPanelModule,
        TooltipModule, DataTableModule, TrimModule,MultiSelectModule,DialogueboxModule],
    declarations: [OnboardingRootComponent, WorkflowsListComponent, 
        WorkflowProfileComponent, InvitationComponent,WorkflowOverviewComponent,InvitationsListComponent,InvitationPreviewComponent,InvitationOverviewComponent,
        TaskListComponent,TaskOverviewComponent,TaskComponent
        
    ],
    providers: [CustomFieldsService]
})
export class OnboardingRootModule { }