package com.ot.cm.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@PropertySource(value = "classpath:restendpoints.properties")
@ConfigurationProperties(prefix = "service")
@Getter
@Setter
@ToString
public class RestEndPointsProperties {

	private Map<String, String> CMS;
	private Map<String, String> CMD;
	private Map<String, String> RA;
	private Map<String, String> ASSETREPO;

}
