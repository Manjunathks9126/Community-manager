package com.ot.cm.rest.client;

import java.util.List;
import java.util.Locale;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.session.annotation.Loggable;

@Component
public class UtilRestClient extends BaseRestClient{


    @Loggable
    public List<Country> getCountries(Locale locale) throws TGOCPRestException {
        String endPoint = globalProperties.getRest().getCmdURL() + "/rest/v1/isoCountryCodes?languageSubtype={locale}&sort=countryDescription";
        HttpEntity<String> requestType = new HttpEntity<>(cmdAppSession());
        String param = locale.getLanguage();
        RequestPayload<List<Country>> payload = new RequestPayload<>(endPoint,
                HttpMethod.GET,
                requestType,
                new ParameterizedTypeReference<List<Country>>() {
                });
        List<Country> restResponse = restTemplate.exchange(payload, ErrorResponse.class, param);
        return restResponse;

    }
}
