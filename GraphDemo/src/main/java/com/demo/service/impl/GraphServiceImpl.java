package com.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demo.dao.GraphApi;
import com.demo.service.GraphService;
import com.huawei.request.CentricEdgeSearchReqObj;
import com.huawei.request.LineSearchReqObj;
import com.huawei.request.VertexElement;
import com.huawei.request.VertexSearchReqObj;
import com.huawei.request.VertexSearchRspObj;

@Service("graphService")
public class GraphServiceImpl implements GraphService {

	GraphApi api = GraphApi.getGraphApi_test();

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return api.getInt();
	}
	
	@Override
	public Map<String, Object> getGraph(Map<String,Object> reqMap) {
		// TODO Auto-generated method stub
		//查询全图节点
		Integer vertexNumLimit = (Integer) reqMap.get("vertexNumLimit");
		VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
		if(null != vertexNumLimit) {
			vertexSearchReqObj.setLimit(vertexNumLimit);
		}else {
			vertexSearchReqObj.setLimit(100);	//默认查询100个节点
		}
		VertexSearchRspObj vertexSearchRspObj = api.searchVertex(vertexSearchReqObj);
		//获取节点id
		List<Integer> vertexIdList = new ArrayList<Integer>();
		List<VertexElement> vertexList = vertexSearchRspObj.getVertexList();
		for (VertexElement vertexElement : vertexList) {
			String id = vertexElement.getId();
			vertexIdList.add(Integer.valueOf(id));
		}
		//查询与点相关的边
		Integer edgeNumLimit = (Integer) reqMap.get("edgeNumLimit");
		CentricEdgeSearchReqObj centricEdgeSearchReqObj = new CentricEdgeSearchReqObj();
		if(null != edgeNumLimit) {
			centricEdgeSearchReqObj.setLimit(edgeNumLimit);
		}else {
			centricEdgeSearchReqObj.setLimit(100);	//默认查询100条边
		}
		centricEdgeSearchReqObj.setVertexIdList(vertexIdList);
		String centricEdgeSearchResult = api.centricEdgeSearch(centricEdgeSearchReqObj);
		//封装返回结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("vertexs", vertexSearchRspObj);
		resultMap.put("links", centricEdgeSearchResult);
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

		// 层数
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

}
