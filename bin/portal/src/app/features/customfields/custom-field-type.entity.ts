export class CustomFieldType {
    uniqueId : string;
    question : string;
    defaultAnswer? : string = null;
    defaultChoice? : choice;
    customFieldId: any;
    mandatory : boolean;
    groupId : string;
    customFieldType : string;
    instruction? :string;
    choices? : choice[]=[];
    dependentCustomfieldId: string;
    dependentChoiceIds:any[]= [];
    dependencyConjunction:string='OR';
    validationRules:any;
}

export class choice{
    id: string;
    position:number;
    value:string
}