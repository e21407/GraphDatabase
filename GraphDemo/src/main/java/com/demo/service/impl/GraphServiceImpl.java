package com.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ArrayELResolver;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.dao.GraphApi;
import com.demo.entity.VertexObj;
import com.demo.service.GraphService;
import com.demo.tool.JsonParseTool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.request.CentricEdgeSearchReqObj;
import com.huawei.request.LineSearchReqObj;
import com.huawei.request.PathSearchReqObj;
import com.huawei.request.PropertyFilter;
import com.huawei.request.VertexElement;
import com.huawei.request.VertexFilter;
import com.huawei.request.VertexSearchReqObj;
import com.huawei.request.VertexSearchRspObj;
import com.huawei.util.PropertyPredicate;

@Service("graphService")
public class GraphServiceImpl implements GraphService {

	GraphApi api = GraphApi.getGraphApi_test();

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return api.getInt();
	}

	/**
	 * 获取初始化主页图谱时候数据，先查询几个客户节点，然后做扩线查询
	 */
	@Override
	public Map<String, Object> getGraph(Map<String, Object> reqMap) {
		// 查询全图节点
		VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
		vertexSearchReqObj.setVertexLabel("customer");
		vertexSearchReqObj.setLimit(5); // 查询5个客户节点
		VertexSearchRspObj vertexSearchRspObj = api.searchVertex(vertexSearchReqObj); // Vertex的全图条件查询
		// 获取节点id
		List<String> vertexIdList = new ArrayList<String>();
		List<VertexElement> vertexList = vertexSearchRspObj.getVertexList();
		for (VertexElement vertexElement : vertexList) {
			String id = vertexElement.getId();
			vertexIdList.add(id);
		}
		// 扩线查询
		LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
		lineSearchReqObj.setVertexIdList(vertexIdList); // 设置id列表，必选
		lineSearchReqObj.setLayer(4); // 设置扩线层数，必选
		lineSearchReqObj.setLimit(150);
		String searchLinesResult = api.searchLines(lineSearchReqObj);

		// 封装返回结果
		JSONObject resultJson = JSONObject.parseObject(searchLinesResult);
		Map<String, Object> resultMap = JsonParseTool.parsePathJsonWithProperty(resultJson);
		return resultMap;
	}

	@Override
	public Map<String, Object> searchRelationship(Map<String, Object> reqMap) {
		if (null == reqMap) {
			return null;
		}
		JSONObject params = (JSONObject) JSONObject.toJSON(reqMap);
		JSONArray JsonParams = params.getJSONArray("params");
		Map<String, String> propertyList1 = null;
		Map<String, String> propertyList2 = null;
		String label1 = null;
		String label2 = null;
		for (Object object : JsonParams) {
			JSONObject property = (JSONObject) object;
			String propertyName = property.get("name").toString();
			String propertyValue = property.getString("value");
			if (null == propertyValue || propertyValue.trim().equals("")) {
				continue;
			}
			propertyName = propertyName.trim();
			if (propertyName.endsWith("1")) {
				if (propertyName.startsWith("forType")) {
					label1 = propertyValue.trim();
				} else {
					propertyValue = propertyValue.trim();
					if (null == propertyList1) {
						propertyList1 = new HashMap<String, String>();
					}
					propertyList1.put(propertyName.split("_")[0], propertyValue);
				}
			} else if (propertyName.endsWith("2")) {
				if (propertyName.startsWith("forType")) {
					label2 = propertyValue.trim();
				} else {
					propertyValue = propertyValue.trim();
					if (null == propertyList2) {
						propertyList2 = new HashMap<String, String>();
					}
					propertyList2.put(propertyName.split("_")[0], propertyValue);
				}
			}
		}
		String vID1 = null;
		String vID2 = null;
		if (null != label1 && null != propertyList1) {
			vID1 = api.getVertexIdByProperty(label1, propertyList1);
		}
		if (null != label2 && null != propertyList2) {
			vID2 = api.getVertexIdByProperty(label2, propertyList2);
		}
		// 有两个点，选择全路径查询
		if (null != vID1 && null != vID2) {
			List<String> vertexIdList = new ArrayList<String>();
			vertexIdList.add(vID1);
			vertexIdList.add(vID2);
			PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
			pathSearchReqObj.setVertexIdList(vertexIdList);
			pathSearchReqObj.setOption("all");
			pathSearchReqObj.setLayer(6);
			String strSearchPathResult = api.searchPath(pathSearchReqObj);
			JSONObject jsonSearchPathResult = JSONObject.parseObject(strSearchPathResult);
			jsonSearchPathResult = jsonSearchPathResult.getJSONArray("pathSet").getJSONObject(0);
			return JsonParseTool.parsePathJsonWithProperty(jsonSearchPathResult);
		}
		// 单个点选择扩线查询
		if (null != vID1) {
			LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
			List<String> vertexIdList = new ArrayList<String>();
			vertexIdList.add(vID1);
			lineSearchReqObj.setVertexIdList(vertexIdList);
			lineSearchReqObj.setLayer(8);
			String strSearchLinesResult = api.searchLines(lineSearchReqObj);
			JSONObject jsonSearchLinesResult = JSONObject.parseObject(strSearchLinesResult);
			return JsonParseTool.parsePathJsonWithProperty(jsonSearchLinesResult);
		}
		if (null != vID2) {
			LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
			List<String> vertexIdList = new ArrayList<String>();
			vertexIdList.add(vID2);
			lineSearchReqObj.setVertexIdList(vertexIdList);
			lineSearchReqObj.setLayer(8);
			String strSearchLinesResult = api.searchLines(lineSearchReqObj);
			JSONObject jsonSearchLinesResult = JSONObject.parseObject(strSearchLinesResult);
			return JsonParseTool.parsePathJsonWithProperty(jsonSearchLinesResult);
		}
		return null;
	}

	@Override
	public Map<String, Object> searchPath(Map<String, Object> reqMap) {
//		String vertexIdLstStr = (String) reqMap.get("vertexIds");
		List<String> vertexIds = (List<String>) reqMap.get("vertexIds");
		String option = (String) reqMap.get("option");
//		ObjectMapper mapper = new ObjectMapper();
//		List<String> vertexIdList = null;
//		try {
//			vertexIdList = mapper.readValue(vertexIdLstStr, new TypeReference<List<String>>() {
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
		pathSearchReqObj.setVertexIdList(vertexIds);
		if ("shortest".equals(option.trim().toLowerCase())) {
			pathSearchReqObj.setOption("shortest"); // 最短路径
		} else if ("circle".equals(option.trim().toLowerCase())) {
			pathSearchReqObj.setOption("circle"); // 查询回环路径
		} else if ("weighted".equals(option.trim().toLowerCase())) {
			pathSearchReqObj.setOption("weighted"); // 查询有权最短路径
			String weightedPropertyName = (String) reqMap.get("weightedPropertyName");
			pathSearchReqObj.setWeightedPropertyName(weightedPropertyName);
		} else {
			pathSearchReqObj.setOption("all"); // 默认查询全路径
		}
		pathSearchReqObj.setLayer(10); // 设置跳数
		String searchPathRsultStr = api.searchPath(pathSearchReqObj);
		JSONObject json = JSONObject.parseObject(searchPathRsultStr);
		json = json.getJSONArray("pathSet").getJSONObject(0);
		Map<String, Object> parsePathJson = JsonParseTool.parsePathJsonWithoutProperty(json);
		return parsePathJson;
	}

	/**
	 * 二次查询方法，未经测试！！！！！！！！
	 */
	public Map<String, Object> secondSearch(Map<String, Object> reqMap) {
		if (null == reqMap) {
			return null;
		}
		JSONObject jsonParams = (JSONObject) JSONObject.toJSON(reqMap);
		JSONArray params = jsonParams.getJSONArray("params");
		JSONArray chooseNodesId = jsonParams.getJSONArray("chooseNodesId");
		String vertexLabel = null;
		String choice = "";
		Map<String, String> propertyList = new HashMap<String, String>();

		for (Object object : params) {
			JSONObject property = (JSONObject) object;
			String propertyName = property.get("name").toString();
			String propertyValue = property.getString("value");
			if ("forType3".equals(propertyName)) {
				vertexLabel = propertyValue;
				continue;
			}
			if ("choice".equals(propertyName)) {
				choice = propertyValue;
				continue;
			}
			if (null != propertyValue && !propertyValue.trim().equals("")) {
				propertyList.put(propertyName.split("_")[0], propertyValue);
			}
		}
		//////////////////////////////////////////////////////////////
		// 多次查询列表
		// List<LineSearchReqObj> lineSearchReqObjList = new
		// ArrayList<LineSearchReqObj>();

		List<String> vertexIdList = new ArrayList<String>();
		vertexIdList.add(chooseNodesId.getString(0));
		LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
		lineSearchReqObj.setVertexIdList(vertexIdList);
		List<VertexFilter> vertexFilterList = new ArrayList<VertexFilter>();
		VertexFilter vertexFilter = new VertexFilter();
		ArrayList<String> labelList = new ArrayList<String>();
		labelList.add(vertexLabel);
		vertexFilter.setVertexLabelList(labelList);
		vertexFilter.setLimit(50);
		List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
		Set<Entry<String, String>> entrySet = propertyList.entrySet();
		for (Entry<String, String> entry : entrySet) {
			PropertyFilter filter = new PropertyFilter();
			filter.setPropertyName(entry.getKey());
			List<String> values = new ArrayList<String>();
			values.add(entry.getValue());
			filter.setValues(values);
			filter.setPredicate(PropertyPredicate.EQUAL);
			propertyFilterList.add(filter);
			vertexFilter.setFilterList(propertyFilterList);
		}
		vertexFilterList.add(vertexFilter);
		lineSearchReqObj.setVertexFilterList(vertexFilterList);
		lineSearchReqObj.setLayer(4);
		lineSearchReqObj.setLayer(150);
		// lineSearchReqObjList.add(lineSearchReqObj);

		// if ("02".equals(choice)) {
		// // 为每个属性创建一个查询对象
		// Set<Entry<String, String>> entrySet = propertyList.entrySet();
		// for (Entry<String, String> entry : entrySet) {
		// LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
		// lineSearchReqObj.setVertexIdList(vertexIdList);
		// List<VertexFilter> vertexFilterList = new ArrayList<VertexFilter>();
		// VertexFilter vertexFilter = new VertexFilter();
		// ArrayList<String> labelList = new ArrayList<String>();
		// labelList.add(vertexLabel);
		// vertexFilter.setVertexLabelList(labelList);
		// List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
		//
		// PropertyFilter filter = new PropertyFilter();
		// filter.setPropertyName(entry.getKey());
		// List<String> values = new ArrayList<String>();
		// values.add(entry.getValue());
		// filter.setValues(values);
		// filter.setPredicate(PropertyPredicate.EQUAL);
		// propertyFilterList.add(filter);
		// vertexFilter.setFilterList(propertyFilterList);
		//
		// vertexFilterList.add(vertexFilter);
		// lineSearchReqObj.setVertexFilterList(vertexFilterList);
		// lineSearchReqObj.setLayer(5);
		// lineSearchReqObjList.add(lineSearchReqObj);
		// }
		// }

		String searchLineResultStr = api.searchLines(lineSearchReqObj);
		JSONObject allSearchLineResult = JSONObject.parseObject(searchLineResultStr);
		// for (LineSearchReqObj lineSearchReqObj : lineSearchReqObjList) {
		// String searchLineResult = api.searchLines(lineSearchReqObj);
		// JSONObject aRes = JSONObject.parseObject(searchLineResult);
		// vertexList.addAll(aRes.getJSONArray("vertexList"));
		// allSearchLineResult.put("vertexList", aRes.getJSONArray("vertexList"));
		// allSearchLineResult.put("edgeList", aRes.getJSONArray("edgeList"));
		// }
		// 封装返回数据
		Map<String, Object> result = JsonParseTool.parsePathJsonWithProperty(allSearchLineResult);
		return result;
	}

}
