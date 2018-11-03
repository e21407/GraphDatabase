package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Cardinality {
    SINGLE,
    LIST,
    SET;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static Cardinality getEnum(String name) {
        for (Cardinality item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
