package com.ot.cm.business.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.xml.sax.SAXException;

import com.gxs.services.bsapi.rs.v2.entity.SuccessResponse;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.response.onboarding.entity.RegistrationTaskOutputWrapper;
import com.ot.cm.rest.response.onboarding.entity.RegistrationWorkflow;
import com.ot.cm.util.Pair;
import com.ot.session.entity.TGOCPSMUserVO;

public interface TaskBasedRegistrationService {
	public Pair<SuccessResponse, RegistrationWorkflow> submitWorkflow(RegistrationTaskOutputWrapper taskWrapper,
			TGOCPSMUserVO tgocpSMUserVO) throws JsonParseException, JsonMappingException, IOException, SAXException,
			ParserConfigurationException, TGOCPRestException, JAXBException;
}
