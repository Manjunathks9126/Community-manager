package com.ot.cm.rest.controller;


import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.rest.response.ErrorResponse;
import com.ot.cm.rest.response.TGOCPRestResponseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("notification")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);


    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    ApplicationProperties applicationProperties;

    @PostMapping("sendMessage")
    public TGOCPRestResponseDetails<String, ErrorResponse> sendNotification(@RequestBody List<String> buIds) {
            boolean statusFlag = true;
            String status = "Message submitted Successfully";
        try {
            jmsTemplate.convertAndSend(applicationProperties.getEmerald().getMessageAMQurl(), buIds);
        } catch (JmsException e) {
            statusFlag =false;
            status = "Error while submitting request to JMS system, Please refer application logs";
            logger.info(e.getMessage());
            logger.error(e.getErrorCode(), e.getMessage());
        }

        TGOCPRestResponseDetails<String, ErrorResponse> response = new TGOCPRestResponseDetails<String, ErrorResponse>();
        response.setResponseEntity(status);
        response.setSuccess(statusFlag);
        response.setStatusMessage(status);
        return response;
    }


}
