import { NgModule } from "@angular/core";
import { Routes, RouterModule } from '@angular/router';
import { InvitedPartnerProfileComponent } from './invPartner-profile.component';
import { InvitedPartnerOverviewComponent } from './overview/invPartner-overview.component';


const InvitedPartnerProfileRouting:Routes=[
    {
        path:'',component:InvitedPartnerProfileComponent,
        children:[
            {path:'',redirectTo:'overview',pathMatch:'full'},
            {path:'overview',component:InvitedPartnerOverviewComponent}
        ]

    }
]
@NgModule({
    imports:[
        RouterModule.forChild(InvitedPartnerProfileRouting)
    ],
    exports:[RouterModule],
    providers:[]
})
export class InvitedPartnerRoutingModule{}