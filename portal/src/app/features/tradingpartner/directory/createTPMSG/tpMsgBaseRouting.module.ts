import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'; 
import { CompanyDetailsComponent } from './add/companyDetails.component';




const TPMSGRoutes: Routes = [
    {
        path: 'add', component: CompanyDetailsComponent
    }]

@NgModule({
    imports: [
        RouterModule.forChild(
            TPMSGRoutes
        ),
    ],
    exports: [
        RouterModule
    ],
    providers: []
})
export class TpMsgRoutingModule { }