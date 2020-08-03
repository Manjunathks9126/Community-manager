import { Component, OnInit, Input, Output, EventEmitter, ElementRef } from "@angular/core";
import { Subject } from "rxjs";
import { Tile, TileHeader, TileContent } from "./entity/tile.entity";
//import { DragulaService } from "ng2-dragula"; 
import {takeUntil} from "rxjs/internal/operators/takeUntil";

@Component({
  selector: 'ot-tile-container',
  templateUrl: './tile.container.component.html'
})
export class TilesContainerComponent implements OnInit {
  destroy$: Subject<boolean> = new Subject<boolean>();

  @Input() tiles: Tile[];
  @Input() additionalData: {};
  @Output() onTileDragged: EventEmitter<any> = new EventEmitter();

  msgTile: Tile = new Tile();
  msgHeader: TileHeader = new TileHeader();
  msgContent: TileContent = new TileContent();



  constructor(public el: ElementRef/* , private dragulaService: DragulaService */) {
   
  }


  ngOnInit() {
  /*   this.dragulaService.drop().pipe(takeUntil(this.destroy$)).subscribe(({ el, source }) => {
      let movedTileIds = new Array<number>();
       value.forEach(element => {
        if (element.id) {
          movedTileIds.push(Number.parseInt(element.id));
        }
      }); 
      //TO  above code modification for upgrade
      this.onTileDragged.emit(movedTileIds); 
    });*/

    this.msgHeader.displayText = "";
    this.msgContent.displaySrc = "";
    this.msgTile.header = this.msgHeader;
    this.msgTile.content = this.msgContent;
  }



  ngOnDestroy() {
    this.destroy$.next(true);
    this.destroy$.unsubscribe();
  }


}