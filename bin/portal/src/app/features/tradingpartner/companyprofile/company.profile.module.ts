import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { TranslateModule } from "@ngx-translate/core";
import { FormsModule } from "@angular/forms";

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';

import { TradingParnterService } from '../../../services/trading-partner.service';
import { TradingParnterDetailsService } from '../../../services/trading-partner-details.service';
import { UserInfoResolver } from "../../../resolvers/user.info.resolver";
import { CompanyProfileRoutingModule } from './company.profile-routing.module';
import { EDICommsRootComponent } from './edicomms/edicomms.root.component';
import { CompanyProfileComponent } from './comp.profile.component';
import { CompanyOverviewComponent } from './overview/comp.overview.component';
import { CompanyContactsComponent } from './contacts/comp.contacts.list.component';
import { CompanyUserComponent } from './users/comp.user.component';
import { CompanyContactsRootComponent } from './contacts/comp.contacts.root.component';
import { PartnerMergedEDIListComponent } from './edicomms/partnerediaddress/partner.mergedediaddress.list.component';
import { PartnerEDIListComponent } from './edicomms/partnerediaddress/partner.ediaddress.list.component';
import { CustomdataComponent } from './customdata/customdata.component';
import { MapsComponent } from './edicomms/maps/maps.component';
import { HistoryRootComponent } from './history/history.root.component';
import { OnboardingHistoryComponent } from './history/onboarding/onboarding.history.component';
import { TPsetupComponent } from './history/onboarding/tpsetup/tpsetup.component';
import { JsonTree } from './history/auditlog/jsonTree/json.tree.component';
import { AuditLogComponent } from './history/auditlog/audit.log.component';
import { CalendarModule, DataTableModule, OtDatePipeModule, CheckboxModule, RadioButtonModule, OtDataListModule, AccordionModule, SelectDropdownModule, AccordionPanelModule, ModalModule, OtDateTimePipeModule, MenuOverlayPanelModule, TooltipModule, TrimModule, DialogueboxModule } from 'tgocp-ng/dist';
import { WorkFlowSelectionModule } from '../directory/list/bpr/workflow-selection/workflow-selection.module';

import { JsonSchemaFormModule } from 'json-editor/dist/src/app/json-schema-form/index';
import { CustomTaskExecutorModule } from 'json-editor/dist/src/app/custom-task-executor/index';
import { CustomTaskService } from 'json-editor/dist/src/app/custom-task-executor/custom-task-service';
import { OtherInfo } from './gds/other-info';
import { EdiAndDocs } from './gds/edi-docs';
import { NgxPermissionsModule } from 'ngx-permissions';

@NgModule({
  imports: [
    PerfectScrollbarModule, CommonModule, CompanyProfileRoutingModule, FormsModule,
    CalendarModule,WorkFlowSelectionModule,
    DataTableModule, OtDatePipeModule, 
    CheckboxModule, RadioButtonModule, TranslateModule, OtDataListModule
    , AccordionModule, SelectDropdownModule, AccordionPanelModule,
    ModalModule, OtDateTimePipeModule, MenuOverlayPanelModule, TooltipModule, TrimModule, PerfectScrollbarModule, DialogueboxModule,JsonSchemaFormModule,CustomTaskExecutorModule,NgxPermissionsModule
  ],
  declarations: [
    EDICommsRootComponent,
    CompanyProfileComponent,OtherInfo,EdiAndDocs,
    CompanyOverviewComponent, AuditLogComponent, CompanyContactsComponent,
    CompanyUserComponent, CompanyContactsRootComponent,
    PartnerEDIListComponent, PartnerMergedEDIListComponent,
    JsonTree, CustomdataComponent,
    MapsComponent, HistoryRootComponent, OnboardingHistoryComponent, TPsetupComponent
  ],
  providers: [
    DatePipe, UserInfoResolver,
    TradingParnterService, TradingParnterDetailsService,CustomTaskService]
})
export class CompanyProfileModule { }
