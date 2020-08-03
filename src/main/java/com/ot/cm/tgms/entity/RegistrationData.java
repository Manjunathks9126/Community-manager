package com.ot.cm.tgms.entity;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "registrationData")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistrationData {
	private BusinessUnit businessUnit;

	private BusinessPointOfContact businessPointOfContact;

	private TgtsProfiles tgtsProfiles;

	private TechnicalPointOfContact technicalPointOfContact;

	private TradingAddress tradingAddress;

	private User user;

	private TgmsBilling tgmsBilling;

	private CompanyInfo companyInfo;

	private ContactInfo contactInfo;

	private ProductionDetails productionDetails;

	private TestDetails testDetails;

	private MessageDetails messageDetails;

	private String ediStandard;

	private VanDetails vanDetails;

	private Maps[] maps;
	private SelectOptions selectOptions;

	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public BusinessPointOfContact getBusinessPointOfContact() {
		return businessPointOfContact;
	}

	public void setBusinessPointOfContact(BusinessPointOfContact businessPointOfContact) {
		this.businessPointOfContact = businessPointOfContact;
	}

	public TgtsProfiles getTgtsProfiles() {
		return tgtsProfiles;
	}

	public void setTgtsProfiles(TgtsProfiles tgtsProfiles) {
		this.tgtsProfiles = tgtsProfiles;
	}

	public TechnicalPointOfContact getTechnicalPointOfContact() {
		return technicalPointOfContact;
	}

	public void setTechnicalPointOfContact(TechnicalPointOfContact technicalPointOfContact) {
		this.technicalPointOfContact = technicalPointOfContact;
	}

	public TradingAddress getTradingAddress() {
		return tradingAddress;
	}

	public void setTradingAddress(TradingAddress tradingAddress) {
		this.tradingAddress = tradingAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TgmsBilling getTgmsBilling() {
		return tgmsBilling;
	}

	public void setTgmsBilling(TgmsBilling tgmsBilling) {
		this.tgmsBilling = tgmsBilling;
	}

	public ProductionDetails getProductionDetails() {
		return productionDetails;
	}

	public void setProductionDetails(ProductionDetails productionDetails) {
		this.productionDetails = productionDetails;
	}

	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public MessageDetails getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(MessageDetails messageDetails) {
		this.messageDetails = messageDetails;
	}

	public String getEdiStandard() {
		return ediStandard;
	}

	public void setEdiStandard(String ediStandard) {
		this.ediStandard = ediStandard;
	}

	public TestDetails getTestDetails() {
		return testDetails;
	}

	public void setTestDetails(TestDetails testDetails) {
		this.testDetails = testDetails;
	}

	public VanDetails getVanDetails() {
		return vanDetails;
	}

	public void setVanDetails(VanDetails vanDetails) {
		this.vanDetails = vanDetails;
	}

	public Maps[] getMaps() {
		return maps;
	}

	public void setMaps(Maps[] maps) {
		this.maps = maps;
	}

	public SelectOptions getSelectOptions() {
		return selectOptions;
	}

	public void setSelectOptions(SelectOptions selectOptions) {
		this.selectOptions = selectOptions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegistrationData [businessUnit=");
		builder.append(businessUnit);
		builder.append(", businessPointOfContact=");
		builder.append(businessPointOfContact);
		builder.append(", tgtsProfiles=");
		builder.append(tgtsProfiles);
		builder.append(", technicalPointOfContact=");
		builder.append(technicalPointOfContact);
		builder.append(", tradingAddress=");
		builder.append(tradingAddress);
		builder.append(", user=");
		builder.append(user);
		builder.append(", tgmsBilling=");
		builder.append(tgmsBilling);
		builder.append(", companyInfo=");
		builder.append(companyInfo);
		builder.append(", contactInfo=");
		builder.append(contactInfo);
		builder.append(", productionDetails=");
		builder.append(productionDetails);
		builder.append(", testDetails=");
		builder.append(testDetails);
		builder.append(", messageDetails=");
		builder.append(messageDetails);
		builder.append(", ediStandard=");
		builder.append(ediStandard);
		builder.append(", vanDetails=");
		builder.append(vanDetails);
		builder.append(", maps=");
		builder.append(Arrays.toString(maps));
		builder.append(", selectOptions=");
		builder.append(selectOptions);
		builder.append("]");
		return builder.toString();
	}

}
