package com.ot.cm.vo;

public class GdsFilterOption {

    private String field_name;
    private String field_value;

    GdsFilterOption() {
    }

    public GdsFilterOption(String field_name, String field_value) {
        this.field_name = field_name;
        this.field_value = field_value;
    }


    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_value() {
        return field_value;
    }

    public void setField_value(String field_value) {
        this.field_value = field_value;
    }
}
