package com.huawei.request;

import java.util.List;

public class AddVertexReqObj {

    private String vertexLabel;

    private List<PropertyReqObj> propertyList;

    public String getVertexLabel() {
        return vertexLabel;
    }

    public void setVertexLabel(String vertexLabel) {
        this.vertexLabel = vertexLabel;
    }

    public List<PropertyReqObj> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<PropertyReqObj> propertyList) {
        this.propertyList = propertyList;
    }

    @Override
    public String toString() {
        return "AddVertexReqObj{" +
                "vertexLabel='" + vertexLabel + '\'' +
                ", propertyList=" + propertyList +
                '}';
    }
}
