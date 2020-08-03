package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ot.cm.constants.ErrorCodes;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.rest.client.UtilRestClient;
import com.ot.cm.rest.response.entity.Country;

@Component
public class UtilService {

    @Autowired
    UtilRestClient utilRestClient;

    public List<Country> getCountries(String lang) throws CMApplicationException {
        List<Country> resp= new ArrayList<>();
        try {
            resp = utilRestClient.getCountries(new Locale(lang));
        } catch (Exception ex) {

            throw new CMApplicationException(ErrorCodes.INTERNAL_SERVER_ERROR,
                    "return fetching get countries from CMD", ex);
        }
        return resp;
    }
}
