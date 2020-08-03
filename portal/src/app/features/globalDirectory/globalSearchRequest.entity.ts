
export class FacetData {
    term: string = "";
    facet: string = "";
    nested:boolean = false;
    unmodifiedTerm:string = "";
}

export class TreeNode{
    isHeading?:boolean;
    label?: string;
    countLabel?: string;
    data?: any;
    dataLabel?: any;
    icon?: any;
    expandedIcon?: any;
    collapsedIcon?: any;
    children?: TreeNode[];
    leaf?: boolean;
    expanded?: boolean;
    type?: string;
    parent?: TreeNode;
    partialSelected?: boolean;
    styleClass?: string;
    draggable?: boolean;
    droppable?: boolean;
    selectable?: boolean;
    key?: string;
    unmodifiedTerm?:string;
}



export class GlobalSearchRequest {

    private term: string = "";
    private searchBy: string = "";
    private sortField: string = "BU_NAME%20asc";
    private startIndex: number = 0;
    private limit: number = 30;
    private wildCardSearch: boolean = false;
    facetData:string = "";
    public queryConditions:string = "";
    public appliedFilters: any[] = [];


    clearFilterData() {
        this.appliedFilters = null;
    }

  
    public getIsWildCardSearch(): boolean {
        return this.wildCardSearch;
    }
    public setIsWildCardSearch(value: boolean) {
        this.wildCardSearch = value;
    }
    public getLimit(): number {
        return this.limit;
    }
    public setLimit(value: number) {
        this.limit = value;
    }
    public getStartIndex(): number {
        return this.startIndex;
    }
    public setStartIndex(value: number) {
        this.startIndex = value;
    }

    public getSearchBy(): string {
        return this.searchBy;
    }
    public setSearchBy(value: string) {
        this.searchBy = value;
    }
    public getSortField(): string {
        return this.sortField;
    }
    public setSortField(value: string) {
        this.sortField = value;
    }
    public getTerm(): string {
        return this.term;
    }
    public setTerm(value: string) {
        this.term = value;
    }

}