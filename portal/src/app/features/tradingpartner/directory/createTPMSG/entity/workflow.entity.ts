//  XML-Root
export class Workflow {
    processingContext: ProcessingContext = new ProcessingContext();
    provisioningRequestData: ProvisioningRequestData = new ProvisioningRequestData();
}

export class ProvisioningRequestData {
    registrationData: RegistrationData = new RegistrationData();
    setup: string = "";
    tpRequestType: string = "";
    customer: Customer = new Customer();
}


export class RegistrationData {
    businessUnit: BusinessUnit = new BusinessUnit();
    businessPointOfContact: BusinessPointOfContact = new BusinessPointOfContact();
    tgtsProfiles: TgtsProfiles = new TgtsProfiles();
    technicalPointOfContact: TechnicalPointOfContact = new TechnicalPointOfContact();
    tradingAddress: TradingAddress = new TradingAddress();
    user: User = new User();
    tgmsBilling: TgmsBilling = new TgmsBilling();
    companyInfo:any;
    contactInfo:any;
    ediStandard:any;
    vanDetails:any;
    maps:any;
    productionDetails:any;
    testDetails:any;
    messageDetails:any;
    selectOptions:any;
}

export class WorkflowFacade {
    id: string = "";
    name: string = "";
}

export class ProcessingContext {
    invitationCode: string = "";
    action: string = "";
    tpIdentifier: TpIdentifier = new TpIdentifier();
    invitationSource:string ="";
}

export class TpIdentifier {
    buId: string = "";
    countryCode: string = "";
    companyName: string = "";
    city: string = "";
}

export class Invitation {
    invitationCode: string = "";
}

export class Customer {
    listAddress:ListAddress[] = [];
    tradingAddresses: TradingAddresses = new TradingAddresses();
}

export class ListAddress {
    constructor(edi){
        this.ediAdress = edi;
    }
    ediAdress: string = "";
}
export class TradingAddresses {
    test: Test = new Test();
    prod: Prod = new Prod();
}

export class Test {
    address: string = "";
    qualifier: string = "";
    gsId: string = "";
}

export class Prod {
    address: string = "";
    qualifier: string = "";
    gsId: string = "";
}

export class User {
    lastName: string = "";
    isRequiredMarketingMaterials: string = "";
    isDayLightSavingsTimeObserved: string = "";
    userLoginPassword: string = "";
    contactInformation: ContactInformation = new ContactInformation();
    address: Address = new Address();
    firstName: string = "";
    userLogin: string = "";
    preferredTimezone: string = "";
    preferredDateFormat: string = "";
    preferredLanguage: string = "";
}

export class Address {
    countryCode: string = "";
    addressLine1: string = "";
    city: string = "";
}

export class Maps {
    tableEntries: string = "";
    docType: string = "";
    direction: string = "";
    documentStandard: string = "";
    docVersion: string = "";
    mapName: string = "";
    acr: string = "";
    usedBy: string = "";
    edi_dc40_test: string = "";
    edi_dc40_prod: string = "";
}

export class TechnicalPointOfContact {
    lastName: string = "";
    contactInformation: ContactInformation = new ContactInformation();
    firstName: string = "";
}

export class Prodtp {
    address: string = "";
    qualifier: string = "";
    gsId: string = "";
}

export class Testtp {
    address: string = "";
    qualifier: string = "";
    gsId: string = "";
}


export class TradingAddress {
    //address: string = "";
    ediStadard: string = "";
    testtp: Testtp = new Testtp();
    prodtp: Prodtp = new Prodtp();
    //qualifier: string = "";
    connectivityType: string = "";
    vanProvider: string = "";
    vanMailboxId: string = "";
    //gsId: string = "";
    testTpAsProdTp: boolean = false;
}






export class TgtsProfiles {
    maps: Maps[] = [];
    messageDetails: MessageDetails = new MessageDetails();
}
export class MessageDetails {
    segmentTerminator: string = "";
    customerTestISAindicator: string = "N";
    functionalAck: string = "N";
    elementSeparator: string = "";
    subelementTerminator: string = "";
    tpTestISAindicator: string = "N";
}

export class BusinessUnit {
     companyAddress: CompanyAddress = new CompanyAddress();
    participationType: string = "";
    companyId: string = "";
    customCompanyId:string="";
    companyName: string = "";
    displayName: string = "";
    buId: string = "";
    companyNumber:string;
    companyEmail:string;
    externalref: ExternalReferenceType[]=[];
}

export class CompanyAddress {
    postalCode: string = "";
    website: string = "";
    state: string = "";
    countryCode: any = new Object();
    addressLine2: string = "";
    addressLine1: string = "";
    city: string = "";
}

export class BusinessPointOfContact {
    lastName: string = "";
    contactInformation: ContactInformation = new ContactInformation();
    firstName: string = "";
}

export class ContactInformation {
    email: string = "";
    telephone: string = "";

}

export class TgmsBilling {
    splitCharges: SplitCharges = new SplitCharges();
    allCharges: string = "";
}

export class SplitCharges {
    receiverMBStorage: string = "";
    sending: string = "";
    receiving: string = "";
}

export class ExternalReferenceType {
     type: string = "";
    id: string = "";
    
}