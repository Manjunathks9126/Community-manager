package com.ot.cm.rest.response;

import java.util.ArrayList;

public class SolrResponse {
    ResponseHeader ResponseHeaderObject;
    Suggest SuggestObject;


    // Getter Methods

    public ResponseHeader getResponseHeader() {
        return ResponseHeaderObject;
    }

    public Suggest getSuggest() {
        return SuggestObject;
    }

    // Setter Methods

    public void setResponseHeader(ResponseHeader responseHeaderObject) {
        this.ResponseHeaderObject = responseHeaderObject;
    }

    public void setSuggest(Suggest suggestObject) {
        this.SuggestObject = suggestObject;
    }

    static class Suggest {
        MySuggester MySuggesterObject;


        // Getter Methods

        public MySuggester getMySuggester() {
            return MySuggesterObject;
        }

        // Setter Methods

        public void setMySuggester(MySuggester mySuggesterObject) {
            this.MySuggesterObject = mySuggesterObject;
        }
    }

    static class MySuggester {
        RAHC_COMPANY RAHC_COMPANYObject;


        // Getter Methods

        public RAHC_COMPANY getRAHC_COMPANY() {
            return RAHC_COMPANYObject;
        }

        // Setter Methods

        public void setRAHC_COMPANY(RAHC_COMPANY RAHC_COMPANYObject) {
            this.RAHC_COMPANYObject = RAHC_COMPANYObject;
        }
    }


    static class RAHC_COMPANY {
        private float numFound;
        ArrayList<Object> suggestions = new ArrayList<Object>();


        // Getter Methods

        public float getNumFound() {
            return numFound;
        }

        // Setter Methods

        public void setNumFound(float numFound) {
            this.numFound = numFound;
        }
    }

    static class ResponseHeader {
        private float status;
        private float QTime;


        // Getter Methods

        public float getStatus() {
            return status;
        }

        public float getQTime() {
            return QTime;
        }

        // Setter Methods

        public void setStatus(float status) {
            this.status = status;
        }

        public void setQTime(float QTime) {
            this.QTime = QTime;
        }
    }


}
