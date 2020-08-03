import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CompanyOverviewComponent } from './overview/comp.overview.component';
import { CompanyContactsRootComponent } from './contacts/comp.contacts.root.component';
import { CompanyContactsComponent } from './contacts/comp.contacts.list.component';
import { CompanyUserComponent } from './users/comp.user.component';
import { EDICommsRootComponent } from './edicomms/edicomms.root.component';
import { PartnerEDIListComponent } from './edicomms/partnerediaddress/partner.ediaddress.list.component';
import { CustomdataComponent } from './customdata/customdata.component';
import { CompanyProfileComponent } from './comp.profile.component';
import { UserInfoResolver } from '../../../resolvers/user.info.resolver';
import { HistoryRootComponent } from './history/history.root.component';
import { AuditLogComponent } from './history/auditlog/audit.log.component';
import { OnboardingHistoryComponent } from './history/onboarding/onboarding.history.component';
import { TPsetupComponent } from './history/onboarding/tpsetup/tpsetup.component';
import { OtherInfo } from './gds/other-info';
import { EdiAndDocs } from './gds/edi-docs';


const CompanyProfileRoutes: Routes = [
  {
    path: '', component: CompanyProfileComponent,
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      { path: 'overview', component: CompanyOverviewComponent },
      {
        path: 'contacts', component: CompanyContactsRootComponent, data: { 'breadcrumb-label': 'tpdir.company.contacts' },
        children: [
          { path: '', component: CompanyContactsComponent },
          { path: 'user/:userId', component: CompanyUserComponent }
        ]
      },
      {
        path: 'otherInfo', component: OtherInfo, data: { 'breadcrumb-label': 'otherInfo' }
      },
      {
        path: 'ediAndDocs', component: EdiAndDocs, data: { 'breadcrumb-label': 'edicomms' }
      },
      {
        path: 'edicomms', component: EDICommsRootComponent, data: { 'breadcrumb-label': 'edicomms' },
        children: [
          { path: 'ediaddress', component: PartnerEDIListComponent, data: { 'breadcrumb-label': 'edicomms.addrs' } },
        ]
      },
      { path: 'customdata', component: CustomdataComponent, data: { 'breadcrumb-label': 'customdata' } },
      {
        path: 'history', component: HistoryRootComponent, data: { 'breadcrumb-label': 'tpdir.company.history' },
        children: [
          { path: 'auditlog', component: AuditLogComponent, data: { 'breadcrumb-label': 'tpdir.company.history.auditlog' } },
          { path: 'onboarding', component: OnboardingHistoryComponent,
            children: [
              { path: 'tpsetup', component: TPsetupComponent, data: { 'breadcrumb-label': 'tpdir.company.onboarding' }, }
            ]
          }

        ]
      },
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(
      CompanyProfileRoutes
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class CompanyProfileRoutingModule { }
