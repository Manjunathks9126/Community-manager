package com.ot.cm.rest.controller.onboarding.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.exception.TGOCPOnboardingRestClientException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.AssetRepoRestClient;

@RestController
@RequestMapping(value = "v1/onboardingservices")
public class AssetRepoFilesController {
	@Autowired
	AssetRepoRestClient assetRepoRestClient;

	@GetMapping(value = "/termsAndConditions/{lang}/{country}", produces = MediaType.TEXT_HTML_VALUE)
	public String getStandardTnC(@PathVariable(value = "lang", required = false) String lang,
			@PathVariable(value = "country", required = false) String country)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		return assetRepoRestClient.getStandardTnC(lang, country);
	}

	@GetMapping(value = "/assets/{type}/{format}/{lang}")
	public ResponseEntity<?> getUserGuide(@PathVariable(value = "lang", required = false) String lang,
			@PathVariable(value = "type", required = true) String type,
			@PathVariable(value = "format", required = false) String format)
			throws TGOCPOnboardingRestClientException, TGOCPRestException {
		String filename = type + "." + format;
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.body(assetRepoRestClient.downloadAsset(lang, type));
	}

}
