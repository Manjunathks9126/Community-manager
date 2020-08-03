package com.ot.cm.vo;


public class TileLink {

	private String displayText;

	private String src;

	private String target;
	
	public TileLink(){}

	public TileLink(String displayText, String src) {
		super();
		this.displayText = displayText;
		this.src = src;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
