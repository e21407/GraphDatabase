package com.huawei.entity;

import com.huawei.util.Cardinality;
import com.huawei.util.DataType;

public class PropertyKey {

    private String name;

    private DataType dataType;

    private Cardinality cardinality;

    private Boolean isHot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    @Override
    public String toString() {
        return "PropertyKey{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", cardinality=" + cardinality +
                ", isHot=" + isHot +
                '}';
    }
}
