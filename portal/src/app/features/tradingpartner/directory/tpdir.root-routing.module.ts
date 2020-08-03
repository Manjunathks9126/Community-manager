import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TPFilterOptionsResolver } from "../../../resolvers/tp.options.resolver";
import { UserInfoResolver } from "../../../resolvers/user.info.resolver";
import { TPDirRootComponent } from "./tpdir.root.component";
import { BaseComponent } from '../create/base.component';
import { KeyMapsComponent } from '../create/keys.component';
import { WorkflowGuard } from '../create/workflow/workflow-guard.service';
import { TradingPartnerCreateComponent } from '../create/tpcreate.component';
import { TradingPartnerListContainerComponent } from './list/tplist.container.component';
import { TradingPartnerCoreComponent } from '../create/core/tp-core.component';
import { CreateInvitationComponent } from './create-invitation/createInvitation.component';
import { TpMsgBaseComponent } from './createTPMSG/tpMsgBase.component';
import { CompanyDetailsComponent } from './createTPMSG/add/companyDetails.component';
import { MsgDetailsComponent } from './createTPMSG/add/msgDetails.component';
import { SummaryComponent } from './createTPMSG/add/summary.component';
import { WorkflowTpMsgGuard } from './createTPMSG/entity/workflow-tpmsgguard.service';
import { ApprovalSummaryComp } from './list/bpr/tpApproval/approval-summary.comp';
import { MsgDetailsTGMSComponent } from './createTPMSG/add/msgDetails-TGMS.component';
import { NgxPermissionsGuard } from 'ngx-permissions';
import { PERMISSSION } from './createTPMSG/entity/permission.constants';

const TPDirRoutes: Routes = [
  {
    path: '', component: TPDirRootComponent, resolve: { TPOPtions: TPFilterOptionsResolver, userLocale: UserInfoResolver },
    children: [
      { path: '', component: TradingPartnerListContainerComponent, data: { 'breadcrumb-label': 'tpdir' } },
      { path: 'core_ot', component: TradingPartnerCoreComponent, canActivate: [NgxPermissionsGuard], data: { permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } } },
      {
        path: 'base', component: BaseComponent, canActivate: [NgxPermissionsGuard], data: { 'breadcrumb-label': 'tpdir.create', permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } },
        children: [
          { path: '', redirectTo: 'add', pathMatch: 'full' },
          { path: 'add', component: TradingPartnerCreateComponent, data: { 'breadcrumb-label': 'tpdir.create' } },
          { path: 'work', component: KeyMapsComponent, data: { 'breadcrumb-label': 'tpdir.create' }, canActivate: [WorkflowGuard] },
        ]
      },
      { path: 'createInvitation/:id', component: CreateInvitationComponent, canActivate: [NgxPermissionsGuard], data: { permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } } },
      { path: 'approval', component: ApprovalSummaryComp },
      {
        path: 'tpmsgbase', component: TpMsgBaseComponent, canActivate: [NgxPermissionsGuard], data: { 'breadcrumb-label': 'tpdir.create', permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } }, children: [
          { path: '', redirectTo: 'add', pathMatch: 'full' },
          { path: 'add', component: CompanyDetailsComponent, data: { 'breadcrumb-label': 'tpdir.create' } },
          { path: 'msgDetails', component: MsgDetailsComponent, canActivate: [WorkflowTpMsgGuard] },
          { path: 'summary', component: SummaryComponent, canActivate: [WorkflowTpMsgGuard] },
        ]
      },
      {
        path: 'tgmsbase', component: TpMsgBaseComponent, canActivate: [NgxPermissionsGuard], data: { 'breadcrumb-label': 'tpdir.create', permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } }, children: [
          { path: '', redirectTo: 'add', pathMatch: 'full' },
          { path: 'add', component: CompanyDetailsComponent, data: { 'breadcrumb-label': 'tpdir.create' } },
          { path: 'billingSplit', component: MsgDetailsTGMSComponent, canActivate: [WorkflowTpMsgGuard] },
          { path: 'summary', component: SummaryComponent, canActivate: [WorkflowTpMsgGuard] },
        ]
      },
      { path: 'company/:id', loadChildren: '../companyprofile/company.profile.module#CompanyProfileModule' },
      { path: 'graph', loadChildren: './tp-graph/tpgraph.module#TPGraphModule' },
      { path: 'invitedPartner/:workflowId', loadChildren: '../invitedPartnerProfile/invPartner-profile.module#InvitePartnerProfileModule' }

    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(
      TPDirRoutes
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class TPRDirectoryRoutingModule { }
