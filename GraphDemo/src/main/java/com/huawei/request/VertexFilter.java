package com.huawei.request;

import com.huawei.util.DataType;

import java.util.List;

public class VertexFilter {
    private List<String> vertexLabelList;
    private List<PropertyFilter> filterList;
    private int limit;
    private DataType propertyDataType;

    public List<String> getVertexLabelList() {
        return vertexLabelList;
    }

    public void setVertexLabelList(List<String> vertexLabelList) {
        this.vertexLabelList = vertexLabelList;
    }

    public List<PropertyFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<PropertyFilter> filterList) {
        this.filterList = filterList;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public DataType getPropertyDataType() {
        return propertyDataType;
    }

    public void setPropertyDataType(DataType propertyDataType) {
        this.propertyDataType = propertyDataType;
    }
}
