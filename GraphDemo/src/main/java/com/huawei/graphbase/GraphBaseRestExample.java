package com.huawei.graphbase;

import com.huawei.entity.EdgeLabel;
import com.huawei.entity.PropertyKey;
import com.huawei.request.*;
import com.huawei.security.GraphHttpClient;
import com.huawei.security.HttpAuthInfo;
import com.huawei.util.*;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.huawei.security.GraphHttpClient.newClient;

public class GraphBaseRestExample {
    private static final Logger LOG = LoggerFactory.getLogger(GraphBaseRestExample.class);

    private static final String DEFAULT_CONFIG_FILE = "conf" + File.separator + "graphbase.properties";
    private static final String SCHEMA_FILE = "conf" + File.separator + "Person.xml";

    public static void main(String[] args) throws Exception {
        GraphHttpClient client = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir") + File.separator + DEFAULT_CONFIG_FILE);
            Properties p = new Properties();
            p.load(inputStream);
            HttpAuthInfo httpAuthInfo = HttpAuthInfo.newBuilder()
                    .setIp(p.getProperty("ip"))
                    .setPort(Integer.valueOf(p.getProperty("port")))
                    .setService(RestApi.SERVICE)
                    .setCasIP(p.getProperty("casIp"))
                    .setCasPort(Integer.valueOf(p.getProperty("casPort")))
                    .setUsername(p.getProperty("userName"))
                    .setPassword(p.getProperty("password"))
                    .build();

            client = newClient(httpAuthInfo);
            RestApi api = new RestApi(client);
            String graphName = "graphtest";

            //创建图
            api.createGraph(graphName);

            //上传xml文件创建schema
            File file = new File(System.getProperty("user.dir") + File.separator + SCHEMA_FILE);
            api.addSchema(file, graphName);

            //创建vertex label
            api.addVertexLabel("person", graphName);
            api.addVertexLabel("phone", graphName);
            //根据name查询vertex label
            api.queryVertexLabel("person", graphName);
            //查询所有vertex label
            api.queryAllVertexLabel(graphName);

            //创建edge label
            EdgeLabel edgeLabel = new EdgeLabel();
            edgeLabel.setName("friend");
            api.addEdgeLabel(edgeLabel, graphName);
            edgeLabel = new EdgeLabel();
            edgeLabel.setName("knows");
            api.addEdgeLabel(edgeLabel, graphName);
            edgeLabel = new EdgeLabel();
            edgeLabel.setName("call");
            api.addEdgeLabel(edgeLabel, graphName);
            edgeLabel = new EdgeLabel();
            edgeLabel.setName("has");
            api.addEdgeLabel(edgeLabel, graphName);
            //查询edge label
            api.queryEdgeLabel("friend", graphName);
            //查询所有edge label
            api.queryAllEdgeLabel(graphName);

            //创建propertyKey
            PropertyKey propertyKey = new PropertyKey();
            propertyKey.setDataType(DataType.String);
            propertyKey.setName("name");
            api.addPropertyKey(propertyKey, graphName);
            propertyKey = new PropertyKey();
            propertyKey.setDataType(DataType.Integer);
            propertyKey.setName("age");
            api.addPropertyKey(propertyKey, graphName);
            propertyKey = new PropertyKey();
            propertyKey.setDataType(DataType.String);
            propertyKey.setName("telephone");
            api.addPropertyKey(propertyKey, graphName);
            propertyKey = new PropertyKey();
            propertyKey.setDataType(DataType.Float);
            propertyKey.setName("weight");
            api.addPropertyKey(propertyKey, graphName);
            //根据name查询propertyKey
            api.queryPropertyKey("name", graphName);
            //查询所有propertyKey
            api.queryAllPropertyKey(graphName);

            //创建索引：需在创建schema之后，创建点边之前，否则数据查询不到
            addIndex(api, graphName);

            //索引重建
//            api.reCreateGraphIndex("name_index", graphName);
//            api.reCreateGraphIndex("age_index", graphName);
//            api.reCreateGraphIndex("telephone_index", graphName);
//            api.reCreateGraphIndex("weight_index", graphName);

            //查询所有索引
            api.queryAllIndex(graphName);

            //创建person类型的点vertex
            addVertexPerson(api, graphName);
            //创建phone类型的点vertex
            addVertexPhone(api, graphName);

            //根据点id查询点
            String vertexId = getVertexIdByProperty(api,graphName,"person","name","marko");
            api.queryVertex(vertexId, graphName);

            //创建典型场景中的边9-16
            addEdges(api, graphName);

            //根据边id查询边
            String edgeId = getEdgeIdByProperty(api,graphName,"call","weight","0.6");
            api.queryEdge(edgeId, graphName);

            //全图查询点：满足场景查询年龄大于29的人，并按年龄递增排列，结果上限为3
            VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
            vertexSearchReqObj.setVertexLabel("person");
            List<PropertyFilter> propertyFilterList1 = new ArrayList<PropertyFilter>();
            PropertyFilter propertyFilter = new PropertyFilter();
            propertyFilter.setPropertyName("age");
            propertyFilter.setPredicate(PropertyPredicate.GREATER_THAN);
            List<Integer> values = new ArrayList<Integer>();
            values.add(29);
            propertyFilter.setValues(values);
            propertyFilterList1.add(propertyFilter);
            vertexSearchReqObj.setFilterList(propertyFilterList1);
            List<PropertyKeySort> sortList = new ArrayList<PropertyKeySort>();
            PropertyKeySort propertyKeySort = new PropertyKeySort();
            propertyKeySort.setPropertyKeyName("age");
            propertyKeySort.setSortType("ASC");
            sortList.add(propertyKeySort);
            vertexSearchReqObj.setPropertyKeySortList(sortList);
            vertexSearchReqObj.setLimit(3);
            api.searchVertex(vertexSearchReqObj, graphName);

            //全图查询边：场景为查询边类型为knowns，weight不大于0.6的边
            EdgeSearchReqObj edgeSearchReqObj = new EdgeSearchReqObj();
            edgeSearchReqObj.setEdgeLabel("knows");
            edgeSearchReqObj.setLimit(2);
            List<PropertyFilter> propertyFilterList2 = new ArrayList<PropertyFilter>();
            PropertyFilter propertyFilter2 = new PropertyFilter();
            propertyFilter2.setPropertyName("weight");
            propertyFilter2.setPredicate(PropertyPredicate.LESS_THAN_EQUAL);
            List<Float> values2 = new ArrayList();
            values2.add(new Float(0.6));
            propertyFilter2.setValues(values2);
            propertyFilterList2.add(propertyFilter2);
            edgeSearchReqObj.setFilterList(propertyFilterList2);
            api.searchEdge(edgeSearchReqObj, graphName);

            //扩线查询:
            // 场景为：以节点1为起始点,扩线两层
            // 第一层条件：点的过滤条件为age不大于29的人
            // 第二层条件：点的过滤条件为telephone为13729584211的电话
            lineSearch(api, graphName);

            //路经查询
            //查询节点1到节点8的全路径
            allPathSearch(api, graphName);
            //查询节点1到节点8的最短路径
            shortestPathSearch(api, graphName);
            //查询节点1到节点8的路径中，所有节点的age不小于29的路径
            vertexFilterPathSearch(api, graphName);

            //查询正在运行中的任务
            List<Task> tasks = api.queryAllTask();

            //删除运行中的一个任务
            Task task = null;
            if (null != tasks && tasks.size() > 0) {
                task = tasks.get(0);
            }
            api.deleteTask(task.getId(), task.getInstanceIp());

            //删除图
            api.deleteGraph(graphName);

        } catch (Exception e) {
            LOG.info(e.getMessage());
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != client) {
                client.httpClient.close();
            }
        }
    }

    private static String getEdgeIdByProperty(RestApi api, String graphName, String edgeLabel, String name, String value) throws JSONException {
        String edgeId = "";
        EdgeSearchReqObj edgeSearchReqObj = new EdgeSearchReqObj();
        edgeSearchReqObj.setEdgeLabel(edgeLabel);
        edgeSearchReqObj.setLimit(1);
        List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
        PropertyFilter propertyFilter = new PropertyFilter();
        propertyFilter.setPropertyName(name);
        propertyFilter.setPredicate(PropertyPredicate.EQUAL);
        List<Float> values = new ArrayList();
        values.add(new Float(value));
        propertyFilter.setValues(values);
        propertyFilterList.add(propertyFilter);
        edgeSearchReqObj.setFilterList(propertyFilterList);
        EdgeSearchRspObj rspObj = api.searchEdge(edgeSearchReqObj, graphName);
        edgeId = rspObj.getEdgeList().get(0).getEdgeId();
        return edgeId;
    }

    private static void lineSearch(RestApi api, String graphName) throws JSONException {
        LineSearchReqObj lineSearchReqObj = new LineSearchReqObj();
        List<String> vertexIdList = new ArrayList<String>();
        String vertexId = getVertexIdByProperty(api, graphName, "person", "name", "marko");
        vertexIdList.add(vertexId);
        lineSearchReqObj.setVertexIdList(vertexIdList);
        List<VertexFilter> vertexFilterList = new ArrayList<VertexFilter>();
        VertexFilter vertexFilter = new VertexFilter();
        List<PropertyFilter> propertyFilterList1 = new ArrayList<PropertyFilter>();
        PropertyFilter propertyFilter = new PropertyFilter();
        propertyFilter.setPropertyName("age");
        propertyFilter.setPredicate(PropertyPredicate.LESS_THAN_EQUAL);
        List<Integer> values2 = new ArrayList();
        values2.add(29);
        propertyFilter.setValues(values2);
        propertyFilterList1.add(propertyFilter);
        vertexFilter.setFilterList(propertyFilterList1);
        vertexFilterList.add(vertexFilter);
        vertexFilter = new VertexFilter();
        List<String> vertexLabelList = new ArrayList<String>();
        vertexLabelList.add("phone");
        vertexFilter.setVertexLabelList(vertexLabelList);
        vertexFilterList.add(vertexFilter);
        lineSearchReqObj.setVertexFilterList(vertexFilterList);
        lineSearchReqObj.setLayer(2);
        lineSearchReqObj.setLimit(5);
        api.searchLines(lineSearchReqObj, graphName);
    }

    private static void vertexFilterPathSearch(RestApi api, String graphName) throws JSONException {
        PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
        List<String> vertexIdList = new ArrayList<String>();
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "blame"));
        pathSearchReqObj.setVertexIdList(vertexIdList);
        VertexFilter vertexFilter = new VertexFilter();
        List<PropertyFilter> propertyFilterList3 = new ArrayList<PropertyFilter>();
        PropertyFilter propertyFilter = new PropertyFilter();
        propertyFilter.setPropertyName("age");
        propertyFilter.setPredicate(PropertyPredicate.GREATER_THAN_EQUAL);
        List<Integer> values2 = new ArrayList();
        values2.add(29);
        propertyFilter.setValues(values2);
        propertyFilterList3.add(propertyFilter);
        vertexFilter.setFilterList(propertyFilterList3);
        pathSearchReqObj.setVertexFilter(vertexFilter);
        pathSearchReqObj.setLayer(7);
        api.searchPath(pathSearchReqObj, graphName);
    }

    private static void shortestPathSearch(RestApi api, String graphName) throws JSONException {
        PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
        List<String> vertexIdList = new ArrayList<String>();
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "blame"));
        pathSearchReqObj.setVertexIdList(vertexIdList);
        pathSearchReqObj.setLayer(7);
        pathSearchReqObj.setOption("shortest");//option不传时，默认为all,即全路径
        api.searchPath(pathSearchReqObj, graphName);
    }

    private static void allPathSearch(RestApi api, String graphName) throws JSONException {
        PathSearchReqObj pathSearchReqObj = new PathSearchReqObj();
        List<String> vertexIdList = new ArrayList<String>();
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        vertexIdList.add(getVertexIdByProperty(api, graphName, "person", "name", "blame"));
        pathSearchReqObj.setVertexIdList(vertexIdList);
        pathSearchReqObj.setLayer(7);
        api.searchPath(pathSearchReqObj, graphName);
    }

    private static void addIndex(RestApi api, String graphName) throws JSONException {
        //为点属性name创建内置索引
        GraphIndexReqObj graphIndexReqObj = new GraphIndexReqObj();
        graphIndexReqObj.setElementCategory(ElementCategory.VERTEX);
        graphIndexReqObj.setName("name_index");
        graphIndexReqObj.setType(IndexType.COMPOSITE);
        List<KeyTextType> keyTextTypeList1 = new ArrayList<KeyTextType>();
        KeyTextType keyTextType = new KeyTextType();
        keyTextType.setName("name");
        keyTextType.setTextType("");//IndexType为COMPOSITE时，TextType为空
        keyTextTypeList1.add(keyTextType);
        graphIndexReqObj.setKeyTextTypeList(keyTextTypeList1);
        api.addGraphIndex(graphIndexReqObj, graphName);

        //为点属性age创建内置索引
        graphIndexReqObj = new GraphIndexReqObj();
        graphIndexReqObj.setElementCategory(ElementCategory.VERTEX);
        graphIndexReqObj.setName("age_index");
        graphIndexReqObj.setType(IndexType.MIXED);
        List<KeyTextType> keyTextTypeList2 = new ArrayList<KeyTextType>();
        keyTextType = new KeyTextType();
        keyTextType.setName("age");
        keyTextType.setTextType("DEFAULT");//IndexType为COMPOSITE时，TextType为空
        keyTextTypeList2.add(keyTextType);
        graphIndexReqObj.setKeyTextTypeList(keyTextTypeList2);
        api.addGraphIndex(graphIndexReqObj, graphName);

        //为点属性telephone创建混合索引
        graphIndexReqObj = new GraphIndexReqObj();
        graphIndexReqObj.setElementCategory(ElementCategory.VERTEX);
        graphIndexReqObj.setName("telephone_index");
        graphIndexReqObj.setType(IndexType.MIXED);
        List<KeyTextType> keyTextTypeList3 = new ArrayList<KeyTextType>();
        keyTextType = new KeyTextType();
        keyTextType.setName("telephone");
        keyTextType.setTextType("STRING");//IndexType为MIXED时，TextType参照接口文档传参
        keyTextTypeList3.add(keyTextType);
        graphIndexReqObj.setKeyTextTypeList(keyTextTypeList3);
        api.addGraphIndex(graphIndexReqObj, graphName);

        //为边属性weight创建混合索引
        graphIndexReqObj = new GraphIndexReqObj();
        graphIndexReqObj.setElementCategory(ElementCategory.EDGE);
        graphIndexReqObj.setName("weight_index");
        graphIndexReqObj.setType(IndexType.MIXED);
        List<KeyTextType> keyTextTypeList4 = new ArrayList<KeyTextType>();
        keyTextType = new KeyTextType();
        keyTextType.setName("weight");
        keyTextType.setTextType("DEFAULT");//IndexType为MIXED时，TextType参照接口文档传参
        keyTextTypeList4.add(keyTextType);
        graphIndexReqObj.setKeyTextTypeList(keyTextTypeList4);
        api.addGraphIndex(graphIndexReqObj, graphName);
    }


    private static void addEdges(RestApi api, String graphName) throws JSONException {
        //创建场景中的边9
        AddEdgeReqObj addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("knows");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "person", "name", "vadas"));
        List<PropertyReqObj> propertyReqObjList1 = new ArrayList<PropertyReqObj>();
        PropertyReqObj propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.4);
        propertyReqObjList1.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList1);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边10
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("has");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "15023614521"));
        List<PropertyReqObj> propertyReqObjList2 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.8);
        propertyReqObjList2.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList2);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边11
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("has");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "josh"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "15291463258"));
        List<PropertyReqObj> propertyReqObjList3 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.8);
        propertyReqObjList3.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList3);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边12
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("knows");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "josh"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "person", "name", "blame"));
        List<PropertyReqObj> propertyReqObjList4 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.4);
        propertyReqObjList4.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList4);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边13
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("has");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "blame"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13612493615"));
        List<PropertyReqObj> propertyReqObjList5 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.8);
        propertyReqObjList5.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList5);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边14，由于是双向箭头，需要创建一个入边和一个出边
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("call");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13612493615"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13729584211"));
        List<PropertyReqObj> propertyReqObjList6 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.6);
        propertyReqObjList6.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList6);
        api.addEdge(addEdgeReqObj, graphName);
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("call");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13729584211"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13612493615"));
        List<PropertyReqObj> propertyReqObjList7 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.6);
        propertyReqObjList7.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList7);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边15
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("has");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "vadas"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "phone", "telephone", "13729584211"));
        List<PropertyReqObj> propertyReqObjList8 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(0.8);
        propertyReqObjList8.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList8);
        api.addEdge(addEdgeReqObj, graphName);

        //创建场景中的边16
        addEdgeReqObj = new AddEdgeReqObj();
        addEdgeReqObj.setEdgeLabel("friend");
        addEdgeReqObj.setOutVertexId(getVertexIdByProperty(api, graphName, "person", "name", "marko"));
        addEdgeReqObj.setInVertexId(getVertexIdByProperty(api, graphName, "person", "name", "josh"));
        List<PropertyReqObj> propertyReqObjList9 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("weight");
        propertyReqObj.setValue(1.0);
        propertyReqObjList9.add(propertyReqObj);
        addEdgeReqObj.setPropertyList(propertyReqObjList9);
        api.addEdge(addEdgeReqObj, graphName);
    }

    private static String getVertexIdByProperty(RestApi api, String graphName, String vertexLabel, String propertyKeyName, String value) throws JSONException {
        String vertexId = "";
        VertexSearchReqObj vertexSearchReqObj = new VertexSearchReqObj();
        vertexSearchReqObj.setVertexLabel(vertexLabel);
        List<PropertyFilter> propertyFilterList = new ArrayList<PropertyFilter>();
        PropertyFilter propertyFilter = new PropertyFilter();
        propertyFilter.setPropertyName(propertyKeyName);
        propertyFilter.setPredicate(PropertyPredicate.EQUAL);
        List<String> values = new ArrayList();
        values.add(value);
        propertyFilter.setValues(values);
        propertyFilterList.add(propertyFilter);
        vertexSearchReqObj.setFilterList(propertyFilterList);
        vertexSearchReqObj.setLimit(1);
        VertexSearchRspObj rspObj = api.searchVertex(vertexSearchReqObj, graphName);
        vertexId = rspObj.getVertexList().get(0).getId();
        return vertexId;
    }

    private static void addVertexPhone(RestApi api, String graphName) throws JSONException {
        //创建phone节点2
        AddVertexReqObj addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("phone");
        List<PropertyReqObj> propertyReqObjList1 = new ArrayList<PropertyReqObj>();
        PropertyReqObj propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("telephone");
        propertyReqObj.setValue("15023614521");
        propertyReqObjList1.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList1);
        api.addVertex(addVertexReqObj, graphName);

        //创建phone节点3
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("phone");
        List<PropertyReqObj> propertyReqObjList2 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("telephone");
        propertyReqObj.setValue("15291463258");
        propertyReqObjList2.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList2);
        api.addVertex(addVertexReqObj, graphName);

        //创建phone节点6
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("phone");
        List<PropertyReqObj> propertyReqObjList3 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("telephone");
        propertyReqObj.setValue("13729584211");
        propertyReqObjList3.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList3);
        api.addVertex(addVertexReqObj, graphName);

        //创建phone节点7
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("phone");
        List<PropertyReqObj> propertyReqObjList4 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("telephone");
        propertyReqObj.setValue("13612493615");
        propertyReqObjList4.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList4);
        api.addVertex(addVertexReqObj, graphName);
    }

    private static void addVertexPerson(RestApi api, String graphName) throws JSONException {
        //创建person节点1
        AddVertexReqObj addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("person");
        List<PropertyReqObj> propertyReqObjList1 = new ArrayList<PropertyReqObj>();
        PropertyReqObj propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("name");
        propertyReqObj.setValue("marko");
        propertyReqObjList1.add(propertyReqObj);
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("age");
        propertyReqObj.setValue(29);
        propertyReqObjList1.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList1);
        api.addVertex(addVertexReqObj, graphName);

        //创建person节点4
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("person");
        List<PropertyReqObj> propertyReqObjList2 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("name");
        propertyReqObj.setValue("josh");
        propertyReqObjList2.add(propertyReqObj);
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("age");
        propertyReqObj.setValue(32);
        propertyReqObjList2.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList2);
        api.addVertex(addVertexReqObj, graphName);

        //创建person节点4
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("person");
        List<PropertyReqObj> propertyReqObjList3 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("name");
        propertyReqObj.setValue("vadas");
        propertyReqObjList3.add(propertyReqObj);
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("age");
        propertyReqObj.setValue(27);
        propertyReqObjList3.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList3);
        api.addVertex(addVertexReqObj, graphName);

        //创建person节点8
        addVertexReqObj = new AddVertexReqObj();
        addVertexReqObj.setVertexLabel("person");
        List<PropertyReqObj> propertyReqObjList4 = new ArrayList<PropertyReqObj>();
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("name");
        propertyReqObj.setValue("blame");
        propertyReqObjList4.add(propertyReqObj);
        propertyReqObj = new PropertyReqObj();
        propertyReqObj.setName("age");
        propertyReqObj.setValue(30);
        propertyReqObjList4.add(propertyReqObj);
        addVertexReqObj.setPropertyList(propertyReqObjList4);
        api.addVertex(addVertexReqObj, graphName);
    }
}
