import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInfoResolver } from '../../resolvers/user.info.resolver';
import { GlobalDirectory } from './globalDirectory.component';


const globalSearchRoutes: Routes = [
  { path: '', component: GlobalDirectory, data: { 'breadcrumb-label': 'globalSearch' },resolve: {
    userLocale: UserInfoResolver,
  } }
 //, { path: 'company/:id', loadChildren: '../companyprofile/company.profile.module#CompanyProfileModule' }
];

@NgModule({
  imports: [
    RouterModule.forChild(
        globalSearchRoutes
    ),
  ], 
  exports: [
    RouterModule
  ],
  providers: []
})
export class GlobalDirectoryRoutingModule { }