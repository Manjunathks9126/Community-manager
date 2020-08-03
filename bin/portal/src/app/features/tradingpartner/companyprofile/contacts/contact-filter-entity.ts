import { BaseSearchQuery } from "../../../common/search.base.entity";

export class ContactsFilterEntity extends BaseSearchQuery {
  public firstName:string="";
  public lastName:string="";
  public email:string="";
  public dateFrom:string="";
  public dateTo:string="";
  public status:string[]=[];
  public exportColumns:any[]=[];

  constructor(){
    super();
  }
}