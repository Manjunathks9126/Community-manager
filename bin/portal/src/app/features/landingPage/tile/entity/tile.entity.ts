export class Tile {
    serviceInstanceId: number;
    customizationId: number;
    position: number;
    header: TileHeader;
    content: TileContent;
    footer: TileFooter;
    isCustom: boolean;
    constructor() {

    }

    public static parseJsonObject(json: any): Tile {
        let tile = new Tile();
        tile.serviceInstanceId = json.serviceInstanceId;
        tile.customizationId = json.customizationId;
        tile.position = json.position;

        tile.header = new TileHeader();
        tile.header.iconImageSrc = json.header.iconImageSrc;
        tile.header.settingsSrc = json.header.settingsSrc;
        tile.header.displayText = json.header.displayText;
        tile.header.searchEnabled = json.header.searchEnabled;

        tile.footer = new TileFooter();
        tile.footer.expandTilesEnabled = json.footer.expandTilesEnabled;

        tile.content = json.content;
        return tile;
    }

    public static parseJsonArray(tileJson: any): Tile[] {
        let tileList: Tile[] = Array<Tile>();
        return <Tile[]>JSON.parse(tileJson);
    }
}

export class TileHeader {
    iconImageSrc: string;
    settingsSrc: string;
    displayText: string;
    searchEnabled: Boolean;
    isCount:Boolean;
    src: string;
}

export class TileContent {
    displaySrc: string;
    logoutUrl: string;
}

export class TileCustomContent {
    displaySrc: string;
    logoutUrl: string;
}

export class TileFooter {
    expandTilesEnabled: Boolean;
}