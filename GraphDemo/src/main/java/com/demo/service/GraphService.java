package com.demo.service;

import java.util.Map;


public interface GraphService {
	
	//测试
	int getInt();
	
	//查询全图节点和关系
	Map<String, Object> getGraph(Map<String,Object> reqMap);
	
	//扩线查询
	String searchLines(Map<String,Object> reqMap);
	
	// 关联查询，根据点的id产出它们的关系
	String searchRelationshipByVertexsId(Map<String, Object> reqMap);

}
