package com.demo.dao;

import static com.huawei.security.GraphHttpClient.newClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONException;

import com.huawei.graphbase.RestApi;
import com.huawei.request.CentricEdgeSearchReqObj;
import com.huawei.request.EdgeQueryRspObj;
import com.huawei.request.EdgeSearchReqObj;
import com.huawei.request.EdgeSearchRspObj;
import com.huawei.request.LineSearchReqObj;
import com.huawei.request.PathSearchReqObj;
import com.huawei.request.PropertyFilter;
import com.huawei.request.VertexQueryRspObj;
import com.huawei.request.VertexSearchReqObj;
import com.huawei.request.VertexSearchRspObj;
import com.huawei.security.GraphHttpClient;
import com.huawei.security.HttpAuthInfo;
import com.huawei.util.PropertyPredicate;

public class GraphApi {
	private static final String DEFAULT_CONFIG_FILE = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "graphbase.properties";

	private static GraphApi graphApiInstance;
	
	//图数据库名字，随环境而更改
	private static final String graphName = "case_check_graph";

	static RestApi api;

	private GraphApi() {
	}
	
	public static synchronized GraphApi getGraphApi_test() {
		if(graphApiInstance == null) {
			graphApiInstance = new GraphApi();
		}
		return graphApiInstance;
	}

	//交付时启用
	public static synchronized GraphApi getGraphApi() {
		if (graphApiInstance == null) {
			graphApiInstance = new GraphApi();
			GraphHttpClient client = null;
			InputStream inputStream = null;
			try {

				inputStream = new FileInputStream(
						System.getProperty("user.dir") + File.separator + DEFAULT_CONFIG_FILE);
				Properties p = new Properties();
				p.load(inputStream);
				HttpAuthInfo httpAuthInfo = HttpAuthInfo.newBuilder().setIp(p.getProperty("ip"))
						.setPort(Integer.valueOf(p.getProperty("port"))).setService(RestApi.SERVICE)
						.setCasIP(p.getProperty("casIp")).setCasPort(Integer.valueOf(p.getProperty("casPort")))
						.setUsername(p.getProperty("userName")).setPassword(p.getProperty("password")).build();

				client = newClient(httpAuthInfo);
				graphApiInstance.api = new RestApi(client);
			} catch (Exception e) {
				System.out.println(e);

			} finally {
				/*if (null != inputStream) {
					inputStream.close();
				}
				if (null != client) {
					client.httpClient.close();
				}*/
			}
		}
		return graphApiInstance;
	}
	
	//测试
	public int getInt() {
		return 99;
	}
	
	public RestApi getRestApi() {
		return api;
	}
	
	//扩线查询，查找关联关系
	public String searchLines(LineSearchReqObj lineSearchReqObj) {
		return api.searchLines(lineSearchReqObj, graphName);
	}
	
	//Vertex的全图条件查询
	public VertexSearchRspObj searchVertex(VertexSearchReqObj vertexSearchReqObj) {
		return api.searchVertex(vertexSearchReqObj, graphName);
	}
	
	//Edge的全图条件查询
	public EdgeSearchRspObj searchEdge(EdgeSearchReqObj edgeSearchReqObj) {
		return api.searchEdge(edgeSearchReqObj, graphName);
	}
	
	//Edge的以点为中心的条件查询
	public  String centricEdgeSearch(CentricEdgeSearchReqObj centricEdgeSearchReqObj) {
		return api.centricEdgeSearch(centricEdgeSearchReqObj, graphName);
	}
	
	//路径查询
	public String searchPath(PathSearchReqObj pathSearchReqObj) {
		return api.searchPath(pathSearchReqObj, graphName);
	}
	
	//根据点id查询节点信息
	public VertexQueryRspObj queryVertex(String id) {
		return api.queryVertex(id, graphName);
	}
	
	//根据边id查询边信息
	public EdgeQueryRspObj queryEdge(String id) {
		return api.queryEdge(id, graphName);
	}
	
	public String getVertexIdByProperty(String vertexLabel, Map<String,String> propertyList) throws JSONException {
        String vertexId = "";
        VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
        vertexSearchReqObj.setVertexLabel(vertexLabel);
        List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
        PropertyFilter propertyFilter = null;
        for (Entry<String, String> property : propertyList.entrySet()) {
        	String propertyKeyName = property.getKey();
        	String value = property.getValue();
        	 propertyFilter = new PropertyFilter();
        	propertyFilter.setPropertyName(propertyKeyName);
            propertyFilter.setPredicate(PropertyPredicate.EQUAL);
            List<String> values = new ArrayList();
            values.add(value);
            propertyFilter.setValues(values);
		}
        propertyFilterList.add(propertyFilter);
        vertexSearchReqObj.setFilterList(propertyFilterList);
        vertexSearchReqObj.setLimit(1);
        VertexSearchRspObj rspObj = api.searchVertex(vertexSearchReqObj, graphName);
        vertexId = rspObj.getVertexList().get(0).getId();
        return vertexId;
    }

}
