package com.ot.cm.business.service;

import java.text.ParseException;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.request.entity.search.BPRFilterSearchQuery;
import com.ot.cm.vo.ExtendedAttribute;
import com.ot.cm.vo.GlobalSearchRequest;

public interface GlobalDirectoryService {


    String getSuggestions(GlobalSearchRequest globalSearchRequest,String suggestion) throws TGOCPRestException;
    
    String getJsonSchema(String buId, String type)throws TGOCPRestException;

    ExtendedAttribute[] getExtendedAttribute(String buId) throws TGOCPBaseException;

    String updateExtendedAttribute(ExtendedAttribute extendedAttribute, Integer categoryContentId) throws TGOCPBaseException;

    void runDeltaQuery();
}
