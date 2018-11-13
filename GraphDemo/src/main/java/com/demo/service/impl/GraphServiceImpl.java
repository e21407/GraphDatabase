package com.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ArrayELResolver;

import java.util.Set;

import org.springframework.stereotype.Service;

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
		// TODO Auto-generated method stub
		// 查询全图节点
		VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
		vertexSearchReqObj.setVertexLabel("customer");
		vertexSearchReqObj.setLimit(5);	//查询5个客户节点
		VertexSearchRspObj vertexSearchRspObj = api.searchVertex(vertexSearchReqObj); // Vertex的全图条件查询
		// 获取节点id
		List<Integer> vertexIdList = new ArrayList<Integer>();
		List<VertexElement> vertexList = vertexSearchRspObj.getVertexList();
		for (VertexElement vertexElement : vertexList) {
			String id = vertexElement.getId();
			vertexIdList.add(Integer.valueOf(id));
		}
		//扩线查询
		LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
		lineSearchReqObj.setVertexIdList(vertexIdList);	//设置id列表，必选
		lineSearchReqObj.setLayer(2);	//设置扩线层数，必选
		String searchLinesResult = api.searchLines(lineSearchReqObj);
		
		// 封装返回结果
		JSONObject resultJson = JSONObject.parseObject(searchLinesResult);
		Map<String, Object> resultMap = JsonParseTool.parsePathJson(resultJson);
		return resultMap;
	}

	@Override
	public String searchLines(Map<String, Object> reqMap) {
		// TODO Auto-generated method stub
		// 账户号
		Integer accountId = (Integer) reqMap.get("account");
		// 客户号
		Integer customerId = (Integer) reqMap.get("customer");
		// 卡号
		Integer cardId = (Integer) reqMap.get("card");
		// 申请书标号
		Integer applyId = (Integer) reqMap.get("apply");
		// 推广人id
		Integer tgrId = (Integer) reqMap.get("tgr");
		// 推广机构id
		Integer tgjgId = (Integer) reqMap.get("tgjg");
		// 所属公司
		Integer companyId = (Integer) reqMap.get("company");

		// 扩线层数
		Integer layer = (Integer) reqMap.get("layer");

		List<Integer> vertexIdList = new ArrayList<Integer>();
		if (null != accountId) {
			vertexIdList.add(accountId);
		}
		if (null != customerId) {
			vertexIdList.add(customerId);
		}
		if (null != cardId) {
			vertexIdList.add(cardId);
		}
		if (null != applyId) {
			vertexIdList.add(applyId);
		}
		if (null != tgrId) {
			vertexIdList.add(tgrId);
		}
		if (null != tgjgId) {
			vertexIdList.add(tgjgId);
		}
		if (null != companyId) {
			vertexIdList.add(companyId);
		}
		LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
		lineSearchReqObj.setVertexIdList(vertexIdList);
		// 扩线层数必须设置，范围(0,10]
		if (null != layer)
			lineSearchReqObj.setLayer(layer);

		return api.searchLines(lineSearchReqObj);
	}

	@Override
	public String searchRelationshipByVertexsId(Map<String, Object> reqMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String searchPath(Map<String, Object> reqMap) {
		String vertexIdLstStr = (String) reqMap.get("vertexIds");
		String option = (String) reqMap.get("option");
		ObjectMapper mapper = new ObjectMapper();
		List<String> vertexIdList = null;
		try {
			vertexIdList = mapper.readValue(vertexIdLstStr, new TypeReference<List<String>>() {
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
		pathSearchReqObj.setVertexIdList(vertexIdList);
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
		return api.searchPath(pathSearchReqObj);
	}

	// 二次查询
	public Map<String, Object> secondSearch(Map<String, Object> reqMap) {
		if (null == reqMap) {
			return null;
		}
		String vertexLabel = (String) reqMap.get("vertexLabel");
		String choice = (String) reqMap.get("choice");
		Integer vertexId = (Integer) reqMap.get("vertexId");
		List<Integer> vertexIdList = new ArrayList<Integer>();
		Map<String, String> propertyList = (Map<String, String>) reqMap.get("propertyList");
		vertexIdList.add(vertexId);
		//多次查询列表
		List<LineSearchReqObj> lineSearchReqObjList = new ArrayList<LineSearchReqObj>();
		
		if ("and".equals(choice)) {
			LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
			lineSearchReqObj.setVertexIdList(vertexIdList);
			List<VertexFilter> vertexFilterList = new ArrayList<VertexFilter>();
			VertexFilter vertexFilter = new VertexFilter();
			ArrayList<String> labelList = new ArrayList<String>();
			labelList.add(vertexLabel);
			vertexFilter.setVertexLabelList(labelList);
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
			lineSearchReqObjList.add(lineSearchReqObj);
		}
		
		if("or".equals(choice)) {
			//为每个属性创建一个查询对象
			Set<Entry<String, String>> entrySet = propertyList.entrySet();
			for (Entry<String, String> entry : entrySet) {
				LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
				lineSearchReqObj.setVertexIdList(vertexIdList);
				List<VertexFilter> vertexFilterList = new ArrayList<VertexFilter>();
				VertexFilter vertexFilter = new VertexFilter();
				ArrayList<String> labelList = new ArrayList<String>();
				labelList.add(vertexLabel);
				vertexFilter.setVertexLabelList(labelList);
				List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
				
				PropertyFilter filter = new PropertyFilter();
				filter.setPropertyName(entry.getKey());
				List<String> values = new ArrayList<String>();
				values.add(entry.getValue());
				filter.setValues(values);
				filter.setPredicate(PropertyPredicate.EQUAL);
				propertyFilterList.add(filter);
				vertexFilter.setFilterList(propertyFilterList);
				
				vertexFilterList.add(vertexFilter);
				lineSearchReqObj.setVertexFilterList(vertexFilterList);
				lineSearchReqObj.setLayer(4);
				lineSearchReqObjList.add(lineSearchReqObj);
			}
		}
		//封装返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> resultItem = new ArrayList<String>();
		for (LineSearchReqObj lineSearchReqObj : lineSearchReqObjList) {
			String searchLineResult = api.searchLines(lineSearchReqObj);
			resultItem.add(searchLineResult);
		}
		result.put("data", resultItem);
		return result;
	}

}
