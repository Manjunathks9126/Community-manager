import { Component, OnInit } from '@angular/core';
import { User } from './user.entity';
import { ActivatedRoute, Router } from '@angular/router';
import { BreadCrumbsService } from "tgocp-ng/dist";
import { CompanyProfileService } from '../../../../services/company-profile.service';
import { NotificationHandler } from '../../../../util/exception/notfication.handler';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'tp-profile-user',
  templateUrl: './comp.user.component.html'
})
export class CompanyUserComponent implements OnInit {

  userDetail: any = new User();

  constructor(private activatedRoute: ActivatedRoute, private service: CompanyProfileService,
    private notficationHandler: NotificationHandler, private breadCrumbService: BreadCrumbsService,
    private router: Router, private translate: TranslateService) { }

  ngOnInit() {
    let userId = this.activatedRoute.snapshot.params['userId'];
    this.userDetail = this.service.getUserById(userId);
    this.breadCrumbService.addBreadCrumbItem({ label: this.userDetail.firstName + " " + this.userDetail.lastName, url: this.router.url });
    if (!this.userDetail) {
      this.notficationHandler.notify({ severity: 'error', details: this.translate.instant("tpdir.company.user.selectUserID") });
    }
  }

}
