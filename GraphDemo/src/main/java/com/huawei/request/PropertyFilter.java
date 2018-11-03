package com.huawei.request;

import com.huawei.util.PropertyPredicate;

import java.util.List;

public class PropertyFilter {

    private String propertyName;

    private PropertyPredicate predicate;

    private List values;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public PropertyPredicate getPredicate() {
        return predicate;
    }

    public void setPredicate(PropertyPredicate predicate) {
        this.predicate = predicate;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }
}
