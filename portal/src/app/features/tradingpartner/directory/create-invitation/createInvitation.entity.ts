export class CreateInvitationEntity {
  public companyDetailsEntity: CompanyDetailsEntity = new CompanyDetailsEntity();
  public invitationEntity: any;
  public requestorTask: string;
  public workflowId: any;
  public taskId: any;
  public serviceName: any;
}

export class ReinvitationEntity{
  public invitationCode:string;
	public invitationId:any;
  public workflowId:any;
  public serviceName:any;
  public taskId:any;
	public companyDetailsEntity: CompanyDetailsEntity = new CompanyDetailsEntity();
	public requestorTask:string;
}

export class CompanyDetailsEntity {
  displayName: string;
  companyName: string;
  addressLine1: string;
  addressLine2: string;
  city: string;
  state: string;
  postalcode: string;
  tptype: string;
  companyId: string;
  companyWebsiteURL: string;
  phone: string;
  email: string;
  contact: ContactEntity = new ContactEntity();
  country: string;
  selectedInvCode: string;
  keys: string[];
}
export class ContactEntity {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
}