package com.ot.cm.helper;

import com.ot.cm.vo.GlobalSearchRequest;
import com.ot.config.properties.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.ot.cm.constants.ApplicationConstants.ADDRESS;
import static com.ot.cm.constants.ApplicationConstants.SOLR_SPECTIAL_CHAR_LIST;


@Component
public class GlobalSearchHelper {

    @Autowired
    private GlobalProperties globalProperties;

    public URI formSolrSimpleQueryURI(GlobalSearchRequest gsr) {
        String SEARCH_BY_FIELD = "BU".equalsIgnoreCase(gsr.getSearchBy()) ? "BU_NAME" : "ADDRESS";
        String rowsLimit = String.valueOf(gsr.getLimit() < 30 ? 30 : gsr.getLimit());
        int startIndex = gsr.getStartIndex();
        StringBuilder queryString = formQueryString(gsr, SEARCH_BY_FIELD);
        StringBuilder metaInfo = queryString.append("&rows=")
                .append(rowsLimit)
                .append("&start=")
                .append(startIndex)
                .append(createFilterUrl(gsr))
                .append("&sort=")
                .append(gsr.getSortField())
                .append("&" + gsr.getQueryConditions())
                .append("&fq=CUSTOMER_BU_GPD_VISIBILITY%3AY");


        return getUri(metaInfo);
    }

    public URI formFacadeSearchQueryURI(GlobalSearchRequest gsr) {
        String contextURL = globalProperties.getSolrURL() + "/ra_im/select?q=BPR_ID:" + gsr.getTerm();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(contextURL);
        UriComponents components = builder.build(true);
        return components.toUri();
    }

    public URI formSolrFacetQueryURI(GlobalSearchRequest gsr, String FACET_FIELD) {
        String rawTerm = gsr.getTerm();
        int startIndex = gsr.getStartIndex();
        String SEARCH_BY_FIELD = "BU".equalsIgnoreCase(gsr.getSearchBy()) ? "BU_NAME" : "ADDRESS";
        String rowsLimit = String.valueOf(gsr.getLimit() < 30 ? 30 : gsr.getLimit());
        StringBuilder escCharTerm = formQueryString(gsr, SEARCH_BY_FIELD);
        StringBuilder metaInfo = new StringBuilder("facet.field=" + this.encodeAndEscapeTerm(FACET_FIELD) + "&")
                .append(escCharTerm)
                .append("&facet=on&facet.limit=")
                .append(rowsLimit)
                .append("&facet.offset=")
                .append(startIndex)
                .append("&sort=")
                .append(gsr.getSortField())
                .append("&start=")
                .append(startIndex)
                .append(createFilterUrl(gsr))
                .append("&" + gsr.getQueryConditions())
                .append("&fq=CUSTOMER_BU_GPD_VISIBILITY%3AY");
        if (FACET_FIELD.equalsIgnoreCase(ADDRESS)) {
            metaInfo.append("&json.facet=%7B%7D&facet.mincount=1&facet.contains=")
                    .append(rawTerm.replace(" ", "%20"));
        }
        return getUri(metaInfo);
    }

    private URI getUri(StringBuilder solrQuery) {
        String contextURL = globalProperties.getSolrURL() + "/ra_im/select?";
        System.out.println("NEW URLS FOR SIMPLE QUERY :: " + contextURL + solrQuery);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(contextURL + solrQuery);
        UriComponents components = builder.build(true);
        return components.toUri();
    }

    private StringBuilder formQueryString(GlobalSearchRequest gsr, String SEARCH_FIELD) {
        StringBuilder prefix = new StringBuilder();
        String spaceEncodedTerm = this.encodeAndEscapeTerm(gsr.getTerm());
        if (gsr.isWildCardSearch()) {
            prefix.append("q=" + SEARCH_FIELD + ":(*").append(spaceEncodedTerm).append("*)");
        } else {
            prefix.append("q=").append(SEARCH_FIELD).append(":").append(spaceEncodedTerm.length() == 0 ? "*":spaceEncodedTerm);
        }
        return prefix;
    }

    private String createFilterUrl(GlobalSearchRequest globalSearchRequest) {
        StringBuilder filterQuery = new StringBuilder();
        if (null != globalSearchRequest.getAppliedFilters() && globalSearchRequest.getAppliedFilters().size() > 0) {
            for (Map<String, List<String>> entry : globalSearchRequest.getAppliedFilters()) {
                for (String key : entry.keySet()) {
                    if (null != entry.get(key) && entry.get(key).size() > 0) {
                        filterQuery.append("&fq=").append(encodeAndEscapeTerm(key)).append(":(");
                        for (String param : entry.get(key)) {
                            filterQuery.append(encodeAndEscapeTerm(param) + "+OR+");
                        }
                        if (filterQuery.length() > 0) {
                            filterQuery = filterQuery.delete(filterQuery.length() - 4, filterQuery.length());
                            filterQuery.append(")");
                        }
                    }
                }
            }
        }
        return filterQuery.toString();
    }

    private String encodeAndEscapeTerm(String term) {
        try {
            term = URLEncoder.encode(term, "UTF-8");
            for (String spChar : SOLR_SPECTIAL_CHAR_LIST) {
                if (term.contains(spChar)) {
                    term = term.replace(spChar, "%5C" + spChar);
                }
            }
            term = term.replace("+", "%5C%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return term;
    }
}
