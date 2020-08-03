import { Injectable } from "@angular/core";
import { HttpClient, HttpEvent, HttpEventType, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { catchError, map } from "rxjs/operators";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { DatePipe } from "@angular/common";
import * as _ from "lodash";

@Injectable()
export class OnboardingService {

    constructor(private http: HttpClient, private datePipe: DatePipe) { }
    // GDS
    isGDSflag: boolean = false;
    gdsCompanyId = "";
    OTHER_INFO_EW: any = [];
    EDI_ADDRESS: any[] = [];
    DOCUMENTS: any[] = [];
    loggedBuId: any;
    selectedCompanyName: string = "";


    private invitationID_onboarding: string = "";
    public workflowList: any[] = [];
    public selectedWorkFlow = {};
    public taskList: any[] = [];
    public invitationList: any[] = [];
    public imageurl: string = "";
    public invitationmessage: string = "";
    public invitationdata: any = {};
    public uploadImage: any;
    public imagePath: any;
    public selectedInvitation: any;
    private skynetUrl_params: any;

    private ediAddressMandatory: boolean = true;

    isEdiAddressMandatory(): boolean {
        return this.ediAddressMandatory;
    }

    setEdiAddressMandatory(ediAddressMandatory: boolean) {
        this.ediAddressMandatory = ediAddressMandatory;
    }

    getSkynetURL_params() {
        return this.skynetUrl_params;
    }
    setSkynetUrl_params(url) {
        this.skynetUrl_params = url;
    }

    public getInvIdForOnboarding(): string {
        return this.invitationID_onboarding;
    }
    public setInvIdForOnboarding(invitationIdCore: string) {
        this.invitationID_onboarding = invitationIdCore;
    }


    getJsonSchema(buId, type): Observable<any> {
        return this.http.get("globalsearch/jsonschema?buid=" + buId + "&schematype=" + type)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    updateContent(category, categoryContentId): Observable<any> {
        return this.http.post("globalsearch/extendedAttributes/" + categoryContentId, category)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getDocumentMeta(buId, filterCategId): Observable<any> {
        return this.http.get("globalsearch/document?buId=" + buId + "&filterCategoryId=" + filterCategId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    downloadDocumnet(extendedId) {
        return "globalsearch/document/" + extendedId + "/download";
    }


    listWorkflows(isBulk?: boolean): Observable<any> {
        let api = "onboarding/workflows";
        if (isBulk)
            api += "?bulkOnly=true"
        return this.http.get(api)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    listWorkflowInvitations(workflowId, status): Observable<any> {
        return this.http.get("workflow/invitations/" + workflowId + (!status ? "" : "?invitationStatus=" + status))
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getWorkflow(id: any): Observable<any> {
        return this.http.get("onboarding/workflows/" + id)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    createInvitation(invitationData): Observable<any> {
        return this.http.post("workflow/invitations/" + this.selectedWorkFlow['workflowId'], invitationData)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getEdiAddressList(limit: number, after: number): Observable<any> {
        return this.http.get("ediaddresses?limit=" + limit + "&after=" + after)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getEdiAddressCount(): Observable<any> {
        return this.http.get("ediaddresses?countOnly=true")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    invite(invitationData): Observable<any> {
        return this.http.post("workflow/invitations/invite", invitationData)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    updateWorkflow(workflowData): Observable<any> {
        return this.http.post("onboarding/workflows/update", workflowData)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    invitedPartners(after: number, limit: number, countOnly: boolean, sortField: string, sortOrder: number): Observable<any> {
        let url = "onboarding/invitedpartners?limit=" + limit + "&after=" + after + "&countOnly=" + countOnly;
        if (sortField) {
            url += "&sortField=" + sortField + "&sortOrder=" + sortOrder;
        }
        return this.http.get(url)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getInvitationmessage() {
        return this.invitationmessage;
    }
    setInvitationmessage(invitationmessage: string) {
        this.invitationmessage = invitationmessage;
    }
    getPreviewUtil(): Observable<any> {
        return this.http.get("workflow/invitations/previewUtil");
    }
    setCachedInvitationData(invitation: any) {
        this.invitationdata = invitation;
    }
    getCachedInvitationData() {
        return this.invitationdata;
    }

    getOnboardingWorkflowReport(workflowId): Observable<any> {
        return this.http.get("onboarding/workflows/report/" + workflowId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getInvitationDetails(invitationId: number): Observable<any> {
        return this.http.get("workflow/invitations/details/" + invitationId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    updateInvitation(invitation: any): Observable<any> {
        return this.http.post("workflow/invitations/" + this.selectedWorkFlow['workflowId'], invitation)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    public exportToCsv(exportData: any) {

        let data = exportData;
        let csv = '\ufeff';
        const csvSeparator: string = ',';
        let date = new Date();
        let exportFilename = "WFReport_" + this.datePipe.transform(date, 'MM_dd_y_') + date.getHours() + "_" + date.getMinutes() + "_" + date.getSeconds();

        let exportColumns = Object.keys(data[0]);
        //headers
        for (let i = 0; i < exportColumns.length; i++) {
            csv += '"' + (exportColumns[i]) + '"';
            if (i < (exportColumns.length - 1)) {
                csv += csvSeparator;
            }
        }

        //body
        data.forEach((record, i) => {
            csv += '\n';
            for (let i = 0; i < exportColumns.length; i++) {
                var fieldValue = record[Object.keys(record)[i]];
                if (fieldValue)
                    fieldValue = fieldValue.toString().replace(/"/g, '""');
                else {
                    fieldValue = '';
                }
                csv += '"' + fieldValue + '"';
                if (i < (exportColumns.length - 1)) {
                    csv += csvSeparator;
                }
            }
        });

        let blob = new Blob([csv], {
            type: 'text/csv;charset=utf-8;'
        });

        if (window.navigator.msSaveOrOpenBlob) {
            navigator.msSaveOrOpenBlob(blob, exportFilename + '.csv');
        }
        else {
            let link = document.createElement("a");
            link.style.display = 'none';
            document.body.appendChild(link);
            if (link.download !== undefined) {
                link.setAttribute('href', URL.createObjectURL(blob));
                link.setAttribute('download', exportFilename + '.csv');
                link.click();
            }
            else {
                csv = 'data:text/csv;charset=utf-8,' + csv;
                window.open(encodeURI(csv));
            }
            document.body.removeChild(link);
        }
    }


    bulkInviteApi = "bulk/invitations";

    upload(invitationId, formData, workflowId) {
        return this.http.post<any>(this.bulkInviteApi + "/" + workflowId + "/validate?invitationId=" + invitationId, formData, {
            reportProgress: true,
            observe: 'events'
        }).pipe(
            map(event => this.getEventMessage(event, formData)),
            catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    private getEventMessage(event: HttpEvent<any>, formData) {
        switch (event.type) {

            case HttpEventType.UploadProgress:
                return this.fileUploadProgress(event);

            case HttpEventType.Response:
                return this.apiResponse(event);

            /*  default:
                 return `File "${formData.get('file').name}" surprising upload event: ${event.type}.`; */
        }

    }

    private fileUploadProgress(event) {
        const percentDone = Math.round(100 * event.loaded / event.total);
        return { status: 'progress', message: percentDone };
    }

    private apiResponse(event) {
        return { status: 'completed', message: event.body.responseDetails.responseEntity };
    }

    downloadTemaple(workflowId, template) {
        return this.bulkInviteApi + "/" + workflowId + "/template?templateName=" + template;
    }

    submitUploadedFile(invitationId, formData: FormData, workflowId) {
        return this.http.post(this.bulkInviteApi + "/" + workflowId + "?invitationId=" + invitationId, formData)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    listWorkflowTasks(workflowId, stage): Observable<any> {
        return this.http.get("workflow/" + workflowId + "/tasks" + (stage ? "?taskStage=" + stage : ""))
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getTask(taskId, locale): Observable<any> {
        return this.http.get("workflow/tasks/" + taskId + "?locale=" + locale)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getRequesterTaskdata(requestId): Observable<any> {
        return this.http.get("workflow/invitations/" + requestId + "/data");
    }

    cachedTaskList: any[] = [];
    cachedTaskDetail: any = {};
    setCachedTaskList(taskList) {
        this.cachedTaskList = taskList;
    }
    setCachedTaskDetail(taskDetail) {
        this.cachedTaskDetail = taskDetail;
    }
    getCachedTaskDetail() {
        return this.cachedTaskDetail;
    }
    getTaskById(taskId) {
        return _.find(this.cachedTaskList, function (task) {
            return task.task.taskId == taskId;
        })

    }
    getAssociatedFGIds(taskId: any) {
        return this.http.get("workflow/tasks/" + taskId + "/fieldgroups")
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    saveFieldGroup(taskId, onbotask) {
        return this.http.post("workflow/tasks/" + taskId + "/fieldgroups", onbotask)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getAppUrl(): Observable<any> {
        let params = new HttpParams().set('hideLoader', 'true');
        return this.http.get("util/getAppUrl", { params: params })
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    createCustomTask(task: any, workflowId: number) {
        return this.http.post("workflow/" + workflowId + "/tasks", task)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    deleteCustomTask(workflowTaskIds: number[], workflowId: number) {
        return this.http.post("workflow/" + workflowId + "/tasks/delete", workflowTaskIds).pipe(
            catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }
    getLoggedInCompanyDetails(): Observable<any> {
        return this.http.get("session/company");
    }

    getInternalCompanyDetails(): Observable<any> {
        return this.http.get("util/internalCompanyDetails");
    }
}