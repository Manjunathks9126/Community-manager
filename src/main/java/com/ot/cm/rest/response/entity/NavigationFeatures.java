package com.ot.cm.rest.response.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ot.cm.vo.Tile;

@JsonInclude(Include.NON_NULL)
public class NavigationFeatures {
	private List<Tile> tiles;
	private List<Tile> appLauncherApps;
	private List<HamburgerMenu> hamburgerMenu;
	private List<String> remarks;

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public List<Tile> getAppLauncherApps() {
		return appLauncherApps;
	}

	public void setAppLauncherApps(List<Tile> appLauncherApps) {
		this.appLauncherApps = appLauncherApps;
	}

	public List<HamburgerMenu> getHamburgerMenu() {
		return hamburgerMenu;
	}

	public void setHamburgerMenu(List<HamburgerMenu> hamburgerMenu) {
		this.hamburgerMenu = hamburgerMenu;
	}

	public List<String> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NavigationFeatures [tiles=");
		builder.append(tiles);
		builder.append(", appLauncherApps=");
		builder.append(appLauncherApps);
		builder.append(", hamburgerMenu=");
		builder.append(hamburgerMenu);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append("]");
		return builder.toString();
	}

}
