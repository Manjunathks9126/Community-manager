package com.ot.cm.business.service;

import java.util.Map;

public interface IMDataService {

    Map<String, Object> getDocumentMetadata(String buId,String filterCategoryId);
    Map<String, Object> downloadFile(String extendedId);
}
