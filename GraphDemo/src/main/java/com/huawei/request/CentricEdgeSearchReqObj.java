package com.huawei.request;

import java.util.List;

/**
 * 
 * @author Liubaichuan
 *
 */
public class CentricEdgeSearchReqObj {
	private List<Integer> vertexIdList;
	
	private PropertyFilter filterList;
	
	private String edgeLabel;
	
	private List<PropertyKeySort> propertyKeySortList;
	
	private int limit;

	public List<Integer> getVertexIdList() {
		return vertexIdList;
	}

	public void setVertexIdList(List<Integer> vertexIdList) {
		this.vertexIdList = vertexIdList;
	}

	public PropertyFilter getFilterList() {
		return filterList;
	}

	public void setFilterList(PropertyFilter filterList) {
		this.filterList = filterList;
	}

	public String getEdgeLabel() {
		return edgeLabel;
	}

	public void setEdgeLabel(String edgeLabel) {
		this.edgeLabel = edgeLabel;
	}

	public List<PropertyKeySort> getPropertyKeySortList() {
		return propertyKeySortList;
	}

	public void setPropertyKeySortList(List<PropertyKeySort> propertyKeySortList) {
		this.propertyKeySortList = propertyKeySortList;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	
}
