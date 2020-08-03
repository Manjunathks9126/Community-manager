package com.ot.cm.test.tpr;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxs.services.bsapi.rs.v2.entity.TradingPartnerRelationshipDetailsType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TPRDetailsTest {

	public static void main(String ar[]) {
		try {
			Client client = Client.create();
			// String url =
			// "http://qtatcfa1.qa.gxsonline.net:8078/CommunityManagementServices/v2/tradingpartnerrelationships?where_sender_company_id=GC00000125M1&where_partner_company_id=GC00012512M1";
			// String url =
			// "http://qtatcfa1.qa.gxsonline.net:8078/CommunityManagementServices/v2/tradingpartnerrelationships?where_sender_company_id=GC00000125M1&where_partner_company_id=GC00014897M1";
			// String url =
			// "http://qtatcfa1.qa.gxsonline.net:8078/CommunityManagementServices/v2/tradingpartnerrelationships?where_sender_company_id=GC33350425BU&where_partner_company_id=GC42964218PW";
			String url = "http://qtatcfa1.qa.gxsonline.net:8078/CommunityManagementServices/v2/tradingpartnerrelationships?where_sender_company_id=GC33350425BU&where_partner_company_id=GC57813351SQ";

			URI uri = UriBuilder.fromUri(url).build();
			WebResource resource = client.resource(uri);
			ClientResponse clientResponse = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);
			TradingPartnerRelationshipDetailsType[] attype = (TradingPartnerRelationshipDetailsType[]) getClientResponse(
					clientResponse, TradingPartnerRelationshipDetailsType[].class);
			// TradingPartnerDetailsServiceImpl serviceImpl = new
			// TradingPartnerDetailsServiceImpl();
			// serviceImpl.transformResponse(attype);
			// System.out.println(transformResponse(attype));
			// System.out.println(serviceImpl.transformResponse(attype));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Object getClientResponse(ClientResponse clientResponse, Class valueType) throws Exception {

		Object clientResponseObject = null;
		try {
			String responseStr = clientResponse.getEntity(String.class);
			clientResponseObject = new ObjectMapper().readValue(responseStr, valueType);
		} catch (Exception e) {
			System.out.println("Exception message ::: " + e.getMessage());
		}
		return clientResponseObject;
	}
}
