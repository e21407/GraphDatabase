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
import com.demo.tool.JsonParseTool;
import com.demo.tool.StringTool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class GraphController {

	private static final String DEFAULT_DATA_FILE = "src" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "simulationData.json";

	@Autowired
	private GraphService graphService;

	@RequestMapping(value = "/getGraph", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getGraph(@RequestBody(required = false) Map<String, Object> reqMap) {
		File JsonFile = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATA_FILE);
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		Map<String, Object> resultMap = JsonParseTool.parsePathJsonWithProperty(inJsonObject);
		return resultMap;
		// return graphService.getGraph(reqMap);
	}

	// 二次查询，根据一个点的id查询它附近的关系
	@RequestMapping(value = "/secondSearch"/* , method = { RequestMethod.POST } */)
	@ResponseBody
	public Map<String, Object> secondSearch(@RequestBody(required = false) Map<String, Object> reqMap) {
		System.out.println("==============================>secondSearch");
		System.out.println(reqMap);
		File JsonFile = new File("searcPathSimRes.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		Map<String, Object> resultMap = JsonParseTool.parsePathJsonWithoutProperty(inJsonObject);
		return resultMap;
		// return graphService.searchLines(reqMap);
	}

	// 关联查询，根据点的id产出它们的关系
	@RequestMapping(value = "/searchRelationship"/* , method = { RequestMethod.POST } */)
	@ResponseBody
	public Map<String, Object> searchRelationship(@RequestBody(required = false) Map<String, Object> reqMap) {
		System.out.println("=============================>searchRelationship");
		JSONObject params = (JSONObject) JSONObject.toJSON(reqMap);
		System.out.println(params.getJSONArray("params"));
		File JsonFile = new File("searcPathSimRes.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		Map<String, Object> resultMap = JsonParseTool.parsePathJsonWithoutProperty(inJsonObject);
		return resultMap;
		// return graphService.searchLines(reqMap);
	}

	/**
	 * 路径查询，必要参数：节点id列表vertexIdList，路径类型option=（all，shortest， circle， weighted）
	 * 
	 * @param reqMap
	 * @return
	 */
	@RequestMapping(value = "/searchPath")
	@ResponseBody
	public Map<String, Object> searchPath(@RequestBody(required = false) Map<String, Object> reqMap) {
		String option = (String) reqMap.get("option");
		List<String> vertexIds = (List<String>) reqMap.get("vertexIds");
		System.out.println(option);
		System.out.println(vertexIds);
		File JsonFile = new File("searcPathSimRes.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		Map<String, Object> resultMap = JsonParseTool.parsePathJsonWithoutProperty(inJsonObject);
		return resultMap;
		// return graphService.searchPath(reqMap);
	}

}