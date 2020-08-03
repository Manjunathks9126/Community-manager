package com.ot.cm.business.service;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.helper.GlobalSearchHelper;
import com.ot.cm.rest.client.BaseRestClient;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.util.CommonUtil;
import com.ot.cm.vo.ExtendedAttribute;
import com.ot.cm.vo.GlobalSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.ParseException;

import static com.ot.cm.constants.ApplicationConstants.*;

@Service
public class GlobalDirectorySearchImpl extends BaseRestClient implements GlobalDirectoryService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GlobalSearchHelper globalSearchHelper;

    @Override
    public String getSuggestions(GlobalSearchRequest gsr, String suggestion) throws TGOCPRestException {

        URI uri;

        switch (suggestion) {
            case BU_NAME:
                uri = globalSearchHelper.formSolrSimpleQueryURI(gsr);
                break;
            case FACADE:
                uri = globalSearchHelper.formFacadeSearchQueryURI(gsr);
                break;
            default:
                uri = globalSearchHelper.formSolrFacetQueryURI(gsr,suggestion);
        }

		return getResponseAsString(uri);
	}

	private String getResponseAsString(URI uri) throws TGOCPRestException {
		ResponseEntity<String> exchange;
		try {
			exchange = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>("headers", basicHeaders()), String.class);
		} catch (RestClientException e) {
			throw new TGOCPRestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "500", e.getMessage(), CommonUtil.getStackTrace(e));
		}
		return exchange.getBody();
	}

	@Override
    public String getJsonSchema(String buId, String type) throws TGOCPRestException {
        ResponseEntity<String> exchange;
        String neoRestURl = globalProperties.getRest().getRaURL();
        try {
            exchange = restTemplate.exchange(neoRestURl + "/asset/jsonschema?buId=" + buId + "&type=" + type, HttpMethod.GET, new HttpEntity<>("headers", basicHeaders()), String.class);
        } catch (RestClientException e) {
            throw new TGOCPRestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "500", e.getMessage(), CommonUtil.getStackTrace(e));
        }
        return exchange.getBody();
    }

    public ExtendedAttribute[] getExtendedAttribute(String buId) throws TGOCPBaseException {
        ResponseEntity<ExtendedAttribute[]> exchange;
        String cmdRestURL = globalProperties.getRest().getCmdURL();
        try {
            exchange = restTemplate.exchange(cmdRestURL + "/rest/v1/categoryContents?buId=" + buId, HttpMethod.GET, new HttpEntity<>("headers", cmdAppSession()), ExtendedAttribute[].class);
        } catch (RestClientException e) {
            throw new TGOCPRestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "500", e.getMessage(), CommonUtil.getStackTrace(e));
        }
        return exchange.getBody();
    }

    public String updateExtendedAttribute(ExtendedAttribute extendedAttribute,Integer categoryContentId) throws TGOCPBaseException {
        ResponseEntity<String> exchange;
        String cmdRestURL = globalProperties.getRest().getCmdURL();
        HttpEntity<ExtendedAttribute> requestEntity = new HttpEntity<>(extendedAttribute, cmdAppSession());
        try {
            exchange = restTemplate.exchange(cmdRestURL + "/rest/v1/categoryContents/" + categoryContentId, HttpMethod.PUT, requestEntity, String.class);
        } catch (RestClientException e) {
            throw new TGOCPRestException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), "500", e.getMessage(), CommonUtil.getStackTrace(e));
        }
        return exchange.getBody();
    }

    /**
     * Solr call to delta-import of bpr data
     */
     @Override
    public void runDeltaQuery() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("cache-control", "no-cache");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("command", "delta-import");
        formData.add("verbose", "false");
        formData.add("clean", "false");
        formData.add("commit", "true");
        formData.add("core", "ra_im");
        formData.add("name", "dataimport");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(globalProperties.getSolrURL() + "/ra_im/dataimport?indent=on&wt=json", HttpMethod.POST, request, String.class);
        System.out.println("Delta import result at BPR => " + exchange + "formData: " + formData.toString());
    }
}
