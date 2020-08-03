/*
 * *****************************************************************************
 *  All rights reserved. All information contained in this software is confidential
 *  and proprietary to OpenText. No part of this software may be
 *  reproduced or transmitted in any form or any means, electronic, mechanical,
 *  photocopying, recording or otherwise stored in any retrieval system of any
 *  nature without the prior written permission of OpenText.
 *  This material is a trade secret and its confidentiality is strictly maintained.
 *  Use of any copyright notice does not imply unrestricted public access to this
 *  material.
 *
 *  (c) OpenText
 *
 *  *******************************************************************************
 *  Change Log:
 *  Date          Name                Defect#           Description
 *  -------------------------------------------------------------------------------
 *  11/20/2018          Rishabh                              Initial Creation
 *  *****************************************************************************
 */
package com.ot.cm.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "cm.config")
@Getter
@Setter
@ToString
public class ApplicationProperties {
    private String tpGraphAppURL;
    private String keybank_maps;
    private String registrationAppURL;
    private String tpDirectoryRowsPerPage;
    private String tpDirBulkReqEndpoint;
    private String appTypeCode;
    private String tpProfileSegmentTerminator;
    private String tpProfileElementSeparator;
    private String tpProfileSubelementSeparator;
    private String onboardingReportMaxQueryLimit;
    private PSP psp;
    private boolean showTestOption;
    private boolean bulkTPEnabled;
    private ProvisionMap provisionMap;
    private Emerald emerald;
    private String onboardingMaps;
    private Onboarding onboarding;
    @Getter
    @Setter
    @ToString
    public static class PSP {
        private String hostURL;
        private String username;
        private String pwd;
        private String bpmName;
        private String cookie_name;
    }

    @Getter
    @Setter
    @ToString
    public static class Emerald {
        private List<String> refineOptions;
        private String messageAMQurl;
    }


    @Getter
    @Setter
    @ToString
    public static class ProvisionMap {
        private List<String> mapName;
        private List<String> docType;
        private List<String> direction;
        private List<String> documentStandard;
        private List<String> docVersion;
        private List<String> usedBy;
        private List<String> tableEntries;
        private List<String> acr;
        private List<String> edi_dc40_test;
        private List<String> edi_dc40_prod;

    }

    @Getter
    @Setter
    @ToString
    public static class Onboarding {
    	 private String tpProfileSegmentTerminator;
    	    private String tpProfileElementSeparator;
    	    private String tpProfileSubelementSeparator;
    }

}
