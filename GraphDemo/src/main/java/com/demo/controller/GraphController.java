package com.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.entity.EdgeObj;
import com.demo.entity.VertexObj;
import com.demo.service.GraphService;
import com.demo.tool.FileTool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GraphController {

	@Autowired
	private GraphService graphService;

	@RequestMapping("/getAllGraph")
	@ResponseBody
	public Map<String, Object> getAllGraph() {
		Map<String, Object> result = new HashMap<String, Object>();
		// 读取点数据
		File file = new File("datas_vertex.json");
		Long filelength = file.length();
		byte[] fileContent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String strFileContent = null;
		try {
			strFileContent = new String(fileContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<Object> vertexs = null;
		try {
			vertexs = mapper.readValue(strFileContent, new TypeReference<List<VertexObj>>() {
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 读取边数据
		file = new File("datas_edge.json");
		filelength = file.length();
		fileContent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			strFileContent = new String(fileContent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Object> edges = null;
		try {
			edges = mapper.readValue(strFileContent, new TypeReference<List<EdgeObj>>() {
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 封装返回数据
		result.put("datas", vertexs);
		result.put("links", edges);
		return result;

	}

	@RequestMapping(value = "/getGraph", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getGraph(@RequestBody(required = false) Map<String, Object> reqMap) {
		File JsonFile = new File("simulationData.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		JSONArray vJsonArray = inJsonObject.getJSONArray("vertexList");
		JSONArray datas = new JSONArray();
		JSONArray links = new JSONArray();
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
					}
					else if ("IP".equals(propertyName)) {
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
		JSONArray eJsonArray = inJsonObject.getJSONArray("edgeList");
		for (Object object : eJsonArray) {
			JSONObject edge = (JSONObject) object;
			String id  = (String) edge.get("id");
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
				if("telephoneType".equals(propertyName) || "ipType".equals(propertyName)) {
					link.put("relation", propertyValue);
				}
			}
			if(null == link.get("relation")) {
				link.put("relation", "对应");
			}
			links.add(link);
		}

		Map<String, Object> resutlMap = new HashMap<String, Object>();
		resutlMap.put("datas", datas);
		resutlMap.put("links", links);
		return resutlMap;
		// return graphService.getGraph(reqMap);
	}

	// 二次查询，根据一个点的id查询它附近的关系
	@RequestMapping(value = "/secondSearch", method = { RequestMethod.POST })
	@ResponseBody
	public String secondSearch(@RequestBody Map<String, Object> reqMap) {
		return graphService.searchLines(reqMap);
	}

	// 关联查询，根据点的id产出它们的关系
	@RequestMapping(value = "/searchRelationship", method = { RequestMethod.POST })
	@ResponseBody
	public String searchRelationshipByVertexsId(@RequestBody Map<String, Object> reqMap) {
		return graphService.searchLines(reqMap);
	}

	/**
	 * 路径查询，必要参数：节点id列表vertexIdList，路径类型option=（all，shortest， circle， weighted）
	 * 
	 * @param reqMap
	 * @return
	 */
	@RequestMapping(value = "/searchPath", method = { RequestMethod.POST })
	@ResponseBody
	public String searchPath(@RequestBody Map<String, Object> reqMap) {
		// return graphService.searchPath(reqMap);
		return "searchPath: " + reqMap.get("vertexIds") + ", option: " + reqMap.get("option");
	}

}