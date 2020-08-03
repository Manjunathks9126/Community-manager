package com.ot.cm.rest.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ot.cm.rest.response.entity.Country;

@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompanyDetailsEntity {
    private String displayName;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalcode;
    private String tptype;
    private String companyId;
    private String companyWebsiteURL;
    private String phone;
    private String email;
    private ContactEntity contact;
    private Country country;
    private String selectedInvCode;
    private String[] keys;

    public CompanyDetailsEntity() {
    }

    public CompanyDetailsEntity(String displayName, String companyName, String addressLine1, String addressLine2, String city, String state, String postalcode, String tptype, String companyId, String companyWebsiteURL, String phone, String email, ContactEntity contact, Country country, String selectedInvCode, String[] keys) {
        this.displayName = displayName;
        this.companyName = companyName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalcode = postalcode;
        this.tptype = tptype;
        this.companyId = companyId;
        this.companyWebsiteURL = companyWebsiteURL;
        this.phone = phone;
        this.email = email;
        this.contact = contact;
        this.country = country;
        this.selectedInvCode = selectedInvCode;
        this.keys = keys;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getTptype() {
        return tptype;
    }

    public void setTptype(String tptype) {
        this.tptype = tptype;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyWebsiteURL() {
        return companyWebsiteURL;
    }

    public void setCompanyWebsiteURL(String companyWebsiteURL) {
        this.companyWebsiteURL = companyWebsiteURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContactEntity getContact() {
        return contact;
    }

    public void setContact(ContactEntity contact) {
        this.contact = contact;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getSelectedInvCode() {
        return selectedInvCode;
    }

    public void setSelectedInvCode(String selectedInvCode) {
        this.selectedInvCode = selectedInvCode;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
