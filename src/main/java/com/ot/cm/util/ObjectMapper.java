package com.ot.cm.util;

import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.gxs.services.bsapi.rs.v2.entity.BusinessPartnerRelationshipType;
import com.gxs.services.bsapi.rs.v3.entity.AddressType;
import com.gxs.services.bsapi.rs.v3.entity.BillingSplitsType;
import com.gxs.services.bsapi.rs.v3.entity.BuAdditionalCodeType;
import com.gxs.services.bsapi.rs.v3.entity.BusinessUnitType;
import com.gxs.services.bsapi.rs.v3.entity.CommunityIdentifierType;
import com.gxs.services.bsapi.rs.v3.entity.CountryCodeType;
import com.gxs.services.bsapi.rs.v3.entity.Status;
import com.gxs.services.bsapi.rs.v3.entity.TpRoleType;
import com.gxs.services.bsapi.rs.v3.entity.TradingPartnerRelationshipType;
import com.ot.cm.constants.ApplicationConstants;
import com.ot.cm.tgms.entity.ExternalReferenceType;
import com.ot.cm.tgms.entity.Workflow;

public class ObjectMapper {

	public static BusinessPartnerRelationshipType convertBPRTypeVO(String tpBUId) {
		BusinessPartnerRelationshipType businessPartnerRelationshipType = new BusinessPartnerRelationshipType();
		businessPartnerRelationshipType.setPartnerCompanyId(tpBUId);
		businessPartnerRelationshipType.setStatus(ApplicationConstants.BPR_STATUS_REQUESTED);
		return businessPartnerRelationshipType;
	}

	public static BusinessUnitType convertBUStatusVO() {
		BusinessUnitType businessUnitType = new BusinessUnitType();
		businessUnitType.setStatus("ACTIVE");
		return businessUnitType;
	}

	public static TradingPartnerRelationshipType convertTPRTypeVO(Workflow entity, String initiator) {
		TradingPartnerRelationshipType tradingPartnerRelationshipType = new TradingPartnerRelationshipType();

		BillingSplitsType billingInfo = new BillingSplitsType();
		billingInfo.setConnectOutPayee(TpRoleType.fromValue("SENDER"));
		billingInfo.setReceivePayee(TpRoleType.fromValue("RECEIVER"));
		billingInfo.setSendPayee(TpRoleType.fromValue("SENDER"));
		billingInfo.setStoragePayee(TpRoleType.fromValue("RECEIVER"));

		com.gxs.services.bsapi.rs.v3.entity.TradingAddressType ownerAddress = new com.gxs.services.bsapi.rs.v3.entity.TradingAddressType();
		if(null != entity.getProvisioningRequestData().getCustomer()){
			ownerAddress.setAddress(
					entity.getProvisioningRequestData().getCustomer().getTradingAddresses().getProd().getAddress());
			if (null != entity.getProvisioningRequestData().getCustomer().getTradingAddresses().getProd().getQualifier()
					&& !"".equals(entity.getProvisioningRequestData().getCustomer().getTradingAddresses().getProd()
							.getQualifier()))
				ownerAddress.setQualifier(
						entity.getProvisioningRequestData().getCustomer().getTradingAddresses().getProd().getQualifier());
		}else{
			String[] strArray = entity.getProvisioningRequestData().getRegistrationData().getProductionDetails().getYourProdAddress().split(":");
			if(strArray.length > 0){
				ownerAddress.setQualifier(strArray[0]);
				ownerAddress.setAddress(strArray[1]);
			}
			
		}

		com.gxs.services.bsapi.rs.v3.entity.TradingAddressType tpAddress = new com.gxs.services.bsapi.rs.v3.entity.TradingAddressType();
		if(null != entity.getProvisioningRequestData().getRegistrationData().getTradingAddress()){
			tpAddress.setAddress(
					entity.getProvisioningRequestData().getRegistrationData().getTradingAddress().getProdtp().getAddress());
			if (null != entity.getProvisioningRequestData().getRegistrationData().getTradingAddress().getProdtp()
					.getQualifier()
					&& !entity.getProvisioningRequestData().getRegistrationData().getTradingAddress().getProdtp()
					.getQualifier().isEmpty())
				tpAddress.setQualifier(entity.getProvisioningRequestData().getRegistrationData().getTradingAddress()
						.getProdtp().getQualifier());
		}else{
				tpAddress.setQualifier(entity.getProvisioningRequestData().getRegistrationData().getProductionDetails().getTpProdId().getQualifier());
				tpAddress.setAddress(entity.getProvisioningRequestData().getRegistrationData().getProductionDetails().getTpProdId().getAddress());
		}
		tradingPartnerRelationshipType.setStatus(Status.fromValue("ACTIVE"));
		tradingPartnerRelationshipType.setBillingInfo(billingInfo);
		tradingPartnerRelationshipType.setCompanyId(initiator);

		tradingPartnerRelationshipType.setOwnerTradingAddress(ownerAddress);
		tradingPartnerRelationshipType.setPartnerTradingAddress(tpAddress);
		tradingPartnerRelationshipType.setPartnershipDirection("BOTH");
		return tradingPartnerRelationshipType;
	}

	public static BusinessUnitType convertBUTypeVO(Workflow wfentity, Map cmap) {
		Charset UTF_8 = Charset.forName("UTF-8");
		BusinessUnitType businessUnitType = new com.gxs.services.bsapi.rs.v3.entity.BusinessUnitType();
		AddressType addressType = new com.gxs.services.bsapi.rs.v3.entity.AddressType();
		CommunityIdentifierType communityIdentifierType = new CommunityIdentifierType();

		businessUnitType.setCompanyName(
				wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyName());

		if (!StringUtils.isEmpty(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
				.getCompanyAddress().getAddressLine1())) {
			addressType.setAddressLine1(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
					.getCompanyAddress().getAddressLine1());
		}

		if (null != wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
				.getAddressLine2()
				&& !wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
						.getAddressLine2().isEmpty())
			addressType.setAddressLine2(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
					.getCompanyAddress().getAddressLine2());

		addressType.setCity(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
				.getCompanyAddress().getCity());
		String countryCode = wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
				.getCompanyAddress().getCountryCode();
		if (!CommonUtil.isEmpty(countryCode))
			addressType.setCountryCode(CountryCodeType.valueOf(countryCode));
		if (null != wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
				.getPostalCode()
				&& !wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
						.getPostalCode().isEmpty())
			addressType.setPostalCode(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
					.getCompanyAddress().getPostalCode());
		if (null != wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
				.getState()
				&& !wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
						.getState().isEmpty())
			addressType.setState(wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit()
					.getCompanyAddress().getState());

		businessUnitType.setStatus(ApplicationConstants.COMPANY_STATUS_INPROGRESS);

		if (null != cmap && !cmap.isEmpty()) {
			businessUnitType.setCompanyParticipationType(cmap.get("COMMUNITY_PARTICIPATION").toString());
			// communityIdentifierType.setCommunityName("Reseller
			// Community_TEST_HCRE413312122_GC73444947MO");
			communityIdentifierType.setCommunityName(cmap.get("COMMUNITY_NAME").toString());
			// communityIdentifierType.setCommunityId("GCM84771");
			businessUnitType.setCommunityIdentifier(communityIdentifierType);
		}

		if (null != wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getCompanyAddress()
				.getWebsite())
			businessUnitType.setCompanyWebsiteURL(wfentity.getProvisioningRequestData().getRegistrationData()
					.getBusinessUnit().getCompanyAddress().getWebsite());

		businessUnitType.setCompanyAddress(addressType);
		return businessUnitType;
	}

	public static BusinessUnitType convertKeybankTypeVO(Workflow wfentity, Map cmap, boolean saveExternalReference) {
		BusinessUnitType businessUnitType = ObjectMapper.convertBUTypeVO(wfentity, cmap);
		// adding external reference - only applicable while CPS Submit
		if (saveExternalReference) {
			ExternalReferenceType externalRefType = new ExternalReferenceType();
			externalRefType.setId(wfentity.getProvisioningRequestData().getRegistrationData().getTradingAddress()
					.getProdtp().getAddress());
			externalRefType.setType("KEYBANK_COMPANY_ID");
			wfentity.getProvisioningRequestData().getRegistrationData().getBusinessUnit().getExternalref()
					.add(externalRefType);
		} else {
			if (null != wfentity.getProcessingContext().getInvitationCode()) {
				BuAdditionalCodeType additionalcodeType = new BuAdditionalCodeType();
				additionalcodeType.setKey("INVITATION_CODE");
				additionalcodeType.getValue().add(wfentity.getProcessingContext().getInvitationCode());
				businessUnitType.getBuAdditionalCodes().add(additionalcodeType);
			}
		}
		return businessUnitType;
	}
}
