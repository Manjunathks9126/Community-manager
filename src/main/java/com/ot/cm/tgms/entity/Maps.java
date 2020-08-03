package com.ot.cm.tgms.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "maps")
@XmlAccessorType(XmlAccessType.FIELD)
public class Maps {

	@XmlElement
	private String tableEntries;

	@XmlElement
	private String docType;

	@XmlElement
	private String direction;

	@XmlElement
	private String documentStandard;

	@XmlElement
	private String docVersion;

	@XmlElement
	private String mapName;

	@XmlElement
	private String acr;
	
	@XmlElement
	private String edi_dc40_test;
	
	@XmlElement
	private String edi_dc40_prod;

	public String getEdi_dc40_test() {
		return edi_dc40_test;
	}

	public void setEdi_dc40_test(String edi_dc40_test) {
		this.edi_dc40_test = edi_dc40_test;
	}

	public String getEdi_dc40_prod() {
		return edi_dc40_prod;
	}

	public void setEdi_dc40_prod(String edi_dc40_prod) {
		this.edi_dc40_prod = edi_dc40_prod;
	}

	@XmlElement
	private String usedBy;

	public String getTableEntries() {
		return tableEntries;
	}

	public void setTableEntries(String tableEntries) {
		this.tableEntries = tableEntries;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDocumentStandard() {
		return documentStandard;
	}

	public void setDocumentStandard(String documentStandard) {
		this.documentStandard = documentStandard;
	}

	public String getDocVersion() {
		return docVersion;
	}

	public void setDocVersion(String docVersion) {
		this.docVersion = docVersion;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getAcr() {
		return acr;
	}

	public void setAcr(String acr) {
		this.acr = acr;
	}

	public String getUsedBy() {
		return usedBy;
	}

	public void setUsedBy(String usedBy) {
		this.usedBy = usedBy;
	}

	@Override
	public String toString() {
		return "ClassPojo [tableEntries = " + tableEntries + ", docType = " + docType + ", direction = " + direction
				+ ", documentStandard = " + documentStandard + ", docVersion = " + docVersion + ", mapName = " + mapName
				+ ", acr = " + acr + ", usedBy = " + usedBy + "]";
	}
}
