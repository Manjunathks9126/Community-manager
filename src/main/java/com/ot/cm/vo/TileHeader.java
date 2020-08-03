package com.ot.cm.vo;

public class TileHeader {

	private String iconImageSrc;
	private String settingsSrc;
	private String src;
	private String displayText;
	private boolean showCount;
	private boolean isSearchEnabled;

	public String getIconImageSrc() {
		return iconImageSrc;
	}

	public void setIconImageSrc(String iconImageSrc) {
		this.iconImageSrc = iconImageSrc;
	}

	public String getSettingsSrc() {
		return settingsSrc;
	}

	public void setSettingsSrc(String settingsSrc) {
		this.settingsSrc = settingsSrc;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public boolean isSearchEnabled() {
		return isSearchEnabled;
	}

	public void setSearchEnabled(boolean isSearchEnabled) {
		this.isSearchEnabled = isSearchEnabled;
	}

	public boolean isShowCount() {
		return showCount;
	}

	public void setShowCount(boolean showCount) {
		this.showCount = showCount;
	}

}
