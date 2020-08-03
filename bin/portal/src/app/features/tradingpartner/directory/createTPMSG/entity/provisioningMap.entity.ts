import { Maps } from "./workflow.entity";

export class ProvisioningMap {
    constructor(name, dType, dir, stand, dVersion, ub, tabEntry, appCrossRef,edi_dc40_TEST,edi_dc40_PROD) {
        this.mapName = name;
        this.direction = dir;
        this.documentStandard = stand;
        this.docType = dType;
        this.docVersion = dVersion;
        this.usedBy = ub;
        this.tableEntries = tabEntry;
        this.acr = appCrossRef;
        this.edi_dc40_test = edi_dc40_TEST;
        this.edi_dc40_prod = edi_dc40_PROD;
    }

    map: Maps[];
 
    edi_dc40_test: string = "";
    edi_dc40_prod: string = "";
    mapName: string = "";
    documentStandard: string = "";
    docType: string = "";
    docVersion: string = "";
    direction: string = "";
    usedBy: string = "";
    tableEntries: string = "";
    acr: string = "";
}