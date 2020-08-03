import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NgxPermissionsGuard } from 'ngx-permissions';
import { PERMISSSION } from './features/tradingpartner/directory/createTPMSG/entity/permission.constants';
import { UserInfoResolver } from "./resolvers/user.info.resolver";
import { SystemErrorComponent } from './util/systemerror/system-error.component';

const appRoutes: Routes = [
  { path: 'tpdir', loadChildren: './features/tradingpartner/directory/tpdir.root.module#TPDirectoryRootModule' },
  { path: 'communitypage', loadChildren: './features/landingPage/community-page.module#CommunityPageModule' },
  { path: 'tpgraph', loadChildren: './features/tradingpartner/directory/tp-graph/tpgraph.module#TPGraphModule' },
  {
    path: 'globalDirectory', canActivate: [NgxPermissionsGuard], data: { permissions: { only: [PERMISSSION.enablingGlobalDirectory], redirectTo: '**' } },
    loadChildren: './features/globalDirectory/globalDirectory.module#GlobalDirectoryModule'
  },
  {
    path: 'customfieldsmgmt', canActivate: [NgxPermissionsGuard], data: { permissions: { only: [PERMISSSION.enablingCustomData], redirectTo: '**' } },
    loadChildren: './features/customfields/custom-fields.root.module#CustomFieldsRootModule'
  },
  {
    path: 'onboarding', canActivate: [NgxPermissionsGuard], data: { permissions: { only: [PERMISSSION.enablingOnboarding], redirectTo: '**' } },
    loadChildren: './features/onboarding/onboarding.root.module#OnboardingRootModule'
  },
  { path: 'errors', component: SystemErrorComponent, resolve: { userLocale: UserInfoResolver } },
  { path: '**', component: SystemErrorComponent, resolve: { userLocale: UserInfoResolver }, data: { 'PageNotFound': true } }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes, { useHash: true, enableTracing: false }
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class AppRoutingModule { }
