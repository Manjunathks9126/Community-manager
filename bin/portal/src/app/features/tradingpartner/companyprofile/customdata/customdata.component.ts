import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as _ from "lodash";
import { TranslateService } from '@ngx-translate/core';
import { CustomFieldsService } from '../../../../services/custom-fields.service';
import { NotificationHandler } from '../../../../util/exception/notfication.handler';
import { CompanyProfileService } from '../../../../services/company-profile.service';
import { CustomTaskService } from 'json-editor/dist/src/app/custom-task-executor/custom-task-service';

@Component({
  templateUrl: './customdata.component.html',
  providers: [CustomFieldsService]
})
export class CustomdataComponent implements OnInit {
  data: any;
  companyId: string;
  customDataJson: any;
  FieldGroups: any = [];
  globalDirFlag: boolean = false;

  constructor(private route: ActivatedRoute, private customFieldService: CustomFieldsService, private notificationHandler: NotificationHandler,
    private cpService: CompanyProfileService, private translate: TranslateService, private customTaskService: CustomTaskService,
    private router: Router) { }

  ngOnInit() {
    this.companyId = this.cpService.getCompany().companyId;
    this.translate.get("customdata").subscribe(res => { this.customDataJson = res; })
    if (this.route.snapshot.queryParamMap.has("gd")) {
      this.globalDirFlag = true;
    } else {
      this.globalDirFlag = false;
    }
    this.initializeData();
  }
  private initializeData() {
    this.customFieldService.getCustomfDataFieldGroup(this.companyId).subscribe(
      data => {
        this.customTaskFlag = false;
        this.FieldGroups = data;
        this.customizedGroupdata();
      }, error => {
        if (error.status != 404)
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
      });
  }

  customTaskFlag: boolean = false;
  customizedGroupdata() {
    this.customTaskFlag = false;
    this.FieldGroups.forEach(group => {
      group.customFieldsUrl = "customdata/partner/withAnswer?groupId=" + group.uniqueId + "&partnerBuId=" + this.companyId;
    });
    this.customTaskFlag = true;
  }

  getdataToSave(data) {
    let keys = Object.keys(data);
    let objArray = [];
    for (let key of keys) {
      if (Object.keys(data[key]).length > 0) {
        let obj = {};
        obj["groupId"] = key;
        obj["value"] = data[key];
        objArray.push(obj);
      }
    }
    return objArray;
  }

  onSaveFn(data) {
    //console.log("data for save: ",data)
    const saveData = this.getdataToSave(data);

    this.customFieldService.updateCustomFieldWithAnswer(this.companyId, saveData).subscribe(
      data => {
        if (data['responseDetails']['success']) {
          this.notificationHandler.notify({ severity: 'success', title: data['responseDetails']['statusMessage'] });
          //this.customTaskService.confirmCustomTaskSaveMsg(true);
          this.initializeData();
        }
      }, error => {
        if (error.status != 404) {
          this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
          this.customTaskService.confirmCustomTaskSaveMsg(false);
        }
      }
    );

  }

  reloadGroupData() {
    this.initializeData();
  }

  cancle(data) {
    if (this.globalDirFlag) {
      this.router.navigate(['globalDirectory'], { queryParams: { gd: 't' }, skipLocationChange: true  });
    } else {
      this.router.navigate(['/tpdir']);
    }

  }

  handleFileNotification(message) {
    this.notificationHandler.notify({ severity: 'warning', title: message })
  }

}
