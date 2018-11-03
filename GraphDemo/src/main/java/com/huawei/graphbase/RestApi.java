package com.huawei.graphbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huawei.RestHelper;
import com.huawei.entity.EdgeLabel;
import com.huawei.entity.Index;
import com.huawei.entity.PropertyKey;
import com.huawei.entity.VertexLabel;
import com.huawei.request.*;
import com.huawei.security.GraphHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RestApi {
    private static final Logger LOG = LoggerFactory.getLogger(RestApi.class);
    // URL PREFIX
    public static final String URL_HTTP = "https://";
    // URL SEP
    public static final String URL_SEP = "/";

    public static final String CSRF_TOKEN = "CSRF-Token";

    // 服务名定义
    public static final String SERVICE = "graphbase";

    //异常报错信息
    public static final String ERROR_MSG = "Json parsing error.";

    private GraphHttpClient client;

    private RestApi() {
        // nothing
    }

    public RestApi(GraphHttpClient client) {
        this.client = client;
    }

    public boolean createGraph(String graphName) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("graphName", graphName);

        JSONObject rspJson = sendHttpPostReq("/graph", reqJson);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: create graph[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                graphName,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean deleteGraph(String graphName) {

        JSONObject rspJson = sendHttpDeleteReq("/graph?graphName=" + graphName, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: delete graph[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                graphName,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean addVertexLabel(String name, String graphName) {
        JSONObject reqJson = new JSONObject();
        reqJson.put("name", name);

        JSONObject rspJson = sendHttpPostReq("/graph/" + graphName + "/schema/vertex-label", reqJson);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: create vertex label[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                name,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public VertexLabel queryVertexLabel(String name, String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/vertex-label/" + name, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query vertex label[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                name,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), VertexLabel.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public List<VertexLabel> queryAllVertexLabel(String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/vertex-label", null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query all vertex label %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data") && null != rspJson.getJSONObject("data").getJSONArray("vertexLabelList")) {
                return (List<VertexLabel>) mapper.readValue(rspJson.getJSONObject("data").getJSONArray("vertexLabelList").toString(), getCollectionType(mapper, List.class, VertexLabel.class));
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public boolean addEdgeLabel(EdgeLabel edgeLabel, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/schema/edge-label", RestHelper.toJsonString(edgeLabel));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: create edge label[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                edgeLabel.getName(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public EdgeLabel queryEdgeLabel(String name, String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/edge-label/" + name, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query edge label[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                name,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), EdgeLabel.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public List<EdgeLabel> queryAllEdgeLabel(String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/edge-label", null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query all edge label %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data") && null != rspJson.getJSONObject("data").getJSONArray("edgeLabelList")) {
                return (List<EdgeLabel>) mapper.readValue(rspJson.getJSONObject("data").getJSONArray("edgeLabelList").toString(), getCollectionType(mapper, List.class, EdgeLabel.class));
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    //创建propertyKey接口
    public boolean addPropertyKey(PropertyKey propertyKey, String graphName) {
        JSONObject rspJson = null;
        try {
            //调用REST接口url，传入请求参数propertyKey
            rspJson = sendHttpPostReq("/graph/" + graphName + "/schema/property-key", RestHelper.toJsonString(propertyKey));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }
        //解析返回参数rspJson
        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));
        //打印日志到控制台
        String log = String.format("[%s]: create property key[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                propertyKey.getName(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);
        return isSuccessfully;
    }

    public PropertyKey queryPropertyKey(String name, String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/property-key/" + name, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query property key[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                name,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), PropertyKey.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public List<PropertyKey> queryAllPropertyKey(String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/property-key", null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query all property key %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data") && null != rspJson.getJSONObject("data").getJSONArray("propertyKeyList")) {
                return (List<PropertyKey>) mapper.readValue(rspJson.getJSONObject("data").getJSONArray("propertyKeyList").toString(), getCollectionType(mapper, List.class, PropertyKey.class));
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public boolean deleteGraphIndex(String name, String graphName) {
        JSONObject rspJson = null;
        rspJson = sendHttpDeleteReq("/graph/" + graphName + "/schema/index/" + name, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: delete graph index[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                name,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean addVertex(AddVertexReqObj addVertexReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/vertex", RestHelper.toJsonString(addVertexReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: add vertex[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                addVertexReqObj.getVertexLabel(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean addEdge(AddEdgeReqObj addEdgeReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/edge", RestHelper.toJsonString(addEdgeReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: add edge[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                addEdgeReqObj.getEdgeLabel(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public EdgeQueryRspObj queryEdge(String id, String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/edge/" + id, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query edge[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                id,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), EdgeQueryRspObj.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public EdgeSearchRspObj searchEdge(EdgeSearchReqObj edgeSearchReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/edge/search", RestHelper.toJsonString(edgeSearchReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: search edge [%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                edgeSearchReqObj.getEdgeLabel(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), EdgeSearchRspObj.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public boolean deleteVertex(String id, String graphName) {
        JSONObject rspJson = null;
        rspJson = sendHttpDeleteReq("/graph/" + graphName + "/vertex/" + id, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: delete vertex[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                id,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public VertexQueryRspObj queryVertex(String id, String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/vertex/" + id, null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query vertex[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                id,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), VertexQueryRspObj.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public VertexSearchRspObj searchVertex(VertexSearchReqObj vertexSearchReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/vertex/search", RestHelper.toJsonString(vertexSearchReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: search vertex [%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                vertexSearchReqObj.getVertexLabel(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data")) {
                return mapper.readValue(rspJson.getJSONObject("data").toString(), VertexSearchRspObj.class);
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public String searchPath(PathSearchReqObj pathSearchReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/paths", RestHelper.toJsonString(pathSearchReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: search path %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }
        if (null != rspJson.getJSONObject("data")) {
            return rspJson.getJSONObject("data").toString();
        }
        return null;
    }

    public String searchLines(LineSearchReqObj lineSearchReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/lines", RestHelper.toJsonString(lineSearchReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: search path %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }
        if (null != rspJson.getJSONObject("data")) {
            return rspJson.getJSONObject("data").toString();
        }
        return null;
    }
    
    /**
     * liubaichuan写的实现Edge的以点为中心的条件查询接口
     * @param centricEdgeSearchReqObj
     * @param graphName
     * @return
     */
    public String centricEdgeSearch(CentricEdgeSearchReqObj centricEdgeSearchReqObj, String  graphName) {
    	JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/centric/edge/search", RestHelper.toJsonString(centricEdgeSearchReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: Centric Edge Search %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }
        if (null != rspJson.getJSONObject("data")) {
            return rspJson.getJSONObject("data").toString();
        }
        return null;
    }

    public List<Index> queryAllIndex(String graphName) {
        JSONObject rspJson = sendHttpGetReq("/graph/" + graphName + "/schema/index", null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query all index %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data") && null != rspJson.getJSONObject("data").getJSONArray("indexList")) {
                return (List<Index>) mapper.readValue(rspJson.getJSONObject("data").getJSONArray("indexList").toString(), getCollectionType(mapper, List.class, Index.class));
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public List<Task> queryAllTask() {
        JSONObject rspJson = sendHttpGetReq("/task", null);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: query all task %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        if (!isSuccessfully) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            if (null != rspJson.getJSONObject("data") && null != rspJson.getJSONObject("data").getJSONArray("tasks")) {
                return (List<Task>) mapper.readValue(rspJson.getJSONObject("data").getJSONArray("tasks").toString(), getCollectionType(mapper, List.class, Task.class));
            }
            return null;
        } catch (IOException e) {
            LOG.info(ERROR_MSG);
            return null;
        }
    }

    public boolean deleteTask(String id, String ip) {
        JSONObject rspJson = null;
        if (null != ip || ip.equals("")) {
            rspJson = sendHttpDeleteReq("/task/" + id, null);
        } else {
            rspJson = sendHttpDeleteReq("/task/" + id + "?ip=" + ip, null);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: delete task[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                id,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean addGraphIndex(GraphIndexReqObj graphIndexReqObj, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/schema/index/graph", RestHelper.toJsonString(graphIndexReqObj));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: create [%s] graph index[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                graphIndexReqObj.getType(),
                graphIndexReqObj.getName(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean reCreateGraphIndex(String graphIndex, String graphName) {

        JSONObject rspJson = sendHttpPutReq("/graph/" + graphName + "/schema/index/reindex/" + graphIndex);

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: reCreate graph index[%s]  %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                graphIndex,
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    public boolean addMixedGraphIndexKey(MixedIndexKeyReq mixedIndexKeyReq, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPutReq("/graph/" + graphName + "/schema/index/graph/key", RestHelper.toJsonString(mixedIndexKeyReq));
        } catch (JsonProcessingException e) {
            LOG.info(ERROR_MSG);
        }

        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));

        String log = String.format("[%s]: create MIXED graph index[%s] %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                mixedIndexKeyReq.getName(),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);

        return isSuccessfully;
    }

    //上传schema文件接口
    public boolean addSchema(File file, String graphName) {
        JSONObject rspJson = null;
        try {
            rspJson = sendHttpPostReq("/graph/" + graphName + "/schema", file);
        } catch (Exception e) {
            LOG.info(ERROR_MSG);
        }
        //解析返回参数rspJson
        final boolean isSuccessfully = (null == rspJson ? false : rspJson.getString("code").equals("0"));
        //打印日志到控制台
        String log = String.format("[%s]: create schema %s",
                (isSuccessfully ? "SUCCESS" : "FAIL"),
                (isSuccessfully ? "successfully." : "fail."));
        System.out.println(log);
        return isSuccessfully;
    }

    private JSONObject sendHttpPostReq(String uri, File file) {
        String fullUrl = buildUrl(uri);
        HttpPost httpPost = new HttpPost(fullUrl);
        httpPost.addHeader(CSRF_TOKEN, this.client.csrfToken);

        CloseableHttpResponse response = null;
        JSONObject rspJson = null;

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addBinaryBody("file", file);
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);
            printHttpReqHeader(httpPost);

            response = client.httpClient.execute(httpPost);
            RestHelper.checkHttpRsp(response);

            rspJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            printHttpRspBody(rspJson);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
        return rspJson;
    }

    private JSONObject sendHttpPostReq(String uri, JSONObject reqJson) {
        return sendHttpPostReq(uri, reqJson.toString());
    }

    private JSONObject sendHttpPostReq(String uri, String reqJsonStr) {
        String fullUrl = buildUrl(uri);
        HttpPost httpPost = new HttpPost(fullUrl);
        httpPost.addHeader(CSRF_TOKEN, this.client.csrfToken);
        CloseableHttpResponse response = null;
        JSONObject rspJson = null;
        try {
            httpPost.setEntity(new StringEntity(reqJsonStr));
            printHttpReqHeader(httpPost);
            printHttpReqBody(reqJsonStr);

            response = client.httpClient.execute(httpPost);
            RestHelper.checkHttpRsp(response);

            rspJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            printHttpRspBody(rspJson);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }
        return rspJson;
    }

    private JSONObject sendHttpDeleteReq(String uri, String reqJsonStr) {
        String fullUrl = buildUrl(uri);
        HttpDelete httpDelete = new HttpDelete(fullUrl);
        httpDelete.addHeader(CSRF_TOKEN, this.client.csrfToken);

        CloseableHttpResponse response = null;
        JSONObject rspJson = null;

        try {
            printHttpReqHeader(httpDelete);
            printHttpReqBody(reqJsonStr);

            response = client.httpClient.execute(httpDelete);
            RestHelper.checkHttpRsp(response);

            rspJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            printHttpRspBody(rspJson);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }

        return rspJson;
    }

    private JSONObject sendHttpPutReq(String uri, JSONObject reqJson) {
        return sendHttpPutReq(uri, reqJson.toString());
    }

    private JSONObject sendHttpPutReq(String uri) {
        return sendHttpPutReq(uri, "");
    }

    private JSONObject sendHttpPutReq(String uri, String reqJsonStr) {
        String fullUrl = buildUrl(uri);
        HttpPut httpPut = new HttpPut(fullUrl);
        httpPut.addHeader(CSRF_TOKEN, this.client.csrfToken);

        CloseableHttpResponse response = null;
        JSONObject rspJson = null;

        try {
            if (reqJsonStr != null && !reqJsonStr.isEmpty()) {
                httpPut.setEntity(new StringEntity(reqJsonStr));
            }
            printHttpReqHeader(httpPut);
            printHttpReqBody(reqJsonStr);

            response = client.httpClient.execute(httpPut);
            RestHelper.checkHttpRsp(response);

            rspJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            printHttpRspBody(rspJson);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }

        return rspJson;
    }

    private JSONObject sendHttpGetReq(String uri, JSONObject reqJson) {
        String fullUrl = buildUrl(uri);
        HttpGet httpGet = new HttpGet(fullUrl);
        CloseableHttpResponse response = null;
        JSONObject rspJson = null;
        try {
            printHttpReqHeader(httpGet);

            if (null != reqJson) {
                printHttpReqBody(reqJson);
            }

            response = this.client.httpClient.execute(httpGet);
            RestHelper.checkHttpRsp(response);

            rspJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            printHttpRspBody(rspJson);
        } catch (Exception e) {
            LOG.info(e.getMessage());
            return null;
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    // nothing to do
                }
            }
        }

        return rspJson;
    }

    private String buildUrl(String partUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL_HTTP)
                .append(this.client.httpAuthInfo.getIp()).append(":").append(this.client.httpAuthInfo.getPort()).append(URL_SEP)
                .append(SERVICE)
                .append(partUrl);
        String fullUrl = sb.toString();
        return fullUrl;
    }

    public void printHttpReqHeader(HttpUriRequest httpUriRequest) {
        System.out.println("REQ HEADER: " + httpUriRequest.getRequestLine());
    }

    public void printHttpReqBody(JSONObject reqJson) {
        System.out.println("REQ BODY:   " + (null == reqJson ? "null" : reqJson.toString(4)));
    }

    public void printHttpReqBody(String reqJsonStr) {
        System.out.println("REQ BODY:   " + (null == reqJsonStr ? "null" : reqJsonStr));
    }

    public void printHttpRspBody(JSONObject rspJson) {
        System.out.println("RSP BODY:   " + (null == rspJson ? "null" : rspJson.toString(4)));
    }

    public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
