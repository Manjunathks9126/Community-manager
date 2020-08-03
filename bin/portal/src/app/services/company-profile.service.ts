import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { catchError } from "rxjs/operators";
import { HttpClient } from "@angular/common/http";
import { ExceptionWrapper } from '../util/exception/exception-wrapper';
import { BusinessFacades } from "../features/tradingpartner/facades.entity";
import { Company } from "../features/tradingpartner/companyprofile/companyProfile.entity";
import { User } from "../features/tradingpartner/companyprofile/users/user.entity";
import { ContactsFilterEntity } from "../features/tradingpartner/companyprofile/contacts/contact-filter-entity";

@Injectable()
export class CompanyProfileService {

    isPrivateViewEnabled = false;
    setFlag = false;
    selectedBuId = "";
    selectedEdi = ""
    otherInfo:boolean = false;
    hasEdiOrDocs:boolean = false;
//    Setting for GDS
    selectedInvitaion = "";
    private _PUBLIC_PROFILE_SUB = new Subject<boolean>();
    private formValid = new Subject<boolean>();
    private observableFacadeData = new Subject<BusinessFacades>();
    noRecordFlag: boolean = false;
    // private ediData = new Subject<any>();

    constructor(private httpClient: HttpClient) { }

    company: Company;
    userList: User[];
    user: User;
    onboardingHistory: any;
    buId:any;
    ediData : any;
    companyData : any;



    setEdi(edi: any) {
        this.ediData = edi;
    }
    getEdi(): any {
        return this.ediData;
    }

    setCompanyName(cname: any) {
        this.companyData = cname;
    }
    getCompanyName(): any {
        return this.companyData;
    }

    isPrivateView(): Observable<any> {
        return this._PUBLIC_PROFILE_SUB.asObservable();
    }

    setGpdFlag(flag: boolean){
        this.setFlag=flag;
    }

    setPrivateView(flag: boolean) {
        this.isPrivateViewEnabled = flag;
        this._PUBLIC_PROFILE_SUB.next(flag);
    }
    setFormValid(flag: boolean) {
        this.formValid.next(flag);
    }
    getFormValid(): Observable<any> {
        return this.formValid.asObservable();
    }

    setObservableFacadeData(data: BusinessFacades) {
        this.observableFacadeData.next(data);
    }
    getObservableFacadeData(): Observable<any> {
        return this.observableFacadeData.asObservable();
    }
    setCompany(company: Company) {
        this.company = company;
    }
    getCompany(): Company {
        return this.company;
    }

    setOnboardingHistory(onboardingHistory: any) {
        this.onboardingHistory = onboardingHistory;
    }

    getOnboardingHistory(): any {
        return this.onboardingHistory;
    }

    companyById(companyId: any): Observable<any> {
        return this.httpClient.get("companies/" + companyId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getUsers(companyId: any, filterEntity: ContactsFilterEntity) {
        return this.httpClient.post("companies/" + companyId + "/users", filterEntity)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    getExtendedAttribute(companyId: any):Observable<any> {
        return this.httpClient.get("globalsearch/extendedAttributes?buid=" + companyId)
            .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
    }

    companyGpdConsent():Observable<any>{
        return this.httpClient.get("globalsearch/companygpdconsent")
        .pipe(catchError(exception => ExceptionWrapper.getErrorText(exception)));
      }

      
    setUsers(users: User[]) {
        this.userList = users;
    }

    getUserById(id: any) {
        if (this.userList) {
            this.user = this.userList.find(x => x.userId == id);
        }
        return this.user;
    }

    setNoRecordGpdFlag(noRecordFlag){
        this.noRecordFlag = noRecordFlag;
    }

    getNoRecordGpdFlag(){
        return this.noRecordFlag;
    }

}