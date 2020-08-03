export class TileContentDetail{
    elements :TileContentElement[]
}

export class TileContentElement{
    lines:DisplayText[][];
    src: string;
    iconImageSrc : string
    children: TileContentElement[];
    contentMeta:TileContentMetaData;
}

// Aditional fields can be added on requirement
export class TileContentMetaData{
    buid:string;
    buName:string;
    bprId:string;
}

export class DisplayText{
    headerText:string;
    subtext:string;
}
