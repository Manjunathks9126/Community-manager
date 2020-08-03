import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from "@ngx-translate/core";
import {UserInfoService} from '../../services/user-info.service';

/* Feature Modules */
import { SystemErrorComponent } from './system-error.component';
import { UserInfoResolver } from "../../resolvers/user.info.resolver";


@NgModule({
  declarations: [
    SystemErrorComponent
  ],
  imports: [CommonModule,TranslateModule],
  providers: [UserInfoResolver,UserInfoService],

})
export class SystemErrorModule { }
