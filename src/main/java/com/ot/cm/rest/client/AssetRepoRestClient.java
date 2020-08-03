package com.ot.cm.rest.client;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.ot.cm.constants.AssetRepoHeadersConstants;
import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.entity.AssetList;
import com.ot.cm.rest.template.RequestPayload;
import com.ot.cm.rest.template.TGOCPOnboardingRestTemplate;

@Component
public class AssetRepoRestClient extends BaseRestClient {

	@Autowired
	private TGOCPOnboardingRestTemplate onboardingRestTemplate;

	public String getStandardTnC(String lang, String country)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.ASSETREPO("SEARCH");
		HttpEntity<String> requestEnitity = new HttpEntity<>(setTCAssetSearchHeaders(lang, country));
		RequestPayload<AssetList> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<AssetList>() {
				});
		AssetList searchResponse = onboardingRestTemplate.exchange(payload, ErrorResponse.class);
		if (null != searchResponse && null != searchResponse.getAsset() && searchResponse.getAsset().length > 0) {
			return getCustomTnC(searchResponse.getAsset()[0].getAssetId().toString(), lang);
		} else {
			return null;
		}
	}

	public String getCustomTnC(String customTermsAssetId, String lang)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.ASSETREPO("DOWNLOAD");
		HttpEntity<String> requestEnitity = new HttpEntity<>(setTCAssetSearchHeadersBasic(lang));
		RequestPayload<String> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<String>() {
				});

		return onboardingRestTemplate.exchange(payload, ErrorResponse.class, customTermsAssetId);
	}

	public byte[] downloadAsset(String lang, String assetType)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String assetId = searchAssetID(lang, assetType);
		if (assetId == null) {
			throw new TGOCPOnboardingRestClientException(HttpStatus.NOT_FOUND, "ASSET_SEARCH", "", "");
		}
		String endPoint = serviceURLResolver.ASSETREPO("DOWNLOAD");
		HttpEntity<String> requestEnitity = new HttpEntity<>(downloadAssetHeaders(lang));
		RequestPayload<byte[]> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<byte[]>() {
				});
		return onboardingRestTemplate.exchange(payload, ErrorResponse.class, assetId);
	}

	private String searchAssetID(String lang, String assetType)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String endPoint = serviceURLResolver.ASSETREPO("SEARCH");
		HttpEntity<String> requestEnitity = new HttpEntity<>(setSearchAssetHeaders(lang, assetType));
		RequestPayload<AssetList> payload = new RequestPayload<>(endPoint, HttpMethod.GET, requestEnitity,
				new ParameterizedTypeReference<AssetList>() {
				});
		AssetList searchResponse = onboardingRestTemplate.exchange(payload, ErrorResponse.class);
		if (null != searchResponse && null != searchResponse.getAsset() && searchResponse.getAsset().length > 0) {
			return searchResponse.getAsset()[0].getAssetId().toString();
		} else {
			return null;
		}
	}

	private HttpHeaders setSearchAssetHeaders(String lang, String assetType) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(AssetRepoHeadersConstants.ASSET_SERVICE_NAME, AssetRepoHeadersConstants.NEO_REG_SCHEMA);
		headers.set(AssetRepoHeadersConstants.ASSET_LOCALE,
				(lang != null ? lang : AssetRepoHeadersConstants.TC_LOCALE));
		headers.set(AssetRepoHeadersConstants.ASSET_TYPE, assetType);
		headers.setContentType(MediaType.APPLICATION_XML);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
		return headers;
	}

	private HttpHeaders setTCAssetSearchHeaders(String lang, String country) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(AssetRepoHeadersConstants.ASSET_SERVICE_NAME, AssetRepoHeadersConstants.TC_SERVICE);
		headers.set(AssetRepoHeadersConstants.ASSET_OWNER_BUID, AssetRepoHeadersConstants.ASSET_OWNER_BUID_VALUE);
		headers.set(AssetRepoHeadersConstants.ASSET_LOCALE,
				(lang != null ? lang : AssetRepoHeadersConstants.TC_LOCALE));
		headers.set(AssetRepoHeadersConstants.ASSET_TYPE, AssetRepoHeadersConstants.ASSET_TYPE_VALUE);
		headers.set(AssetRepoHeadersConstants.ASSET_CUSTOM_KEY1_LABEL, AssetRepoHeadersConstants.ASSET_CUSTOM_KEY1);
		headers.set(AssetRepoHeadersConstants.ASSET_CUSTOM_VALUE1_LABEL, AssetRepoHeadersConstants.ASSET_CUSTOM_VALUE1);

		headers.set(AssetRepoHeadersConstants.ASSET_CK_NAME_IDENTIFIER_TC_COUNTRY,
				AssetRepoHeadersConstants.ASSET_CK_NAME_IDENTIFIER_TC_COUNTRY_VALUE);
		headers.set(AssetRepoHeadersConstants.ASSET_CK_VALUE_IDENTIFIER_TC_COUNTRY, (country != null ? country
				: AssetRepoHeadersConstants.ASSET_CK_VALUE_IDENTIFIER_TC_COUNTRY_DEFAULT_VALUE));
		headers.setContentType(MediaType.APPLICATION_XML);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));

		return headers;
	}

	private HttpHeaders setTCAssetSearchHeadersBasic(String lang) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(AssetRepoHeadersConstants.ASSET_SERVICE_NAME, AssetRepoHeadersConstants.TC_SERVICE);
		headers.set(AssetRepoHeadersConstants.ASSET_LOCALE,
				(lang != null ? lang : AssetRepoHeadersConstants.TC_LOCALE));
		headers.setContentType(MediaType.TEXT_HTML);
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));

		return headers;
	}

	private HttpHeaders downloadAssetHeaders(String lang) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(AssetRepoHeadersConstants.ASSET_SERVICE_NAME, AssetRepoHeadersConstants.NEO_REG_SCHEMA);
		headers.set(AssetRepoHeadersConstants.ASSET_LOCALE,
				(lang != null ? lang : AssetRepoHeadersConstants.TC_LOCALE));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		return headers;
	}

}
