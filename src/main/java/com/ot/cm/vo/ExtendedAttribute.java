package com.ot.cm.vo;

        import java.util.List;

        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "catagoryContentId",
        "category",
        "typeName",
        "typeId",
        "buId",
        "jsonContent",
        "filterExtendedList",
        "creationDate",
        "lastModifiedDate",
        "content"
})
public class ExtendedAttribute {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String companyName;
    @JsonProperty("catagoryContentId")
    private Integer catagoryContentId;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("typeName")
    private String typeName;
    @JsonProperty("typeId")
    private Integer typeId;
    @JsonProperty("buId")
    private String buId;
    @JsonProperty("jsonContent")
    private String jsonContent;
    @JsonProperty("filterExtendedList")
    private List<FilterExtendedList> filterExtendedList = null;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;
    @JsonProperty("content")
    private String content;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("catagoryContentId")
    public Integer getCatagoryContentId() {
        return catagoryContentId;
    }

    @JsonProperty("catagoryContentId")
    public void setCatagoryContentId(Integer catagoryContentId) {
        this.catagoryContentId = catagoryContentId;
    }

    @JsonProperty("category")
    public Category getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(Category category) {
        this.category = category;
    }

    @JsonProperty("typeName")
    public String getTypeName() {
        return typeName;
    }

    @JsonProperty("typeName")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @JsonProperty("typeId")
    public Integer getTypeId() {
        return typeId;
    }

    @JsonProperty("typeId")
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @JsonProperty("buId")
    public String getBuId() {
        return buId;
    }

    @JsonProperty("buId")
    public void setBuId(String buId) {
        this.buId = buId;
    }

    @JsonProperty("jsonContent")
    public String getJsonContent() {
        return jsonContent;
    }

    @JsonProperty("jsonContent")
    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    @JsonProperty("filterExtendedList")
    public List<FilterExtendedList> getFilterExtendedList() {
        return filterExtendedList;
    }

    @JsonProperty("filterExtendedList")
    public void setFilterExtendedList(List<FilterExtendedList> filterExtendedList) {
        this.filterExtendedList = filterExtendedList;
    }

    @JsonProperty("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("lastModifiedDate")
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonProperty("lastModifiedDate")
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "categoryId",
            "name"
    })
    public class Category {

        @JsonProperty("categoryId")
        private Integer categoryId;
        @JsonProperty("name")
        private String name;

        @JsonProperty("categoryId")
        public Integer getCategoryId() {
            return categoryId;
        }

        @JsonProperty("categoryId")
        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "uniqueId",
            "catagoryContentId",
            "fileType",
            "fileName",
            "fileContent",
            "creationDate",
            "lastModifiedDate"
    })
    public class FilterExtendedList {

        @JsonProperty("uniqueId")
        private Integer uniqueId;
        @JsonProperty("catagoryContentId")
        private Integer catagoryContentId;
        @JsonProperty("fileType")
        private String fileType;
        @JsonProperty("fileName")
        private String fileName;
        @JsonProperty("fileContent")
        private List<String> fileContent = null;
        @JsonProperty("creationDate")
        private String creationDate;
        @JsonProperty("lastModifiedDate")
        private String lastModifiedDate;

        @JsonProperty("uniqueId")
        public Integer getUniqueId() {
            return uniqueId;
        }

        @JsonProperty("uniqueId")
        public void setUniqueId(Integer uniqueId) {
            this.uniqueId = uniqueId;
        }

        @JsonProperty("catagoryContentId")
        public Integer getCatagoryContentId() {
            return catagoryContentId;
        }

        @JsonProperty("catagoryContentId")
        public void setCatagoryContentId(Integer catagoryContentId) {
            this.catagoryContentId = catagoryContentId;
        }

        @JsonProperty("fileType")
        public String getFileType() {
            return fileType;
        }

        @JsonProperty("fileType")
        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        @JsonProperty("fileName")
        public String getFileName() {
            return fileName;
        }

        @JsonProperty("fileName")
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        @JsonProperty("fileContent")
        public List<String> getFileContent() {
            return fileContent;
        }

        @JsonProperty("fileContent")
        public void setFileContent(List<String> fileContent) {
            this.fileContent = fileContent;
        }

        @JsonProperty("creationDate")
        public String getCreationDate() {
            return creationDate;
        }

        @JsonProperty("creationDate")
        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        @JsonProperty("lastModifiedDate")
        public String getLastModifiedDate() {
            return lastModifiedDate;
        }

        @JsonProperty("lastModifiedDate")
        public void setLastModifiedDate(String lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }
    }
}