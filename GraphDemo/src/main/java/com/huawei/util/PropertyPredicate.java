package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PropertyPredicate {
    //String类型的运算符
    TEXT_CONTAINS("text_contains"),
    TEXT_CONTAINS_PREFIX("text_contains_prefix"),
    TEXT_CONTAINS_REGEX("text_contains_regex"),
    STRING_CONTAINS_PREFIX("string_contains_prefix"),
    STRING_CONTAINS_REGES("string_contains_regex"),
    EQUAL("="),
    NOT_EQUAL("!="),
    //数值型运算符（包括“=”，“!=”）
    LESS_THAN("<"),
    LESS_THAN_EQUAL("<="),
    GREATER_THAN(">"),
    GREATER_THAN_EQUAL(">="),
    RANGE("range"),
    //地理位置类型运算符
    GEO_WITHIN("geowithin"),
    GEO_INTERSECT("geointersect"),
    GEO_DISJOINT("geodisjoint"),
    GEO_CONTAINS("geocontains");

    private String value;

    PropertyPredicate(String value)
    {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static PropertyPredicate getEnum(String value) {
        for (PropertyPredicate item : values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
