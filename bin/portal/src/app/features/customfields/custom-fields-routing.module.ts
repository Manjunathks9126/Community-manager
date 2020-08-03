import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserInfoResolver } from '../../resolvers/user.info.resolver';
import { CustomFieldsRootComponent } from './custom-fields.root.component';
import { ManagementComponent } from './management/management.component';

const CustomFieldsRoutes: Routes = [
  {
    path: '', component: CustomFieldsRootComponent, resolve: { userLocale: UserInfoResolver },
    children: [
      { path: '', component: ManagementComponent, data: { 'breadcrumb-label': 'customField' } }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(CustomFieldsRoutes)],
  exports: [RouterModule]
})
export class CustomFieldsRoutingModule { }
