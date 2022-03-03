package com.codescannerqr.generator.model;

import java.io.Serializable;

public class ResultList implements Serializable {

    private final String key;
    private String value;

    public ResultList(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
