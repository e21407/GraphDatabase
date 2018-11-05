package com.demo.dao;

import static com.huawei.security.GraphHttpClient.newClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.huawei.graphbase.RestApi;
import com.huawei.request.CentricEdgeSearchReqObj;
import com.huawei.request.LineSearchReqObj;
import com.huawei.request.PathSearchReqObj;
import com.huawei.request.VertexSearchReqObj;
import com.huawei.request.VertexSearchRspObj;
import com.huawei.security.GraphHttpClient;
import com.huawei.security.HttpAuthInfo;

public class GraphApi {
	private static final String DEFAULT_CONFIG_FILE = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "graphbase.properties";

	private static GraphApi graphApiInstance;
	
	//图数据库名字，随环境而更改
	private static final String graphName = "graphtest";

	static RestApi api;

	private GraphApi() {
	}
	
	public static synchronized GraphApi getGraphApi_test() {
		if(graphApiInstance == null) {
			graphApiInstance = new GraphApi();
		}
		return graphApiInstance;
	}

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
	
	//扩线查询，查找关联关系
	public String searchLines(LineSearchReqObj lineSearchReqObj) {
		return api.searchLines(lineSearchReqObj, graphName);
	}
	
	//Vertex的全图条件查询
	public VertexSearchRspObj searchVertex(VertexSearchReqObj vertexSearchReqObj) {
		return api.searchVertex(vertexSearchReqObj, graphName);
	}
	
	//Edge的以点为中心的条件查询
	public  String centricEdgeSearch(CentricEdgeSearchReqObj centricEdgeSearchReqObj) {
		return api.centricEdgeSearch(centricEdgeSearchReqObj, graphName);
	}
	
	//路径查询
	public String searchPath(PathSearchReqObj pathSearchReqObj) {
		return api.searchPath(pathSearchReqObj, graphName);
	}

}
