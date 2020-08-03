import { NgModule } from "@angular/core";
import { InvitedPartnerProfileComponent } from './invPartner-profile.component';
import { InvitedPartnerRoutingModule } from './invPartner-profile-routing.module';
import { InvitedPartnerOverviewComponent } from './overview/invPartner-overview.component';


import {JsonSchemaFormModule} from 'json-editor/dist/src/app/json-schema-form/index';
import { SchemaFormOutputService } from 'json-editor/dist/src/app/services/schema-form-output.service';
import { TranslateModule } from '@ngx-translate/core';
import { DialogueboxModule } from 'tgocp-ng/dist';
import { NgxPermissionsModule } from 'ngx-permissions';


@NgModule({
    imports:[InvitedPartnerRoutingModule,JsonSchemaFormModule,TranslateModule,DialogueboxModule,NgxPermissionsModule],
    declarations:[InvitedPartnerProfileComponent,InvitedPartnerOverviewComponent],
    providers:[SchemaFormOutputService]

})
export class InvitePartnerProfileModule{}