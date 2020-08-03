package com.ot.cm.helper;

import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitDetailsType;
import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.exception.TGOCPRestException;
import com.ot.cm.rest.client.BusinessUntilRestClient;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.vo.TileLink;
import com.ot.cm.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TileContentHelper {

    @Autowired
    NeoRestClient neoRestClient;

    @Autowired
    private BusinessUntilRestClient companyService;

    public TileLink[] getTileContent(UserInfo tgocpSMUserVO) throws TGOCPBaseException, ExecutionException, InterruptedException {
        CompletableFuture<TileLink[]> contentList = neoRestClient.getTileContent(tgocpSMUserVO);
        CompletableFuture<BusinessUnitDetailsType> company = this.companyService.getBusinessUnitDetails(tgocpSMUserVO.getCompanyId());
        CompletableFuture.allOf(contentList,company).join();
        TileLink[] content = contentList.get();
        BusinessUnitDetailsType bu = company.get();
        // Removing GPD id Consent not given
        if (null != bu && "No".equalsIgnoreCase(bu.getCustomerBUGPDVisibility())) {
            content = Arrays.stream(content).filter(tileLink -> !tileLink.getSrc().contains("globalDirectory")).toArray(item -> new TileLink[item]);
        }

        return content;
    }
}
