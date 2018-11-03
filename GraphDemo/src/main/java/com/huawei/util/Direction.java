package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Direction {

    BOTH;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static Direction getEnum(String name) {
        for (Direction item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
