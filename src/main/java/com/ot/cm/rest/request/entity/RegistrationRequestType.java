package com.ot.cm.rest.request.entity;

public class RegistrationRequestType {
    protected int requestId;
    protected int trackingId;
    protected String workflowId;

    protected RegistrationRequestType.Request request;
    protected RegistrationRequestType.Company company;
    protected String mailboxId;
    protected String tradingAddress;
    protected String userId;
    protected String userLogin;
    protected String description;
    protected String approvedBy;
    protected String createdDate;
    protected String modifiedDate;

    public RegistrationRequestType() {
    }

    public int getRequestId() {
        return this.requestId;
    }

    public void setRequestId(int var1) {
        this.requestId = var1;
    }

    public int getTrackingId() {
        return this.trackingId;
    }

    public void setTrackingId(int var1) {
        this.trackingId = var1;
    }

    public String getWorkflowId() {
        return this.workflowId;
    }

    public void setWorkflowId(String var1) {
        this.workflowId = var1;
    }

    public RegistrationRequestType.Request getRequest() {
        return this.request;
    }

    public void setRequest(RegistrationRequestType.Request var1) {
        this.request = var1;
    }

    public RegistrationRequestType.Company getCompany() {
        return this.company;
    }

    public void setCompany(RegistrationRequestType.Company var1) {
        this.company = var1;
    }

    public String getMailboxId() {
        return this.mailboxId;
    }

    public void setMailboxId(String var1) {
        this.mailboxId = var1;
    }

    public String getTradingAddress() {
        return this.tradingAddress;
    }

    public void setTradingAddress(String var1) {
        this.tradingAddress = var1;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String var1) {
        this.userId = var1;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public void setUserLogin(String var1) {
        this.userLogin = var1;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String var1) {
        this.description = var1;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(String var1) {
        this.approvedBy = var1;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String var1) {
        this.createdDate = var1;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(String var1) {
        this.modifiedDate = var1;
    }

    @Override
	public boolean equals(Object var1) {
        if (var1 != null && this.getClass() == var1.getClass()) {
            if (this == var1) {
                return true;
            } else {
                RegistrationRequestType var2 = (RegistrationRequestType)var1;
                int var3 = this.getRequestId();
                int var4 = var2.getRequestId();
                if (var3 != var4) {
                    return false;
                } else {
                    var3 = this.getTrackingId();
                    var4 = var2.getTrackingId();
                    if (var3 != var4) {
                        return false;
                    } else {
                        String var5 = this.getWorkflowId();
                        String var8 = var2.getWorkflowId();
                        if (this.workflowId != null) {
                            if (var2.workflowId == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.workflowId != null) {
                            return false;
                        }

                        RegistrationRequestType.Request var6 = this.getRequest();
                        RegistrationRequestType.Request var9 = var2.getRequest();
                        if (this.request != null) {
                            if (var2.request == null) {
                                return false;
                            }

                            if (!var6.equals(var9)) {
                                return false;
                            }
                        } else if (var2.request != null) {
                            return false;
                        }

                        RegistrationRequestType.Company var7 = this.getCompany();
                        RegistrationRequestType.Company var10 = var2.getCompany();
                        if (this.company != null) {
                            if (var2.company == null) {
                                return false;
                            }

                            if (!var7.equals(var10)) {
                                return false;
                            }
                        } else if (var2.company != null) {
                            return false;
                        }

                        var5 = this.getMailboxId();
                        var8 = var2.getMailboxId();
                        if (this.mailboxId != null) {
                            if (var2.mailboxId == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.mailboxId != null) {
                            return false;
                        }

                        var5 = this.getTradingAddress();
                        var8 = var2.getTradingAddress();
                        if (this.tradingAddress != null) {
                            if (var2.tradingAddress == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.tradingAddress != null) {
                            return false;
                        }

                        var5 = this.getUserId();
                        var8 = var2.getUserId();
                        if (this.userId != null) {
                            if (var2.userId == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.userId != null) {
                            return false;
                        }

                        var5 = this.getUserLogin();
                        var8 = var2.getUserLogin();
                        if (this.userLogin != null) {
                            if (var2.userLogin == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.userLogin != null) {
                            return false;
                        }

                        var5 = this.getDescription();
                        var8 = var2.getDescription();
                        if (this.description != null) {
                            if (var2.description == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.description != null) {
                            return false;
                        }

                        var5 = this.getApprovedBy();
                        var8 = var2.getApprovedBy();
                        if (this.approvedBy != null) {
                            if (var2.approvedBy == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.approvedBy != null) {
                            return false;
                        }

                        var5 = this.getCreatedDate();
                        var8 = var2.getCreatedDate();
                        if (this.createdDate != null) {
                            if (var2.createdDate == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.createdDate != null) {
                            return false;
                        }

                        var5 = this.getModifiedDate();
                        var8 = var2.getModifiedDate();
                        if (this.modifiedDate != null) {
                            if (var2.modifiedDate == null) {
                                return false;
                            }

                            if (!var5.equals(var8)) {
                                return false;
                            }
                        } else if (var2.modifiedDate != null) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        } else {
            return false;
        }
    }

    @Override
	public int hashCode() {
        byte var1 = 1;
        int var3 = var1 * 31;
        int var2 = this.getRequestId();
        var3 += var2;
        var3 *= 31;
        var2 = this.getTrackingId();
        var3 += var2;
        var3 *= 31;
        String var4 = this.getWorkflowId();
        if (this.workflowId != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        RegistrationRequestType.Request var5 = this.getRequest();
        if (this.request != null) {
            var3 += var5.hashCode();
        }

        var3 *= 31;
        RegistrationRequestType.Company var6 = this.getCompany();
        if (this.company != null) {
            var3 += var6.hashCode();
        }

        var3 *= 31;
        var4 = this.getMailboxId();
        if (this.mailboxId != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getTradingAddress();
        if (this.tradingAddress != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getUserId();
        if (this.userId != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getUserLogin();
        if (this.userLogin != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getDescription();
        if (this.description != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getApprovedBy();
        if (this.approvedBy != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getCreatedDate();
        if (this.createdDate != null) {
            var3 += var4.hashCode();
        }

        var3 *= 31;
        var4 = this.getModifiedDate();
        if (this.modifiedDate != null) {
            var3 += var4.hashCode();
        }

        return var3;
    }


    public static class Request {

        protected String status;
        protected String type;
        protected String content;

        public Request() {
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String var1) {
            this.status = var1;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String var1) {
            this.type = var1;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String var1) {
            this.content = var1;
        }

        @Override
		public boolean equals(Object var1) {
            if (var1 != null && this.getClass() == var1.getClass()) {
                if (this == var1) {
                    return true;
                } else {
                    RegistrationRequestType.Request var2 = (RegistrationRequestType.Request)var1;
                    String var3 = this.getStatus();
                    String var4 = var2.getStatus();
                    if (this.status != null) {
                        if (var2.status == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.status != null) {
                        return false;
                    }

                    var3 = this.getType();
                    var4 = var2.getType();
                    if (this.type != null) {
                        if (var2.type == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.type != null) {
                        return false;
                    }

                    var3 = this.getContent();
                    var4 = var2.getContent();
                    if (this.content != null) {
                        if (var2.content == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.content != null) {
                        return false;
                    }

                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
		public int hashCode() {
            byte var1 = 1;
            int var3 = var1 * 31;
            String var2 = this.getStatus();
            if (this.status != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getType();
            if (this.type != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getContent();
            if (this.content != null) {
                var3 += var2.hashCode();
            }

            return var3;
        }
    }

    public static class Company {
        protected String id;
        protected String name;
        protected String city;
        protected String state;
        protected String countryCode;

        public Company() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String var1) {
            this.id = var1;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String var1) {
            this.name = var1;
        }

        public String getCity() {
            return this.city;
        }

        public void setCity(String city) {
    		this.city = city;
    	}

        public String getState() {
            return this.state;
        }

        public void setState(String var1) {
            this.state = var1;
        }

        public String getCountryCode() {
            return this.countryCode;
        }

        public void setCountryCode(String countryCode) {
    		this.countryCode =countryCode;
    	}


        @Override
		public boolean equals(Object var1) {
            if (var1 != null && this.getClass() == var1.getClass()) {
                if (this == var1) {
                    return true;
                } else {
                    RegistrationRequestType.Company var2 = (RegistrationRequestType.Company)var1;
                    String var3 = this.getId();
                    String var4 = var2.getId();
                    if (this.id != null) {
                        if (var2.id == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.id != null) {
                        return false;
                    }

                    var3 = this.getName();
                    var4 = var2.getName();
                    if (this.name != null) {
                        if (var2.name == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.name != null) {
                        return false;
                    }

                    var3 = this.getCity();
                    var4 = var2.getCity();
                    if (this.city != null) {
                        if (var2.city == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.city != null) {
                        return false;
                    }

                    var3 = this.getState();
                    var4 = var2.getState();
                    if (this.state != null) {
                        if (var2.state == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.state != null) {
                        return false;
                    }

                    var3 = this.getCountryCode();
                    var4 = var2.getCountryCode();
                    if (this.countryCode != null) {
                        if (var2.countryCode == null) {
                            return false;
                        }

                        if (!var3.equals(var4)) {
                            return false;
                        }
                    } else if (var2.countryCode != null) {
                        return false;
                    }

                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
		public int hashCode() {
            byte var1 = 1;
            int var3 = var1 * 31;
            String var2 = this.getId();
            if (this.id != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getName();
            if (this.name != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getCity();
            if (this.city != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getState();
            if (this.state != null) {
                var3 += var2.hashCode();
            }

            var3 *= 31;
            var2 = this.getCountryCode();
            if (this.countryCode != null) {
                var3 += var2.hashCode();
            }

            return var3;
        }
    }
}
