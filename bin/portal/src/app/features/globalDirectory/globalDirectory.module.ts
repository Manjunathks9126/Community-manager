import { NgModule } from '@angular/core';
import { PerfectScrollbarModule, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DataTableModule, OtDatePipeModule, CheckboxModule, RadioButtonModule, OtDataListModule, AccordionModule, SelectDropdownModule, AccordionPanelModule, ModalModule, OtDateTimePipeModule, MenuOverlayPanelModule, TooltipModule, TrimModule, AutoCompleteModule, TreeCheckboxModule, DialogueboxModule } from 'tgocp-ng/dist';
import { TranslateModule } from '@ngx-translate/core';
import { GlobalDirectoryRoutingModule } from './globalDiectory-routing.module';
import { GlobalDirectory } from './globalDirectory.component';
import { GlobalDirectoryService } from '../../services/global-directory.service';
import { WorkFlowSelectionModule } from '../tradingpartner/directory/list/bpr/workflow-selection/workflow-selection.module';


const PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
};

@NgModule({
    imports: [GlobalDirectoryRoutingModule, WorkFlowSelectionModule,DialogueboxModule,
        CommonModule, FormsModule,
        AutoCompleteModule,TreeCheckboxModule,
        DataTableModule, OtDatePipeModule,
        CheckboxModule, RadioButtonModule, TranslateModule, OtDataListModule
        , AccordionModule, PerfectScrollbarModule, SelectDropdownModule, AccordionPanelModule,
        ModalModule, OtDateTimePipeModule, MenuOverlayPanelModule, TooltipModule, TrimModule],
    declarations: [GlobalDirectory],
    providers: [GlobalDirectoryService]
})
export class GlobalDirectoryModule {


}