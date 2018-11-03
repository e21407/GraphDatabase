package com.huawei.entity;

import com.huawei.util.Direction;

import java.util.List;

public class EdgeLabel {

    private String name;

    private String sortOrder;

    private List<String> sortKeyList;

    private Direction direction;

    private String multiplicity;

    private List<String> signatureList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setSortKeyList(List<String> sortKeyList) {
        this.sortKeyList = sortKeyList;
    }

    public List<String> getSortKeyList() {
        return sortKeyList;
    }

    public List<String> getSignatureList() {
        return signatureList;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public void setSignatureList(List<String> signatureList) {
        this.signatureList = signatureList;
    }

    @Override
    public String toString() {
        return "EdgeLabel{" +
                "name='" + name + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", sortKeyList=" + sortKeyList +
                ", direction=" + direction +
                ", multiplicity='" + multiplicity + '\'' +
                ", signatureList=" + signatureList +
                '}';
    }
}
