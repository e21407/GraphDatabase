package com.demo.entity;

import java.util.Map;

public class EdgeObj {
	
	private String target;
	
	private String source;
	
	private Integer weight;
	
	private Integer value;
	
	private String relation;
	
	private Map<String, String> linkSetting;

//	public EdgeObj(String target, String source, Integer weight, Integer value, String relation,
//			Map<String, String> linkSetting) {
//		super();
//		this.target = target;
//		this.source = source;
//		this.weight = weight;
//		this.value = value;
//		this.relation = relation;
//		this.linkSetting = linkSetting;
//	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Map<String, String> getLinkSetting() {
		return linkSetting;
	}

	public void setLinkSetting(Map<String, String> linkSetting) {
		this.linkSetting = linkSetting;
	}
	
	

}
