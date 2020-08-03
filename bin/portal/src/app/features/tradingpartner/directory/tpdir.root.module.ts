
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { TranslateModule } from "@ngx-translate/core";
import { FormsModule } from "@angular/forms";
import { SelectDropdownModule } from 'tgocp-ng/dist/components/selectDropdown/selectDropdown';
import { CalendarModule } from 'tgocp-ng/dist/components/calendar/calendar';
import { CheckboxModule } from 'tgocp-ng/dist/components/checkbox/checkbox';
import { ModalModule } from 'tgocp-ng/dist/components/modal/modal';
import { OtDateTimePipeModule } from 'tgocp-ng/dist/pipes/ot-datetime.pipe';
import { MenuOverlayPanelModule } from 'tgocp-ng/dist/components/menu-overlaypanel/menu-overlaypanel';
import { RadioButtonModule } from 'tgocp-ng/dist/components/radioButton/radioButton.component';
import { DataTableModule } from 'tgocp-ng/dist/components/datatable/datatable';
import { OtDatePipeModule } from 'tgocp-ng/dist/pipes/ot-date.pipe';
import { OtDataListModule } from 'tgocp-ng/dist/components/datalist/datalist';
import { AccordionPanelModule } from 'tgocp-ng/dist/components/accordianPanel/accordion-panel';
import { AccordionModule } from 'tgocp-ng/dist/components/accordion/accordion';
import { TooltipModule } from 'tgocp-ng/dist/directives/tooltip/tooltip.directive';
import { TrimModule } from 'tgocp-ng/dist/directives/trim/trim.directive';

// TP related
import { TPDirRootComponent } from "./tpdir.root.component";
import { TradingPartnerListContainerComponent } from './list/tplist.container.component';
import { BusinessPartnerListComponent } from "./list/bpr/bpr-list.component";
import { TradingPartnerListComponent } from './list/tpr/tpr-list.component';
import { TPFilterOptionsResolver } from "../../../resolvers/tp.options.resolver";
import { WorkflowGuard } from '../create/workflow/workflow-guard.service';
import { WorkflowService } from '../create/workflow/workflow.service';
import { FormDataService } from '../create/entity/formData.service';
import { KeyMapsComponent } from '../create/keys.component';
import { BaseComponent } from '../create/base.component';
import { NavComponent } from '../create/nav.component';
import { TradingPartnerCreateComponent } from '../create/tpcreate.component';
import { TradingPartnerCoreComponent } from '../create/core/tp-core.component';
import { TradingParnterDetailsService } from '../../../services/trading-partner-details.service';
import { TPGraphService } from '../../../services/tp-graph.service';
import { TPCreateMsgService } from '../../../services/tp-create.service';
import { UserInfoResolver } from "../../../resolvers/user.info.resolver";
import { PerfectScrollbarConfigInterface, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';

import { CreateInvitationComponent } from './create-invitation/createInvitation.component';
import { TpMsgBaseComponent } from './createTPMSG/tpMsgBase.component';
import { StepperComponent } from './createTPMSG/stepper/stepper.component';
import { TpMsgRoutingModule } from './createTPMSG/tpMsgBaseRouting.module';
import { CompanyDetailsComponent } from './createTPMSG/add/companyDetails.component';
import { MsgDetailsComponent } from './createTPMSG/add/msgDetails.component';
import { TPMSGService } from '../../../services/tpmsg.service';
import { SummaryComponent } from './createTPMSG/add/summary.component';
import { WorkflowTpMsgGuard } from './createTPMSG/entity/workflow-tpmsgguard.service';
import { WorkflowTpMsgService } from './createTPMSG/entity/workflowtpmsg.service';
import { InvitedListComponent } from './list/invited/invited-list.component';
import { ApprovalSummaryComp } from './list/bpr/tpApproval/approval-summary.comp';
import { MsgDetailsTGMSComponent } from './createTPMSG/add/msgDetails-TGMS.component';
import { TradingParnterService } from '../../../services/trading-partner.service';
import { TPRDirectoryRoutingModule } from './tpdir.root-routing.module';
import { DialogueboxModule } from 'tgocp-ng/dist';
import { WorkFlowSelectionModule } from './list/bpr/workflow-selection/workflow-selection.module';
import { JsonSchemaFormModule } from 'json-editor/dist/src/app/json-schema-form/index';
import { SchemaFormOutputService } from 'json-editor/dist/src/app/services/schema-form-output.service';
import { NgxPermissionsModule, NgxPermissionsService } from 'ngx-permissions';
import { WorkflowExecutorModule } from 'json-editor/dist/src/app/workflow-executor';
import { JsonSharedService } from 'json-editor/dist/src/app/services/json-shared.service';
import { CompanyRegistrationService } from '../../../services/company.registration.service';
import { BulkImportModule } from './list/bpr/bulk-import/bulk-import.module';

const PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

@NgModule({
  imports: [
    PerfectScrollbarModule,
    CommonModule, TPRDirectoryRoutingModule, FormsModule, TpMsgRoutingModule,
    CalendarModule, WorkFlowSelectionModule, JsonSchemaFormModule,
    DataTableModule, OtDatePipeModule,
    CheckboxModule, RadioButtonModule, TranslateModule, OtDataListModule
    , AccordionModule, PerfectScrollbarModule, SelectDropdownModule, AccordionPanelModule,
    ModalModule, OtDateTimePipeModule, MenuOverlayPanelModule, TooltipModule, TrimModule, DialogueboxModule,
    NgxPermissionsModule, WorkflowExecutorModule, BulkImportModule
  ],
  declarations: [
    TradingPartnerListContainerComponent, TpMsgBaseComponent, StepperComponent, SummaryComponent, CompanyDetailsComponent, MsgDetailsComponent, MsgDetailsTGMSComponent,
    TradingPartnerListComponent, ApprovalSummaryComp,
    TradingPartnerCreateComponent, BusinessPartnerListComponent,
    TPDirRootComponent, BaseComponent,
    NavComponent, KeyMapsComponent,
    TradingPartnerCoreComponent, CreateInvitationComponent, InvitedListComponent],
  providers: [
    TPCreateMsgService, TradingParnterDetailsService, TPGraphService,
    DatePipe, TPFilterOptionsResolver, UserInfoResolver, WorkflowService, TPMSGService, SchemaFormOutputService,
    FormDataService, WorkflowGuard, WorkflowTpMsgGuard, WorkflowTpMsgService, TradingParnterService, NgxPermissionsService,
    JsonSharedService, CompanyRegistrationService]
})
export class TPDirectoryRootModule { }
