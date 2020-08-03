package com.ot.cm.rest.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.gxs.services.bsapi.rs.v2.entity.ErrorResponse;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.response.TGOCPRestResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import com.ot.session.entity.Feedback;
import com.ot.session.service.FeedbackService;


@RestController
@RequestMapping("/feedback")

public class FeedbackController {
	@Autowired
	FeedbackService feedbackService;
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public TGOCPRestResponse<String, ErrorResponse> sendFeedback(@RequestBody Feedback feedback,
			HttpServletRequest httpServletRequest) throws TGOCPBaseException, JsonParseException, JsonMappingException, JsonProcessingException, IOException {
	TGOCPRestResponse<String, ErrorResponse> response = null;
	TGOCPRestResponseDetails<String, ErrorResponse> responseDetails = new TGOCPRestResponseDetails<>();
	feedback.setSource("Community Manager");
	String feedbackResponse=feedbackService.getFeedbackData(feedback,httpServletRequest);
	responseDetails.setResponseEntity(feedbackResponse);
	responseDetails.setSuccess(true);
	responseDetails.setHttpStatus(HttpStatus.OK);
	response = new TGOCPRestResponse<>(responseDetails);
	return response;
}
}
