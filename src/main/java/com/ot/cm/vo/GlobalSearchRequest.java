package com.ot.cm.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class GlobalSearchRequest {

    private String term;
    private String searchBy;
    private String sortField;
    private Integer startIndex;
    private Integer limit;
    private String facetData;
    private String queryConditions;
    private boolean wildCardSearch;
    private List<Map<String, List<String>>> appliedFilters;
}
