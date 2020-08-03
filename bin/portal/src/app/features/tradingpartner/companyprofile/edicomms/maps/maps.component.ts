import { Component, OnInit } from "@angular/core";
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector:'tp-profile-maps',
    templateUrl:'./maps.component.html'
})
export class MapsComponent implements OnInit{

    mapList:any=[];
    selectedData:any[]=[];
    totalRecords: number = 0;
    isFilterApplied = false;
    isFilterClicked=false;
    isSearchClicked=false;
    searchMap:any;
    currentLanguage: string = "en";
    constructor(private translate:TranslateService) {
      }
    ngOnInit(): void {

        this.mapList=[
            {mapName:"map1",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound' },
            {mapName:"map2",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Outbound'  },
            {mapName:"map3",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  },
            {mapName:"map4",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  },
            {mapName:"map5",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  },
            {mapName:"map6",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Outbound'  },
            {mapName:"map7",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Outbound'  },
            {mapName:"map8",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  },
            {mapName:"map9",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  },
            {mapName:"map10",docStandard:'ANSI X12',docType:'810',version:'5020',direction:'Inbound'  }

        ]
       this.totalRecords=this.mapList.length;
       this.currentLanguage=this.translate.currentLang;
    }

    displaySearchPanel() {
        this.isSearchClicked = !this.isSearchClicked; 
        this.isFilterClicked=false;   
    }
    displayFilterPanel() {
        this.isFilterClicked = !this.isFilterClicked;
        this.isSearchClicked=false;
    }
    lazyLoadMapList(event){
    }
    searchdata(){
        this.isFilterApplied=true;
        
    }
    clearSearchCriteria(){

    }

}
