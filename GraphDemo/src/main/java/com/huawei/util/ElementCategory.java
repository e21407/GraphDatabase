package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ElementCategory {
    VERTEX,
    EDGE;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static ElementCategory getEnum(String name) {
        for (ElementCategory item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}

