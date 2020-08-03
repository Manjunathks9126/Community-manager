import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable, Subject } from "rxjs";
import { Workflow } from "../features/tradingpartner/directory/createTPMSG/entity/workflow.entity";
import { ProvisioningMap } from "../features/tradingpartner/directory/createTPMSG/entity/provisioningMap.entity";

@Injectable()
export class TPMSGService {

    isGDFlag = false;
    private isTGMSworkFlow: boolean = false;
    TPMG_ROUTER_SUB = new Subject<any>();
    cachedworkFlow: Workflow = new Workflow();
    public hideStepperSUB = new Subject<any>();
    private countriesList: any;
    private vanProvider: any;
    httpParameters = new HttpParams().set('hideLoader', 'true');
    provisioningMap: ProvisioningMap[] = [];
    selectedMap = [];
    public moveToStepSUB = new Subject<any>();
    private sel_prod_address = "";
    private sel_test_address = "";
    private customerTestISAindicator = false;
    private tpTestISAindicator = false;
    private functionalAck = false;
    private selMapFlag: boolean;
    private tempSelectedMaps: ProvisioningMap[] = [];
    public loggedinBUID: string = "";
    EDI_ARRAY = [];
    public gxsCompany: string = "";

    constructor(private httpClient: HttpClient) {

    }


    isTGMSworkflow() {
        return this.isTGMSworkFlow;
    }

    setTGMSWorkFlowType(flag) {
        this.isTGMSworkFlow = flag;
    }

    clearCache() {
        this.cachedworkFlow = new Workflow();
        this.vanProvider = "";
        this.sel_prod_address = ""
        this.sel_test_address = "";
        this.provisioningMap = [];
        this.selectedMap = [];
        this.customerTestISAindicator = false;
        this.tpTestISAindicator = false;
        this.functionalAck = false;
        this.selMapFlag = false;
        this.tempSelectedMaps = [];
        this.billingOption = "";
        this.sendingPaidBy = "";
        this.receivingPaidBy = "";
        this.receiverMailbxStoragePaidBy = "";
        this.allChargesPaidBy = "";
        this.selectedEDI = [];
        this.companyIdVanProviderMap = {};
    }

    getSelectedMapFlag() {
        return this.selMapFlag;
    }
    setSelectedMapFlag(flag) {
        this.selMapFlag = flag;
    }
    setTempSelMap(map) {
        this.tempSelectedMaps = map;
    }
    getTempSelMap() {
        return this.tempSelectedMaps;
    }
    setSelectedProvisioningMap(map) {
        this.selectedMap = map;
    }
    getSelectedProvisioningMap() {
        return this.selectedMap;
    }
    activateRoute(step) {
        this.TPMG_ROUTER_SUB.next(step);
    }


    getCustTestISAindicator() {
        return this.customerTestISAindicator;
    }

    setCustTestISAindicator(flag) {
        this.customerTestISAindicator = flag;
    }

    getTpTestISAindicator() {
        return this.tpTestISAindicator;
    }

    setTpTestISAindicator(flag) {
        this.tpTestISAindicator = flag;
    }

    getfunctionalAck() {
        return this.functionalAck;
    }
    setfunctionalAck(flag) {
        this.functionalAck = flag;
    }

    hideStepper(flag) {
        this.hideStepperSUB.next(flag);
    }
    isReadonly: boolean = false;
    holdCompanyDataTemp(workflow: Workflow, countries, readOnlyFlag) {
        this.cachedworkFlow = workflow;
        this.countriesList = countries;
        this.isReadonly = readOnlyFlag;
    }

    saveWorkflow(workflow: Workflow) {
        this.cachedworkFlow = workflow;
    }

    getIsReadOnly() {
        return this.isReadonly;
    }

    set_Step_2_ProdAdd(address) {
        this.sel_prod_address = address;
    }
    set_sel_test_address(address) {
        this.sel_test_address = address;
    }
    get_sel_test_address() {
        return this.sel_test_address;
    }
    get_Step_2_ProdAdd() {
        return this.sel_prod_address;
    }

    nextStep(stepName) {
        this.moveToStepSUB.next(stepName);
    }

    setVanProvider(van) {
        this.vanProvider = van;
    }

    getVanProvider() {
        return this.vanProvider;
    }

    getCachedWorkFlow() {
        return this.cachedworkFlow;
    }

    getCachedCountryList() {
        return this.countriesList;
    }

    private companyIdVanProviderMap: object = {};

    setCompanyIdVanProviderMap(comapnyId: string, vanProvider: string) {
        this.companyIdVanProviderMap = { "companyId": comapnyId, "vanProvider": vanProvider };
    }

    getCompanyIdVanProviderMap() {
        return this.companyIdVanProviderMap;
    }

    billingOption: any = "";
    sendingPaidBy: any = "";
    receivingPaidBy: any = "";
    receiverMailbxStoragePaidBy: any = "";
    allChargesPaidBy: any = "";

    saveBilliingOptions(billingOp, sendPaidBy, receivPaidBy, receivMailbxStrgPaidBy, allChargPaidBy) {
        this.billingOption = billingOp;
        this.sendingPaidBy = sendPaidBy;
        this.receivingPaidBy = receivPaidBy;
        this.receiverMailbxStoragePaidBy = receivMailbxStrgPaidBy;
        this.allChargesPaidBy = allChargPaidBy;
    }

    getCachedBillingInfo() {
        return {
            "billingOption": this.billingOption,
            "sendingPaidBy": this.sendingPaidBy,
            "receivingPaidBy": this.receivingPaidBy,
            "receiverMailbxStoragePaidBy": this.receiverMailbxStoragePaidBy,
            "allChargesPaidBy": this.allChargesPaidBy
        }
    }

    selectedEDI = [];
    saveSelectedEdis(selEDI) {
        this.selectedEDI = selEDI;
    }

    getSelectedEdi() {
        return this.selectedEDI;
    }


    getProvisioningMapsRest(): Observable<any> {
        return this.httpClient.get("provisioningMap");
    }

    getEDIList(after): Observable<any> {
        return this.httpClient.get("companyediaddresses?after=" + after, { params: this.httpParameters });
    }

    getSeparator(): Observable<any> {
        return this.httpClient.get("msgDetailseparator");
    }

    validateBPR(companyId): Observable<any> {
        return this.httpClient.get("validatebpr/" + companyId);
    }
    validateTP(companyName, city, country): Observable<any> {
        return this.httpClient.get("validatetpExist?companyName=" + companyName + "&city=" + city + "&country=" + country);
    }

    isTestOptionVisible(): Observable<any> {
        return this.httpClient.get("util/isTestOptionVisible");
    }

    getInternalCompanyDetails(): Observable<any> {
        return this.httpClient.get("util/internalCompanyDetails");
    }

    getLoggedInCompanyDetails(): Observable<any> {
        return this.httpClient.get("session/company");
    }

    postWorkflow(invId, workflow): Observable<any> {
        if (typeof workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode === 'object')
            workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode.countryCode;
        else if (workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode == '')
            workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = null;
        return this.httpClient.post("initiatevantpflow?invitationId=" + invId, workflow);
    }

    postTGMSworkFlow(invId, workflow): Observable<any> {
        if (typeof workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode === 'object')
            workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode.countryCode;
        else if (workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode == '')
            workflow.provisioningRequestData.registrationData.businessUnit.companyAddress.countryCode = null;

        return this.httpClient.post("initiateTGMSflow?invitationId=" + invId, workflow)
    }

    getCompanyVan(companyId: string, hideLoader?: boolean): Observable<any> {
        let params = new HttpParams().set('hideLoader', hideLoader ? 'true' : 'false');
        return this.httpClient.get("ediaddresses/" + companyId + "/vanprovider", { params: params });
    }
}