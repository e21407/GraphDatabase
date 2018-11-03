package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IndexType {
    COMPOSITE,
    MIXED;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static IndexType getEnum(String name) {
        for (IndexType item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
