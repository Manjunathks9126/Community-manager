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
 *  10/10/2018          Pavan                              Initial Creation
 *  *****************************************************************************
 */

package com.ot.cm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.FileSystemUtils;

import com.ot.cm.config.properties.ApplicationProperties;
import com.ot.cm.constants.BulkUploadConstants;
import com.ot.config.properties.GlobalProperties;

@SpringBootApplication
public class CommunityManagerApplicationInitializer extends SpringBootServletInitializer implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(CommunityManagerApplicationInitializer.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CommunityManagerApplicationInitializer.class);
	}

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	GlobalProperties globalProperties;

	@PostConstruct
	private void init() {
		System.out.println("Global Properties loaded during TGOCP-CM component intitalization " + globalProperties);
		System.out.println("**************************************************************************************");
		System.out.println("CommunityManager Properties loaded during intitalization " + applicationProperties);
	}

	@Override
	public void run(String... args) {

		try {

			FileSystemUtils.deleteRecursively(Paths.get(BulkUploadConstants.TEMP_LOCATION));
			System.out.println(
					"Deleting " + BulkUploadConstants.TEMP_LOCATION + " Temporary directory contents compeleted");
			new File(BulkUploadConstants.TEMP_LOCATION).mkdir();
		} catch (IOException e) {
			System.out.println("Deleting " + BulkUploadConstants.TEMP_LOCATION + " Temporary directory failed");
		}

	}

}
