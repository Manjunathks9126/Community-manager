export class BaseSearchQuery {
  public after :number;
  public limit:number;
  public sortField:string="";
  public sortOrder:string="";
  countOnly:boolean;

  constructor(){
    
  }
}