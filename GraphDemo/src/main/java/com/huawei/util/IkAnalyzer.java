package com.huawei.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IkAnalyzer {

    ik_smart,
    ik_max_word;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static IkAnalyzer getEnum(String name) {
        for (IkAnalyzer item : values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}
