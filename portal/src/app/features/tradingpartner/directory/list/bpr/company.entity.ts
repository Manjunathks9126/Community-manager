export class Company {
  public businessUnitId:string;
  public name :string;
  public status:string;
  public tprcount:number;
  public createTimeStamp:string;
  public countryCode:string;
  public displayName:string;
  public ediAddresses:any[];

	constructor(id: string, name: string, status: string, createTimeStamp: string, country: string,ediAddresses?:any[], displayName?: string,tprcount?: number) {
		this.businessUnitId = id;
		this.name = name;
		this.status = status;
		this.createTimeStamp = createTimeStamp;
		this.countryCode = country;
		this.displayName = displayName;
		this.tprcount = tprcount;
		this.ediAddresses=ediAddresses;
		
	}
 

}