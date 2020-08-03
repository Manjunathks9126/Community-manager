import { Component, OnInit, Input } from "@angular/core";
import { Tile } from "../entity/tile.entity"; 



@Component({
  selector: 'ot-tile',
  templateUrl: './tile.component.html'
})
export class TileComponent implements OnInit {




  constructor() {

  }

  @Input() tile: Tile;


  @Input() index:any;
  ngOnInit() {

  }
 
}