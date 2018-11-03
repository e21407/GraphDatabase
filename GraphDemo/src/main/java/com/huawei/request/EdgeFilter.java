package com.huawei.request;

import java.util.List;

public class EdgeFilter {
    private List<String> edgeLabelList;
    private List<PropertyFilter> filterList;
    private String direction;
    private int limit;

    public List<String> getEdgeLabelList() {
        return edgeLabelList;
    }

    public void setEdgeLabelList(List<String> edgeLabelList) {
        this.edgeLabelList = edgeLabelList;
    }

    public List<PropertyFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<PropertyFilter> filterList) {
        this.filterList = filterList;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
