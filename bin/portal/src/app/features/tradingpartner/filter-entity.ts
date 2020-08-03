export class FilterEntity {
  public name:string="";
  public displayName:string="";
  public companyName:string="";
  public partnerCompanyId:string='';
  public after :number;
  public limit:number;
  public sortField:string="";
  public sortOrder:number;
  public status:string[];
  public countOnly:boolean;
  public ediAddress:string="";
  public qualifier:string="";
  public exportColumns:any[]=[];
  public exportAll:boolean=false;
  public dateFrom:any;
  public dateTo:any;

  constructor(){
    
  }

}