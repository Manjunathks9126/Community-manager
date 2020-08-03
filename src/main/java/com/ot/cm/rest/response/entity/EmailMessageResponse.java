/**
 * 
 */
package com.ot.cm.rest.response.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author nkumari
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailMessageResponse {
	@XmlElement
	private String subject;
	@XmlElement
	private String content;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
