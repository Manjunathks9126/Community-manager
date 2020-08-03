import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommunityPageComponent } from './community-page.component';
import { UserInfoResolver } from '../../resolvers/user.info.resolver';


const communityPageRoutes: Routes = [
  { path: '', component: CommunityPageComponent, data: { 'breadcrumb-label': 'communitypage' },resolve: {
    userLocale: UserInfoResolver,
  } },
];

@NgModule({
  imports: [
    RouterModule.forChild(
      communityPageRoutes
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class CommunityPageRoutingModule { }