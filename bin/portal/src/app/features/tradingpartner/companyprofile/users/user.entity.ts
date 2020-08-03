 import { Contact } from './contact.entity';
 import { Address } from './address.entity';
export class User {
    companyId : String;
    userId : String;   
    firstName : String;
    lastName : String; 
    userLogin : String;
    status : String;
    lastLoginDate : String;
    address : Address = new Address();
    contactInformation : Contact=new Contact();
}

