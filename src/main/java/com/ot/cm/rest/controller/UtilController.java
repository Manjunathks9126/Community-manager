package com.ot.cm.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.business.service.UtilService;
import com.ot.cm.exception.CMApplicationException;
import com.ot.cm.rest.response.entity.Country;
import com.ot.cm.util.TGOCPSessionUtil;

@RestController
@RequestMapping("util")
public class UtilController extends BaseRestController {

	@Autowired
	private UtilService utilService;

	@GetMapping(value = "/countries")
	public List<Country> getCountries(HttpServletRequest httpServletRequest) throws CMApplicationException {
		String lang = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest).getLocale().getLanguage();
		return utilService.getCountries((lang == null ? "en" : lang));
	}

	@GetMapping(value = "/isTestOptionVisible")
	public Map<String, Boolean> testOptionFlag() {
		Map<String, Boolean> result = new HashMap<>();
		result.put("showTest", appProperties.isShowTestOption());
		return result;
	}

	@GetMapping(value = "/internalCompanyDetails")
	public Map<String, String> getInternalCompany() {
		Map<String, String> result = new HashMap<>();
		result.put("buId", globalProperties.getGxscompanyid());
		result.put("buName", globalProperties.getGxsCompanyName());
		return result;
	}

	@GetMapping(value = "/getAppUrl")
	public Map<String, List<String>> getSkynetUrl() {
		Map<String, List<String>> result = new HashMap<>();
		result.put("urls", globalProperties.getNonNeoURLs());
		return result;
	}
}
