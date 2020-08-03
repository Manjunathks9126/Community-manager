export class Company{
    createdOn:string;
    updatedOn:string;
    status:string;
    companyName:string;
    companyId:string;
    tradingPartnerType:string;
    compAddr1:string;
    compAddr2:string;
    city:string;
    country:string;
    state:string;
    postalCode:string;
    phone:string;
    email:string;
    website:string;
    dunsNo:string;
    globalLocationNumber:string;
    vatId:string;
    federalTaxId:string;
    naics:string;
    sic:string;
    targetBuCustomId:string;
    facadeCompanyId:string;
    bprId:string;
    bprStatus:string;
    homeZoneId:string;
    customerBUGPDVisibility: any;
    internalBUGPDVisibility: any;
    
    constructor(createdNo:string,
        updatedOn:string,
        status:string,
        companyName:string,
        companyId:string,
        tradingPartnerType:string,
        compAddr1:string,
        compAddr2:string,
        city:string,
        country:string,
        state:string,
        postalCode:string,
        website:string,
        dunsNo:string,
        globalLocationNumber:string,
        homeZoneId:string,
        custGPDvisibilty?: any,
        internalGPDvisibilty?: any,

         // Below values are not available in API 

        vatId?:string,
        federalTaxId?:string,
        naics?:string,
        sic?:string,
        phone?:string,
        email?:string){

            this.createdOn=createdNo;
            this.updatedOn=updatedOn;
            this.status=status;
            this.companyName=companyName;
            this.companyId=companyId;
            this.tradingPartnerType=tradingPartnerType;

            this.compAddr1=compAddr1;
            this.compAddr2=compAddr2;
            this.city=city;
            this.country=country;
            this.state=state;
            this.postalCode=postalCode;

            this.customerBUGPDVisibility = custGPDvisibilty == 'Yes' ? true:false;
            this.internalBUGPDVisibility = internalGPDvisibilty;
           
            this.website=website;
            this.dunsNo=dunsNo;
            this.globalLocationNumber=globalLocationNumber;
            this.homeZoneId=homeZoneId;
            this.vatId=vatId;

            this.federalTaxId=federalTaxId;
            this.naics=naics;
            this.sic=sic;
            this.phone=phone;
            this.email=email;
    }
}