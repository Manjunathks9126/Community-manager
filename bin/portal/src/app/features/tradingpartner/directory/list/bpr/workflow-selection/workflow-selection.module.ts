import { NgModule } from '@angular/core';
import { WorkflowSelection } from './workflow-selection.component';
import { ModalModule, TooltipModule, RadioButtonModule, SelectDropdownModule } from 'tgocp-ng/dist';
import { TranslateModule } from '@ngx-translate/core';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';

@NgModule({
    imports:[ModalModule,TranslateModule,TooltipModule,PerfectScrollbarModule,RadioButtonModule,SelectDropdownModule],
    declarations: [
        WorkflowSelection
    ],
    exports: [
        WorkflowSelection
    ]
})
  
export class WorkFlowSelectionModule {}