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

	@Autowired
	private GraphService graphService;

	@RequestMapping(StringTool.projectPrefix + "/getAllGraph")
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

	@RequestMapping(value = StringTool.projectPrefix + "/getGraph", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> getGraph(@RequestBody(required = false) Map<String, Object> reqMap) {
		File JsonFile = new File("simulationData.json");
		String strFromFile = FileTool.getStrFromFile(JsonFile);
		JSONObject inJsonObject = JSON.parseObject(strFromFile);
		Map<String, Object> resultMap = JsonParseTool.parsePathJson(inJsonObject);
		return resultMap;
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
	@RequestMapping(value = StringTool.projectPrefix + "/searchPath", method = { RequestMethod.POST })
	@ResponseBody
	public String searchPath(@RequestBody Map<String, Object> reqMap) {
		// return graphService.searchPath(reqMap);
		return "searchPath: " + reqMap.get("vertexIds") + ", option: " + reqMap.get("option");
	}

}