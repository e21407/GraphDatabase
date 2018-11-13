package com.demo.service;

import java.util.Map;


public interface GraphService {
	
	//测试
	int getInt();
	
	//查询全图节点和关系
	Map<String, Object> getGraph(Map<String,Object> reqMap);
	
	// 关联查询，根据点的id产出它们的关系
	String searchRelationshipByVertexsId(Map<String, Object> reqMap);
	
	//路径查询，可以查询最短路径，全路径（点对点之间的所有路径），有权最短路径
	Map<String, Object> searchPath(Map<String,Object> reqMap);
	
	//二次查询
	public Map<String,Object> secondSearch(Map<String, Object> reqMap);

}
