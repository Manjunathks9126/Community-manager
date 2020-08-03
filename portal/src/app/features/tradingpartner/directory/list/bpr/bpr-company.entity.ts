export class Company {
  public businessUnitId:string;
  public partnerCompanyId:string;
  public partnerCompanyName :string;
  public partnerCompanyDisplayName :string;
  public facadeCompanyId :string;
  public bprStatus :string;
  public partnerShipActivationDate :string;
  public bprId:string;
  public partnerCompanyStatus:string;
  public ediAddresses:any[];

  constructor(businessUnitId: string, partnerCompanyId: string, partnerCompanyName: string, partnerCompanyDisplayName: string,
     facadeCompanyId: string,bprStatus:string,partnerShipActivationDate:string,bprId:string,partnerCompanyStatus:string,ediAddresses?:any[]) {
		this.businessUnitId = businessUnitId;
		this.partnerCompanyId = partnerCompanyId;
		this.partnerCompanyName = partnerCompanyName;
		this.partnerCompanyDisplayName = partnerCompanyDisplayName;
		this.facadeCompanyId = facadeCompanyId;partnerCompanyStatus
		this.bprStatus = bprStatus;
    this.partnerShipActivationDate = partnerShipActivationDate;
    this.bprId = bprId;
    this.partnerCompanyStatus = partnerCompanyStatus;
		this.ediAddresses=ediAddresses;

	}
}
