import { Component, OnInit, ViewChild } from "@angular/core";
import { NotificationHandler } from "../../../../../util/exception/notfication.handler";
import { OnboardingService } from "../../../../../services/onboarding.service";
import * as _ from "lodash";
import { LazyLoadEvent } from 'tgocp-ng/dist/components/common/lazyloadevent';
import { DataTable } from 'tgocp-ng/dist';
import { DatePipe } from '@angular/common';
import { InvitedPartnerProfileService } from '../../../../../services/invitedPartner-profile.service';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { PERMISSSION } from '../../createTPMSG/entity/permission.constants';

@Component({
    selector: 'invited-list',
    templateUrl: './invited-list.component.html'

})

export class InvitedListComponent implements OnInit {
    invitedList = [];
    isFilterApplied = false;
    after: number;
    limit: number;
    countOnly: boolean;
    totalInvitationRecords: number = 0;
    selectedData: any = [];
    sortField: string;
    sortOrder: number;
    inviteMessage: any;
    permissions:any;
    currentLanguage:string="en";
    @ViewChild(DataTable) dataTableComponent: DataTable;

    constructor(private notificationHandler: NotificationHandler, private onboardingService: OnboardingService,
        private datePipe: DatePipe, private invitedPartnerProfileService: InvitedPartnerProfileService,
        private router: Router, private translateService: TranslateService) { }

    ngOnInit() {
        this.permissions = PERMISSSION;
        this.translateService.get("invitedTps.messages").subscribe(res => { this.inviteMessage = res; })
        this.currentLanguage=this.translateService.currentLang;
    }
    lazyLoadInvitedPartnerList(event: LazyLoadEvent) {
        this.after = event.first;
        this.limit = event.rows;
        if (event.sortField && event.sortOrder) {
            this.sortField = event.sortField;
            this.sortOrder = event.sortOrder;
        }
        if (this.totalInvitationRecords == 0) {
            this.getInvitedPartnerListCount();
        }
        else {
            this.getInvitedPartnerList();
        }
    }
    getInvitedPartnerListCount() {
        this.countOnly = true;
        this.onboardingService.invitedPartners(this.after, this.limit, this.countOnly, null, null).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.totalInvitationRecords = data['responseDetails']['responseEntity'].itemCount;
                    if (this.totalInvitationRecords > 0) {
                        this.getInvitedPartnerList();
                    }
                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            })
    }

    getInvitedPartnerList() {
        this.countOnly = false;
        this.onboardingService.invitedPartners(this.after, this.limit, this.countOnly, this.sortField, this.sortOrder).subscribe(
            data => {
                if (data['responseDetails']['success']) {
                    this.invitedList = data['responseDetails']['responseEntity'].itemList;

                }
            }, error => {
                this.notificationHandler.notify({ severity: 'error', details: error.userMessage });
            });
    }

    exportToCsv() {
        var date = new Date();
        this.dataTableComponent.exportFilename =
            "InvitedTPList_" +
            this.datePipe.transform(date, "MM_dd_y_") +
            date.getHours() +
            "_" +
            date.getMinutes() +
            "_" +
            date.getSeconds();
        var clonedDataTable = Object.assign([], this.dataTableComponent.columns);
        this.dataTableComponent.exportCSV({ selectionOnly: true });
        this.dataTableComponent.columns = clonedDataTable;
        this.selectedData = [];
    }
    redirectToProfile(invitation) {
        this.invitedPartnerProfileService.setInvitedPartnerInfo(invitation);
        this.router.navigate(['tpdir/invitedPartner', invitation.workflowId, 'overview'], { skipLocationChange: true });
    }

    reinvite() {
        let sentdata: Array<any> = [];
        this.selectedData.forEach(data => {
            sentdata.push({
                "invitationCode": data.invitationCode,
                "invitationId": data.invitationId,
                "workflowId": data.workflowId,
                "serviceName": data.serviceName,
            })
        });

        this.invitedPartnerProfileService.reInvite(sentdata).subscribe(data => {
            this.selectedData = [];
            this.notificationHandler.notify({ severity: 'success', title: this.inviteMessage.success.reinviteMsg });
        }, error => {
            this.notificationHandler.notify({ severity: 'error', title: this.inviteMessage.error.reinviteMsg });
        })
    }
}