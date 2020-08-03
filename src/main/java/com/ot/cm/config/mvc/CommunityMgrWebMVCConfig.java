/******************************************************************************
 All rights reserved. All information contained in this software is confidential
 and proprietary to opentext. No part of this software may be
 reproduced or transmitted in any form or any means, electronic, mechanical,
 photocopying, recording or otherwise stored in any retrieval system of any
 nature without the prior written permission of opentext.
 This material is a trade secret and its confidentiality is strictly maintained.
 Use of any copyright notice does not imply unrestricted public access to this
 material.

 (c) opentext

 *******************************************************************************
 Change Log:
 Date          Name                Defect#           Description
 -------------------------------------------------------------------------------
 08/04/2017    Madan                              Initial Creation
 ******************************************************************************/

package com.ot.cm.config.mvc;

import static com.ot.cm.constants.EnvironmentConstants.REST_TIME_OUT;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ot.cm.filter.session.CommunityMgrSessionBuilder;
import com.ot.cm.interceptor.resthandler.CommunityMgrHandlerInterceptor;
import com.ot.cm.util.JacksonWrapper;
import com.ot.config.properties.GlobalProperties;
import com.ot.session.filter.MappedDiagnosticContextFilter;

/**
 * Java-based configuration for of MVC. it is equivalent to web.xml in
 * traditional applications
 * 
 * @author Madan
 */

@Configuration
@ComponentScan(basePackages = "com.ot")
@EnableAspectJAutoProxy
public class CommunityMgrWebMVCConfig implements WebMvcConfigurer {

	@Autowired
	protected GlobalProperties globalProperties;

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver irv = new InternalResourceViewResolver();
		irv.setPrefix("/");
		irv.setSuffix(".jsp");
		return irv;
	}

	@Bean
	public RestTemplate customRestTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

		httpRequestFactory.setConnectionRequestTimeout(REST_TIME_OUT);
		httpRequestFactory.setConnectTimeout(REST_TIME_OUT);
		httpRequestFactory.setReadTimeout(REST_TIME_OUT);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

		return restTemplate;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/ApplicationMessages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		Properties properties = new Properties();
		properties.setProperty("mail.mime.charset", "utf-8");
		properties.setProperty("mail.smtp.allow8bitmime", "true");
		properties.setProperty("mail.smtps.allow8bitmime", "true");
		mailSenderImpl.setJavaMailProperties(properties);
		mailSenderImpl.setProtocol(globalProperties.getMailProtocol());
		mailSenderImpl.setHost(globalProperties.getMailHost());
		return mailSenderImpl;
	}

	@Bean
	public CommunityMgrHandlerInterceptor interceptor() {
		return new CommunityMgrHandlerInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor());
	}

	@Bean(name = "jacksonWrapper")
	@Primary
	public JacksonWrapper getjacksonwrapper() {
		return new JacksonWrapper();
	}

	@Bean
	public FilterRegistrationBean<Filter> registerSessionFilter() {
		FilterRegistrationBean<Filter> sessionFilterregistration = new FilterRegistrationBean<Filter>();
		sessionFilterregistration.setFilter(new CommunityMgrSessionBuilder());
		return sessionFilterregistration;
	}

	/**
	 * Registers the MDC filter in startup to set MDC key in logs
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<Filter> registerMDCFilter() {
		final FilterRegistrationBean<Filter> mdcRegistration = new FilterRegistrationBean<Filter>();
		mdcRegistration.setFilter(new MappedDiagnosticContextFilter());
		return mdcRegistration;
	}
}
