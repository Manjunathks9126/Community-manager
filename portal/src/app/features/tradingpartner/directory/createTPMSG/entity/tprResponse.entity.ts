import { ModifiedTp } from './modifiedTp.entity';

export class TprRequestEntity {
  companyId: string="";
  tprId: string="";
  companyName: string="";
  displayName: string="";
  yourRole: string="";
  modifiedData: ModifiedTp = new ModifiedTp();
}
