import { Component, OnInit, Input } from "@angular/core"; 
import { TileFooter } from "../../entity/tile.entity";


@Component({
  selector: 'ot-tile-footer',
  templateUrl : './tile.footer.component.html'
})
export class TileFooterComponent implements OnInit {

  constructor() { }

  @Input() tileFooter :TileFooter;

  ngOnInit() {
  }

}