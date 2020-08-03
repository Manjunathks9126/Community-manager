import { Contact } from "./contact.entity";



export class FormData {
     displayName:string='';
     companyName:string='';
     addressLine1:string='';
     addressLine2:string='';
     city:string='';
     state:string='';
     postalcode:string='';
     tptype:string='';
     companyId:string='';
     country:string='';
     companyWebsiteURL:string='';
     phone:string='';
     email:string='';
     selectedInvCode:string
     keys:any;
     contact:Contact;
    clear() {
        this.displayName='';
        this.companyName='';
        this.addressLine1='';
        this.addressLine2='';
        this.city='';
        this.state='';
        this.postalcode='';
        this.tptype='';
        this.companyId='';
        this.country='';
        this.companyWebsiteURL='';
        this.phone='';
        this.email='';
        if(null!=this.contact && typeof(this.contact)!= 'undefined')
        {
        this.contact.email='';
        this.contact.firstName='';
        this.contact.lastName='';
        this.contact.phone='';
        }
        this.keys=null;
        this.selectedInvCode='';
    }
}
