import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { PerfectScrollbarConfigInterface, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { TranslateModule } from '@ngx-translate/core';
import {
  AccordionModule, TooltipModule, DataTableModule, ModalModule, SelectDropdownModule, CheckboxModule, DialogueboxModule, AccordionPanelModule, BubbleTipModule,
  OnlyDigitsModule, TrimModule, MenuOverlayPanelModule,FocusModule, NumericModule, RadioButtonModule,MultiSelectModule, CalendarModule
} from 'tgocp-ng/dist';

import { CustomFieldsService } from '../../services/custom-fields.service';
import { CustomFieldsRoutingModule } from './custom-fields-routing.module';
import { ManagementComponent } from './management/management.component';
import { CustomFieldsRootComponent } from './custom-fields.root.component';
import { CustomFieldComponent } from './customField/custom-field.component';
import { EditorModule } from 'tgocp-ng/dist/components/editor/editor.component';
import { minValidatorModule } from 'tgocp-ng/dist/directives/number-range-validator/minimun.validator';



const PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

@NgModule({
  imports: [
   EditorModule, CommonModule, PerfectScrollbarModule, TranslateModule, FormsModule, CheckboxModule, CalendarModule, AccordionPanelModule, BubbleTipModule,
    CustomFieldsRoutingModule, AccordionModule, TooltipModule, DataTableModule, ModalModule, SelectDropdownModule,
    DialogueboxModule, OnlyDigitsModule, TrimModule,MenuOverlayPanelModule,FocusModule,NumericModule,RadioButtonModule,MultiSelectModule, minValidatorModule
  ],
  declarations: [ManagementComponent, CustomFieldsRootComponent,CustomFieldComponent],
    
  providers: [CustomFieldsService]
})
export class CustomFieldsRootModule { }
