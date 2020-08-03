export class CustomFieldGroupType {
    uniqueId: string;
    name: string;
    description: string;
    businessUnitId: string;
    dependentCustomfieldId: string;
    dependentChoiceIds: any[] = [];
    dependencyConjunction: string = 'OR';
    categoryId: string;

    constructor(uniqueId: string, name: string, description: string, businessUnitId: string, dependentCustomfieldId: string, dependentChoiceIds: any[], dependencyConjunction: string, categoryId: string) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.businessUnitId = businessUnitId;
        this.dependentCustomfieldId = dependentCustomfieldId;
        this.dependentChoiceIds = dependentChoiceIds;
        this.dependencyConjunction = dependencyConjunction == 'AND' ? 'AND' : 'OR';
        this.categoryId = categoryId;
    }
}