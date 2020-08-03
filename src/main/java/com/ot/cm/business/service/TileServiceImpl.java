package com.ot.cm.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ot.cm.exception.TGOCPBaseException;
import com.ot.cm.rest.client.NeoRestClient;
import com.ot.cm.rest.response.entity.NavigationFeatures;
import com.ot.cm.vo.Tile;
import com.ot.cm.vo.TileContent;
import com.ot.cm.vo.TileHeader;
import com.ot.cm.vo.UserInfo;

@Service
public class TileServiceImpl {

	@Autowired
	NeoRestClient neoRest;

	String[][] tile_Const = { { "Trading Partners", "false", "true", "1210718", "tpdir" },
			{ "TP Distribution", "false", "false", "0", null },
			{ "Onboarding Statistics", "false", "false", "0", null } };

	public List<Tile> listCommunityPageTiles() {

		List<Tile> tiles = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Tile tile = new Tile();
			tile.setPosition(i);

			TileHeader header = new TileHeader();
			header.setDisplayText(tile_Const[i][0]);
			header.setSearchEnabled(Boolean.valueOf(tile_Const[i][1]));
			header.setShowCount(Boolean.valueOf(tile_Const[i][2]));
			header.setSrc(tile_Const[i][4]);
			TileContent content = new TileContent();
			content.setDisplaySrc(tile_Const[i][3]);

			tile.setContent(content);
			tile.setHeader(header);
			tiles.add(tile);
		}
		return tiles;

	}

	public NavigationFeatures getHeaderFeatures(UserInfo userInfo) throws TGOCPBaseException {
		return neoRest.getHeaderFeatures(userInfo);
	}
}
