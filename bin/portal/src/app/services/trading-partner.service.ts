import { TprRequestEntity } from './../features/tradingpartner/directory/createTPMSG/entity/tprResponse.entity';
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { FilterEntity } from "../features/tradingpartner/filter-entity";
import { BusinessFacades } from "../features/tradingpartner/facades.entity";
import { Workflow } from "../features/tradingpartner/directory/createTPMSG/entity/workflow.entity";

@Injectable()
export class TradingParnterService {

    public selectedinvCodeSubject: string;
    public selectedTpId: string;
    private workFlow: Workflow = new Workflow();
    private bprID = "";
    displayName:any=""
    constructor(private http: HttpClient) { }

    setWorkFlow(wf: Workflow, bprid) {
        this.bprID = bprid;
        this.workFlow = wf;
    }
    getWorkFlow() {
        return this.workFlow;
    }
    getBprId() {
        return this.bprID;
    }


    tpDirectoryFilterOptions(): Observable<any> {
        return this.http.get("tpDirectoryFilterOptions")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    graphApplnData(): Observable<any> {
        return this.http.get("graphApplnData")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    listCompanyStatus(): any {
        return [
            90, 60, 30, 10
        ]
    }

    getCountries(): Observable<any> {
        return this.http.get("util/countries").pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    createTP(workflow: Workflow, invId: string): Observable<any> {
        return this.http.post("tradingpartner/psp?invitationId=" + invId, workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    createCoreTP(workflow: Workflow, invId): Observable<any> {
        return this.http.post("tradingpartner/core?invitationId=" + invId, workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    initiateWorkflow(workflow: Workflow, tpId: string): Observable<any> {
        return this.http.post("tradingpartner/psp/" + tpId, workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    saveTP(workflow: Workflow, invId: string): Observable<any> {
        return this.http.post("tradingpartner/save?invitationId=" + invId, workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    verifyTP(workflow: Workflow): Observable<any> {

        return this.http.post("tradingpartner/verify", workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    editTP(workflow: Workflow, tpId: string): Observable<any> {
        return this.http.post("tradingpartner/save/" + tpId, workflow)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getWorkFlowInvitations(): Observable<any> {
        return this.http.get("workflow/invitations")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getKeyBankMaps() {
        return this.http.get("getKeyBankmaps")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    public observeinvCodeData(data: string) {
        this.selectedinvCodeSubject = data;
    }

    public setSelectedTPIdData(data: string) {
        this.selectedTpId = data;
    }


    bprCount(filterEntity: FilterEntity): Observable<any> {
        return this.http.post("businessPartner/list", filterEntity)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    listBusinessPartners(filterEntity: FilterEntity): Observable<any> {
        return this.http.post("businessPartner/list", filterEntity)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    addUpdateFacades(facades: BusinessFacades): Observable<any> {
        return this.http.post("businessPartner/facades", facades)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getEdis(bprId: string, countOnly, limit, startFrom): Observable<any> {
        return this.http.get("businessPartner/edis?bprId=" + bprId + "&countOnly=" + countOnly + "&limit=" + limit + "&startingFrom=" + startFrom)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    deleteFacades(data: BusinessFacades): Observable<any> {
        return this.http.post("businessPartner/facades/delete?id=" + data.uniqueId, data)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    statsByBPRStatus(): Observable<any> {
        return this.http.get("businessPartner/stats/status")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getRegistrationData(companyId: string): Observable<any> {
        return this.http.get("reviewTpInfo/" + companyId);
    }

    approveTp(data: Workflow, bprid): Observable<any> {
        return this.http.post("approvetp/" + bprid, data)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    listTradingPartnerRelationships(filterEntity: FilterEntity): Observable<any> {
        return this.http.post("tradingpartnerrelationships", filterEntity)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    totalTradingPartnerRelationships(filterEntity: FilterEntity): Observable<any> {
        return this.http.post("tradingpartnerrelationships", filterEntity)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    terminateTprList(modifiedTp: TprRequestEntity[]): Observable<any> {
        return this.http.post('tradingpartnerrelationships/terminate', modifiedTp)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    approveRejectTprList(modifiedTp: TprRequestEntity[]): Observable<any> {
        return this.http.post('tradingpartnerrelationships/approveReject', modifiedTp)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
// *************** SOLR CHANGES ***********************
    listBusinessPartnersBySolr(filterEntity: FilterEntity): Observable<any> {
      return this.http.post('globalsearch/businessPartners', filterEntity)
          .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
  }
}

