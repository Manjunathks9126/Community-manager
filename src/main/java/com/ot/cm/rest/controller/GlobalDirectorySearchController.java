package com.ot.cm.rest.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.audit.aspect.Auditable;
import com.ot.audit.constants.AuditType;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.vo.ExtendedAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ot.cm.aspect.CheckPermissions;
import com.ot.cm.business.service.GlobalDirectoryService;
import com.ot.cm.business.service.IMDataService;
import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.PermissionConstants;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.cm.util.TGOCPSessionUtil;
import com.ot.cm.vo.GdsFilterOption;
import com.ot.cm.vo.GlobalSearchRequest;
import com.ot.cm.vo.UserInfo;

@RestController
@RequestMapping(value = "globalsearch")
public class GlobalDirectorySearchController {

    @Autowired
    GlobalDirectoryService searchService;

    @Autowired
    ApplicationProperties appProperties;

    @Autowired
    IMDataService imDataService;

    @Autowired
    private BusinessUntilRestClient businessUntilRestClient;


    @PostMapping(value = "/suggestions/{suggestion}")
    @CheckPermissions(permissions = PermissionConstants.VIEW_GDS)
    public TGOCPRestResponse<String, ErrorResponse> getSuggestions(
            @RequestBody GlobalSearchRequest globalSearchRequest,
            @PathVariable("suggestion") String suggestion) throws TGOCPBaseException {

        TGOCPRestResponse<String, ErrorResponse> response;
        TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
        responseDetails.setSuccess(true);
        responseDetails.setResponseEntity(searchService.getSuggestions(globalSearchRequest, suggestion));
        response = new TGOCPRestResponse<>(responseDetails);

        return response;
    }

    @GetMapping(value = "/companygpdconsent")
    public TGOCPRestResponse<String, ErrorResponse> gpdConsent(HttpServletRequest httpServletRequest) throws TGOCPBaseException  {
        UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);
        BusinessUnitDetailsType businessType = businessUntilRestClient.getBusinessUnitDetailsType(userInfo.getCompanyId());

        TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
        responseDetails.setSuccess(true);
        responseDetails.setResponseEntity(businessType.getCustomerBUGPDVisibility());
        return new TGOCPRestResponse<>(responseDetails);
    }



    @GetMapping(value = "/jsonschema")
    public TGOCPRestResponse<String, ErrorResponse> jsonSchema(@RequestParam String buid,
                                                               @RequestParam String schematype) throws TGOCPBaseException  {
        TGOCPRestResponse<String, ErrorResponse> response;
        TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
        responseDetails.setSuccess(true);
        responseDetails.setResponseEntity(searchService.getJsonSchema(buid,schematype));
        response = new TGOCPRestResponse<>(responseDetails);

        return response;
    }


    @GetMapping(value = "/extendedAttributes")
    public TGOCPRestResponse<ExtendedAttribute[], ErrorResponse> extendedAttributesJson(@RequestParam String buid) throws TGOCPBaseException  {
        TGOCPRestResponse<ExtendedAttribute[], ErrorResponse> response;
        TGOCPRestResponseDetails<ExtendedAttribute[], ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
        responseDetails.setSuccess(true);
        responseDetails.setResponseEntity(searchService.getExtendedAttribute(buid));
        response = new TGOCPRestResponse<>(responseDetails);

        return response;
    }


    @PostMapping(value = "/extendedAttributes/{categoryContentId}")
    @Auditable(auditType = AuditType.JMS_AUDIT, actionCode = "NEO-OTHER_INFO-U", actionSummary = "Update Other info", actionDetail = "{0} updated Other info for {1}", hasContent = true)
    public TGOCPRestResponse<String, ErrorResponse> updateExtendedAttribute(@RequestBody ExtendedAttribute extendedData,
                                                                       @PathVariable("categoryContentId") Integer categoryContentId,
                                                                       HttpServletRequest httpServletRequest) throws TGOCPBaseException  {
        UserInfo userInfo = TGOCPSessionUtil.getUserInfoInSession(httpServletRequest);

        TGOCPRestResponse<String, ErrorResponse> response;
        TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
        responseDetails.setSuccess(true);
        responseDetails.setResponseEntity(searchService.updateExtendedAttribute(extendedData,categoryContentId));
        response = new TGOCPRestResponse<>(responseDetails,userInfo.getFirstName()+" "+userInfo.getLastName(),
                extendedData.getCompanyName(),extendedData);
        searchService.runDeltaQuery();
        return response;
    }


    @GetMapping(value = "/refineOptions")
    @CheckPermissions(permissions = PermissionConstants.VIEW_GDS)
    public List<GdsFilterOption> getRefineOptions() {
        List<GdsFilterOption> refineOptions = new ArrayList<>();
        List<String> refineRawOptions = appProperties.getEmerald().getRefineOptions();
        for (String rawString : refineRawOptions) {
            if (rawString.contains("=")) {
                String[] keyValArray = rawString.split("=");
                refineOptions.add(new GdsFilterOption(keyValArray[0], keyValArray[1]));
            }
        }
        return refineOptions;
    }
}
