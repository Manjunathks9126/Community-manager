import { BaseSearchQuery } from "../common/search.base.entity";

export class TPRSearchQuery extends BaseSearchQuery {
  public partnerCompanyId:string="";
  public ediAdderess:string[];
  public tprIds:string[];
  public dateFrom:string;
  public dateTo:string;
  public qualifier:string="";
  public partnerediaddress:string="";
}