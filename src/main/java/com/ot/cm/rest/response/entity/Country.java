package com.ot.cm.rest.response.entity;

public class Country {

    private String countryCode;
    private String countryDescription;

    public Country(String countryCode, String countryDescription) {
        this.countryCode = countryCode;
        this.countryDescription = countryDescription;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryDescription() {
        return countryDescription;
    }

    public void setCountryDescription(String countryDescription) {
        this.countryDescription = countryDescription;
    }

    public Country() {
    }
}
