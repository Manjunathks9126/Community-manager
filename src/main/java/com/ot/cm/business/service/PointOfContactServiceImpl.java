package com.ot.cm.business.service;

import com.gxs.services.bsapi.rs.v2.entity.PointOfContactDetailsType;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class PointOfContactServiceImpl {

    @Autowired
    BusinessUntilRestClient businessUntilRestClient;
    private com.gxs.services.bsapi.rs.v2.entity.PocTypes PocTypes;

    public PointOfContactDetailsType getTechnicalPointOfContact(String companyId) throws TGOCPBaseException {
        PointOfContactDetailsType[] pointOfContactsArray =  businessUntilRestClient.getPointOfcontacts(companyId);
        Optional<PointOfContactDetailsType> optionalContact = Arrays.stream(pointOfContactsArray).filter(
                data->data.getContactType()==com.gxs.services.bsapi.rs.v2.entity.PocTypes.fromValue("TECHNICAL_CONTACT")).findAny();

        if(optionalContact.isPresent()){
            return optionalContact.get();
        }else{
            throw new TGOCPBaseException("404","No Technical point of contact found");
        }
    }
}
