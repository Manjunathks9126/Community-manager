import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GraphComponent } from './tp-graph.component';
import { TranslateService } from "@ngx-translate/core";
import { ActivatedRoute } from "@angular/router";
import { UserInfoResolver } from '../../../../resolvers/user.info.resolver';

const TPGraphRoutes: Routes = [
  {
    path: '', component: GraphComponent, resolve: {
      userLocale: UserInfoResolver
    },
    data: { 'breadcrumb-label': 'graph' }
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(
      TPGraphRoutes
    ),
  ],
  exports: [
    RouterModule
  ],
  providers: []
})
export class TPGraphRoutingModule {
  constructor(private translate: TranslateService, private route: ActivatedRoute) { }
  ngOnInit(): void {
    this.translate.use(this.route.snapshot.data['userLocale']);
  }
}