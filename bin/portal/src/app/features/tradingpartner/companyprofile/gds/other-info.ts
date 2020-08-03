import { Component, OnInit } from '@angular/core';
import { OnboardingService } from '../../../../services/onboarding.service';
import { CompanyProfileService } from '../../../../services/company-profile.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SchemaFormOutputService } from 'json-editor/dist/src/app/services/schema-form-output.service';
import { NotificationHandler } from '../../../../util/exception/notfication.handler';
import { TranslateService } from '@ngx-translate/core';
import { MatomoTracker } from 'ngx-matomo';

@Component({
  templateUrl: './other-info.html',
  providers: [MatomoTracker]
})
export class OtherInfo implements OnInit {
  selectedType = "";
  details: any;
  buId = "";
  filterList: any = [];
  leftNavList: any = [];
  outputJSON = "";
  templateJSON = "";
  isGDFlag: boolean = false;
  categoryContentId: number;
  private successMessage: any;
  private errorMessage: any;
  isEdit: boolean = false;
  canEdit: boolean = false;


  private getNormalizedName(value) {
    return value.replace(new RegExp('_', 'g'), ' ');
  }

  constructor(private onboardingService: OnboardingService, private route: ActivatedRoute, private translate: TranslateService,
    private schemaFormOutputService: SchemaFormOutputService, private notificationHandler: NotificationHandler, private router: Router,private matomoTracker: MatomoTracker) { }

  ngOnInit() {
    this.translate.get("common.success").subscribe(res => { this.successMessage = res; })
    this.translate.get("common.errorMessage").subscribe(res => { this.errorMessage = res; })
    this.setupLeftNav();
    if (this.route.snapshot.queryParamMap.has("gd")) {
      this.isGDFlag = true;
    }
  }

  setupLeftNav() {
    // Added for piwik to track OtherInfo tab when clicked.
    this.matomoTracker.trackPageView("OtherInfo");
    this.matomoTracker.trackEvent("OtherInfo ", this.onboardingService.gdsCompanyId+" - "+this.onboardingService.selectedCompanyName," OtherInfo");


    this.filterList = [...this.onboardingService.OTHER_INFO_EW];
    this.buId = this.onboardingService.gdsCompanyId;
    for (let i = 0; i < this.filterList.length; i++) {
      if (typeof this.filterList[i].jsonContent === "string") {
        this.filterList[i].jsonContent = JSON.parse(this.filterList[i].jsonContent);
      }
      this.leftNavList[i] = {
        value: this.getNormalizedName(this.filterList[i].category.name),
        json: this.filterList[i].jsonContent,
        categoryContentId: this.filterList[i].catagoryContentId
      };
    }
    this.leftNavList.sort(function (a, b) { return a.value.localeCompare(b.value) });
    this.outputJSON = this.leftNavList[0].json;
    this.categoryContentId = this.leftNavList[0].categoryContentId;
    this.getJsonSchema(this.buId, this.leftNavList[0].value);

    if (this.leftNavList && this.leftNavList.length > 0) {
      this.selectedType = this.leftNavList[0].value;
    }
    console.log(this.onboardingService.gdsCompanyId,this.onboardingService.loggedBuId);
    if(this.onboardingService.gdsCompanyId == this.onboardingService.loggedBuId){
      this.canEdit = true;
    }
  }

  // edit() {
  //   this.isEdit = !this.isEdit;
  //   let mode = 'W';
  //   this.schemaFormOutputService.sendResponse(mode);
  // }

  save() {
    this.isEdit = !this.isEdit;
    let mode = 'R';
    this.schemaFormOutputService.sendResponse(mode);
    this.schemaFormOutputService.callOutput();
  }

  updateContent(event) {
    console.log(event, this.categoryContentId);
    let category = this.filterList.filter(data => data.catagoryContentId == this.categoryContentId)[0];
    category.companyName = this.onboardingService.selectedCompanyName;
    category.jsonContent = JSON.stringify(event);
    this.onboardingService.updateContent(category, this.categoryContentId).subscribe(data => {
      console.log(data);
      this.notificationHandler.notify({ severity: 'success', title: this.successMessage });
    }, error => {
      this.notificationHandler.notify({ severity: 'error', title: this.errorMessage });
    });
  }


  onTypeSelect(data) {
    this.selectedType = data.value;
    this.outputJSON = data.json;
    this.categoryContentId = data.categoryContentId;
    this.getJsonSchema(this.buId, data.value);
  }



  private getJsonSchema(buid, type) {
    this.onboardingService.getJsonSchema(buid, type).subscribe(data => {
      this.templateJSON = JSON.parse(data.responseDetails.responseEntity);

    }, error => {
      console.log(error);
    })
  }


  cancel() {
    if (this.isEdit) {
      let mode = 'R';
      this.schemaFormOutputService.sendResponse(mode);
      this.isEdit = !this.isEdit;
    } else {
      let navigationExtras = {
        queryParams: { gd: 't' },
        skipLocationChange: true
      }
      if (this.isGDFlag) {
        this.router.navigate(['/globalDirectory'], navigationExtras);
      } else {
        this.router.navigate(['/tpdir']);
      }
    }

  }



  downloadDoc(id) {
    window.open(this.onboardingService.downloadDocumnet(id), '_self');
  }

  private getDoucmentName(details) {
    let filterId = "";
    if (details && details.value.indexOf('Safe') >= 0) {
      filterId = "1";
    } else {
      filterId = "2";
    }

    this.onboardingService.getDocumentMeta(this.buId, filterId).subscribe(data => {
      if (data && data.KEY) {
        details.documentName = data.KEY;
        details.docId = data.EXTENDED_ID;
      }

    })
  }
}
