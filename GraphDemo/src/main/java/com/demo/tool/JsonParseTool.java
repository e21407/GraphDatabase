package com.demo.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonParseTool {
	public static Map<String, Object> parsePathJson(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		JSONArray vJsonArray = jsonObject.getJSONArray("vertexList");
		JSONArray datas = new JSONArray();
		JSONArray links = new JSONArray();
		if (null != vJsonArray) {
			for (Object object : vJsonArray) {
				JSONObject node = (JSONObject) object;
				String id = (String) node.get("id");
				String label = (String) node.getJSONArray("labelList").get(0);
				if ("customer".equals(label)) {
					int numOfProperty = 1;
					JSONArray propertyList = node.getJSONArray("propertyList");
					for (Object object2 : propertyList) {
						JSONObject property = (JSONObject) object2;
						String propertyName = (String) property.get("name");
						String propertyValue = (String) property.get("value").toString();
						// 创建原节点
						if ("customerNumber".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "客户");
							oriNode.put("symbol", "static/img/icon12@3x.png"); // 设置客户节点icon
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						} else if ("company".equals(propertyName) || "deviceNumber".equals(propertyName)
								|| "email".equals(propertyName)) {
							// 创建属性节点
							JSONObject propertyNode = new JSONObject();
							propertyNode.put("type", "property");
							String proID = "property" + id + "_" + numOfProperty++;
							propertyNode.put("id", proID);
							if ("company".equals(propertyName)) {
								propertyNode.put("descript", "公司");
								propertyNode.put("symbol", "static/img/icon12@3x.png");
							} else if ("deviceNumber".equals(propertyName)) {
								propertyNode.put("descript", "设备号");
								propertyNode.put("symbol", "static/img/icon11@3x.png");
							} else if ("email".equals(propertyName)) {
								propertyNode.put("descript", "邮箱");
								propertyNode.put("symbol", "static/img/icon5@3x.png");
							}
							propertyNode.put("name", propertyValue);
							propertyNode.put("width", 32);
							propertyNode.put("height", 32);
							propertyNode.put("showLabelText", false);
							datas.add(propertyNode);
							// 属性节点到原节点的边
							JSONObject proEdge = new JSONObject();
							proEdge.put("target", proID);
							proEdge.put("source", id);
							proEdge.put("relation", "从属");
							links.add(proEdge);
						}

					}
				} else if ("card".equals(label)) {
					int numOfProperty = 1;
					JSONArray propertyList = node.getJSONArray("propertyList");
					for (Object object2 : propertyList) {
						JSONObject property = (JSONObject) object2;
						String propertyName = (String) property.get("name");
						String propertyValue = (String) property.get("value").toString();
						// 创建原节点
						if ("cardNumber".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "卡");
							oriNode.put("symbol", "static/img/icon11@3x.png"); //
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						} else if ("merchantNumber".equals(propertyName) || "paymentCardNumber".equals(propertyName)) {
							// 创建属性节点
							JSONObject propertyNode = new JSONObject();
							propertyNode.put("type", "property");
							String proID = "property" + id + "_" + numOfProperty++;
							propertyNode.put("id", proID);
							if ("merchantNumber".equals(propertyName)) {
								propertyNode.put("descript", "商户号");
								propertyNode.put("symbol", "static/img/icon5@3x.png");
							} else if ("paymentCardNumber".equals(propertyName)) {
								propertyNode.put("descript", "还款卡号");
								propertyNode.put("symbol", "static/img/icon11@3x.png");
							}
							propertyNode.put("name", propertyValue);
							propertyNode.put("width", 32);
							propertyNode.put("height", 32);
							propertyNode.put("showLabelText", false);
							datas.add(propertyNode);
							// 属性节点到原节点的边
							JSONObject proEdge = new JSONObject();
							proEdge.put("target", proID);
							proEdge.put("source", id);
							proEdge.put("relation", "从属");
							links.add(proEdge);
						}

					}
				} else if ("applicationForm".equals(label)) {
					int numOfProperty = 1;
					JSONArray propertyList = node.getJSONArray("propertyList");
					for (Object object2 : propertyList) {
						JSONObject property = (JSONObject) object2;
						String propertyName = (String) property.get("name");
						String propertyValue = (String) property.get("value").toString();
						// 创建原节点
						if ("applicationFormNumber".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "申请书编号");
							oriNode.put("symbol", "static/img/icon8@3x.png"); //
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						} else if ("company".equals(propertyName) || "agent".equals(propertyName)) {
							// 创建属性节点
							JSONObject propertyNode = new JSONObject();
							propertyNode.put("type", "property");
							String proID = "property" + id + "_" + numOfProperty++;
							propertyNode.put("id", proID);
							if ("company".equals(propertyName)) {
								propertyNode.put("descript", "公司");
								propertyNode.put("symbol", "static/img/icon12@3x.png");
							} else if ("agent".equals(propertyName)) {
								propertyNode.put("descript", "推广员");
								propertyNode.put("symbol", "static/img/icon11@3x.png");
							}
							propertyNode.put("name", propertyValue);
							propertyNode.put("width", 32);
							propertyNode.put("height", 32);
							propertyNode.put("showLabelText", false);
							datas.add(propertyNode);
							// 属性节点到原节点的边
							JSONObject proEdge = new JSONObject();
							proEdge.put("target", proID);
							proEdge.put("source", id);
							proEdge.put("relation", "从属");
							links.add(proEdge);
						}

					}
				} else if ("account".equals(label) || "telephone".equals(label) || "IP".equals(label)) {
					JSONArray propertyList = node.getJSONArray("propertyList");
					for (Object object2 : propertyList) {
						JSONObject property = (JSONObject) object2;
						String propertyName = (String) property.get("name");
						String propertyValue = (String) property.get("value").toString();
						// 创建原节点
						if ("accountNumber".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "账户号");
							oriNode.put("symbol", "static/img/icon4@3x.png"); //
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						} else if ("telepheNumber".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "电话号码");
							oriNode.put("symbol", "static/img/icon10@3x.png"); //
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						} else if ("IP".equals(propertyName)) {
							JSONObject oriNode = new JSONObject();
							oriNode.put("type", "node");
							oriNode.put("id", id);
							oriNode.put("descript", "IP");
							oriNode.put("symbol", "static/img/icon6@3x.png"); //
							oriNode.put("name", propertyValue);
							oriNode.put("width", 52);
							oriNode.put("height", 52);
							oriNode.put("showLabelText", true);
							datas.add(oriNode);
						}
					}
				}
			}
		}
		JSONArray eJsonArray = jsonObject.getJSONArray("edgeList");
		if (null != eJsonArray) {
			for (Object object : eJsonArray) {
				JSONObject edge = (JSONObject) object;
				String id = (String) edge.get("id");
				String target = (String) edge.get("end");
				String source = (String) edge.get("start");
				String label = (String) edge.get("label");
				JSONObject link = new JSONObject();
				link.put("id", id);
				link.put("target", target);
				link.put("source", source);
				link.put("label", label);
				JSONArray propertyList = edge.getJSONArray("propertyList");
				for (Object object2 : propertyList) {
					JSONObject property = (JSONObject) object2;
					String propertyName = (String) property.get("name");
					String propertyValue = (String) property.get("value").toString();
					if ("telephoneType".equals(propertyName) || "ipType".equals(propertyName)) {
						link.put("relation", propertyValue);
					}
				}
				if (null == link.get("relation")) {
					link.put("relation", "对应");
				}
				links.add(link);
			}
		}
		Map<String, Object> resutlMap = new HashMap<String, Object>();
		resutlMap.put("datas", datas);
		resutlMap.put("links", links);
		return resutlMap;
	}

	@Test
	public void testNull() {
		List<String> listStr = null;
		for (String string : listStr) {
			System.out.println(string);
		}
	}
}
