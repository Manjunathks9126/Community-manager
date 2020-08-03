package com.ot.cm.rest.response.entity;

import java.util.List;

public class SearchQueryResponse<T> {

	private List<T> itemList;

	private Integer itemCount;

	public List<T> getItemList() {
		return itemList;
	}

	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

}
