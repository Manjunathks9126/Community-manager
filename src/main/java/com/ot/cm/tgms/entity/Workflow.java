package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class Workflow {
	private ProvisioningRequestData provisioningRequestData;

	private ProcessingContext processingContext;

	public ProvisioningRequestData getProvisioningRequestData ()
	{
		return provisioningRequestData;
	}

	public void setProvisioningRequestData (ProvisioningRequestData provisioningRequestData)
	{
		this.provisioningRequestData = provisioningRequestData;
	}

	public ProcessingContext getProcessingContext ()
	{
		return processingContext;
	}

	public void setProcessingContext (ProcessingContext processingContext)
	{
		this.processingContext = processingContext;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [provisioningRequestData = "+provisioningRequestData+", processingContext = "+processingContext+"]";
	}

}
