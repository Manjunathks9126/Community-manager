package com.ot.cm.rest.client.resolver;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ot.cm.config.properties.RestEndPointsProperties;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.util.TGOCPAssert;
import com.ot.config.properties.GlobalProperties;

@Component
public class ServiceURLResolver {

	private String cmsServiceHost;
	private String cmdServiceHost;
	private String raServiceHost;
	private String assetRepoServiceHost;
	@Autowired
	private RestEndPointsProperties restEndPointsLexicon;

	@Autowired
	private GlobalProperties globalProperties;

	@PostConstruct
	private void init() {
		cmsServiceHost = globalProperties.getRest().getCmsURL();
		cmdServiceHost = globalProperties.getRest().getCmdURL();
		raServiceHost = globalProperties.getRest().getRaURL();
		assetRepoServiceHost = globalProperties.getRest().getAssetRepoURL();
	}

	public String CMS(String apiConstant, String version) throws TGOCPRestException {
		StringBuilder restURL = new StringBuilder();
		TGOCPAssert.notNull(cmsServiceHost, "CMS REST Server Details is not found");
		restURL.append(cmsServiceHost);
		String endPoint = restEndPointsLexicon.getCMS().get(apiConstant + "." + version);
		TGOCPAssert.notNull(endPoint, "EndPoint Not Found:" + endPoint);
		restURL.append(endPoint);
		return restURL.toString();
	}

	public String CMD(String apiConstant, String version) throws TGOCPRestException {
		StringBuilder restURL = new StringBuilder();
		TGOCPAssert.notNull(cmdServiceHost, "CMD REST Server details are not found");
		restURL.append(cmdServiceHost);
		String endPoint = restEndPointsLexicon.getCMD().get(apiConstant + "." + version);
		TGOCPAssert.notNull(endPoint, "EndPoint Not Found:" + endPoint);
		restURL.append(endPoint);
		return restURL.toString();
	}

	public String RAREST(String apiConstant, String version) throws TGOCPRestException {
		StringBuilder restURL = new StringBuilder();
		TGOCPAssert.notNull(raServiceHost, "RA REST Server details are not found");
		restURL.append(raServiceHost);
		String endPoint = restEndPointsLexicon.getRA().get(apiConstant + "." + version);
		TGOCPAssert.notNull(endPoint, "EndPoint Not Found:" + endPoint);
		restURL.append(endPoint);
		return restURL.toString();
	}

	public String ASSETREPO(String apiConstant) throws TGOCPRestException {
		StringBuilder restURL = new StringBuilder();
		TGOCPAssert.notNull(assetRepoServiceHost, "AssetRepo REST Server details are not found");
		restURL.append(assetRepoServiceHost);
		String endPoint = restEndPointsLexicon.getASSETREPO().get(apiConstant);
		TGOCPAssert.notNull(endPoint, "EndPoint Not Found:" + endPoint);
		restURL.append(endPoint);
		return restURL.toString();
	}

}
