package com.ot.cm.vo;

public class TileContent {

	String displaySrc;
	String logoutUrl;

	public String getDisplaySrc() {
		return displaySrc;
	}

	public void setDisplaySrc(String displaySrc) {
		this.displaySrc = displaySrc;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	@Override
	public String toString() {
		return "TileContentVO [displaySrc=" + displaySrc + ", logoutUrl=" + logoutUrl + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displaySrc == null) ? 0 : displaySrc.hashCode());
		result = prime * result + ((logoutUrl == null) ? 0 : logoutUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileContent other = (TileContent) obj;
		if (displaySrc == null) {
			if (other.displaySrc != null)
				return false;
		} else if (!displaySrc.equals(other.displaySrc))
			return false;
		if (logoutUrl == null) {
			if (other.logoutUrl != null)
				return false;
		} else if (!logoutUrl.equals(other.logoutUrl))
			return false;
		return true;
	}

}
