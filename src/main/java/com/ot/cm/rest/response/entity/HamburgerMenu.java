package com.ot.cm.rest.response.entity;

import java.util.List;

import com.ot.cm.vo.TileContent;

public class HamburgerMenu {
	private String displayText;
	private String src;
	private String icon;
	private String target;
	private TileContent content;
	private List<HamburgerMenu> children;

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public TileContent getContent() {
		return content;
	}

	public void setContent(TileContent content) {
		this.content = content;
	}

	public List<HamburgerMenu> getChildren() {
		return children;
	}

	public void setChildren(List<HamburgerMenu> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((displayText == null) ? 0 : displayText.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((src == null) ? 0 : src.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
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
		HamburgerMenu other = (HamburgerMenu) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (displayText == null) {
			if (other.displayText != null)
				return false;
		} else if (!displayText.equals(other.displayText))
			return false;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (src == null) {
			if (other.src != null)
				return false;
		} else if (!src.equals(other.src))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HamburgerMenu [displayText=");
		builder.append(displayText);
		builder.append(", src=");
		builder.append(src);
		builder.append(", icon=");
		builder.append(icon);
		builder.append(", target=");
		builder.append(target);
		builder.append(", content=");
		builder.append(content);
		builder.append(", children=");
		builder.append(children);
		builder.append("]");
		return builder.toString();
	}

}
