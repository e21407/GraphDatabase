package com.demo.entity;

import java.util.Map;

public class VertexObj {

	private String id;

	private String descript;

	private String symbol;

	private String name;

	private Integer category;

	private Integer value;

	private String nodeId;

	private String parentId;

	private Integer level;

	private Integer width;

	private Integer height;

	private Map<String, String> nodeSetting;
	
	private Boolean showLabelText;
	
	
	
//	public VertexObj() {}
//
//	public VertexObj(String id, String descript, String symbol, String name, Integer category, Integer value,
//			String nodeId, String parentId, Integer level, Integer width, Integer height, Boolean showLabelText) {
//		super();
//		this.id = id;
//		this.descript = descript;
//		this.symbol = symbol;
//		this.name = name;
//		this.category = category;
//		this.value = value;
//		this.nodeId = nodeId;
//		this.parentId = parentId;
//		this.level = level;
//		this.width = width;
//		this.height = height;
//		this.showLabelText = showLabelText;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Boolean getShowLabelText() {
		return showLabelText;
	}

	public void setShowLabelText(Boolean showLabelText) {
		this.showLabelText = showLabelText;
	}

	public Map<String, String> getNodeSetting() {
		return nodeSetting;
	}

	public void setNodeSetting(Map<String, String> nodeSetting) {
		this.nodeSetting = nodeSetting;
	}

}
