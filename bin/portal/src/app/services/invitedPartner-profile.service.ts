import { Injectable } from "@angular/core";
import { Subject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class InvitedPartnerProfileService {
    constructor(private http: HttpClient) { }

    invitedPartnerInfo: any;
    taskId:any;
    requesterTaskSchema: any = new Subject<any>();
    requesterTaskData: any = new Subject<any>();

    setInvitedPartnerInfo(invitedTPData) {
        this.invitedPartnerInfo = invitedTPData;
    }
    getInvitedPartnerInfo() {
        return this.invitedPartnerInfo;
    }
    setRequesterTaskSchema(taskSchema) {
        this.requesterTaskSchema.next(taskSchema);
    }

    getRequesterTaskSchema(): Observable<any> {
        return this.requesterTaskSchema.asObservable();
    }

    setRequesterTaskData(taskSchema) {
        this.requesterTaskData.next(taskSchema);
    }

    getRequesterTaskdata(): Observable<any> {
        return this.requesterTaskData.asObservable();
    }

    deleteInvitationSent(invitationCode): Observable<any> {
        return this.http.post("workflow/invitations/delete", [invitationCode]);
    }

    reInvite(data){
        return this.http.post("workflow/invitations/reInvite",data);
    }

    reinviteWithNew(data){
        return this.http.post("workflow/invitations/reInviteWithNewInvCode",data)
    }

}