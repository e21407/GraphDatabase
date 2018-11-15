package com.huawei.request;

import java.util.List;

public class LineSearchReqObj {
    private List<String> vertexIdList;
    private int layer;
    private List<VertexFilter> vertexFilterList;
    private List<EdgeFilter> edgeFilterList;
    private Boolean withProperty;
    private List<String> vertexPropertyList;
    private int limit;

    public List<String> getVertexIdList() {
        return vertexIdList;
    }

    public void setVertexIdList(List<String> vertexIdList) {
        this.vertexIdList = vertexIdList;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public List<VertexFilter> getVertexFilterList() {
        return vertexFilterList;
    }

    public void setVertexFilterList(List<VertexFilter> vertexFilterList) {
        this.vertexFilterList = vertexFilterList;
    }

    public List<EdgeFilter> getEdgeFilterList() {
        return edgeFilterList;
    }

    public void setEdgeFilterList(List<EdgeFilter> edgeFilterList) {
        this.edgeFilterList = edgeFilterList;
    }

    public Boolean getWithProperty() {
        return withProperty;
    }

    public void setWithProperty(Boolean withProperty) {
        this.withProperty = withProperty;
    }

    public List<String> getVertexPropertyList() {
        return vertexPropertyList;
    }

    public void setVertexPropertyList(List<String> vertexPropertyList) {
        this.vertexPropertyList = vertexPropertyList;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
